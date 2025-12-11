package by.it.group451004.volynets.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {
    public static void main(String[] args) {
        // Получаем каталог src
        String srcDir = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(srcDir);

        List<FileInfo> results = new ArrayList<>();

        try {
            // Обходим все .java файлы рекурсивно
            Files.walk(srcPath)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            processFile(p, srcPath, results);
                        } catch (Exception e) {

                        }
                    });
        } catch (IOException e) {

        }

        results.sort((a, b) -> {
            int sizeCompare = Long.compare(a.size, b.size);
            if (sizeCompare != 0) return sizeCompare;
            return a.relativePath.compareTo(b.relativePath);
        });

        for (FileInfo info : results) {
            System.out.println(info.size + " " + info.relativePath);
        }
    }

    private static void processFile(Path filePath, Path srcPath, List<FileInfo> results) {
        try {
            byte[] fileBytes = Files.readAllBytes(filePath);
            String content;

            try {
                content = new String(fileBytes, StandardCharsets.UTF_8);
            } catch (Exception e) {
                try {
                    content = new String(fileBytes, StandardCharsets.ISO_8859_1);
                } catch (Exception e2) {
                    content = new String(fileBytes, StandardCharsets.US_ASCII);
                }
            }

            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            String processed = processContent(content);

            byte[] resultBytes = processed.getBytes(StandardCharsets.UTF_8);
            long size = resultBytes.length;

            Path relative = srcPath.relativize(filePath);
            String relativePath = relative.toString();

            results.add(new FileInfo(size, relativePath));

        } catch (IOException e) {
        }
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        int length = content.length();
        int i = 0;

        boolean inLineComment = false;
        boolean inBlockComment = false;
        boolean inString = false;
        char stringChar = '"';
        boolean escaped = false;

        StringBuilder currentLine = new StringBuilder();

        while (i < length) {
            char c = content.charAt(i);
            char nextChar = i + 1 < length ? content.charAt(i + 1) : 0;

            if (escaped) {
                currentLine.append(c);
                escaped = false;
                i++;
                continue;
            }

            if (inString) {
                if (c == '\\') {
                    escaped = true;
                    currentLine.append(c);
                } else if (c == stringChar) {
                    inString = false;
                    currentLine.append(c);
                } else {
                    currentLine.append(c);
                }
                i++;
                continue;
            }

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    currentLine.append(c);
                }
                i++;
                continue;
            }

            if (inBlockComment) {
                if (c == '*' && nextChar == '/') {
                    inBlockComment = false;
                    i += 2;
                } else {
                    i++;
                }
                continue;
            }

            if (c == '/' && nextChar == '/') {
                inLineComment = true;
                i += 2;
                continue;
            }

            if (c == '/' && nextChar == '*') {
                inBlockComment = true;
                i += 2;
                continue;
            }

            if (c == '"' || c == '\'') {
                inString = true;
                stringChar = c;
                currentLine.append(c);
                i++;
                continue;
            }

            currentLine.append(c);

            if (c == '\n') {
                String line = currentLine.toString();
                currentLine.setLength(0);

                String trimmed = line.trim();
                if (!trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                    if (!isBlankLine(line)) {
                        result.append(line);
                    }
                }
            }

            i++;
        }

        if (currentLine.length() > 0) {
            String line = currentLine.toString();
            String trimmed = line.trim();
            if (!trimmed.startsWith("package ") && !trimmed.startsWith("import ") && !trimmed.isEmpty()) {
                result.append(line);
            }
        }

        String text = result.toString();
        text = trimControlChars(text);

        return text;
    }

    private static boolean isBlankLine(String line) {
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c != '\n' && c != '\r' && c != ' ' && c != '\t') {
                return false;
            }
        }
        return true;
    }

    private static String trimControlChars(String text) {
        if (text.isEmpty()) {
            return text;
        }

        // Удаляем символы с кодом < 33 с начала
        int start = 0;
        while (start < text.length() && text.charAt(start) < 33) {
            start++;
        }

        // Удаляем символы с кодом < 33 с конца
        int end = text.length() - 1;
        while (end >= start && text.charAt(end) < 33) {
            end--;
        }

        return start <= end ? text.substring(start, end + 1) : "";
    }

    private static class FileInfo {
        final long size;
        final String relativePath;

        FileInfo(long size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}