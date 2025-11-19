package by.it.group451001.khomenkov.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerB {

    static class FileData implements Comparable<FileData> {
        String relativePath;
        int size;

        FileData(String relativePath, int size) {
            this.relativePath = relativePath;
            this.size = size;
        }

        @Override
        public int compareTo(FileData other) {
            if (this.size != other.size) {
                return Integer.compare(this.size, other.size);
            }
            return this.relativePath.compareTo(other.relativePath);
        }
    }

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<FileData> fileDataList = new ArrayList<>();

        Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".java")) {
                    processJavaFile(file, srcPath, fileDataList);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        Collections.sort(fileDataList);

        for (FileData fileData : fileDataList) {
            System.out.println(fileData.size + " " + fileData.relativePath);
        }
    }

    private static void processJavaFile(Path file, Path srcPath, List<FileData> fileDataList) {
        try {
            // Читаем файл с обработкой ошибок кодировки
            byte[] fileBytes = Files.readAllBytes(file);
            String content;
            try {
                content = new String(fileBytes, StandardCharsets.UTF_8);
            } catch (Exception e) {
                // Если UTF-8 не работает, пробуем другие кодировки
                content = new String(fileBytes, StandardCharsets.ISO_8859_1);
            }

            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            String processedContent = processContent(content);
            byte[] bytes = processedContent.getBytes(StandardCharsets.UTF_8);
            String relativePath = srcPath.relativize(file).toString();

            fileDataList.add(new FileData(relativePath, bytes.length));

        } catch (MalformedInputException e) {
            // Игнорируем файлы с ошибками кодировки
        } catch (IOException e) {
            // Игнорируем файлы, которые не удалось прочитать
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
        String[] lines = content.split("\r?\n");

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package") && !trimmedLine.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int length = content.length();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean inChar = false;

        for (int i = 0; i < length; i++) {
            char current = content.charAt(i);
            char next = (i < length - 1) ? content.charAt(i + 1) : 0;

            if (inBlockComment) {
                if (current == '*' && next == '/') {
                    inBlockComment = false;
                    i++; // Пропускаем '/'
                }
            } else if (inLineComment) {
                if (current == '\n') {
                    inLineComment = false;
                    result.append(current);
                }
            } else if (inString) {
                result.append(current);
                if (current == '\\' && next == '"') {
                    result.append(next);
                    i++;
                } else if (current == '"') {
                    inString = false;
                }
            } else if (inChar) {
                result.append(current);
                if (current == '\\' && next == '\'') {
                    result.append(next);
                    i++;
                } else if (current == '\'') {
                    inChar = false;
                }
            } else {
                if (current == '"') {
                    inString = true;
                    result.append(current);
                } else if (current == '\'') {
                    inChar = true;
                    result.append(current);
                } else if (current == '/' && next == '*') {
                    inBlockComment = true;
                    i++; // Пропускаем '*'
                } else if (current == '/' && next == '/') {
                    inLineComment = true;
                    i++; // Пропускаем второй '/'
                } else {
                    result.append(current);
                }
            }
        }

        return result.toString();
    }

    private static String removeLowCharsFromStart(String str) {
        int i = 0;
        while (i < str.length() && str.charAt(i) < 33) {
            i++;
        }
        return str.substring(i);
    }

    private static String removeLowCharsFromEnd(String str) {
        int i = str.length() - 1;
        while (i >= 0 && str.charAt(i) < 33) {
            i--;
        }
        return str.substring(0, i + 1);
    }

    private static String removeEmptyLines(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\r?\n");

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }
}
