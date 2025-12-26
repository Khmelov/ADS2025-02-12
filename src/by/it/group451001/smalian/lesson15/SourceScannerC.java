package by.it.group451001.smalian.lesson15;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.IntStream;

public class SourceScannerC {
    private static final int THRESH = 9;

    public static void main(String[] args) throws Exception {
        Path root = Paths.get(System.getProperty("user.dir") + System.getProperty("file.separator") + "src" + System.getProperty("file.separator"));
        if (!Files.exists(root)) return;

        List<FileData> files = new ArrayList<>();
        try (var walk = Files.walk(root)) {
            walk.filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".java")).forEach(p -> {
                String txt = readUtf8Safe(p);
                if (txt == null) return;
                if (txt.contains("@Test") || txt.contains("org.junit.Test")) return;
                txt = removeComments(txt);
                txt = stripPkgImportLines(txt);
                txt = collapseCtlToSpace(txt).trim();
                files.add(new FileData(root.relativize(p).toString(), txt));
            });
        }

        files.sort(Comparator.comparing(f -> f.path));

        Map<Integer, List<FileData>> byHash = new HashMap<>();
        for (FileData f : files) byHash.computeIfAbsent(f.text.hashCode(), k -> new ArrayList<>()).add(f);

        ConcurrentSkipListMap<String, ConcurrentSkipListSet<String>> copies = new ConcurrentSkipListMap<>();
        List<FileData> uniques = new ArrayList<>();
        for (List<FileData> group : byHash.values()) {
            for (int i = 0; i < group.size(); i++) {
                for (int j = i + 1; j < group.size(); j++) {
                    FileData a = group.get(i), b = group.get(j);
                    if (a.text.equals(b.text)) {
                        copies.computeIfAbsent(a.path, k -> new ConcurrentSkipListSet<>()).add(b.path);
                        copies.computeIfAbsent(b.path, k -> new ConcurrentSkipListSet<>()).add(a.path);
                    }
                }
            }
            uniques.add(group.get(0));
        }

        Map<Integer, List<FileData>> buckets = new HashMap<>();
        for (FileData f : uniques) buckets.computeIfAbsent(f.len, k -> new ArrayList<>()).add(f);

        int n = uniques.size();
        IntStream.range(0, n).parallel().forEach(i -> {
            FileData a = uniques.get(i);
            for (int d = -THRESH; d <= THRESH; d++) {
                List<FileData> bucket = buckets.get(a.len + d);
                if (bucket == null) continue;
                for (FileData b : bucket) {
                    if (a.path.compareTo(b.path) >= 0) continue;
                    if (!quickSigMatch(a, b)) continue;
                    int dist = levenshteinWithLimit(a.text, b.text, THRESH);
                    if (dist <= THRESH) {
                        copies.computeIfAbsent(a.path, k -> new ConcurrentSkipListSet<>()).add(b.path);
                        copies.computeIfAbsent(b.path, k -> new ConcurrentSkipListSet<>()).add(a.path);
                    }
                }
            }
        });

        for (var e : copies.entrySet()) {
            System.out.println(e.getKey());
            for (String p : e.getValue()) System.out.println(p);
        }
    }

    private static String readUtf8Safe(Path p) {
        try {
            byte[] b = Files.readAllBytes(p);
            CharsetDecoder dec = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT);
            return dec.decode(ByteBuffer.wrap(b)).toString();
        } catch (Exception ex) {
            return null;
        }
    }

    private static String stripPkgImportLines(String s) {
        StringBuilder sb = new StringBuilder();
        for (String line : s.split("\\R")) {
            String t = line.trim();
            if (t.startsWith("package") || t.startsWith("import")) continue;
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

    private static String collapseCtlToSpace(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        boolean sp = false;
        for (char c : s.toCharArray()) {
            if (c < 33) {
                if (!sp) sb.append(' ');
                sp = true;
            } else {
                sb.append(c);
                sp = false;
            }
        }
        return sb.toString();
    }

    private static String removeComments(String s) {
        StringBuilder out = new StringBuilder(s.length());
        boolean inStr = false, inChar = false, inSL = false, inML = false, esc = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            char n = i + 1 < s.length() ? s.charAt(i + 1) : 0;
            if (inSL) {
                if (c == '\n') { inSL = false; out.append(c); }
            } else if (inML) {
                if (c == '*' && n == '/') { inML = false; i++; }
            } else if (inStr) {
                out.append(c);
                if (esc) esc = false;
                else if (c == '\\') esc = true;
                else if (c == '"') inStr = false;
            } else if (inChar) {
                out.append(c);
                if (esc) esc = false;
                else if (c == '\\') esc = true;
                else if (c == '\'') inChar = false;
            } else {
                if (c == '/' && n == '/') { inSL = true; i++; }
                else if (c == '/' && n == '*') { inML = true; i++; }
                else {
                    out.append(c);
                    if (c == '"') inStr = true;
                    else if (c == '\'') inChar = true;
                }
            }
        }
        return out.toString();
    }

    private static boolean quickSigMatch(FileData a, FileData b) {
        int match = 0;
        if (a.sig1 == b.sig1) match++;
        if (a.sig2 == b.sig2) match++;
        if (a.sig3 == b.sig3) match++;
        return match >= 2;
    }

    private static int levenshteinWithLimit(String a, String b, int limit) {
        if (a.equals(b)) return 0;
        int n = a.length(), m = b.length();
        if (Math.abs(n - m) > limit) return limit + 1;
        if (n == 0) return m;
        if (m == 0) return n;
        if (n > m) { String t = a; a = b; b = t; n = a.length(); m = b.length(); }
        int[] prev = new int[m + 1], cur = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;
        for (int i = 1; i <= n; i++) {
            cur[0] = i;
            char ca = a.charAt(i - 1);
            int rowMin = cur[0];
            for (int j = 1; j <= m; j++) {
                int cost = ca == b.charAt(j - 1) ? 0 : 1;
                int v = Math.min(Math.min(cur[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
                cur[j] = v;
                if (v < rowMin) rowMin = v;
            }
            if (rowMin > limit) return limit + 1;
            int[] t = prev; prev = cur; cur = t;
        }
        return prev[m];
    }

    private static class FileData {
        final String path;
        final String text;
        final int len;
        final int sig1, sig2, sig3;
        FileData(String path, String text) {
            this.path = path;
            this.text = text;
            this.len = text.length();
            int s = Math.min(64, Math.max(1, len));
            sig1 = text.substring(0, Math.min(s, len)).hashCode();
            sig2 = text.substring(len / 2, Math.min(len / 2 + s, len)).hashCode();
            sig3 = text.substring(Math.max(0, len - s), len).hashCode();
        }
    }
}
