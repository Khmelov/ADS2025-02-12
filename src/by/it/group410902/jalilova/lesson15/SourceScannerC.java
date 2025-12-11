package by.it.group410902.jalilova.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    private static final int LIMIT = 9;

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileText> files = new ArrayList<>();

        try {
            Files.walk(Paths.get(src))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String text = read(path);
                            if (text == null) return;

                            if (text.contains("@Test") || text.contains("org.junit.Test"))
                                return;

                            String cleaned = clean(text);
                            if (cleaned.isEmpty()) return;

                            files.add(new FileText(
                                    path.toString().substring(src.length()),
                                    cleaned));
                        } catch (Exception ignored) {}
                    });
        } catch (Exception ignored) {}

        Map<Integer, List<FileText>> buckets = new HashMap<>();
        for (FileText f : files) {
            int len = f.text.length();
            for (int d = -LIMIT; d <= LIMIT; d++) {
                buckets.computeIfAbsent(len + d, k -> new ArrayList<>()).add(f);
            }
        }

        Map<String, List<String>> result = new TreeMap<>();

        for (FileText a : files) {

            List<String> copies = new ArrayList<>();
            int la = a.text.length();

            List<FileText> bucket = buckets.get(la);
            if (bucket == null) continue;

            for (FileText b : bucket) {
                if (a == b) continue;

                if (!likelySimilar(a, b)) continue;

                if (!prefixSimilar(a.text, b.text)) continue;

                int dist = boundedLevenshtein(a.text, b.text, LIMIT);
                if (dist <= LIMIT) {
                    copies.add(b.path);
                }
            }

            if (!copies.isEmpty()) {
                Collections.sort(copies);
                result.put(a.path, copies);
            }
        }

        for (String p : result.keySet()) {
            System.out.println(p);
            for (String c : result.get(p)) {
                System.out.println(c);
            }
        }
    }

    private static String read(Path p) {
        try {
            return Files.readString(p, Charset.defaultCharset());
        } catch (MalformedInputException e) {
            try {
                return Files.readString(p, Charset.forName("UTF-8"));
            } catch (Exception ex) {
                return null;
            }
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String clean(String s) {
        StringBuilder sb = new StringBuilder();
        for (String line : s.split("\n")) {
            String t = line.trim();
            if (t.startsWith("package") || t.startsWith("import"))
                continue;
            sb.append(line).append("\n");
        }

        String noCom = delComments(sb.toString());

        StringBuilder out = new StringBuilder(noCom.length());
        for (char c : noCom.toCharArray()) {
            out.append(c < 33 ? ' ' : c);
        }

        return out.toString().trim();
    }

    private static String delComments(String s) {
        StringBuilder out = new StringBuilder();
        boolean line = false, block = false;
        int n = s.length();

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);

            if (line) {
                if (c == '\n') {
                    line = false;
                    out.append(c);
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
                if (d == '/') { line = true; i++; continue; }
                if (d == '*') { block = true; i++; continue; }
            }

            out.append(c);
        }

        return out.toString();
    }

    private static long hash64(String s) {
        long h = 1125899906842597L;
        for (int i = 0; i < s.length(); i++) {
            h = (h ^ s.charAt(i)) * 146527;
        }
        return h;
    }

    private static boolean likelySimilar(FileText a, FileText b) {
        if (Math.abs(a.text.length() - b.text.length()) > LIMIT)
            return false;
        return a.hash == b.hash || Math.abs(a.prefixHash - b.prefixHash) < 50000000;
    }

    private static boolean prefixSimilar(String a, String b) {
        int k = Math.min(Math.min(a.length(), b.length()), 50);
        return a.substring(0, k).equals(b.substring(0, k));
    }

    private static int boundedLevenshtein(String a, String b, int limit) {
        int n = a.length(), m = b.length();
        if (Math.abs(n - m) > limit) return limit + 1;

        if (n > m) { String t = a; a = b; b = t; int tt = n; n = m; m = tt; }

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) prev[j] = j;

        for (int i = 1; i <= n; i++) {
            curr[0] = i;

            int minRow = curr[0];
            char ca = a.charAt(i - 1);

            int start = Math.max(1, i - limit);
            int end = Math.min(m, i + limit);

            Arrays.fill(curr, start, m + 1, limit + 1);

            for (int j = start; j <= end; j++) {
                int cost = (ca == b.charAt(j - 1)) ? 0 : 1;

                int del = prev[j] + 1;
                int ins = curr[j - 1] + 1;
                int sub = prev[j - 1] + cost;

                curr[j] = Math.min(Math.min(del, ins), sub);

                if (curr[j] < minRow) minRow = curr[j];
            }

            if (minRow > limit) return limit + 1;

            int[] tmp = prev; prev = curr; curr = tmp;
        }

        return prev[m];
    }

    static class FileText {
        String path;
        String text;
        long hash;
        long prefixHash;

        FileText(String p, String t) {
            path = p;
            text = t;
            hash = hash64(t);
            prefixHash = hash64(t.length() > 60 ? t.substring(0, 60) : t);
        }
    }
}