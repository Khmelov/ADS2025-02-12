package by.it.group410902.gavlev.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.nio.ByteBuffer;
import java.util.concurrent.*;

public class SourceScannerC {

    public static class FFile {
        public int size;
        public String path;
        public boolean checked = false;
        public String content;

        public FFile(int size, String path, String content) {
            this.size = size;
            this.path = path;
            this.content = content;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<FFile> files = new ArrayList<>();

        Files.walk(srcPath).filter(p -> p.toString().endsWith(".java")).forEach(path -> {
            String text = safeReadFile(path);
            if (text == null) return;
            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

            String processed = removeComments(text);
            processed = processText(processed);

            if (processed.length() > 500) processed = processed.substring(0, 500);

            Path relative = srcPath.relativize(path);
            int size = processed.getBytes(StandardCharsets.UTF_8).length;
            files.add(new FFile(size, relative.toString(), processed));
        });

        files.sort((a, b) -> {
            if (a.content.length() != b.content.length())
                return Integer.compare(a.content.length(), b.content.length());
            return a.path.compareTo(b.path);
        });

        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        List<Future<?>> tasks = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            final int idx = i;
            tasks.add(pool.submit(() -> {
                FFile f1 = files.get(idx);
                if (f1.checked || f1.content.length() < 11) return;
                List<String> copies = new ArrayList<>();
                for (int j = idx + 1; j < files.size(); j++) {
                    FFile f2 = files.get(j);
                    if (f2.checked || f2.content.length() < 8) continue;

                    if (Math.abs(f1.content.length() - f2.content.length()) > 10) break;
                    if (!f1.content.regionMatches(0, f2.content, 0,
                            Math.min(100, Math.min(f1.content.length(), f2.content.length())))) continue;

                    int dist = levenshtein(f1.content, f2.content);
                    if (dist < 10) {
                        copies.add(f2.path);
                        f2.checked = true;
                    }
                }
                if (!copies.isEmpty()) {
                    StringBuilder out = new StringBuilder();
                    out.append("\nИсходный: ").append(f1.path).append("\n");
                    for (String c : copies) out.append("Копия: ").append(c).append("\n");
                    synchronized (System.out) {
                        System.out.print(out);
                    }
                }
            }));
        }

        for (Future<?> f : tasks) {
            try { f.get(); } catch (Exception ignored) {}
        }
        pool.shutdown();
    }

    private static String safeReadFile(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            try {
                CharsetDecoder dec = StandardCharsets.UTF_8.newDecoder()
                        .onMalformedInput(CodingErrorAction.REPORT)
                        .onUnmappableCharacter(CodingErrorAction.REPORT);
                return dec.decode(ByteBuffer.wrap(bytes)).toString();
            } catch (CharacterCodingException e) {
                try {
                    CharsetDecoder dec1251 = Charset.forName("windows-1251").newDecoder()
                            .onMalformedInput(CodingErrorAction.REPORT)
                            .onUnmappableCharacter(CodingErrorAction.REPORT);
                    return dec1251.decode(ByteBuffer.wrap(bytes)).toString();
                } catch (CharacterCodingException bad) {
                    return null;
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

    private static String processText(String text) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.startsWith("package") || trimmed.startsWith("import")) continue;
                sb.append(line).append("\n");
            }
        } catch (IOException ignored) {}
        return onlySpaces(sb.toString());
    }

    private static String onlySpaces(String s) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (c < 33) {
                while (i < s.length() && s.charAt(i) < 33) i++;
                out.append(' ');
            } else {
                out.append(c);
                i++;
            }
        }
        return out.toString().trim();
    }

    private static String removeComments(String s) {
        StringBuilder out = new StringBuilder();
        int n = s.length();
        int i = 0;
        boolean inString = false, inChar = false, inBlock = false, inLine = false;
        while (i < n) {
            char c = s.charAt(i);
            if (inString) {
                out.append(c);
                if (c == '"' && s.charAt(i - 1) != '\\') inString = false;
                i++; continue;
            }
            if (inChar) {
                out.append(c);
                if (c == '\'' && s.charAt(i - 1) != '\\') inChar = false;
                i++; continue;
            }
            if (inLine) {
                if (c == '\n') { out.append('\n'); inLine = false; }
                i++; continue;
            }
            if (inBlock) {
                if (c == '*' && i + 1 < n && s.charAt(i + 1) == '/') { inBlock = false; i += 2; }
                else i++;
                continue;
            }
            if (c == '"') { inString = true; out.append(c); i++; continue; }
            if (c == '\'') { inChar = true; out.append(c); i++; continue; }
            if (c == '/' && i + 1 < n && s.charAt(i + 1) == '/') { inLine = true; i += 2; continue; }
            if (c == '/' && i + 1 < n && s.charAt(i + 1) == '*') { inBlock = true; i += 2; continue; }
            out.append(c); i++;
        }
        return out.toString();
    }

    public static int levenshtein(String a, String b) {
        int n = a.length(), m = b.length();
        if (Math.abs(n - m) >= 10) return 10;
        int[] prev = new int[m + 1], curr = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;
        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            int minInRow = curr[0];
            char ca = a.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                int cost = (ca == b.charAt(j - 1)) ? 0 : 1;
                int v = prev[j] + 1, h = curr[j - 1] + 1, d = prev[j - 1] + cost;
                int res = v < h ? (v < d ? v : d) : (h < d ? h : d);
                curr[j] = res;
                if (res < minInRow) minInRow = res;
            }
            if (minInRow >= 10) return 10;
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[m];
    }
}
