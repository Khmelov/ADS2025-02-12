package by.it.group410902.jalilova.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {

        long start = System.nanoTime(); // старт времени

        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Paths.get(src);

        Map<String, String> filesMap = new TreeMap<>();
        Map<Integer, List<String>> hashMap = new HashMap<>(); // hash -> список путей

        // --- Чтение и очистка файлов ---
        try {
            Files.walk(root)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        String text;
                        try {
                            try {
                                text = Files.readString(p);
                            } catch (MalformedInputException e) {
                                return; // игнорируем битые файлы
                            }

                            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                            String cleaned = cleanText(text);
                            if (!cleaned.isEmpty()) {
                                String relPath = root.relativize(p).toString();
                                filesMap.put(relPath, cleaned);

                                // hash первых 50 символов для оптимизации
                                int h = cleaned.substring(0, Math.min(50, cleaned.length())).hashCode();
                                hashMap.computeIfAbsent(h, k -> new ArrayList<>()).add(relPath);
                            }

                        } catch (IOException ignored) {}
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // --- Сравнение текстов ---
        Map<String, List<String>> copies = new TreeMap<>();

        for (List<String> group : hashMap.values()) {
            for (int i = 0; i < group.size(); i++) {
                String path1 = group.get(i);
                String text1 = filesMap.get(path1);

                for (int j = i + 1; j < group.size(); j++) {
                    String path2 = group.get(j);
                    String text2 = filesMap.get(path2);

                    // фильтр по длине
                    if (Math.abs(text1.length() - text2.length()) >= 20) continue;

                    int dist = levenshtein(text1, text2, 10);
                    if (dist < 10) {
                        copies.computeIfAbsent(path1, k -> new ArrayList<>()).add(path2);
                    }
                }
            }
        }

        // --- Вывод ---
        for (String path : copies.keySet()) {
            System.out.println(path);
            for (String copy : copies.get(path)) {
                System.out.println("    " + copy);
            }
        }

        long end = System.nanoTime(); // конец времени
        long durationMs = (end - start) / 1_000_000;
        System.out.println("TIME = " + durationMs + " ms"); // вывод производительности
    }

    private static String cleanText(String text) {
        StringBuilder sb = new StringBuilder();
        boolean inBlock = false;
        for (String line : text.split("\n")) {
            String s = line;

            if (inBlock) {
                if (s.contains("*/")) {
                    s = s.substring(s.indexOf("*/") + 2);
                    inBlock = false;
                } else continue;
            }

            while (s.contains("/*")) {
                int start = s.indexOf("/*");
                int end = s.indexOf("*/", start + 2);
                if (end >= 0) {
                    s = s.substring(0, start) + s.substring(end + 2);
                } else {
                    s = s.substring(0, start);
                    inBlock = true;
                    break;
                }
            }

            int idx = s.indexOf("//");
            if (idx >= 0) s = s.substring(0, idx);

            String t = s.strip();
            if (t.isEmpty()) continue;
            if (t.startsWith("package ") || t.startsWith("import ")) continue;

            sb.append(t).append("\n");
        }

        // Заменяем все символы <33 на пробел и trim
        return sb.toString().replaceAll("[\\x00-\\x1F]+", " ").trim();
    }

    // Расстояние Левенштейна с ранним выходом
    private static int levenshtein(String a, String b, int threshold) {
        if (a.equals(b)) return 0;
        if (Math.abs(a.length() - b.length()) >= threshold) return threshold;

        int[] prev = new int[b.length() + 1];
        int[] curr = new int[b.length() + 1];

        for (int j = 0; j <= b.length(); j++) prev[j] = j;

        for (int i = 1; i <= a.length(); i++) {
            curr[0] = i;
            int min = curr[0];
            for (int j = 1; j <= b.length(); j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
                min = Math.min(min, curr[j]);
            }
            if (min >= threshold) return threshold;
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[b.length()];
    }
}
