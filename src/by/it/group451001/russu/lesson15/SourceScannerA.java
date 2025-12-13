package by.it.group451001.russu.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileInfo> results = new ArrayList<>();

        try {
            Files.walk(Paths.get(src))
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String content;
                            try {
                                content = Files.readString(path, Charset.defaultCharset());
                            } catch (MalformedInputException e) {
                                try {
                                    content = Files.readString(path, Charset.forName("ISO-8859-1"));
                                } catch (IOException ex) {
                                    return;
                                }
                            }

                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            StringBuilder sb = new StringBuilder();
                            for (String line : content.split("\n")) {
                                String trimmed = line.trim();
                                if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                                    continue;
                                }
                                sb.append(line).append("\n");
                            }

                            String processed = trimLowChars(sb.toString());

                            int size = processed.getBytes(Charset.defaultCharset()).length;

                            String relative = src.endsWith(File.separator)
                                    ? src
                                    : src + File.separator;
                            String relPath = path.toString().substring(relative.length());

                            results.add(new FileInfo(size, relPath));
                        } catch (IOException ignored) {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        results.sort(Comparator
                .comparingInt(FileInfo::size)
                .thenComparing(FileInfo::path));

        for (FileInfo fi : results) {
            System.out.println(fi.size + " " + fi.path);
        }
    }

    private static String trimLowChars(String text) {
        int start = 0;
        int end = text.length();

        while (start < end && text.charAt(start) < 33) {
            start++;
        }
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }
        return text.substring(start, end);
    }

    private static class FileInfo {
        int size;
        String path;

        FileInfo(int size, String path) {
            this.size = size;
            this.path = path;
        }

        int size() {
            return size;
        }

        String path() {
            return path;
        }
    }
}
