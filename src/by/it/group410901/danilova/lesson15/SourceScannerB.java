package by.it.group410901.danilova.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        try {
            List<FileInfo> fileInfos = processJavaFiles(src);
            printResults(fileInfos);
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }

    private static List<FileInfo> processJavaFiles(String srcDir) throws IOException {
        Path root = Paths.get(srcDir);
        List<FileInfo> result = new ArrayList<>();

        if (!Files.exists(root)) {
            return result;
        }

        Files.walk(root)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        String content = readFileWithFallback(path);

                        if (content.contains("@Test") || content.contains("org.junit.Test")) {
                            return;
                        }

                        String processedContent = processContent(content);
                        String relativePath = root.relativize(path).toString();
                        int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

                        result.add(new FileInfo(relativePath, size));

                    } catch (IOException e) {
                        // Ignore files with read errors
                    }
                });

        return result;
    }

    private static String readFileWithFallback(Path path) throws IOException {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            try {
                return Files.readString(path, Charset.defaultCharset());
            } catch (IOException e2) {
                // If both encodings fail, return empty string
                return "";
            }
        }
    }

    private static String processContent(String content) {
        // Удаляем package и imports
        content = removePackageAndImports(content);

        // Удаляем комментарии
        content = removeComments(content);

        // Удаляем символы с кодом <33 в начале и конце
        content = removeLowCharsFromStart(content);
        content = removeLowCharsFromEnd(content);

        // Удаляем пустые строки
        content = removeEmptyLines(content);

        return content;
    }

    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package ") && !trimmedLine.startsWith("import ")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int length = content.length();
        int i = 0;

        while (i < length) {
            if (i < length - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                // Однострочный комментарий - пропускаем до конца строки
                while (i < length && content.charAt(i) != '\n') {
                    i++;
                }
            } else if (i < length - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                // Многострочный комментарий - пропускаем до */
                i += 2;
                while (i < length - 1 && !(content.charAt(i) == '*' && content.charAt(i + 1) == '/')) {
                    i++;
                }
                i += 2; // Пропускаем */
            } else {
                // Обычный текст - добавляем
                result.append(content.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    private static String removeEmptyLines(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String removeLowCharsFromStart(String text) {
        int start = 0;
        while (start < text.length() && text.charAt(start) < 33) {
            start++;
        }
        return text.substring(start);
    }

    private static String removeLowCharsFromEnd(String text) {
        int end = text.length();
        while (end > 0 && text.charAt(end - 1) < 33) {
            end--;
        }
        return text.substring(0, end);
    }

    private static void printResults(List<FileInfo> fileInfos) {
        List<FileInfo> sorted = fileInfos.stream()
                .sorted(Comparator
                        .comparingInt(FileInfo::getSize)
                        .thenComparing(FileInfo::getPath))
                .collect(Collectors.toList());

        for (FileInfo fileInfo : sorted) {
            System.out.println(fileInfo.getSize() + " " + fileInfo.getPath());
        }
    }

    private static class FileInfo {
        private final String path;
        private final int size;

        public FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
        }

        public String getPath() {
            return path;
        }

        public int getSize() {
            return size;
        }
    }
}