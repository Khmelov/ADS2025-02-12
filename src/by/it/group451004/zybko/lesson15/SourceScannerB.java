package by.it.group451004.zybko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            List<Path> javaFiles = findJavaFiles(Paths.get(src));

            for (Path filePath : javaFiles) {
                try {
                    String relativePath = getRelativePath(src, filePath.toString());

                    String content = readFileWithErrorHandling(filePath);

                    if (content.contains("@Test") || content.contains("org.junit.Test")) {
                        continue;
                    }

                    String processedContent = processContent(content);

                    int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

                    fileInfos.add(new FileInfo(relativePath, size));

                } catch (IOException e) {
                    System.err.println("Ошибка при обработке файла: " + filePath);
                    e.printStackTrace();
                }
            }

            Collections.sort(fileInfos);

            for (FileInfo fileInfo : fileInfos) {
                System.out.println(fileInfo.size + " " + fileInfo.path);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при поиске файлов: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<Path> findJavaFiles(Path startPath) throws IOException {
        try (Stream<Path> walk = Files.walk(startPath)) {
            return walk
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .collect(Collectors.toList());
        }
    }

    private static String readFileWithErrorHandling(Path filePath) throws IOException {
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPLACE)
                .onUnmappableCharacter(CodingErrorAction.REPLACE);

        byte[] fileBytes = Files.readAllBytes(filePath);
        return new String(fileBytes, decoder.charset());
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        boolean inBlockComment = false;

        for (String line : lines) {
            String processedLine = processLine(line, inBlockComment);
            inBlockComment = isInBlockCommentAfterProcessing(line, inBlockComment);

            if (processedLine != null && !processedLine.isEmpty()) {
                // Удаляем символы с кодом <33 в конце строки
                processedLine = trimTrailingLowChars(processedLine);

                // Удаляем символы с кодом <33 в начале строки
                processedLine = trimLeadingLowChars(processedLine);

                if (!processedLine.isEmpty()) {
                    result.append(processedLine).append("\n");
                }
            }
        }

        String processed = result.toString();

        // Удаляем символы с кодом <33 в начале и конце всего текста
        processed = trimLeadingLowChars(processed);
        processed = trimTrailingLowChars(processed);

        return processed;
    }

    private static String processLine(String line, boolean inBlockComment) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = line.length();

        while (i < n) {
            // Пропускаем package и import в начале строки
            if (i == 0 && line.trim().startsWith("package ")) {
                return null;
            }
            if (i == 0 && line.trim().startsWith("import ")) {
                return null;
            }

            if (inBlockComment) {
                // Ищем конец блочного комментария
                if (i < n - 1 && line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i += 2;
                    continue;
                }
                i++;
                continue;
            }

            // Проверяем начало блочного комментария
            if (i < n - 1 && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                inBlockComment = true;
                i += 2;
                continue;
            }

            // Проверяем однострочный комментарий
            if (i < n - 1 && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                break; // Пропускаем остаток строки
            }

            // Проверяем строковые литералы
            if (line.charAt(i) == '"') {
                result.append('"');
                i++;
                while (i < n && line.charAt(i) != '"') {
                    // Экранированные кавычки
                    if (line.charAt(i) == '\\' && i + 1 < n) {
                        result.append(line.charAt(i)).append(line.charAt(i + 1));
                        i += 2;
                    } else {
                        result.append(line.charAt(i));
                        i++;
                    }
                }
                if (i < n) {
                    result.append('"');
                    i++;
                }
                continue;
            }

            // Проверяем символьные литералы
            if (line.charAt(i) == '\'') {
                result.append('\'');
                i++;
                while (i < n && line.charAt(i) != '\'') {
                    // Экранированные апострофы
                    if (line.charAt(i) == '\\' && i + 1 < n) {
                        result.append(line.charAt(i)).append(line.charAt(i + 1));
                        i += 2;
                    } else {
                        result.append(line.charAt(i));
                        i++;
                    }
                }
                if (i < n) {
                    result.append('\'');
                    i++;
                }
                continue;
            }

            // Обычный символ
            result.append(line.charAt(i));
            i++;
        }

        return result.toString();
    }

    private static boolean isInBlockCommentAfterProcessing(String line, boolean wasInBlockComment) {
        int i = 0;
        int n = line.length();
        boolean inBlockComment = wasInBlockComment;

        while (i < n) {
            if (inBlockComment) {
                if (i < n - 1 && line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i += 2;
                    continue;
                }
                i++;
                continue;
            }

            // Проверяем начало блочного комментария
            if (i < n - 1 && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                inBlockComment = true;
                i += 2;
                continue;
            }

            // Проверяем однострочный комментарий
            if (i < n - 1 && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                break;
            }

            // Пропускаем строковые и символьные литералы
            if (line.charAt(i) == '"') {
                i++;
                while (i < n && line.charAt(i) != '"') {
                    if (line.charAt(i) == '\\' && i + 1 < n) {
                        i += 2;
                    } else {
                        i++;
                    }
                }
                if (i < n) i++;
                continue;
            }

            if (line.charAt(i) == '\'') {
                i++;
                while (i < n && line.charAt(i) != '\'') {
                    if (line.charAt(i) == '\\' && i + 1 < n) {
                        i += 2;
                    } else {
                        i++;
                    }
                }
                if (i < n) i++;
                continue;
            }

            i++;
        }

        return inBlockComment;
    }

    private static String trimLeadingLowChars(String str) {
        int i = 0;
        while (i < str.length() && str.charAt(i) < 33) {
            i++;
        }
        return str.substring(i);
    }

    private static String trimTrailingLowChars(String str) {
        int i = str.length() - 1;
        while (i >= 0 && str.charAt(i) < 33) {
            i--;
        }
        return str.substring(0, i + 1);
    }

    private static String getRelativePath(String basePath, String fullPath) {
        return fullPath.substring(basePath.length());
    }

    private static class FileInfo implements Comparable<FileInfo> {
        String path;
        int size;

        FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
        }

        @Override
        public int compareTo(FileInfo other) {
            if (this.size != other.size) {
                return Integer.compare(this.size, other.size);
            }
            return this.path.compareTo(other.path);
        }
    }
}