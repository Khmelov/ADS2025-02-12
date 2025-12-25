package by.it.group410902.jalilova.lesson15;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerA {

    public static void main(String[] args) {
        new SourceScannerA().process();
    }

    private void process() {
        Path root = Paths.get(System.getProperty("user.dir"), "src");

        if (!Files.exists(root)) {
            System.out.println("src directory not found");
            return;
        }

        List<FileInfo> results = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(root)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> processFile(p, root, results));
        } catch (IOException e) {
            e.printStackTrace();
        }

        results.sort(Comparator
                .comparingLong((FileInfo f) -> f.size)
                .thenComparing(f -> f.relativePath));

        for (FileInfo info : results) {
            System.out.println(info.size + " " + info.relativePath);
        }
    }

    private void processFile(Path file, Path root, List<FileInfo> outList) {
        String text;

        try {
            text = Files.readString(file);
        } catch (MalformedInputException e) {
            return;
        } catch (IOException e) {
            return;
        }

        if (text.contains("@Test") || text.contains("org.junit.Test")) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String line : text.split("\n")) {
            String trimmed = line.trim();
            if (trimmed.startsWith("package ") || trimmed.startsWith("import "))
                continue;
            sb.append(line).append("\n");
        }

        String processed = sb.toString();

        processed = trimLowChars(processed);

        long size = processed.getBytes(java.nio.charset.StandardCharsets.UTF_8).length;

        String relative = root.relativize(file).toString().replace("/", "\\");

        outList.add(new FileInfo(size, relative));
    }

    private String trimLowChars(String s) {
        int start = 0;
        int end = s.length() - 1;

        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;

        if (start > end) return "";
        return s.substring(start, end + 1);
    }

    private static class FileInfo {
        long size;
        String relativePath;

        FileInfo(long size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}