package by.it.group451001.suprunovich.lesson15;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {
    public static void main(String[] args) throws Exception {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path root = Paths.get(src);

        List<Result> results = new ArrayList<>();

        Files.walk(root).forEach(p -> {
            if (!Files.isRegularFile(p)) return;
            if (!p.toString().endsWith(".java")) return;

            String text;
            try {
                text = readFileIgnoringErrors(p);
            } catch (IOException e) {
                return;
            }

            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

            String cleaned = cleanText(text);
            int size = cleaned.getBytes(Charset.defaultCharset()).length;

            String rel = root.relativize(p).toString();
            results.add(new Result(size, rel));
        });

        results.sort((a, b) -> {
            int c = Integer.compare(a.size, b.size);
            if (c != 0) return c;
            return a.path.compareTo(b.path);
        });

        for (Result r : results) {
            System.out.println(r.size + " " + r.path);
        }
    }

    static String readFileIgnoringErrors(Path p) throws IOException {
        try {
            return Files.readString(p);
        } catch (MalformedInputException e) {
            return new String(Files.readAllBytes(p), Charset.defaultCharset());
        }
    }

    static String cleanText(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        boolean inLineComment = false;
        boolean inBlockComment = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    sb.append(c);
                }
                continue;
            }

            if (inBlockComment) {
                if (c == '*' && i + 1 < text.length() && text.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i++;
                }
                continue;
            }

            if (c == '/' && i + 1 < text.length()) {
                char n = text.charAt(i + 1);
                if (n == '/') {
                    inLineComment = true;
                    i++;
                    continue;
                }
                if (n == '*') {
                    inBlockComment = true;
                    i++;
                    continue;
                }
            }

            sb.append(c);
        }

        String[] lines = sb.toString().split("\n", -1);
        StringBuilder out = new StringBuilder();
        for (String line : lines) {
            String t = line.trim();
            if (t.startsWith("package ") || t.startsWith("import ")) continue;
            if (t.isEmpty()) continue;
            out.append(line).append('\n');
        }

        String s = out.toString();
        int start = 0;
        while (start < s.length() && s.charAt(start) < 33) start++;
        int end = s.length() - 1;
        while (end >= 0 && s.charAt(end) < 33) end--;
        if (end < start) return "";
        return s.substring(start, end + 1);
    }

    static class Result {
        int size;
        String path;
        Result(int s, String p) { size = s; path = p; }
    }
}