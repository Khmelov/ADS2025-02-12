package by.it.group410901.evtuhovskaya.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Paths.get(src);
        List<FileInfo> results = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java")).forEach(path -> {
                try {
                    // Чтение всех байтов для обхода MalformedInputException
                    byte[] bytes = Files.readAllBytes(path);
                    String content = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);

                    // 1. Проверка на тесты (не участвуют в обработке)
                    if (content.contains("@Test") || content.contains("org.junit.Test")) {
                        return;
                    }

                    // 2. Обработка текста
                    String processed = processText(content);

                    if (!processed.isEmpty()) {
                        // Важно: приводим пути к виду "folder/file.java" даже на Windows
                        String relPath = root.relativize(path).toString().replace(File.separator, "/");
                        results.add(new FileInfo(relPath, processed));
                    }
                } catch (IOException ignored) {}
            });
        } catch (IOException ignored) {}

        // Сортируем все файлы лексикографически по пути
        results.sort(Comparator.comparing(f -> f.path));

        // 3. Сравнение всех файлов
        for (int i = 0; i < results.size(); i++) {
            FileInfo current = results.get(i);
            List<String> copies = new ArrayList<>();

            for (int j = 0; j < results.size(); j++) {
                if (i == j) continue;
                FileInfo other = results.get(j);

                // Оптимизация по длине (если разница >= 10, то расстояние точно >= 10)
                if (Math.abs(current.text.length() - other.text.length()) < 10) {
                    if (levenshteinDist(current.text, other.text, 10) < 10) {
                        copies.add(other.path);
                    }
                }
            }

            // 4. Вывод результата
            if (!copies.isEmpty()) {
                System.out.println(current.path);
                Collections.sort(copies);
                for (String c : copies) {
                    System.out.println(c);
                }
            }
        }
    }

    private static String processText(String text) {
        // Удаление комментариев за O(n) (состояния: обычный текст, строка, блок, линия)
        StringBuilder sb = new StringBuilder();
        int n = text.length();
        boolean inBlock = false, inLine = false, inString = false;

        for (int i = 0; i < n; i++) {
            char c = text.charAt(i);
            if (inBlock) {
                if (c == '*' && i + 1 < n && text.charAt(i + 1) == '/') {
                    inBlock = false; i++;
                }
            } else if (inLine) {
                if (c == '\n' || c == '\r') {
                    inLine = false; sb.append('\n');
                }
            } else if (inString) {
                if (c == '\\' && i + 1 < n) {
                    sb.append(c).append(text.charAt(++i));
                } else {
                    if (c == '"') inString = false;
                    sb.append(c);
                }
            } else {
                if (c == '/' && i + 1 < n && text.charAt(i + 1) == '/') {
                    inLine = true; i++;
                } else if (c == '/' && i + 1 < n && text.charAt(i + 1) == '*') {
                    inBlock = true; i++;
                } else {
                    if (c == '"') inString = true;
                    sb.append(c);
                }
            }
        }

        // Удаление строк package и import
        String[] lines = sb.toString().split("\n");
        sb = new StringBuilder();
        for (String line : lines) {
            String t = line.trim();
            if (t.startsWith("package ") || t.startsWith("import ") || t.isEmpty()) continue;
            sb.append(line).append(" ");
        }

        // Замена всех символов < 33 на пробел (32) и схлопывание пробелов
        StringBuilder result = new StringBuilder();
        boolean lastWasSpace = false;
        String temp = sb.toString();
        for (int i = 0; i < temp.length(); i++) {
            char c = temp.charAt(i);
            if (c < 33) {
                if (!lastWasSpace) {
                    result.append(' ');
                    lastWasSpace = true;
                }
            } else {
                result.append(c);
                lastWasSpace = false;
            }
        }
        return result.toString().trim();
    }

    private static int levenshteinDist(String s1, String s2, int limit) {
        int n = s1.length();
        int m = s2.length();
        if (Math.abs(n - m) >= limit) return limit;
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;
        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            int minRow = curr[0];
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
                if (curr[j] < minRow) minRow = curr[j];
            }
            if (minRow >= limit) return limit;
            int[] t = prev; prev = curr; curr = t;
        }
        return prev[m];
    }

    private static class FileInfo {
        String path, text;
        FileInfo(String p, String t) { this.path = p; this.text = t; }
    }
}