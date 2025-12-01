package by.it.group410902.yarmashuk.lesson15;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class SourceScannerC {

    // Вспомогательный класс для хранения данных о файле (путь и очищенный текст)
    static class ProcessedFile {
        String relativePath;
        String cleanedContent;
        HashSet<Integer> shingleHashes; // Добавляем хеши шинглов для оптимизации

        ProcessedFile(String relativePath, String cleanedContent, int kShingleSize) {
            this.relativePath = relativePath;
            this.cleanedContent = cleanedContent;
            this.shingleHashes = generateShingleHashes(cleanedContent, kShingleSize);
        }

        // Генерирует HashSet хешей K-шинглов для текста
        private HashSet<Integer> generateShingleHashes(String text, int k) {
            HashSet<Integer> hashes = new HashSet<>();
            if (text.length() < k) {
                if (text.length() > 0) { // Если текст короткий, но не пустой, хешируем его целиком
                    hashes.add(text.hashCode());
                }
                return hashes;
            }
            for (int i = 0; i <= text.length() - k; i++) {
                hashes.add(text.substring(i, i + k).hashCode());
            }
            return hashes;
        }
    }

    // --- Параметры оптимизации ---
    private static final int LEVENSHTEIN_THRESHOLD = 10;
    private static final int K_SHINGLE_SIZE = 5; // Размер шингла для хеширования (например, 5 символов)
    // Порог схожести шинглов. Если процент общих шинглов ниже, Levenshtein не вычисляется.
    // Пример: 0.2f означает, что если общих шинглов менее 20% от минимального кол-ва шинглов, то не сравниваем.
    private static final float SHINGLE_SIMILARITY_THRESHOLD = 0.2f;
    // --- Конец параметров оптимизации ---

    public static void main(String[] args) {
        String srcPath = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        File srcDirectory = new File(srcPath);

        List<ProcessedFile> allProcessedFiles = new ArrayList<>();

        processDirectory(srcDirectory, srcDirectory.getAbsolutePath(), allProcessedFiles);

        DSUForCopies dsu = new DSUForCopies(allProcessedFiles.size());

        for (int i = 0; i < allProcessedFiles.size(); i++) {

            ProcessedFile file1 = allProcessedFiles.get(i);
            for (int j = i + 1; j < allProcessedFiles.size(); j++) {

                ProcessedFile file2 = allProcessedFiles.get(j);

                // Оптимизация 1: Быстрая отбраковка по длине
                if (Math.abs(file1.cleanedContent.length() - file2.cleanedContent.length()) >= LEVENSHTEIN_THRESHOLD) {
                    continue; // Разница в длинах слишком велика, чтобы Levenshtein был < LEVENSHTEIN_THRESHOLD
                }

                // Оптимизация 2: Предварительная фильтрация с помощью хешей шинглов
                if (!areShinglesPotentiallySimilar(file1.shingleHashes, file2.shingleHashes, SHINGLE_SIMILARITY_THRESHOLD)) {
                    continue; // Шилы слишком сильно отличаются, Levenshtein будет большим
                }

                // Если фильтры пройдены, вычисляем точное расстояние Левенштейна
                int distance = levenshteinDistance(file1.cleanedContent, file2.cleanedContent);

                if (distance < LEVENSHTEIN_THRESHOLD) {
                    dsu.union(i, j); // Объединяем индексы файлов, если они являются копиями
                }
            }
        }

        // Теперь собираем группы файлов из DSU
        List<List<String>> copyGroups = new ArrayList<>();
        boolean[] rootProcessed = new boolean[allProcessedFiles.size()];

        for (int i = 0; i < allProcessedFiles.size(); i++) {
            int root = dsu.find(i);
            if (!rootProcessed[root]) {
                List<String> group = new ArrayList<>();
                for (int j = 0; j < allProcessedFiles.size(); j++) {
                    if (dsu.find(j) == root) {
                        group.add(allProcessedFiles.get(j).relativePath);
                    }
                }
                // Только группы с более чем одним файлом нас интересуют как "копии"
                if (group.size() > 1) {
                    Collections.sort(group); // Сортируем пути внутри группы лексикографически
                    copyGroups.add(group);
                }
                rootProcessed[root] = true;
            }
        }

        // Сортируем группы по первому элементу (пути) для вывода
        Collections.sort(copyGroups, Comparator.comparing(g -> g.get(0)));

        // Выводим результаты
        for (List<String> group : copyGroups) {
            System.out.println(group.get(0)); // Первый файл в группе - как "оригинал"
            for (int i = 1; i < group.size(); i++) {
                System.out.println("  " + group.get(i)); // Копии с отступом
            }
        }
    }


    private static boolean areShinglesPotentiallySimilar(HashSet<Integer> hashes1, HashSet<Integer> hashes2, float threshold) {
        if (hashes1.isEmpty() && hashes2.isEmpty()) return true; // Оба пусты, считаем похожими
        if (hashes1.isEmpty() || hashes2.isEmpty()) return false; // Один пуст, другой нет, считаем непохожими

        HashSet<Integer> intersection = new HashSet<>(hashes1);
        intersection.retainAll(hashes2); // Находим пересечение

        float similarity = (float) intersection.size() / Math.min(hashes1.size(), hashes2.size());
        return similarity >= threshold;
    }


    private static void processDirectory(File directory, String srcBasePath, List<ProcessedFile> allProcessedFiles) {
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Каталог не найден или не является каталогом: " + directory.getAbsolutePath());
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                processDirectory(file, srcBasePath, allProcessedFiles);
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                try {
                    String fileContent = readFileContent(file);

                    if (fileContent.contains("@Test") || fileContent.contains("org.junit.Test")) {
                        continue;
                    }

                    String cleanedContent = cleanFileContent(fileContent);

                    String relativePath = file.getAbsolutePath().substring(srcBasePath.length());

                    // Создаем ProcessedFile с генерацией шинглов
                    allProcessedFiles.add(new ProcessedFile(relativePath, cleanedContent, K_SHINGLE_SIZE));

                } catch (IOException e) {
                    System.err.println("Ошибка чтения файла " + file.getAbsolutePath() + ": " + e.getMessage());
                }
            }
        }
    }

    private static String readFileContent(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));

        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        decoder.onMalformedInput(CodingErrorAction.REPLACE);
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try {
            return decoder.decode(buffer).toString();
        } catch (CharacterCodingException e) {
            System.err.println("Проблема с декодированием файла " + file.getAbsolutePath() + ". Некоторые символы могли быть заменены.");
            return new String(bytes, Arrays.toString(Charset.forName("UTF-8").newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).decode(ByteBuffer.wrap(bytes)).array()));
        }
    }

    private static String cleanFileContent(String content) {
        String contentWithoutComments = removeComments(content);

        StringBuilder sbRaw = new StringBuilder();
        String[] lines = contentWithoutComments.split("\r?\n");

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")) {
                continue;
            }
            sbRaw.append(line).append('\n');
        }

        if (sbRaw.length() > 0 && sbRaw.charAt(sbRaw.length() - 1) == '\n') {
            sbRaw.setLength(sbRaw.length() - 1);
        }

        String textAfterInitialCleaning = sbRaw.toString();

        StringBuilder sbFinal = new StringBuilder();
        boolean lastWasLowAscii = false;

        for (int i = 0; i < textAfterInitialCleaning.length(); i++) {
            char c = textAfterInitialCleaning.charAt(i);
            if (c < 33) { // Символы с кодом <33
                if (!lastWasLowAscii) {
                    sbFinal.append(' '); // Заменяем последовательности на один пробел
                }
                lastWasLowAscii = true;
            } else {
                sbFinal.append(c);
                lastWasLowAscii = false;
            }
        }

        return sbFinal.toString().trim();
    }

    private static String removeComments(String content) {
        StringBuilder sb = new StringBuilder();
        boolean inBlockComment = false;
        boolean inString = false;
        boolean inChar = false;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            char nextC = (i + 1 < content.length()) ? content.charAt(i + 1) : '\0';

            if (inBlockComment) {
                if (c == '*' && nextC == '/') {
                    inBlockComment = false;
                    i++;
                }
            } else if (inString) {
                sb.append(c);
                if (c == '\\' && nextC != '\0') {
                    sb.append(nextC);
                    i++;
                } else if (c == '"') {
                    inString = false;
                }
            } else if (inChar) {
                sb.append(c);
                if (c == '\\' && nextC != '\0') {
                    sb.append(nextC);
                    i++;
                } else if (c == '\'') {
                    inChar = false;
                }
            } else {
                if (c == '/' && nextC == '/') {
                    i++;
                    while (i < content.length() && content.charAt(i) != '\n' && content.charAt(i) != '\r') {
                        i++;
                    }
                    if (i < content.length()) {
                        sb.append(content.charAt(i));
                    }
                } else if (c == '/' && nextC == '*') {
                    inBlockComment = true;
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

    public static int levenshteinDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        if (m == 0) return n;
        if (n == 0) return m;

        // Оптимизация для Levenshtein: если разница в длине уже больше порога,
        // то и расстояние будет больше или равно.
        if (Math.abs(m - n) >= LEVENSHTEIN_THRESHOLD) {
            return LEVENSHTEIN_THRESHOLD; // или любое значение >= порогу
        }

        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        for (int j = 0; j <= n; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= m; i++) {
            curr[0] = i;
            for (int j = 1; j <= n; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(prev[j] + 1,
                                curr[j - 1] + 1),
                        prev[j - 1] + cost
                );
            }
            System.arraycopy(curr, 0, prev, 0, n + 1);
        }
        return prev[n];
    }
}

// DSUForCopies остается без изменений, она уже оптимальна для своей задачи
class DSUForCopies {
    int[] parent;
    int[] size;

    DSUForCopies(int nElements) {
        parent = new int[nElements];
        size = new int[nElements];
        for (int i = 0; i < nElements; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    int find(int i) {
        if (parent[i] == i) {
            return i;
        }
        return parent[i] = find(parent[i]);
    }

    void union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);

        if (rootI != rootJ) {
            if (size[rootI] < size[rootJ]) {
                parent[rootI] = rootJ;
                size[rootJ] += size[rootI];
            } else {
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
            }
        }
    }
}
