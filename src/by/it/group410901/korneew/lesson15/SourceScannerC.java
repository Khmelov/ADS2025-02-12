package by.it.group410901.korneew.lesson15;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerC {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileText> fileTexts = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(Paths.get(src))) {
            stream
                    .filter(p -> !Files.isDirectory(p))
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String text = Files.readString(path, StandardCharsets.UTF_8);
                            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                            // Удалить строки package и import
                            List<String> lines = Arrays.asList(text.split("\n"));
                            List<String> filtered = new ArrayList<>();
                            for (String line : lines) {
                                String trimmed = line.replace("\r", "").trim();
                                if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) continue;
                                filtered.add(line);
                            }
                            String joined = String.join("\n", filtered);

                            // Удалить все комментарии
                            joined = removeComments(joined);

                            // Заменить все последовательности символов <33 на ' '
                            joined = normalizeLowChars(joined);

                            // trim
                            joined = joined.trim();

                            String relPath = src.endsWith(File.separator)
                                    ? path.toString().substring(src.length())
                                    : path.toString().replace(src, "");

                            fileTexts.add(new FileText(relPath, joined));
                        } catch (MalformedInputException e) {
                            // Игнорируем некорректные файлы
                        } catch (IOException e) {
                            // Игнорируем ошибки чтения
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка обхода файлов: " + e.getMessage());
        }

        fileTexts.sort(Comparator.comparing(ft -> ft.relPath));

        // Поиск копий по оптимизированному алгоритму
        int n = fileTexts.size();
        boolean[] reported = new boolean[n];
        for (int i = 0; i < n; ++i) {
            List<String> copies = new ArrayList<>();
            FileText a = fileTexts.get(i);

            for (int j = 0; j < n; ++j) {
                if (i == j) continue;
                FileText b = fileTexts.get(j);

                // Быстрая фильтрация по длине
                if (Math.abs(a.text.length() - b.text.length()) > 10) continue;

                // Быстрая фильтрация по начальным символам (до 64)
                int prefixLen = Math.min(64, Math.min(a.text.length(), b.text.length()));
                if (!a.text.substring(0, prefixLen).equals(b.text.substring(0, prefixLen)))
                    continue;

                // Оптимизированный Levenshtein
                if (levenshteinBanded(a.text, b.text, 10) < 10) {
                    copies.add(b.relPath);
                    reported[j] = true;
                }
            }
            if (!copies.isEmpty() && !reported[i]) {
                Collections.sort(copies);
                System.out.println(a.relPath);
                for (String cp : copies) {
                    System.out.println(cp);
                }
            }
        }
    }

    // Удаление всех комментариев: однострочных и многострочных (O(n))
    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder();
        int len = text.length();
        boolean isBlock = false, isLine = false;
        for (int i = 0; i < len; ) {
            if (!isBlock && !isLine && i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '*') {
                isBlock = true; i += 2; continue;
            }
            if (isBlock && i + 1 < len && text.charAt(i) == '*' && text.charAt(i + 1) == '/') {
                isBlock = false; i += 2; continue;
            }
            if (!isBlock && !isLine && i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '/') {
                isLine = true; i += 2; continue;
            }
            if (isLine && (text.charAt(i) == '\n' || text.charAt(i) == '\r')) {
                isLine = false; sb.append(text.charAt(i)); i++; continue;
            }
            if (isBlock || isLine) { i++; continue; }
            sb.append(text.charAt(i)); i++;
        }
        return sb.toString();
    }

    // Заменяет все последовательности символов < 33 одним пробелом
    private static String normalizeLowChars(String text) {
        StringBuilder sb = new StringBuilder();
        boolean inSeq = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!inSeq) sb.append(' ');
                inSeq = true;
            } else {
                sb.append(c);
                inSeq = false;
            }
        }
        return sb.toString();
    }

    // Banded Levenshtein с порогом, ранний выход
    private static int levenshteinBanded(String a, String b, int max) {
        int lenA = a.length(), lenB = b.length();
        if (Math.abs(lenA - lenB) > max) return max;
        int[] prev = new int[lenB + 1], curr = new int[lenB + 1];
        for (int j = 0; j <= lenB; j++) prev[j] = j;
        for (int i = 1; i <= lenA; i++) {
            curr[0] = i;
            int from = Math.max(1, i - max);
            int to = Math.min(lenB, i + max);
            int localMin = curr[0];
            for (int j = from; j <= to; j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
                localMin = Math.min(localMin, curr[j]);
            }
            if (localMin > max) return max;
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[lenB];
    }

    static class FileText {
        final String relPath;
        final String text;
        FileText(String relPath, String text) {
            this.relPath = relPath;
            this.text = text;
        }
    }
}


