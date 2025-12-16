package by.it.group410902.sinyutin.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.CRC32;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerC {

    // Параметры LSH/MinHash
    private static final int SHINGLE_SIZE = 3;
    private static final int NUM_HASHES = 100; // можно уменьшить до 64 для скорости, но оставил 100
    private static final int BANDS = 20;
    private static final int ROWS = NUM_HASHES / BANDS; // 5

    static class SourceFile {
        final Path path;
        final String content;
        final int[] minHashSignature;
        final long quickFingerprint; // быстрая проверка (CRC32 + length)

        SourceFile(Path path, String content, int[] minHashSignature, long quickFingerprint) {
            this.path = path;
            this.content = content;
            this.minHashSignature = minHashSignature;
            this.quickFingerprint = quickFingerprint;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String srcPath = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Paths.get(srcPath);
        if (!Files.exists(root)) {
            System.out.println("Каталог src не найден: " + srcPath);
            return;
        }

        // Инициализация "хеш-функций" для MinHash (коэффициенты)
        Random rnd = new Random(42);
        long[] hashA = new long[NUM_HASHES];
        long[] hashB = new long[NUM_HASHES];
        for (int i = 0; i < NUM_HASHES; i++) {
            hashA[i] = (rnd.nextLong() & 0x7fffffffffffffffL) | 1L; // положительное не нулевое
            hashB[i] = (rnd.nextLong() & 0x7fffffffffffffffL);
        }

        // Читаем и обрабатываем файлы параллельно (ограничиваем параллелизм)
        List<SourceFile> processedFiles;
        ExecutorService ioPool = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() / 2));
        try (Stream<Path> paths = Files.walk(root)) {
            List<Callable<SourceFile>> tasks = paths
                    .filter(p -> p.toString().endsWith(".java"))
                    .map(path -> (Callable<SourceFile>) () -> readAndProcessFile(path, hashA, hashB))
                    .collect(Collectors.toList());

            List<Future<SourceFile>> futures = ioPool.invokeAll(tasks);
            processedFiles = new ArrayList<>(futures.size());
            for (Future<SourceFile> f : futures) {
                try {
                    SourceFile sf = f.get();
                    if (sf != null) processedFiles.add(sf);
                } catch (ExecutionException ignored) { /* файл пропускаем */ }
            }
        } finally {
            ioPool.shutdown();
        }

        // LSH buckets: ConcurrentHashMap<bandHash, List<SourceFile>>
        ConcurrentHashMap<Integer, List<SourceFile>> lshBuckets = new ConcurrentHashMap<>();

        for (SourceFile file : processedFiles) {
            if (file.minHashSignature == null) continue;
            for (int b = 0; b < BANDS; b++) {
                int start = b * ROWS;
                int bandHash = 1;
                // быстрое объединение ROWS элементов в int-хеш (без аллокаций)
                for (int r = start; r < start + ROWS; r++) {
                    bandHash = 31 * bandHash + file.minHashSignature[r];
                }
                // synchronized list для многопоточности при добавлении
                lshBuckets.computeIfAbsent(bandHash, k -> Collections.synchronizedList(new ArrayList<>())).add(file);
            }
        }

        // Сет для уникальных пар (канонический ключ "pathA|pathB")
        Set<String> seenPairs = ConcurrentHashMap.newKeySet();
        ConcurrentHashMap<String, List<String>> results = new ConcurrentHashMap<>();

        // Параллельно обрабатываем бакеты
        ExecutorService comparePool = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors()));
        List<Callable<Void>> compareTasks = new ArrayList<>();

        for (List<SourceFile> bucket : lshBuckets.values()) {
            // Каждую работу создаём в виде задачи
            compareTasks.add(() -> {
                int size = bucket.size();
                // локально сортируем по пути, чтобы ключ пары был детерминирован
                bucket.sort(Comparator.comparing(a -> a.path.toString()));
                for (int i = 0; i < size; i++) {
                    SourceFile a = bucket.get(i);
                    for (int j = i + 1; j < size; j++) {
                        SourceFile b = bucket.get(j);

                        // создаём канонический ключ пары
                        String key = a.path.toString().compareTo(b.path.toString()) <= 0
                                ? a.path.toString() + "|" + b.path.toString()
                                : b.path.toString() + "|" + a.path.toString();

                        // если уже проверяли эту пару — пропускаем
                        if (!seenPairs.add(key)) continue;

                        // быстрые фильтры до тяжёлого сравнения:
                        // 1) длина
                        if (Math.abs(a.content.length() - b.content.length()) >= 10) continue;

                        // 2) быстрый fingerprint (CRC32 + length)
                        long f1 = a.quickFingerprint;
                        long f2 = b.quickFingerprint;
                        // если CRC различается и длины отличаются — почти точно не копия
                        if (Math.abs((int)(f1 ^ f2)) > 1 && Math.abs(a.content.length() - b.content.length()) > 2) {
                            // дополнительно проверяем небольшим семплом
                            if (!quickSampleMatch(a.content, b.content)) continue;
                        }

                        // 3) сравнение сигнатур MinHash (оценка похожести) — если мало схожих элементов, пропускаем
                        if (!minHashCandidateCheck(a.minHashSignature, b.minHashSignature)) continue;

                        // 4) финальный проверочный алгоритм (оптимизированный пороговый Левенштейн)
                        if (isCopy(a.content, b.content)) {
                            results.computeIfAbsent(a.path.toString(), k -> Collections.synchronizedList(new ArrayList<>()))
                                    .add(b.path.toString());
                            results.computeIfAbsent(b.path.toString(), k -> Collections.synchronizedList(new ArrayList<>()))
                                    .add(a.path.toString());
                        }
                    }
                }
                return null;
            });
        }

        try {
            List<Future<Void>> resFutures = comparePool.invokeAll(compareTasks);
            for (Future<Void> f : resFutures) {
                try { f.get(); } catch (ExecutionException ignored) {}
            }
        } finally {
            comparePool.shutdown();
        }

        // Вывод результатов (уникальные пары)
        List<String> sortedKeys = new ArrayList<>(results.keySet());
        Collections.sort(sortedKeys);
        Set<String> reported = new HashSet<>();
        for (String k : sortedKeys) {
            if (reported.contains(k)) continue;
            System.out.println(k);
            reported.add(k);
            List<String> copies = results.get(k);
            if (copies == null) continue;
            Collections.sort(copies);
            for (String c : copies) {
                if (!reported.contains(c)) {
                    System.out.println("  " + c);
                    reported.add(c);
                }
            }
        }
    }

    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ

    private static SourceFile readAndProcessFile(Path path, long[] hashA, long[] hashB) {
        String rawContent;
        try {
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.IGNORE);

            byte[] bytes = Files.readAllBytes(path);
            CharBuffer chars = decoder.decode(ByteBuffer.wrap(bytes));
            rawContent = chars.toString();
        } catch (IOException e) {
            return null;
        }

        // Отфильтровать тесты
        if (rawContent.contains("@Test") || rawContent.contains("org.junit.Test")) return null;

        // Удаляем package/imports, комментарии, нормализуем пробелы
        String noImports = rawContent.replaceAll("(?m)^\\s*(package|import)\\s+.*?;\\s*$", "");
        String noComments = removeComments(noImports);
        String normalized = normalizeWhitespace(noComments).trim();
        if (normalized.isEmpty()) return null;

        // Быстрый fingerprint: CRC32 + длина
        CRC32 crc = new CRC32();
        crc.update(normalized.getBytes(StandardCharsets.UTF_8));
        long fingerprint = ((crc.getValue() & 0xffffffffL) << 32) ^ normalized.length();

        // Вычисляем MinHash напрямую в одном проходе по шинглам (без Set и без substring)
        int[] signature = computeMinHashRolling(normalized, SHINGLE_SIZE, hashA, hashB);

        return new SourceFile(path, normalized, signature, fingerprint);
    }

    /**
     * Rolling shingle: вычисляем хеш текущего окна символов за O(1) при сдвиге.
     * Обновляем min-hash сигнатуру прямо во время прохода — НЕ СОЗДАЁМ set шинглов.
     */
    private static int[] computeMinHashRolling(String text, int k, long[] hashA, long[] hashB) {
        int N = NUM_HASHES;
        long[] sig = new long[N];
        Arrays.fill(sig, Long.MAX_VALUE);

        if (text.length() < k) {
            int[] res = new int[N];
            Arrays.fill(res, Integer.MAX_VALUE);
            return res;
        }

        // rolling polynomial hash (mod 2^64 via overflow)
        final long base = 127; // невелик, удобен для символов
        long hash = 0;
        long pow = 1;
        for (int i = 0; i < k; i++) {
            hash = hash * base + text.charAt(i);
            if (i > 0) pow *= base;
        }

        // Обновляем сигнатуру для первого окна
        updateMinHashWithShingle(sig, (int)hash, hashA, hashB);

        for (int i = k; i < text.length(); i++) {
            // slide: удалить старый, добавить новый
            int oldChar = text.charAt(i - k);
            int newChar = text.charAt(i);
            // hash = (hash - oldChar * pow) * base + newChar  (mod 2^64)
            hash = (hash - oldChar * pow) * base + newChar;
            updateMinHashWithShingle(sig, (int)hash, hashA, hashB);
        }

        // конвертируем long->int (ок)
        int[] signature = new int[N];
        for (int i = 0; i < N; i++) signature[i] = (int)(sig[i] ^ (sig[i] >>> 32));
        return signature;
    }

    private static void updateMinHashWithShingle(long[] sig, int shingleHash, long[] hashA, long[] hashB) {
        // минимизация по NUM_HASHES универсальным хешем (быстро, без мода)
        for (int i = 0; i < sig.length; i++) {
            long h = (hashA[i] * (long) shingleHash) + hashB[i];
            if (h < sig[i]) sig[i] = h;
        }
    }

    /**
     * Быстрая оценка по MinHash: считаем долю совпадений в сигнатурах по всем хешам.
     * Если совпадений мало — пара не кандидат.
     */
    private static boolean minHashCandidateCheck(int[] s1, int[] s2) {
        int same = 0;
        for (int i = 0; i < s1.length; i++) {
            if (s1[i] == s2[i]) same++;
            // ранний выход: если даже все оставшиеся совпадения не дадут нужного порога, можно выйти
            int remaining = s1.length - 1 - i;
            // требуемый порог примерно 0.2 из всех (настраиваем) -> если нельзя набрать хотя бы 8 совпадений — выйти
            if (same + remaining < 8) return false;
        }
        double sim = (double) same / (double) s1.length;
        // порог довольно низкий — только отбросить очевидно непохожие
        return sim >= 0.08;
    }

    /**
     * Быстрая семпловая проверка: сравниваем первые/средние/последние K символов
     */
    private static boolean quickSampleMatch(String a, String b) {
        int k = 16;
        if (a.length() < k || b.length() < k) return true;
        String a1 = a.substring(0, k);
        String b1 = b.substring(0, k);
        if (a1.equals(b1)) return true;
        int midA = a.length() / 2;
        int midB = b.length() / 2;
        String a2 = a.substring(Math.max(0, midA - k / 2), Math.min(a.length(), midA + k / 2));
        String b2 = b.substring(Math.max(0, midB - k / 2), Math.min(b.length(), midB + k / 2));
        if (a2.equals(b2)) return true;
        String a3 = a.substring(Math.max(0, a.length() - k));
        String b3 = b.substring(Math.max(0, b.length() - k));
        return a3.equals(b3);
    }

    // Удаление комментариев — один проход, без создания лишних строк
    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        boolean inString = false, inChar = false, inBlock = false, inLine = false;
        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);
            char next = (i + 1 < len) ? text.charAt(i + 1) : '\0';

            if (inBlock) {
                if (c == '*' && next == '/') {
                    inBlock = false;
                    i++;
                }
            } else if (inLine) {
                if (c == '\n' || c == '\r') {
                    inLine = false;
                    sb.append(c);
                }
            } else if (inString) {
                sb.append(c);
                if (c == '\\' && next != '\0') {
                    sb.append(next);
                    i++;
                } else if (c == '"') {
                    inString = false;
                }
            } else if (inChar) {
                sb.append(c);
                if (c == '\\' && next != '\0') {
                    sb.append(next);
                    i++;
                } else if (c == '\'') {
                    inChar = false;
                }
            } else {
                if (c == '/' && next == '*') {
                    inBlock = true;
                    i++;
                } else if (c == '/' && next == '/') {
                    inLine = true;
                    i++;
                } else if (c == '"') {
                    inString = true;
                    sb.append(c);
                } else if (c == '\'') {
                    inChar = true;
                    sb.append(c);
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    // Нормализация пробелов — один проход
    private static String normalizeWhitespace(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        boolean lastWasSpace = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!lastWasSpace) {
                    sb.append(' ');
                    lastWasSpace = true;
                }
            } else {
                sb.append(c);
                lastWasSpace = false;
            }
        }
        return sb.toString();
    }

    /**
     * Пороговая оптимизированная проверка Левенштейна (threshold = 10).
     * Сохраняет ваше ограничение, но вызывается редко благодаря фильтрам.
     */
    private static boolean isCopy(String s1, String s2) {
        final int limit = 10;
        int n = s1.length();
        int m = s2.length();
        if (Math.abs(n - m) >= limit) return false;

        // для экономии памяти используем только m+1 целых
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            int minRow = curr[0];
            char ci = s1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                int cost = (ci == s2.charAt(j - 1)) ? 0 : 1;
                int v = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
                curr[j] = v;
                if (v < minRow) minRow = v;
            }
            if (minRow >= limit) return false;
            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }
        return prev[m] < limit;
    }
}
