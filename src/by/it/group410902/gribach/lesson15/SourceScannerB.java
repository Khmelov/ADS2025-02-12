package by.it.group410902.gribach.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        String srcPath = System.getProperty("user.dir") + File.separator + "src";
        Path srcDir = Path.of(srcPath);
        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            Files.walk(srcDir)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = readFileWithFallback(p);

                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            String processed = processContent(content);
                            int sizeBytes = processed.getBytes().length;
                            String relPath = srcDir.relativize(p).toString();

                            fileInfos.add(new FileInfo(sizeBytes, relPath));

                        } catch (IOException e) {
                            // игнор ошибок чтения файлов
                        }
                    });
        } catch (IOException e) {
            // игнор ошибок обхода директории
        }

        fileInfos.sort(Comparator.comparingInt(FileInfo::getSize)
                .thenComparing(FileInfo::getPath));

        for (FileInfo fi : fileInfos) {
            System.out.println(fi.getSize() + " " + fi.getPath());
        }

        long endTime = System.currentTimeMillis();
        System.err.println("Execution time: " + (endTime - startTime) + "ms");
    }

    private static String readFileWithFallback(Path filePath) throws IOException {
        List<Charset> charsets = Arrays.asList(
                StandardCharsets.UTF_8,
                Charset.forName("Windows-1251"),
                Charset.forName("ISO-8859-5"),
                StandardCharsets.ISO_8859_1
        );

        for (Charset charset : charsets) {
            try {
                return Files.readString(filePath, charset);
            } catch (MalformedInputException e) {
                continue;
            }
        }

        return "";
    }

    private static String processContent(String content) {
        if (content.isEmpty()) return content;

        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean inChar = false;
        char prevChar = 0;

        // Удаление комментариев за O(n)
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (inBlockComment) {
                if (prevChar == '*' && c == '/') {
                    inBlockComment = false;
                }
            } else if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    result.append(c);
                }
            } else if (inString) {
                if (c == '"' && prevChar != '\\') {
                    inString = false;
                }
                result.append(c);
            } else if (inChar) {
                if (c == '\'' && prevChar != '\\') {
                    inChar = false;
                }
                result.append(c);
            } else {
                if (prevChar == '/' && c == '*') {
                    inBlockComment = true;
                    result.deleteCharAt(result.length() - 1);
                } else if (prevChar == '/' && c == '/') {
                    inLineComment = true;
                    result.deleteCharAt(result.length() - 1);
                } else if (c == '"') {
                    inString = true;
                    result.append(c);
                } else if (c == '\'') {
                    inChar = true;
                    result.append(c);
                } else {
                    result.append(c);
                }
            }
            prevChar = c;
        }

        String withoutComments = result.toString();

        // Удаление package и импортов за O(n)
        result.setLength(0);
        String[] lines = withoutComments.split("\r?\n");
        boolean afterPackageImports = false;

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (!afterPackageImports) {
                if (trimmedLine.startsWith("package") || trimmedLine.startsWith("import")) {
                    continue;
                }
                // Если нашли не-package и не-import строку, значит package/imports закончились
                if (!trimmedLine.isEmpty() && !trimmedLine.startsWith("package") &&
                        !trimmedLine.startsWith("import")) {
                    afterPackageImports = true;
                }
            }

            // Добавляем строку только если не пустая (после удаления комментариев могли появиться пустые)
            if (afterPackageImports && !line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        // Удаляем последний лишний перенос строки если есть
        if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        String filteredContent = result.toString();

        // Удаление символов с кодом <33 в начале и конце
        filteredContent = removeControlCharsFromEdges(filteredContent);

        // Удаление пустых строк
        return removeEmptyLines(filteredContent);
    }

    private static String removeControlCharsFromEdges(String text) {
        if (text.isEmpty()) return text;

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

    private static String removeEmptyLines(String text) {
        if (text.isEmpty()) return text;

        StringBuilder result = new StringBuilder();
        String[] lines = text.split("\r?\n");

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        // Удаляем последний лишний перенос строки если есть
        if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    private static class FileInfo {
        private final int size;
        private final String path;

        public FileInfo(int size, String path) {
            this.size = size;
            this.path = path;
        }

        public int getSize() {
            return size;
        }

        public String getPath() {
            return path;
        }
    }
}