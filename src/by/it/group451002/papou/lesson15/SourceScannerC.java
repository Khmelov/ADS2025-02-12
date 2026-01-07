package by.it.group451002.papou.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


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
            if (!prepared.isPresent()) {
                continue;
            }
            String text = prepared.get();
            if (isTest(text)) {
                continue;
            }
            texts.put(root.relativize(p), text);
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
            System.out.println(original.toString());
            dups.stream()
                    .sorted(Comparator.comparing(Path::toString))
                    .forEach(p -> System.out.println(p.toString()));
        }
    }


    // Сбор файлов
    private static List<Path> collectJavaFiles(Path root) throws IOException {
        try {
            List<Path> result = new ArrayList<>();
            Files.walk(root)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".java"))
                    .forEach(result::add);
            return result;
        } catch (IOException e) {
            // пробрасываем дальше, чтобы main решил, что делать
            throw e;
        }
    }

    // Чтение и подготовка текста

    private static Optional<String> readAndPrepare(Path path) {
        String raw;
        try {
            // Важно: использовать явный Charset и корректно ловить MalformedInputException
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

        // 1. Удалить package и все import'ы (построчно)
        String noPkg = removePackageAndImports(raw);

        // 2. Удалить все комментарии за O(n)
        String noComments = stripComments(noPkg);

        // 3. Нормализовать символы (код < 33 → пробел) и привести к одной строке
        String normalized = normalizeToSingleLine(noComments);

        // 4. trim()
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

    /**
     * Удаление комментариев в один проход.
     * Учитываются:
     *    // до конца строки
     *    /* ... *\/
     *    не трогаем // и /* внутри строковых и символьных литералов.
     */
    private static String stripComments(String src) {
        final int n = src.length();
        StringBuilder out = new StringBuilder(n);

        boolean inLineComment = false;
        boolean inBlockComment = false;
        boolean inString = false;
        boolean inChar = false;

        for (int i = 0; i < n; i++) {
            char c = src.charAt(i);
            char next = (i + 1 < n) ? src.charAt(i + 1) : '\0';

            // Выход из однострочного комментария
            if (inLineComment) {
                if (c == '\n' || c == '\r') {
                    inLineComment = false;
                    out.append(c);
                }
                continue;
            }

            // Выход из блочного комментария
            if (inBlockComment) {
                if (c == '*' && next == '/') {
                    inBlockComment = false;
                    i++; // пропускаем '/'
                }
                continue;
            }

            // Обработка внутри строк/символьных литералов
            if (inString) {
                out.append(c);
                if (c == '\\' && next != '\0') {
                    // Экранированный символ
                    out.append(next);
                    i++;
                } else if (c == '\"') {
                    inString = false;
                }
                continue;
            }
            if (inChar) {
                out.append(c);
                if (c == '\\' && next != '\0') {
                    out.append(next);
                    i++;
                } else if (c == '\'') {
                    inChar = false;
                }
                continue;
            }

            // Находим начало комментариев, если не в строке/символе
            if (c == '/' && next == '/') {
                inLineComment = true;
                i++; // пропускаем второй '/'
                continue;
            }
            if (c == '/' && next == '*') {
                inBlockComment = true;
                i++; // пропускаем '*'
                continue;
            }

            // Начало строк/символьных
            if (c == '\"') {
                inString = true;
                out.append(c);
                continue;
            }
            if (c == '\'') {
                inChar = true;
                out.append(c);
                continue;
            }

            out.append(c);
        }

        return out.toString();
    }

    /**
     * Преобразует текст в одну строку:
     *   - все символы с кодом < 33 заменяются на одиночный пробел (несколько подряд → один);
     */
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


    // Фильтрация тестов

    private static boolean isTest(String text) {
        // достаточно простого поиска подстрок
        return text.contains("@Test") || text.contains("org.junit.Test");
    }

    // Поиск копий по расстоянию Левенштейна
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

                int dist = levenshteinBounded(si, sj, LEV_THRESHOLD);
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
     * Расстояние Левенштейна с порогом.
     * Если реальное расстояние > threshold, возвращает -1.
     * Алгоритм «две строки» + диагональная полоса ширины (2*threshold+1).
     */
    private static int levenshteinBounded(String s, String t, int threshold) {
        int n = s.length();
        int m = t.length();

        if (n == 0) return m <= threshold ? m : -1;
        if (m == 0) return n <= threshold ? n : -1;

        // Гарантируем, что n <= m (для экономии)
        if (n > m) {
            String tmp = s;
            s = t;
            t = tmp;
            n = s.length();
            m = t.length();
        }

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            char sc = s.charAt(i - 1);

            // Полоса: j от max(1, i-threshold) до min(m, i+threshold)
            int from = Math.max(1, i - threshold);
            int to = Math.min(m, i + threshold);

            // Значения слева от полосы считаем бесконечностью
            curr[0] = i;

            if (from > 1) {
                curr[from - 1] = Integer.MAX_VALUE / 2;
            }

            int minInRow = Integer.MAX_VALUE;

            for (int j = from; j <= to; j++) {
                int cost = (sc == t.charAt(j - 1)) ? 0 : 1;

                int deletion = prev[j] + 1;
                int insertion = curr[j - 1] + 1;
                int substitution = prev[j - 1] + cost;

                int v = Math.min(Math.min(deletion, insertion), substitution);
                curr[j] = v;

                if (v < minInRow) {
                    minInRow = v;
                }
            }

            // Правее полосы – бесконечность
            if (to < m) {
                curr[to + 1] = Integer.MAX_VALUE / 2;
            }

            if (minInRow > threshold) {
                return -1;
            }

            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }

        return (prev[m] <= threshold) ? prev[m] : -1;
    }
}

