package by.it.group451002.ivash.lesson15;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {

    private static class FileInfo {
        String path;
        long size;

        FileInfo(String path, long size) {
            this.path = path;
            this.size = size;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path srcDir = Paths.get(src);
        List<FileInfo> result = new ArrayList<>();

        try {
            Files.walk(srcDir)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> process(p, srcDir, result));
        } catch (IOException ignored) {
        }

        result.sort((a, b) -> {
            int c = Long.compare(a.size, b.size);
            if (c != 0) return c;
            return a.path.compareTo(b.path);
        });

        for (FileInfo f : result) {
            System.out.println(f.size + " " + f.path);
        }
    }

    private static void process(Path file, Path srcDir, List<FileInfo> result) {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br =
                     Files.newBufferedReader(file, StandardCharsets.UTF_8)) {

            String line;
            boolean isTest = false;

            while ((line = br.readLine()) != null) {
                if (line.contains("@Test") || line.contains("org.junit.Test")) {
                    isTest = true;
                    break;
                }
                sb.append(line).append('\n');
            }

            if (isTest) return;

        } catch (MalformedInputException e) {
            return; // корректно игнорируем битую кодировку
        } catch (IOException e) {
            return;
        }

        String text = sb.toString();
        text = removePackageAndImports(text);
        text = removeComments(text);
        text = trim(text);
        text = removeEmptyLines(text);

        long size = text.getBytes(StandardCharsets.UTF_8).length;
        String relativePath = srcDir.relativize(file).toString();

        result.add(new FileInfo(relativePath, size));
    }

    // ---------- O(n) ----------

    private static String removePackageAndImports(String text) {
        StringBuilder res = new StringBuilder();
        int i = 0, n = text.length();

        while (i < n) {
            int start = i;
            while (i < n && text.charAt(i) != '\n') i++;
            int end = i;
            if (i < n) i++;

            String line = text.substring(start, end).trim();
            if (line.startsWith("package ") || line.startsWith("import ")) {
                continue;
            }
            res.append(text, start, i);
        }
        return res.toString();
    }

    private static String removeComments(String text) {
        StringBuilder res = new StringBuilder();
        int n = text.length();

        boolean line = false;
        boolean block = false;

        for (int i = 0; i < n; i++) {
            char c = text.charAt(i);

            if (line) {
                if (c == '\n') {
                    line = false;
                    res.append(c);
                }
                continue;
            }

            if (block) {
                if (c == '*' && i + 1 < n && text.charAt(i + 1) == '/') {
                    block = false;
                    i++;
                }
                continue;
            }

            if (c == '/' && i + 1 < n) {
                char next = text.charAt(i + 1);
                if (next == '/') {
                    line = true;
                    i++;
                    continue;
                }
                if (next == '*') {
                    block = true;
                    i++;
                    continue;
                }
            }

            res.append(c);
        }

        return res.toString();
    }

    private static String trim(String s) {
        int l = 0, r = s.length();
        while (l < r && s.charAt(l) < 33) l++;
        while (r > l && s.charAt(r - 1) < 33) r--;
        return s.substring(l, r);
    }

    private static String removeEmptyLines(String text) {
        StringBuilder res = new StringBuilder();
        int i = 0, n = text.length();

        while (i < n) {
            int start = i;
            boolean hasChar = false;

            while (i < n && text.charAt(i) != '\n') {
                if (text.charAt(i) >= 33) hasChar = true;
                i++;
            }
            if (i < n) i++;

            if (hasChar) {
                res.append(text, start, i);
            }
        }
        return res.toString();
    }
}
