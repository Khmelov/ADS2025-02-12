package by.it.group410901.bandarzheuskaya.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Сканирует src, подготавливает тексты и ищет наиболее похожие пары
 * по расстоянию Левенштейна (копии: расстояние < 10).
 */
public class SourceScannerC {

    private static final Charset SOURCE_CHARSET = StandardCharsets.UTF_8;
    private static final int LEV_THRESHOLD = 9; // копия, если distance <= 9

    public static void main(String[] args) {
        String srcRoot = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Paths.get(srcRoot);

        if (!Files.isDirectory(root)) {
            System.err.println("Каталог src не найден: " + root);
            return;
        }

        // 1. Собрать все .java-файлы
        List<Path> javaFiles;
        try {
            javaFiles = collectJavaFiles(root);
        } catch (IOException e) {
            System.err.println("Ошибка обхода дерева файлов: " + e.getMessage());
            return;
        }

        // 2. Прочитать и подготовить тексты (без тестов)
        Map<Path, String> texts = new TreeMap<>();
        for (Path p : javaFiles) {
            Optional<String> prepared = readAndPrepare(p);
            if (prepared.isEmpty()) {
                continue;
            }
            String text = prepared.get();
            if (isTest(text)) {
                continue;
            }
            texts.put(p.toAbsolutePath(), text);
        }

        // 3. Поиск «копий» по расстоянию Левенштейна
        Map<Path, List<Path>> copies = findCopies(texts);

        // 4. Вывод
        for (Map.Entry<Path, List<Path>> e : copies.entrySet()) {
            Path original = e.getKey();
            List<Path> dups = e.getValue();
            if (dups.isEmpty()) {
                continue;
            }
            System.out.println(original);
            dups.stream()
                    .sorted(Comparator.comparing(Path::toString))
                    .forEach(System.out::println);
        }
    }

    // ----------------------------------------------------------------------
    // Сбор файлов
    // ----------------------------------------------------------------------

