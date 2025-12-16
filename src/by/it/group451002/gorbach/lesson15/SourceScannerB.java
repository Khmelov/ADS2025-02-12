package by.it.group451002.gorbach.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {
    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<ProcessedFile> processedFiles = new ArrayList<>();

        Files.walk(Paths.get(src))
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        processJavaFile(path, src, processedFiles);
                    } catch (IOException e) {
                        System.err.println("Ошибка обработки файла " + path + ": " + e.getMessage());
                    }
                });

        processedFiles.sort(Comparator
                .comparingInt(ProcessedFile::size)
                .thenComparing(ProcessedFile::relativePath));

        for (ProcessedFile file : processedFiles) {
            System.out.println(file.size() + " " + file.relativePath());
        }
    }

    private static void processJavaFile(Path filePath, String srcRoot, List<ProcessedFile> result) throws IOException {
        String content = readFileWithErrorHandling(filePath);

        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        String processedContent = processContent(content);

        int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

        String relativePath = Paths.get(srcRoot).relativize(filePath).toString();

        result.add(new ProcessedFile(size, relativePath));
    }

    private static String readFileWithErrorHandling(Path path) throws IOException {
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPLACE)
                .onUnmappableCharacter(CodingErrorAction.REPLACE);

        return Files.readString(path, Charset.forName(decoder.charset().name()));
    }

    private static String processContent(String content) {
        String withoutComments = removeComments(content);

        String[] lines = withoutComments.split("\n");
        StringBuilder result = new StringBuilder();

        boolean insideImports = true;

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.isEmpty()) {
                continue;
            }

            if (insideImports && (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import "))) {
                continue;
            }

            if (insideImports && !trimmedLine.isEmpty() &&
                    !trimmedLine.startsWith("package ") &&
                    !trimmedLine.startsWith("import ")) {
                insideImports = false;
            }

            if (!insideImports || (!trimmedLine.startsWith("package ") && !trimmedLine.startsWith("import "))) {
                result.append(line).append("\n");
            }
        }

        String processed = result.toString();

        processed = trimControlChars(processed);

        return removeEmptyLines(processed);
    }

    private static String removeComments(String source) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = source.length();

        while (i < n) {
            if (i + 1 < n && source.charAt(i) == '/' && source.charAt(i + 1) == '/') {
                while (i < n && source.charAt(i) != '\n') {
                    i++;
                }
                if (i < n && source.charAt(i) == '\n') {
                    result.append('\n');
                    i++;
                }
            }
            else if (i + 1 < n && source.charAt(i) == '/' && source.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < n && !(source.charAt(i) == '*' && source.charAt(i + 1) == '/')) {
                    i++;
                }
                if (i + 1 < n) {
                    i += 2;
                }
            }
            else {
                result.append(source.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    private static String trimControlChars(String str) {
        int start = 0;
        int end = str.length();

        while (start < end && str.charAt(start) < 33) {
            start++;
        }

        while (end > start && str.charAt(end - 1) < 33) {
            end--;
        }

        return str.substring(start, end);
    }

    private static String removeEmptyLines(String str) {
        StringBuilder result = new StringBuilder();
        String[] lines = str.split("\n");

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    private record ProcessedFile(int size, String relativePath) {}
}