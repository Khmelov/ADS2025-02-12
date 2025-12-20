package by.it.group451003.yepanchuntsau.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SourceScannerC {

    private static String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            for (String line : Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        } catch (MalformedInputException e) {
            sb.setLength(0);
            try {
                for (String line : Files.readAllLines(file.toPath(), StandardCharsets.ISO_8859_1)) {
                    sb.append(line).append('\n');
                }
                return sb.toString();
            } catch (IOException ex) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    private static void collectJavaFiles(File dir, List<File> out) {
        File[] list = dir.listFiles();
        if (list == null) return;
        for (File f : list) {
            if (f.isDirectory()) {
                collectJavaFiles(f, out);
            } else if (f.isFile() && f.getName().endsWith(".java")) {
                out.add(f);
            }
        }
    }

    private static String removePackageAndImports(String text) {
        String[] lines = text.split("\\R");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue;
            }
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

    private static String removeComments(String text) {
        char[] a = text.toCharArray();
        int n = a.length;
        StringBuilder sb = new StringBuilder(n);
        int i = 0;
        int state = 0;
        while (i < n) {
            char c = a[i];
            if (state == 0) {
                if (c == '"') {
                    state = 3;
                    sb.append(c);
                    i++;
                } else if (c == '\'') {
                    state = 4;
                    sb.append(c);
                    i++;
                } else if (c == '/') {
                    if (i + 1 < n) {
                        char c2 = a[i + 1];
                        if (c2 == '/') {
                            state = 1;
                            i += 2;
                        } else if (c2 == '*') {
                            state = 2;
                            i += 2;
                        } else {
                            sb.append(c);
                            i++;
                        }
                    } else {
                        sb.append(c);
                        i++;
                    }
                } else {
                    sb.append(c);
                    i++;
                }
            } else if (state == 1) {
                if (c == '\n' || c == '\r') {
                    sb.append(c);
                    state = 0;
                }
                i++;
            } else if (state == 2) {
                if (c == '*' && i + 1 < n && a[i + 1] == '/') {
                    i += 2;
                    state = 0;
                } else {
                    i++;
                }
            } else if (state == 3) {
                sb.append(c);
                if (c == '\\' && i + 1 < n) {
                    sb.append(a[i + 1]);
                    i += 2;
                } else if (c == '"') {
                    state = 0;
                    i++;
                } else {
                    i++;
                }
            } else {
                sb.append(c);
                if (c == '\\' && i + 1 < n) {
                    sb.append(a[i + 1]);
                    i += 2;
                } else if (c == '\'') {
                    state = 0;
                    i++;
                } else {
                    i++;
                }
            }
        }
        return sb.toString();
    }

    private static String normalizeWhitespace(String text) {
        char[] a = text.toCharArray();
        int n = a.length;
        StringBuilder sb = new StringBuilder(n);
        boolean inSpace = false;
        for (int i = 0; i < n; i++) {
            char c = a[i];
            if (c < 33) {
                if (!inSpace) {
                    sb.append(' ');
                    inSpace = true;
                }
            } else {
                sb.append(c);
                inSpace = false;
            }
        }
        return sb.toString().trim();
    }

    private static int levenshtein(String a, String b, int limit) {
        int n = a.length();
        int m = b.length();
        int diff = n - m;
        if (diff < 0) diff = -diff;
        if (diff >= limit) return limit;
        if (n == 0) return m < limit ? m : limit;
        if (m == 0) return n < limit ? n : limit;
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;
        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            int minRow = curr[0];
            char ca = a.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char cb = b.charAt(j - 1);
                int cost = (ca == cb) ? 0 : 1;
                int v1 = prev[j] + 1;
                int v2 = curr[j - 1] + 1;
                int v3 = prev[j - 1] + cost;
                int v = v1;
                if (v2 < v) v = v2;
                if (v3 < v) v = v3;
                curr[j] = v;
                if (v < minRow) minRow = v;
            }
            if (minRow >= limit) return limit;
            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }
        int res = prev[m];
        return res < limit ? res : limit;
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        File root = new File(src);
        List<File> files = new ArrayList<>();
        collectJavaFiles(root, files);
        List<String> paths = new ArrayList<>();
        List<String> texts = new ArrayList<>();
        for (File f : files) {
            String content = readFile(f);
            if (content == null) continue;
            if (content.contains("@Test") || content.contains("org.junit.Test")) continue;
            content = removePackageAndImports(content);
            content = removeComments(content);
            content = normalizeWhitespace(content);
            if (content.isEmpty()) continue;
            String path = f.getPath();
            paths.add(path);
            texts.add(content);
        }
        int n = texts.size();
        if (n == 0) return;
        int[] minDist = new int[n];
        @SuppressWarnings("unchecked")
        List<Integer>[] best = new ArrayList[n];
        Arrays.fill(minDist, Integer.MAX_VALUE);
        for (int i = 0; i < n; i++) {
            best[i] = new ArrayList<>();
        }
        for (int i = 0; i < n; i++) {
            String ti = texts.get(i);
            for (int j = i + 1; j < n; j++) {
                String tj = texts.get(j);
                int d = levenshtein(ti, tj, 10);
                if (d < 10) {
                    if (d < minDist[i]) {
                        minDist[i] = d;
                        best[i].clear();
                        best[i].add(j);
                    } else if (d == minDist[i]) {
                        best[i].add(j);
                    }
                    if (d < minDist[j]) {
                        minDist[j] = d;
                        best[j].clear();
                        best[j].add(i);
                    } else if (d == minDist[j]) {
                        best[j].add(i);
                    }
                }
            }
        }
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (minDist[i] < 10 && !best[i].isEmpty()) {
                indices.add(i);
            }
        }
        if (indices.isEmpty()) return;
        Collections.sort(indices, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return paths.get(o1).compareTo(paths.get(o2));
            }
        });
        StringBuilder out = new StringBuilder();
        boolean firstGroup = true;
        for (Integer idx : indices) {
            int i = idx;
            if (minDist[i] >= 10 || best[i].isEmpty()) continue;
            if (!firstGroup) out.append(System.lineSeparator());
            firstGroup = false;
            out.append(paths.get(i)).append(System.lineSeparator());
            List<Integer> copies = new ArrayList<>(best[i]);
            Collections.sort(copies, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return paths.get(o1).compareTo(paths.get(o2));
                }
            });
            for (int j : copies) {
                out.append(paths.get(j)).append(System.lineSeparator());
            }
        }
        System.out.print(out.toString());
    }
}
