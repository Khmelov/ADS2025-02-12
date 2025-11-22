package by.it.group410901.evtuhovskaya.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Paths.get(src);
        List<FileInfo> files = new ArrayList<>();

        // Чтение всех java-файлов
        try {
            Files.walk(root)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> processFile(p, root, files));
        } catch (IOException ignored) {}

        // Дистанции и поиск копий
        Map<String, List<String>> copies = findCopies(files);

        // Печать
        List<String> keys = new ArrayList<>(copies.keySet());
        Collections.sort(keys);

        for (String key : keys) {
            System.out.println(key);
            List<String> list = copies.get(key);
            Collections.sort(list);
            for (String c : list) {
                System.out.println(c);
            }
        }
    }

    private static void processFile(Path file, Path root, List<FileInfo> result) {
        String text;

        // корректное чтение
        try {
            text = Files.readString(file, Charset.defaultCharset());
        } catch (MalformedInputException e) {
            return;
        } catch (IOException e) {
            return;
        }

        // тестовые файлы пропускаем
        if (text.contains("@Test") || text.contains("org.junit.Test"))
            return;

        // удаляем package, import и комментарии
        String cleaned = normalize(cleanComments(text));

        String relative = root.relativize(file).toString();
        result.add(new FileInfo(relative, cleaned));
    }

    // ------------------- НОРМАЛИЗАЦИЯ --------------------

    private static String cleanComments(String text) {
        StringBuilder out = new StringBuilder(text.length());
        boolean inBlock = false;

        try (BufferedReader br = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();

                // удаляем package и import
                if (!inBlock && (trimmed.startsWith("package ") || trimmed.startsWith("import ")))
                    continue;

                int i = 0;
                boolean inLine = false;

                while (i < line.length()) {
                    if (!inBlock && !inLine
                            && i + 1 < line.length()
                            && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                        break; // // комментарий — игнорируем остаток строки
                    }
                    if (!inBlock && !inLine
                            && i + 1 < line.length()
                            && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                        inBlock = true;
                        i += 2;
                        continue;
                    }
                    if (inBlock
                            && i + 1 < line.length()
                            && line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                        inBlock = false;
                        i += 2;
                        continue;
                    }
                    if (!inBlock)
                        out.append(line.charAt(i));

                    i++;
                }
                out.append('\n');
            }
        } catch (IOException ignored) {}

        return out.toString();
    }

    private static String normalize(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        boolean prevSpace = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 33) c = ' ';
            if (c == ' ') {
                if (!prevSpace) sb.append(' ');
                prevSpace = true;
            } else {
                sb.append(c);
                prevSpace = false;
            }
        }

        return sb.toString().trim();
    }

    private static Map<String, List<String>> findCopies(List<FileInfo> files) {
        Map<String, List<String>> result = new HashMap<>();

        files.sort(Comparator.comparing(FileInfo::path));

        for (int i = 0; i < files.size(); i++) {
            FileInfo a = files.get(i);

            for (int j = i + 1; j < files.size(); j++) {
                FileInfo b = files.get(j);

                if (Math.abs(a.text.length() - b.text.length()) >= 10)
                    continue;

                if (levenshtein(a.text, b.text, 10) < 10) {
                    result.computeIfAbsent(a.path, k -> new ArrayList<>()).add(b.path);
                }
            }
        }
        return result;
    }

    private static int levenshtein(String a, String b, int limit) {
        int n = a.length();
        int m = b.length();

        if (Math.abs(n - m) > limit)
            return limit; // невозможно быть <limit

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) prev[j] = j;

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            int min = curr[0];

            for (int j = 1; j <= m; j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(prev[j] + 1, curr[j - 1] + 1),
                        prev[j - 1] + cost
                );
                if (curr[j] < min) min = curr[j];
            }

            if (min > limit) return limit; // раннее прерывание

            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }

        return prev[m];
    }

    record FileInfo(String path, String text) {}
}