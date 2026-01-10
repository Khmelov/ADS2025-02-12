package by.it.group451001.suprunovich.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {
    static final int NGRAM = 4;
    static final int MAX_PREFIX = 200;

    public static void main(String[] args) throws Exception {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        List<String> files = new ArrayList<>();
        collect(new File(src), files);

        List<String> texts = new ArrayList<>();
        List<String> relPaths = new ArrayList<>();
        for (String f : files) {
            try {
                String t = readFile(f);
                if (t.contains("@Test") || t.contains("org.junit.Test")) continue;
                String c = clean(t);
                texts.add(c);
                relPaths.add(f.substring(src.length()));
            } catch (MalformedInputException ignored) {}
        }

        int n = texts.size();

        int[] lengths = new int[n];
        String[] prefixes = new String[n];
        List<Set<String>> ngrams = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            String t = texts.get(i);
            lengths[i] = t.length();
            prefixes[i] = t.substring(0, Math.min(MAX_PREFIX, t.length()));
            ngrams.add(buildNgrams(t));
        }

        Map<String, List<String>> out = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // сравниваем только файлы с одинаковым именем
                String nameI = relPaths.get(i).substring(relPaths.get(i).lastIndexOf(File.separator) + 1);
                String nameJ = relPaths.get(j).substring(relPaths.get(j).lastIndexOf(File.separator) + 1);
                if (!nameI.equals(nameJ)) continue;

                if (Math.abs(lengths[i] - lengths[j]) >= 10) continue;
                if (!prefixSimilar(prefixes[i], prefixes[j])) continue;
                if (!ngramClose(ngrams.get(i), ngrams.get(j))) continue;
                if (levCut(texts.get(i), texts.get(j), 9) < 10) {
                    out.computeIfAbsent(relPaths.get(i), k -> new ArrayList<>()).add(relPaths.get(j));
                }
            }
        }

        for (String k : out.keySet()) {
            System.out.println(k);
            List<String> list = out.get(k);
            Collections.sort(list);
            for (String c : list) System.out.println(c);
        }
    }

    static void collect(File f, List<String> res) {
        if (!f.exists()) return;
        if (f.isDirectory()) {
            File[] arr = f.listFiles();
            if (arr == null) return;
            for (File x : arr) collect(x, res);
        } else if (f.getName().endsWith(".java")) res.add(f.getAbsolutePath());
    }

    static String readFile(String p) throws Exception {
        try {
            return Files.readString(Paths.get(p));
        } catch (MalformedInputException e) {
            throw e;
        }
    }

    static String clean(String s) {
        String[] lines = s.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String t = line.trim();
            if (t.startsWith("package ") || t.startsWith("import ")) continue;
            sb.append(line).append('\n');
        }

        String noCom = removeComments(sb.toString());

        StringBuilder b = new StringBuilder();
        boolean sp = false;
        for (char c : noCom.toCharArray()) {
            if (c < 33) {
                if (!sp) b.append(' ');
                sp = true;
            } else {
                b.append(c);
                sp = false;
            }
        }
        return b.toString().trim();
    }

    static String removeComments(String s) {
        StringBuilder out = new StringBuilder();
        int n = s.length();
        boolean block = false;
        for (int i = 0; i < n; i++) {
            if (!block && i + 1 < n && s.charAt(i) == '/' && s.charAt(i + 1) == '*') { block = true; i++; continue; }
            if (block && i + 1 < n && s.charAt(i) == '*' && s.charAt(i + 1) == '/') { block = false; i++; continue; }
            if (!block && i + 1 < n && s.charAt(i) == '/' && s.charAt(i + 1) == '/') {
                while (i < n && s.charAt(i) != '\n') i++;
                continue;
            }
            if (!block) out.append(s.charAt(i));
        }
        return out.toString();
    }

    static Set<String> buildNgrams(String s) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i + NGRAM <= s.length(); i++) set.add(s.substring(i, i + NGRAM));
        return set;
    }

    static boolean prefixSimilar(String a, String b) {
        int m = Math.min(a.length(), b.length());
        int diff = 0;
        for (int i = 0; i < m; i++) if (a.charAt(i) != b.charAt(i)) {
            if (++diff > 5) return false;
        }
        return true;
    }

    static boolean ngramClose(Set<String> a, Set<String> b) {
        int matches = 0;
        for (String x : a) if (b.contains(x)) matches++;
        int need = Math.min(a.size(), b.size()) / 2;
        return matches >= need;
    }

    static int levCut(String a, String b, int limit) {
        int n = a.length(), m = b.length();
        if (Math.abs(n - m) > limit) return limit + 1;
        int[] dp = new int[m + 1];
        for (int j = 0; j <= m; j++) dp[j] = j;
        for (int i = 1; i <= n; i++) {
            int prev = dp[0]; dp[0] = i;
            int rowMin = dp[0];
            for (int j = 1; j <= m; j++) {
                int tmp = dp[j];
                if (a.charAt(i - 1) == b.charAt(j - 1)) dp[j] = prev;
                else dp[j] = 1 + Math.min(prev, Math.min(dp[j], dp[j - 1]));
                prev = tmp;
                if (dp[j] < rowMin) rowMin = dp[j];
            }
            if (rowMin > limit) return limit + 1;
        }
        return dp[m];
    }
}