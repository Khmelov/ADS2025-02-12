package by.it.group410902.jalilova.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Paths.get(src);

        if (!Files.exists(root)) {
            System.out.println("src folder not found: " + root);
            return;
        }

        List<FileInfo> result = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(root)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .filter(p -> !isTestFile(p))            // исключаем junit тесты
                    .forEach(p -> processFile(p, root, result));
        } catch (IOException e) {
            e.printStackTrace();
        }

        result.sort((a, b) -> {
            if (a.size != b.size)
                return Long.compare(a.size, b.size);
            return a.path.compareTo(b.path);
        });

        for (FileInfo fi : result) {
            System.out.println(fi.size + " " + fi.path);
        }
    }

    private static boolean isTestFile(Path p) {
        try {
            String text = Files.readString(p);
            return text.contains("@Test") || text.contains("org.junit.Test");
        } catch (IOException e) {
            return true;
        }
    }

    private static void processFile(Path file, Path root, List<FileInfo> result) {
        String text;

        try {
            byte[] raw = Files.readAllBytes(file);
            text = new String(raw, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {

            try {
                byte[] raw = Files.readAllBytes(file);
                text = new String(raw, StandardCharsets.ISO_8859_1);
            } catch (IOException ex) {
                return;
            }
        } catch (IOException e) {
            return;
        }

        String processed = cleanCode(text);

        Path relative = root.relativize(file);
        String outPath = relative.toString().replace("/", "\\");

        result.add(new FileInfo(outPath, processed.getBytes(StandardCharsets.UTF_8).length));
    }

    private static String cleanCode(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        String[] lines = text.split("\n");

        boolean inBlockComment = false;

        for (String line : lines) {

            String trimmed = line.trim();
            if (trimmed.startsWith("package ") || trimmed.startsWith("import "))
                continue;

            StringBuilder cleared = new StringBuilder();

            int i = 0;
            while (i < line.length()) {

                if (inBlockComment) {
                    int end = line.indexOf("*/", i);
                    if (end == -1) {
                        i = line.length();
                        continue;
                    } else {
                        inBlockComment = false;
                        i = end + 2;
                        continue;
                    }
                }

                if (i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i += 2;
                    continue;
                }

                if (i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                    break;
                }

                cleared.append(line.charAt(i));
                i++;
            }

            String cleaned = trimLowChars(cleared.toString());

            if (!cleaned.isEmpty())
                sb.append(cleaned).append("\n");
        }

        return sb.toString();
    }

    private static String trimLowChars(String s) {
        int start = 0;
        int end = s.length() - 1;

        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;

        if (start > end) return "";
        return s.substring(start, end + 1);
    }

    static class FileInfo {
        String path;
        long size;

        FileInfo(String path, long size) {
            this.path = path;
            this.size = size;
        }
    }
}