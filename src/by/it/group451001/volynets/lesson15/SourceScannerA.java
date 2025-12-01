package by.it.group451001.volynets.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
        import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Paths.get(src);

        List<Result> results = new ArrayList<>();
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (!attrs.isRegularFile()) return FileVisitResult.CONTINUE;
                if (!file.getFileName().toString().endsWith(".java")) return FileVisitResult.CONTINUE;

                String text = readTextSafely(file);
                if (text == null) return FileVisitResult.CONTINUE;

                if (containsTestMarkers(text)) return FileVisitResult.CONTINUE;

                String cleaned = stripPackageAndImports(text);
                cleaned = trimControlEdges(cleaned);

                int sizeBytes = cleaned.getBytes(Charset.forName("UTF-8")).length;

                // Важно: относительный путь в системном формате (без принудительной замены на '/')
                String rel = root.relativize(file).toString();

                results.add(new Result(rel, sizeBytes));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });

        results.sort((a, b) -> {
            int cmp = Integer.compare(a.size, b.size);
            return (cmp != 0) ? cmp : a.path.compareTo(b.path);
        });

        for (Result r : results) {
            System.out.println(r.size + " " + r.path);
        }
    }

    static class Result {
        final String path;
        final int size;
        Result(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }

    static String readTextSafely(Path file) {
        List<Charset> candidates = List.of(
                Charset.forName("UTF-8"),
                Charset.forName("windows-1251"),
                Charset.forName("ISO-8859-1")
        );
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(file);
        } catch (IOException e) {
            return null;
        }
        for (Charset cs : candidates) {
            try {
                return new String(bytes, cs);
            } catch (Exception e) {
                if (!(e instanceof MalformedInputException)) {
                    // для других ошибок чтения/декодирования — прерываем
                    return null;
                }
                // MalformedInputException — пробуем следующий charset
            }
        }
        return null;
    }

    static boolean containsTestMarkers(String text) {
        return text.contains("@Test") || text.contains("org.junit.Test");
    }

    static String stripPackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;
        while (i < len) {
            int lineStart = i;
            int lineEnd = i;
            while (lineEnd < len) {
                char c = text.charAt(lineEnd);
                if (c == '\n') break;
                lineEnd++;
            }
            String line = text.substring(lineStart, lineEnd);
            String trimmed = fastTrim(line);
            boolean skip = false;
            if (!trimmed.isEmpty()) {
                int p = firstNonControl(trimmed);
                if (p < trimmed.length()) {
                    if (startsWithWord(trimmed, p, "package")) skip = true;
                    else if (startsWithWord(trimmed, p, "import")) skip = true;
                }
            }
            if (!skip) {
                sb.append(line);
            }
            if (lineEnd < len) {
                sb.append('\n');
                i = lineEnd + 1;
            } else {
                i = lineEnd;
            }
        }
        return sb.toString();
    }

    static String fastTrim(String s) {
        int l = 0, r = s.length() - 1;
        while (l <= r && s.charAt(l) < 33) l++;
        while (r >= l && s.charAt(r) < 33) r--;
        return (l <= r) ? s.substring(l, r + 1) : "";
    }

    static int firstNonControl(String s) {
        int i = 0;
        while (i < s.length() && s.charAt(i) < 33) i++;
        return i;
    }

    static boolean startsWithWord(String s, int pos, String word) {
        int wLen = word.length();
        if (pos + wLen > s.length()) return false;
        for (int i = 0; i < wLen; i++) {
            if (s.charAt(pos + i) != word.charAt(i)) return false;
        }
        int next = pos + wLen;
        if (next >= s.length()) return true;
        char c = s.charAt(next);
        return c <= 32 || c == '\t';
    }

    static String trimControlEdges(String text) {
        int l = 0, r = text.length() - 1;
        while (l <= r && text.charAt(l) < 33) l++;
        while (r >= l && text.charAt(r) < 33) r--;
        return (l <= r) ? text.substring(l, r + 1) : "";
    }
}
