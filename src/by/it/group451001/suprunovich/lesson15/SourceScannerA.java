package by.it.group451001.suprunovich.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {
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

            String cleaned = clean(text);
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

    static String clean(String text) {
        String[] lines = text.split("\n", -1);
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String t = line.trim();
            if (t.startsWith("package ")) continue;
            if (t.startsWith("import ")) continue;
            sb.append(line).append('\n');
        }
        String s = sb.toString();

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
