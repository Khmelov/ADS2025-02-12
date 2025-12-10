package by.it.group410902.plekhova.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        String srcDir = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Path.of(srcDir);

        List<FileText> list = new ArrayList<>();

        try (var walk = Files.walk(root)) {
            walk.forEach(path -> {
                if (path.toString().endsWith(".java")) {
                    processFile(path, root).ifPresent(list::add);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        list.sort(Comparator.comparing(ft -> ft.relativePath));

        List<FileText> sortedByLen = new ArrayList<>(list);
        sortedByLen.sort(Comparator.comparingInt(ft -> ft.text.length()));

        Map<FileText, List<FileText>> copies = new TreeMap<>(
                Comparator.comparing(ft -> ft.relativePath)
        );

        for (int i = 0; i < sortedByLen.size(); i++) {
            FileText a = sortedByLen.get(i);

            for (int j = i + 1; j < sortedByLen.size(); j++) {
                FileText b = sortedByLen.get(j);

                if (Math.abs(a.text.length() - b.text.length()) > 15) continue;

                int d = levenshteinLimited(a.text, b.text, 10);
                if (d < 10) {
                    copies.computeIfAbsent(a, k -> new ArrayList<>()).add(b);
                }
            }
        }

        for (var e : copies.entrySet()) {
            System.out.println(e.getKey().relativePath);
            for (var c : e.getValue()) {
                System.out.println(c.relativePath);
            }
        }
    }

    private static Optional<FileText> processFile(Path file, Path root) {
        String text;
        try {
            text = Files.readString(file);
        } catch (IOException e) {
            return Optional.empty();
        }

        if (text.contains("@Test") || text.contains("org.junit.Test")) {
            return Optional.empty();
        }

        StringBuilder sb1 = new StringBuilder(text.length());
        String[] lines = text.split("\n", -1);

        for (String line : lines) {
            String t = line.trim();
            if (t.startsWith("package ") || t.startsWith("import ")) continue;
            sb1.append(line).append("\n");
        }

        String noComments = removeComments(sb1.toString());
        String cleaned = normalizeLowAscii(noComments).trim();

        String relative = root.relativize(file).toString();
        return Optional.of(new FileText(relative, cleaned));
    }

    private static String removeComments(String s) {
        StringBuilder out = new StringBuilder(s.length());
        int n = s.length();
        boolean inBlock = false;
        int i = 0;

        while (i < n) {
            if (!inBlock) {
                if (i + 1 < n && s.charAt(i) == '/' && s.charAt(i + 1) == '*') {
                    inBlock = true;
                    i += 2;
                } else if (i + 1 < n && s.charAt(i) == '/' && s.charAt(i + 1) == '/') {
                    i += 2;
                    while (i < n && s.charAt(i) != '\n') i++;
                } else {
                    out.append(s.charAt(i));
                    i++;
                }
            } else {
                if (i + 1 < n && s.charAt(i) == '*' && s.charAt(i + 1) == '/') {
                    inBlock = false;
                    i += 2;
                } else {
                    i++;
                }
            }
        }
        return out.toString();
    }


     //Оставляем только ЧРЕЗМЕРНО низкие ASCII (<9), остальные пробелы/табы/переводы строк сохраняем.

    private static String normalizeLowAscii(String s) {
        StringBuilder out = new StringBuilder(s.length());
        boolean inSpace = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c < 9) { // только мусорные управляющие символы
                if (!inSpace) {
                    out.append(' ');
                    inSpace = true;
                }
            } else {
                out.append(c);
                inSpace = false;
            }
        }
        return out.toString();
    }


    private static int levenshteinLimited(String a, String b, int limit) {
        if (a.equals(b)) return 0;
        if (Math.abs(a.length() - b.length()) >= limit) return limit;

        if (a.length() > b.length()) {
            String t = a; a = b; b = t;
        }

        int n = a.length();
        int m = b.length();

        long[] peq = new long[256];
        for (int i = 0; i < n; i++) {
            int ch = a.charAt(i);
            if (ch < 256) {
                peq[ch] |= (1L << i);
            }
        }

        long vp = ~0L;
        long vn = 0;

        int current = m;

        for (int i = 0; i < m; i++) {
            int ch = b.charAt(i);
            long mask = (ch < 256) ? peq[ch] : 0;

            long x = mask | vn;
            long d0 = (((x & vp) + vp) ^ vp) | x;

            long hp = vn | ~(d0 | vp);
            long hn = d0 & vp;

            vp = (hn << 1) | ~(d0 | ((hp << 1) | 1));
            vn = (hp << 1) & d0;

            if ((vp & (1L << (n - 1))) != 0)
                current++;
            else if ((vn & (1L << (n - 1))) != 0)
                current--;

            if (current >= limit) return limit;
        }

        return current;
    }

    private static class FileText {
        final String relativePath;
        final String text;

        FileText(String relativePath, String text) {
            this.relativePath = relativePath;
            this.text = text;
        }
    }
}
