package by.it.group410902.dziatko.lesson15;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        Path srcPath = Paths.get(System.getProperty("user.dir"), "src");

        List<FileText> items = new ArrayList<>();
        try {
            Files.walk(srcPath)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String raw = readFileSafe(p);
                            if (raw.contains("@Test") || raw.contains("org.junit.Test")) {
                                return;
                            }
                            String processed = processText(raw);
                            if (processed.isEmpty()) {
                                return;
                            }
                            String rel = srcPath.relativize(p).toString();
                            items.add(new FileText(rel, processed));
                        } catch (IOException e) {
                            System.err.println("Ошибка чтения: " + p + " -> " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка обхода каталога src: " + e.getMessage());
            return;
        }

        Map<String, List<String>> copies = findCopies(items, 9);

        List<String> keys = new ArrayList<>(copies.keySet());
        Collections.sort(keys);
        for (String k : keys) {
            System.out.println(k);
            List<String> cs = new ArrayList<>(copies.get(k));
            Collections.sort(cs);
            for (String c : cs) {
                System.out.println("  -> " + c);
            }
            System.out.println("----------------------------------------");
        }

    }

    private static String readFileSafe(Path path) throws IOException {
        // Порядок: UTF-8 -> Windows-1251 -> ISO-8859-1
        Charset[] tryCharsets = new Charset[] {
                Charset.forName("UTF-8"),
                Charset.forName("Windows-1251"),
                Charset.forName("ISO-8859-1")
        };
        for (Charset cs : tryCharsets) {
            try {
                byte[] bytes = Files.readAllBytes(path);
                return new String(bytes, cs);
            } catch (MalformedInputException ex) {
            }
        }
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, Charset.forName("ISO-8859-1"));
    }

    private static String processText(String text) {
        String noPkgImpNoComments = stripPkgImportsAndComments(text);
        String normalized = collapseControlToSpace(noPkgImpNoComments);
        return normalized.trim();
    }

    private static String stripPkgImportsAndComments(String s) {
        StringBuilder out = new StringBuilder(s.length());
        boolean inBlock = false;
        boolean inLine = false;
        boolean inString = false;
        boolean inChar = false;

        int i = 0;
        StringBuilder lineBuf = new StringBuilder(256);

        while (i < s.length()) {
            char c = s.charAt(i);
            char next = (i + 1 < s.length()) ? s.charAt(i + 1) : '\0';

            if (inLine) {
                if (c == '\n') {
                    appendProcessedLine(out, lineBuf);
                    lineBuf.setLength(0);
                    out.append('\n');
                    inLine = false;
                }
                i++;
                continue;
            }

            if (inBlock) {
                if (c == '*' && next == '/') {
                    inBlock = false;
                    i += 2;
                } else {
                    i++;
                }
                continue;
            }

            if (!inString && !inChar && c == '/' && next == '/') {
                inLine = true;
                i += 2;
                continue;
            }
            if (!inString && !inChar && c == '/' && next == '*') {
                inBlock = true;
                i += 2;
                continue;
            }

            if (!inChar && c == '"' && (i == 0 || s.charAt(i - 1) != '\\')) {
                inString = !inString;
                lineBuf.append(c);
                i++;
                continue;
            }
            if (!inString && c == '\'' && (i == 0 || s.charAt(i - 1) != '\\')) {
                inChar = !inChar;
                lineBuf.append(c);
                i++;
                continue;
            }

            lineBuf.append(c);

            if (c == '\n') {
                appendProcessedLine(out, lineBuf);
                lineBuf.setLength(0);
            }

            i++;
        }

        if (lineBuf.length() > 0) {
            appendProcessedLine(out, lineBuf);
        }

        return out.toString();
    }

    private static void appendProcessedLine(StringBuilder out, StringBuilder lineBuf) {
        String line = lineBuf.toString();
        String trimmed = line.stripLeading();
        if (startsWithWord(trimmed, "package") || startsWithWord(trimmed, "import")) {
            return;
        }
        out.append(line);
    }

    private static boolean startsWithWord(String s, String word) {
        if (!s.startsWith(word)) return false;
        int len = word.length();
        return s.length() == len || Character.isWhitespace(s.charAt(len));
    }

    private static String collapseControlToSpace(String s) {
        StringBuilder out = new StringBuilder(s.length());
        boolean inLowSeq = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 33) {
                if (!inLowSeq) {
                    out.append(' ');
                    inLowSeq = true;
                }
            } else {
                out.append(c);
                inLowSeq = false;
            }
        }
        return out.toString();
    }

    private static Map<String, List<String>> findCopies(List<FileText> items, int maxEdits) {
        Map<Integer, List<FileText>> byLen = new HashMap<>();
        for (FileText ft : items) {
            byLen.computeIfAbsent(ft.text.length(), k -> new ArrayList<>()).add(ft);
        }

        List<Integer> lengths = new ArrayList<>(byLen.keySet());
        Collections.sort(lengths);

        Map<String, List<String>> result = new HashMap<>();

        for (FileText ft : items) {
            ft.fingerprint = fingerprint(ft.text);
        }

        for (FileText a : items) {
            List<String> copies = new ArrayList<>();

            int lenA = a.text.length();
            int from = lenA - maxEdits;
            int to = lenA + maxEdits;

            for (int len : lengths) {
                if (len < from) continue;
                if (len > to) break;

                for (FileText b : byLen.get(len)) {
                    if (a.path.equals(b.path)) continue;
                    if (!prefixLikelySimilar(a.fingerprint, b.fingerprint)) {
                        continue;
                    }

                    if (a.text.equals(b.text)) {
                        copies.add(b.path);
                        continue;
                    }

                    int dist = levenshteinBounded(a.text, b.text, maxEdits);
                    if (dist <= maxEdits) {
                        copies.add(b.path);
                    }
                }
            }

            if (!copies.isEmpty()) {
                result.put(a.path, copies);
            }
        }

        return result;
    }

    private static String fingerprint(String s) {
        int limit = Math.min(64, s.length());
        StringBuilder fp = new StringBuilder(limit);
        for (int i = 0; i < limit; i++) {
            char c = s.charAt(i);
            if (!Character.isWhitespace(c)) {
                fp.append(Character.toLowerCase(c));
            }
        }
        return fp.toString();
    }

    private static boolean prefixLikelySimilar(String a, String b) {
        int n = Math.min(a.length(), b.length());
        if (n == 0) return true;
        int eq = 0;
        for (int i = 0; i < n; i++) {
            if (a.charAt(i) == b.charAt(i)) eq++;
        }
        if (n < 8) return true;
        return eq * 2 >= n;
    }

    private static int levenshteinBounded(String a, String b, int maxEdits) {
        int n = a.length();
        int m = b.length();

        int diff = Math.abs(n - m);
        if (diff > maxEdits) return maxEdits + 1;

        if (n > m) {
            String tmp = a; a = b; b = tmp;
            int t = n; n = m; m = t;
        }

        int band = maxEdits;
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            char ca = a.charAt(i - 1);

            int from = Math.max(1, i - band);
            int to = Math.min(m, i + band);

            if (from > to) return maxEdits + 1;

            curr[0] = i;

            for (int j = from; j <= to; j++) {
                char cb = b.charAt(j - 1);
                int cost = (ca == cb) ? 0 : 1;

                int del = prev[j] + 1;
                int ins = curr[j - 1] + 1;
                int sub = prev[j - 1] + cost;

                int v = Math.min(Math.min(del, ins), sub);
                curr[j] = v;
            }

            for (int j = 0; j < from; j++) curr[j] = maxEdits + 1;
            for (int j = to + 1; j <= m; j++) curr[j] = maxEdits + 1;

            int rowMin = maxEdits + 1;
            for (int j = from; j <= to; j++) {
                rowMin = Math.min(rowMin, curr[j]);
            }
            if (rowMin > maxEdits) return maxEdits + 1;

            int[] tmp = prev; prev = curr; curr = tmp;
        }

        int dist = prev[m];
        return dist;
    }

    private static class FileText {
        final String path;
        final String text;
        String fingerprint;

        FileText(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }
}

