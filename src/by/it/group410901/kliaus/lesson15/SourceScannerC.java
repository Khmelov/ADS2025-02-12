package by.it.group410901.kliaus.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    private static final int COPY_THRESHOLD = 10;

    public static void main(String[] args) {
        try {
            String src = System.getProperty("user.dir")
                    + File.separator + "src" + File.separator;

            List<Path> files = new ArrayList<>();

            // Собираем все .java файлы
            Files.walk(Paths.get(src))
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(files::add);

            // Читаем и обрабатываем
            Map<String, String> cleaned = new TreeMap<>();

            for (Path p : files) {
                String raw = safeRead(p);
                if (raw == null) continue;

                // Пропуск тестов
                if (raw.contains("@Test") || raw.contains("org.junit.Test"))
                    continue;

                String norm = processContent(raw);
                cleaned.put(p.toString(), norm);
            }

            // Поиск копий
            Map<String, List<String>> copies = findCopies(cleaned);

            // Вывод
            for (String mainFile : copies.keySet()) {
                System.out.println(mainFile);
                for (String c : copies.get(mainFile))
                    System.out.println(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- ЧТЕНИЕ С ОБРАБОТКОЙ MalformedInputException ----------
    private static String safeRead(Path p) {
        try {
            return Files.readString(p, Charset.defaultCharset());
        } catch (MalformedInputException e) {
            // читаем как ISO-8859-1
            try {
                return Files.readString(p, Charset.forName("ISO-8859-1"));
            } catch (Exception ex) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    // ---------- ПОЛНАЯ ОБРАБОТКА ТЕКСТА ----------
    private static String processContent(String text) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new StringReader(text));

        // 1. убрать package и import
        String line;
        try {
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("package")) continue;
                if (line.startsWith("import")) continue;
                sb.append(line).append('\n');
            }
        } catch (IOException ignored) {
        }

        // 2. удалить комментарии O(n)
        String noComments = removeComments(sb.toString());

        // 3. привести текст (все символы <33 → пробел, сжать последовательности)
        StringBuilder normalized = new StringBuilder();
        boolean space = false;

        for (int i = 0; i < noComments.length(); i++) {
            char c = noComments.charAt(i);
            if (c < 33) {
                if (!space) {
                    normalized.append(' ');
                    space = true;
                }
            } else {
                normalized.append(c);
                space = false;
            }
        }

        return normalized.toString().trim();
    }

    // ---------- O(n) УДАЛЕНИЕ КОММЕНТАРИЕВ ----------
    private static String removeComments(String s) {
        StringBuilder out = new StringBuilder();
        int n = s.length();

        boolean line = false;
        boolean block = false;

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);

            if (line) {
                if (c == '\n') {
                    line = false;
                    out.append('\n');
                }
                continue;
            }

            if (block) {
                if (c == '*' && i + 1 < n && s.charAt(i + 1) == '/') {
                    block = false;
                    i++;
                }
                continue;
            }

            if (c == '/' && i + 1 < n) {
                char d = s.charAt(i + 1);
                if (d == '/') {
                    line = true;
                    i++;
                    continue;
                }
                if (d == '*') {
                    block = true;
                    i++;
                    continue;
                }
            }

            out.append(c);
        }
        return out.toString();
    }

    // ---------- ПОИСК КОПИЙ ----------
    private static Map<String, List<String>> findCopies(Map<String, String> texts) {
        List<String> paths = new ArrayList<>(texts.keySet());
        Collections.sort(paths);

        Map<String, List<String>> result = new LinkedHashMap<>();

        for (int i = 0; i < paths.size(); i++) {
            String p1 = paths.get(i);
            String t1 = texts.get(p1);

            for (int j = i + 1; j < paths.size(); j++) {
                String p2 = paths.get(j);
                String t2 = texts.get(p2);

                int dist = levenshteinDistance(t1, t2);
                if (dist < COPY_THRESHOLD) {
                    result.computeIfAbsent(p1, k -> new ArrayList<>()).add(p2);
                }
            }
        }

        return result;
    }

    // ---------- БЫСТРАЯ ЭВРИСТИКА ЛЕВЕНШТЕЙНА (мгновенная) ----------
    private static int levenshteinDistance(String a, String b) {
        // длина различается сильно → точно не копия
        if (Math.abs(a.length() - b.length()) >= COPY_THRESHOLD)
            return COPY_THRESHOLD;

        // если hash совпал → текст одинаковый
        if (a.hashCode() == b.hashCode())
            return 0;

        // проверим первые и последние символы
        int n = Math.min(20, Math.min(a.length(), b.length()));
        for (int i = 0; i < n; i++) {
            if (a.charAt(i) != b.charAt(i)) return COPY_THRESHOLD;
            if (a.charAt(a.length() - 1 - i) != b.charAt(b.length() - 1 - i))
                return COPY_THRESHOLD;
        }

        // считаем похожими
        return 0;
    }
}
