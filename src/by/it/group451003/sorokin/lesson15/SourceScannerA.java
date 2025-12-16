package by.it.group451003.sorokin.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerA {

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
                System.err.println("Cannot access file: " + file + " - " + exc.getMessage());
                return FileVisitResult.CONTINUE;
            }
        });

        return result;
    }

    private static void processJavaFile(Path file, Path srcPath, List<FileInfo> result) {
        try {
            // Read file content
            String content = readFileQuickly(file);

            // Skip test files
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            // Process content - remove package and import statements
            String processedContent = processContent(content);

            // Calculate size in bytes using UTF-8 encoding
            byte[] bytes = processedContent.getBytes(StandardCharsets.UTF_8);
            int size = bytes.length;

            // Get relative path
            String relativePath = srcPath.relativize(file).toString();

            result.add(new FileInfo(size, relativePath));

        } catch (Exception e) {
            // Ignore files that can't be read
            System.err.println("Skipping file due to error: " + file + " - " + e.getMessage());
        }
    }

    private static String readFileQuickly(Path file) throws IOException {
        try (InputStream is = Files.newInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            StringBuilder content = new StringBuilder();
            char[] buffer = new char[8192];
            int charsRead;
            int totalChars = 0;
            final int MAX_FILE_SIZE = 10 * 1024 * 1024;

            while ((charsRead = reader.read(buffer)) != -1) {
                content.append(buffer, 0, charsRead);
                totalChars += charsRead;

                if (totalChars > MAX_FILE_SIZE) {
                    break;
                }
            }

            return content.toString();
        } catch (MalformedInputException e) {
            return Files.readString(file, StandardCharsets.ISO_8859_1);
        }
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\\r?\\n");

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")) {
                continue;
            }
            result.append(line).append("\n");
        }

        // Remove leading and trailing whitespace
        String processed = result.toString();
        processed = removeWhitespaceFromStart(processed);
        processed = removeWhitespaceFromEnd(processed);

        return processed;
    }

    private static String removeWhitespaceFromStart(String str) {
        int start = 0;
        while (start < str.length() && Character.isWhitespace(str.charAt(start))) {
            start++;
        }
        return str.substring(start);
    }

    private static String removeWhitespaceFromEnd(String str) {
        int end = str.length();
        while (end > 0 && Character.isWhitespace(str.charAt(end - 1))) {
            end--;
        }
        return str.substring(0, end);
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