    private static List<Path> collectJavaFiles(Path root) throws IOException {
        List<Path> result = new ArrayList<>();
        try (var stream = Files.walk(root)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".java"))
                    .forEach(result::add);
        }
        return result;
    }

    private static Optional<String> readAndPrepare(Path path) {
        String raw;
        try {
            byte[] bytes = Files.readAllBytes(path);
            raw = new String(bytes, SOURCE_CHARSET);
        } catch (MalformedInputException mie) {
            // Файл имеет неверную кодировку / битый – аккуратно пропускаем
            System.err.println("Пропуск (MalformedInputException): " + path);
            return Optional.empty();
        } catch (IOException ioe) {
            System.err.println("Ошибка чтения: " + path + " : " + ioe.getMessage());
            return Optional.empty();
        }

        // удалить package и все импорты
        String noPkg = removePackageAndImports(raw);

        // удалить все комментарии
        String noComments = stripComments(noPkg);

        // нормализовать символы (код < 33 → пробел) и привести к одной строке
        String normalized = normalizeToSingleLine(noComments);

        // trim()
        String trimmed = normalized.trim();

        if (trimmed.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(trimmed);
    }

    private static String removePackageAndImports(String text) {
        String[] lines = text.split("\\R"); // любой перевод строки
        StringBuilder sb = new StringBuilder(text.length());
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue;
            }
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

    private static String stripComments(String content) {
        StringBuilder sb = new StringBuilder();
        boolean inBlockComment = false;

        for (String line : content.split("\n")) {
            if (!inBlockComment) {
                int blockStart = line.indexOf("/*");
                int lineComment = line.indexOf("//");

                if (blockStart >= 0 && (lineComment == -1 || blockStart < lineComment)) {
                    inBlockComment = true;
                    sb.append(line, 0, blockStart);
                } else if (lineComment >= 0) {
                    sb.append(line, 0, lineComment);
                } else {
                    sb.append(line);
                }
            } else {
                int blockEnd = line.indexOf("*/");
                if (blockEnd >= 0) {
                    inBlockComment = false;
                    if (blockEnd + 2 < line.length()) {
                        sb.append(line, blockEnd + 2, line.length());
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static String normalizeToSingleLine(String text) {
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

    private static boolean isTest(String text) {
        // достаточно простого поиска подстрок
        return text.contains("@Test") || text.contains("org.junit.Test");
    }

    private static Map<Path, List<Path>> findCopies(Map<Path, String> texts) {
        List<Map.Entry<Path, String>> entries = new ArrayList<>(texts.entrySet());
        int n = entries.size();

        Map<Path, List<Path>> result = new TreeMap<>(Comparator.comparing(Path::toString));

        for (int i = 0; i < n; i++) {
            Path pi = entries.get(i).getKey();
            String si = entries.get(i).getValue();
            List<Path> copiesForI = result.computeIfAbsent(pi, k -> new ArrayList<>());

            for (int j = i + 1; j < n; j++) {
                Path pj = entries.get(j).getKey();
                String sj = entries.get(j).getValue();

                // Быстрая отсечка по разнице длин
                int lenDiff = Math.abs(si.length() - sj.length());
                if (lenDiff > LEV_THRESHOLD) {
                    continue;
                }

                int dist = levenshteinBounded(si, sj);
                if (dist >= 0 && dist <= LEV_THRESHOLD) {
                    copiesForI.add(pj);
                    // Также добавим обратную связь
                    result.computeIfAbsent(pj, k -> new ArrayList<>()).add(pi);
                }
            }
        }

        return result;
    }

    /**
     * Вычисляет расстояние Левенштейна между двумя строками
     * с ограничением по порогу для оптимизации.
     * Сколько минимально изменений (добавить/убрать/заменить букву) нужно,
     * чтобы из одной строки получить другую.
     */
    private static int levenshteinBounded(String s, String t) {
        int n = s.length();
        int m = t.length();

        if (n == 0) return m <= LEV_THRESHOLD ? m : -1;
        if (m == 0) return n <= LEV_THRESHOLD ? n : -1;

        // Гарантируем, что n <= m (для экономии) - меняет строки местами, если первая длиннее второй
        if (n > m) {
            String tmp = s;
            s = t;
            t = tmp;
            n = s.length();
            m = t.length();
        }

        // таблицы, которые хранят, сколько шагов было затрачено до текущего состояния
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        // чтобы превратить пустую строку в первые j символов t, нужно j вставок
        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            char sc = s.charAt(i - 1); // Шаг по символам первой строки

            // Полоса: j от max(1, i-LEV_THRESHOLD) до min(m, i+LEV_THRESHOLD) вблизи главной диагонали
            int from = Math.max(1, i - LEV_THRESHOLD);
            int to = Math.min(m, i + LEV_THRESHOLD);

            // Значения слева от полосы считаем бесконечностью
            curr[0] = i;

            if (from > 1) {
                curr[from - 1] = Integer.MAX_VALUE / 2; // очень большое число, чтобы забыть дальние ячейки
            }

            int minInRow = Integer.MAX_VALUE;

            // Сравниваем sc и текущий символ второй строки: если совпадают, изменений не надо (cost=0), иначе нужно заменить букву (cost=1)
            for (int j = from; j <= to; j++) {
                int cost = (sc == t.charAt(j - 1)) ? 0 : 1;

                // для каждого варианта считаем цену
                int deletion = prev[j] + 1;
                int insertion = curr[j - 1] + 1;
                int substitution = prev[j - 1] + cost;

                // выбираем минимальный
                int v = Math.min(Math.min(deletion, insertion), substitution);
                curr[j] = v;

                // Запоминаем самый маленький результат
                if (v < minInRow) {
                    minInRow = v;
                }
            }

            // Правее полосы – бесконечность
            if (to < m) {
                curr[to + 1] = Integer.MAX_VALUE / 2;
            }

            if (minInRow > LEV_THRESHOLD) {
                return -1;
            }

            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }

        return (prev[m] <= LEV_THRESHOLD) ? prev[m] : -1;
    }
}