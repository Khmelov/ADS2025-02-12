package by.it.group410902.grigorev.lesson15;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerB {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileInfo> results = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(Paths.get(src))) {
            stream
                    .filter(p -> !Files.isDirectory(p))
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {

                            String text = Files.readString(path, StandardCharsets.UTF_8);

                            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                            List<String> lines = Arrays.asList(text.split("\n"));
                            List<String> filtered = new ArrayList<>();
                            for (String line : lines) {
                                line = line.replace("\r", "");
                                String trimmed = line.trim();
                                if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) continue;
                                filtered.add(line);
                            }
                            String joined = String.join("\n", filtered);

                            joined = removeComments(joined);

                            joined = trimControlChars(joined);

                            lines = Arrays.asList(joined.split("\n"));
                            filtered = new ArrayList<>();
                            for (String line : lines) {
                                if (line.trim().isEmpty()) continue;
                                filtered.add(line);
                            }
                            joined = String.join("\n", filtered);

                            String rel = src.endsWith(File.separator)
                                    ? path.toString().substring(src.length())
                                    : path.toString().replace(src, "");

                            int byteSize = joined.getBytes(StandardCharsets.UTF_8).length;

                            results.add(new FileInfo(rel, byteSize));
                        } catch (MalformedInputException e) {
                            // Корректно игнорируем некорректные файлы
                        } catch (IOException e) {
                            // Игнорируем другие IO-ошибки
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка обхода файлов: " + e.getMessage());
        }

        results.stream()
                .sorted(Comparator.comparingInt(FileInfo::getSize)
                        .thenComparing(FileInfo::getRelPath))
                .forEach(info ->
                        System.out.println(info.size + " " + info.relPath)
                );
    }

    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder();
        int len = text.length();
        boolean isBlock = false, isLine = false;
        for (int i = 0; i < len; ) {
            if (!isBlock && !isLine && i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '*') {
                isBlock = true; i += 2; continue;
            }
            if (isBlock && i + 1 < len && text.charAt(i) == '*' && text.charAt(i + 1) == '/') {
                isBlock = false; i += 2; continue;
            }
            if (!isBlock && !isLine && i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '/') {
                isLine = true; i += 2; continue;
            }
            if (isLine && (text.charAt(i) == '\n' || text.charAt(i) == '\r')) {
                isLine = false; sb.append(text.charAt(i)); i++; continue;
            }
            if (isBlock || isLine) { i++; continue; }
            sb.append(text.charAt(i)); i++;
        }
        return sb.toString();
    }

    private static String trimControlChars(String text) {
        int start = 0, end = text.length();
        while (start < end && text.charAt(start) < 33) ++start;
        while (end > start && text.charAt(end - 1) < 33) --end;
        return text.substring(start, end);
    }

    // Класс для хранения информации о файле
    static class FileInfo {
        final String relPath;
        final int size;
        FileInfo(String relPath, int size) {
            this.relPath = relPath;
            this.size = size;
        }
        int getSize() { return size; }
        String getRelPath() { return relPath; }
    }
}

