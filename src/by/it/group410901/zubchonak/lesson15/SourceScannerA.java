package by.it.group410901.zubchonak.lesson15;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerA {

    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        Path srcRoot = Paths.get(userDir, "src");

        List<FileData> results = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(srcRoot)) {
            paths
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = safeReadString(p);
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            String cleaned = cleanCodeA(content);
                            long size = cleaned.getBytes(StandardCharsets.UTF_8).length;

                            Path relPath = srcRoot.relativize(p);
                            results.add(new FileData(size, relPath.toString()));

                        } catch (IOException e) {

                        }
                    });
        } catch (IOException ignored) {}

        results.stream()
                .sorted(Comparator
                        .comparing(FileData::size)
                        .thenComparing(FileData::path))
                .forEach(fd -> System.out.println(fd.size + " " + fd.path));
    }

    private static String safeReadString(Path path) throws IOException {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (java.nio.charset.MalformedInputException e) {
            return new String(Files.readAllBytes(path), StandardCharsets.ISO_8859_1);
        }
    }

    // Удаляет package и import строки за O(n); удаляет whitespace <33 с краёв
    private static String cleanCodeA(String input) {
        if (input == null) return "";

        StringBuilder sb = new StringBuilder();
        boolean inPackageOrImport = false;

        for (String line : input.split("\n", -1)) {
            String trimmed = line.stripLeading();
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                inPackageOrImport = true;
                continue;
            }
            if (inPackageOrImport && trimmed.isEmpty()) {
                continue;
            }
            inPackageOrImport = false;
            sb.append(line).append('\n');
        }

        String result = sb.toString();
        // Удаляем символы <33 с начала и конца строки (trim по кодам <= 32)
        int start = 0, end = result.length();
        while (start < end && result.charAt(start) <= 32) start++;
        while (end > start && result.charAt(end - 1) <= 32) end--;

        return result.substring(start, end);
    }

    private static record FileData(long size, String path) {}
}