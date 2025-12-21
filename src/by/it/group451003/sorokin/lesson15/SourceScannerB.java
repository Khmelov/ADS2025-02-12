package by.it.group451003.sorokin.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        try {
            List<FileInfo> fileInfos = processJavaFiles(src);

            // Sort by size (ascending), then by path lexicographically
            fileInfos.sort((f1, f2) -> {
                int sizeCompare = Integer.compare(f1.size, f2.size);
                if (sizeCompare != 0) {
                    return sizeCompare;
                }
                return f1.relativePath.compareTo(f2.relativePath);
            });

            // Output results
            for (FileInfo info : fileInfos) {
                System.out.println(info.size + " " + info.relativePath);
            }

        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }

    private static List<FileInfo> processJavaFiles(String srcDir) throws IOException {
        List<FileInfo> result = new ArrayList<>();
        Path srcPath = Paths.get(srcDir);

        Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".java")) {
                    try {
                        processJavaFile(file, srcPath, result);
                    } catch (Exception e) {
                        // Log and continue if there's an error with a specific file
                        System.err.println("Error processing file: " + file + " - " + e.getMessage());
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                // Ignore files that can't be accessed
                return FileVisitResult.CONTINUE;
            }
        });

        return result;
    }

    private static void processJavaFile(Path file, Path srcPath, List<FileInfo> result) {
        try {
            // Read file content with encoding fallback
            String content = readFileWithFallback(file);

            // Skip test files
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            // Process content: remove package, imports, comments, empty lines
            String processedContent = processContent(content);

            // Remove characters with code < 33 from start and end
            processedContent = removeLowAsciiFromStart(processedContent);
            processedContent = removeLowAsciiFromEnd(processedContent);

            // Remove empty lines
            processedContent = removeEmptyLines(processedContent);

            // Calculate size in bytes using UTF-8 encoding
            byte[] bytes = processedContent.getBytes(StandardCharsets.UTF_8);
            int size = bytes.length;

            // Get relative path
            String relativePath = srcPath.relativize(file).toString();

            result.add(new FileInfo(size, relativePath));

        } catch (Exception e) {
            // Ignore files that can't be read
        }
    }

    private static String readFileWithFallback(Path file) throws IOException {
        // Try UTF-8 first
        try {
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            // Fallback to ISO-8859-1 for files with encoding issues
            try {
                return Files.readString(file, StandardCharsets.ISO_8859_1);
            } catch (MalformedInputException e2) {
                // Final fallback - read as bytes and convert with replacement
                byte[] bytes = Files.readAllBytes(file);
                return new String(bytes, StandardCharsets.UTF_8);
            }
        }
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\\r?\\n");

        boolean inBlockComment = false;

        for (String line : lines) {
            String processedLine = processLine(line, inBlockComment);

            // Update block comment state
            inBlockComment = isStillInBlockComment(line, inBlockComment);

            // Skip package and import statements
            String trimmedLine = processedLine.trim();
            if (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")) {
                continue;
            }

            // Add non-empty lines
            if (!processedLine.isEmpty()) {
                result.append(processedLine).append("\n");
            }
        }

        return result.toString();
    }

    private static String processLine(String line, boolean inBlockComment) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = line.length();

        while (i < n) {
            if (inBlockComment) {
                // Look for end of block comment
                if (i < n - 1 && line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i += 2;
                } else {
                    i++;
                }
            } else {
                // Check for line comment
                if (i < n - 1 && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                    break; // Skip rest of the line
                }
                // Check for block comment start
                else if (i < n - 1 && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i += 2;
                }
                // Regular character
                else {
                    result.append(line.charAt(i));
                    i++;
                }
            }
        }

        return result.toString();
    }

    private static boolean isStillInBlockComment(String line, boolean inBlockComment) {
        if (!inBlockComment) {
            return false;
        }

        // Check if block comment ends in this line
        int endIndex = line.indexOf("*/");
        if (endIndex != -1) {
            // Check if there's another block comment start after this end
            int nextStart = line.indexOf("/*", endIndex + 2);
            return nextStart != -1;
        }
        return true;
    }

    private static String removeLowAsciiFromStart(String str) {
        int start = 0;
        while (start < str.length() && str.charAt(start) < 33) {
            start++;
        }
        return str.substring(start);
    }

    private static String removeLowAsciiFromEnd(String str) {
        int end = str.length();
        while (end > 0 && str.charAt(end - 1) < 33) {
            end--;
        }
        return str.substring(0, end);
    }

    private static String removeEmptyLines(String str) {
        StringBuilder result = new StringBuilder();
        String[] lines = str.split("\\r?\\n");

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        // Remove trailing newline if present
        if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    private static class FileInfo {
        int size;
        String relativePath;

        FileInfo(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}