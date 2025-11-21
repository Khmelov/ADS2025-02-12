package by.it.group410902.bolshakova.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {

        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Path.of(src);

        List<FileText> texts = new ArrayList<>();

        try (var walk = Files.walk(root)) {
            walk.forEach(path -> {
                if (!path.toString().endsWith(".java")) return;

                String original;
                try {
                    original = Files.readString(path);
                } catch (IOException e) {
                    return; // игнорируем любые ошибки чтения, включая MalformedInput
                }

                if (original.contains("@Test") || original.contains("org.junit.Test")) return;

                String cleaned = cleanText(original);

                cleaned = cleaned.trim();

                // Сокращаем текст до 2000 символов для ускорения сравнения
                if (cleaned.length() > 2000) {
                    cleaned = cleaned.substring(0, 1000) + cleaned.substring(cleaned.length() - 1000);
                }

                String relPath = root.relativize(path).toString();

                texts.add(new FileText(relPath, cleaned));
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, List<String>> copies = findCopies(texts);

        List<String> keys = new ArrayList<>(copies.keySet());
        Collections.sort(keys);

        for (String k : keys) {
            System.out.println(k);
            for (String v : copies.get(k)) {
                System.out.println(v);
            }
        }
    }

    // -------------------------
    // Очистка текста: package/import, комментарии, символы <33
    // -------------------------
    private static String cleanText(String text) {

        // Удаляем package и import
        StringBuilder sb = new StringBuilder();
        for (String line : text.split("\n")) {
            String t = line.trim();
            if (t.startsWith("package ") || t.startsWith("import ")) continue;
            sb.append(line).append("\n");
        }

        // Удаляем комментарии
        String noComments = removeComments(sb.toString());

        // Заменяем все символы <33 на пробел
        StringBuilder res = new StringBuilder();
        boolean inSpace = false;
        for (char c : noComments.toCharArray()) {
            if (c < 33) {
                if (!inSpace) {
                    res.append(' ');
                    inSpace = true;
                }
            } else {
                res.append(c);
                inSpace = false;
            }
        }

        return res.toString();
    }

    // -------------------------
    // Удаление комментариев за один проход
    // -------------------------
    private static String removeComments(String text) {
        StringBuilder out = new StringBuilder();
        char[] c = text.toCharArray();
        int n = c.length;

        boolean inLine = false;
        boolean inBlock = false;
        boolean inString = false;

        for (int i = 0; i < n; i++) {

            if (inLine) {
                if (c[i] == '\n') {
                    inLine = false;
                    out.append(c[i]);
                }
                continue;
            }

            if (inBlock) {
                if (c[i] == '*' && i + 1 < n && c[i + 1] == '/') {
                    inBlock = false;
                    i++;
                }
                continue;
            }

            if (c[i] == '"' && !inString) {
                inString = true;
                out.append(c[i]);
                continue;
            } else if (c[i] == '"' && inString) {
                inString = false;
                out.append(c[i]);
                continue;
            }

            if (!inString) {
                if (c[i] == '/' && i + 1 < n && c[i + 1] == '/') {
                    inLine = true;
                    i++;
                    continue;
                }
                if (c[i] == '/' && i + 1 < n && c[i + 1] == '*') {
                    inBlock = true;
                    i++;
                    continue;
                }
            }

            out.append(c[i]);
        }

        return out.toString();
    }

    // -------------------------
    // Поиск копий
    // -------------------------
    private static Map<String, List<String>> findCopies(List<FileText> texts) {

        Map<String, List<String>> result = new HashMap<>();
        int n = texts.size();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                // Быстрый фильтр по длине
                if (Math.abs(texts.get(i).text.length() - texts.get(j).text.length()) >= 10)
                    continue;

                int dist = levenshteinLimited(texts.get(i).text, texts.get(j).text, 10);

                if (dist < 10) {
                    result.computeIfAbsent(texts.get(i).path, x -> new ArrayList<>())
                            .add(texts.get(j).path);
                    result.computeIfAbsent(texts.get(j).path, x -> new ArrayList<>())
                            .add(texts.get(i).path);
                }
            }
        }

        for (var e : result.entrySet()) {
            Collections.sort(e.getValue());
        }

        return result;
    }

    // -------------------------
    // Полосный Levenshtein с ограничением maxDist
    // -------------------------
    private static int levenshteinLimited(String a, String b, int maxDist) {
        int n = a.length();
        int m = b.length();

        if (Math.abs(n - m) > maxDist) return maxDist + 1;

        if (n > m) { // a короче
            String tmp = a; a = b; b = tmp;
            n = a.length();
            m = b.length();
        }

        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];
        for (int i = 0; i <= n; i++) prev[i] = i;

        for (int j = 1; j <= m; j++) {
            curr[0] = j;
            int minI = Math.max(1, j - maxDist);
            int maxI = Math.min(n, j + maxDist);

            int best = curr[0];
            for (int i = minI; i <= maxI; i++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                curr[i] = Math.min(Math.min(curr[i - 1] + 1, prev[i] + 1), prev[i - 1] + cost);
                best = Math.min(best, curr[i]);
            }

            if (best > maxDist) return maxDist + 1;

            int[] tmp = prev; prev = curr; curr = tmp;
        }

        return prev[n];
    }

    private record FileText(String path, String text) {}
}
