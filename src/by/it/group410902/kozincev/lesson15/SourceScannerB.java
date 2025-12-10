package by.it.group410902.kozincev.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.Collections;

public class SourceScannerB {

    static class JavaFileData implements Comparable<JavaFileData> {
        final String filePath;
        final int fileSize;

        JavaFileData(String filePath, int fileSize) {
            this.filePath = filePath;
            this.fileSize = fileSize;
        }

        @Override
        public int compareTo(JavaFileData other) {
            if (this.fileSize != other.fileSize) {
                return Integer.compare(this.fileSize, other.fileSize);
            }
            return this.filePath.compareTo(other.filePath);
        }
    }

    public static void main(String[] args) {
        String sourceRoot = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        Path basePath = Path.of(sourceRoot);
        List<JavaFileData> fileList = new ArrayList<>();

        try {
            Files.walk(basePath)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        processJavaFile(path, basePath, fileList);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(fileList);

        for (JavaFileData fileData : fileList) {
            System.out.println(fileData.fileSize + " " + fileData.filePath);
        }
    }

    private static void processJavaFile(Path javaFile, Path rootDir, List<JavaFileData> resultList) {
        try {
            String fileContent = readFileSafely(javaFile);

            if (fileContent.contains("@Test") || fileContent.contains("org.junit.Test")) {
                return;
            }

            String processedContent = processJavaSource(fileContent);
            int sizeInBytes = processedContent.getBytes(StandardCharsets.UTF_8).length;
            String relativePath = rootDir.relativize(javaFile).toString();

            resultList.add(new JavaFileData(relativePath, sizeInBytes));

        } catch (IOException e) {
            // Игнорируем файлы с ошибками чтения
        }
    }

    private static String readFileSafely(Path filePath) throws IOException {
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
        decoder.onMalformedInput(CodingErrorAction.IGNORE);
        decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);

        byte[] fileBytes = Files.readAllBytes(filePath);
        return new String(fileBytes, StandardCharsets.UTF_8);
    }

    private static String processJavaSource(String sourceCode) {
        // Удаляем комментарии
        String withoutComments = removeAllComments(sourceCode);
        // Удаляем package и imports
        String withoutPackages = removePackageAndImports(withoutComments);
        // Удаляем пустые строки и обрезаем пробелы
        return removeEmptyLinesAndTrim(withoutPackages);
    }

    private static String removeAllComments(String code) {
        StringBuilder result = new StringBuilder();
        int length = code.length();
        int i = 0;

        while (i < length) {
            // Проверяем однострочный комментарий
            if (i + 1 < length && code.charAt(i) == '/' && code.charAt(i + 1) == '/') {
                i = skipSingleLineComment(code, i, length);
                continue;
            }

            // Проверяем многострочный комментарий
            if (i + 1 < length && code.charAt(i) == '/' && code.charAt(i + 1) == '*') {
                i = skipMultiLineComment(code, i, length);
                continue;
            }

            // Обрабатываем строковые литералы, чтобы не удалять комментарии внутри них
            if (code.charAt(i) == '"') {
                int stringEnd = processStringLiteral(code, i, length, result);
                if (stringEnd > i) {
                    i = stringEnd;
                    continue;
                }
            }

            // Обрабатываем символьные литералы
            if (code.charAt(i) == '\'') {
                int charEnd = processCharLiteral(code, i, length, result);
                if (charEnd > i) {
                    i = charEnd;
                    continue;
                }
            }

            result.append(code.charAt(i));
            i++;
        }

        return result.toString();
    }

    private static int skipSingleLineComment(String code, int start, int length) {
        int i = start + 2;
        while (i < length && code.charAt(i) != '\n') {
            i++;
        }
        return i;
    }

    private static int skipMultiLineComment(String code, int start, int length) {
        int i = start + 2;
        while (i < length - 1) {
            if (code.charAt(i) == '*' && code.charAt(i + 1) == '/') {
                return i + 2;
            }
            i++;
        }
        return length;
    }

    private static int processStringLiteral(String code, int start, int length, StringBuilder result) {
        result.append(code.charAt(start)); // Добавляем открывающую кавычку
        int i = start + 1;

        while (i < length) {
            char current = code.charAt(i);
            result.append(current);

            if (current == '"' && code.charAt(i - 1) != '\\') {
                return i + 1; // Закрывающая кавычка
            }
            if (current == '\\' && i + 1 < length) {
                result.append(code.charAt(i + 1)); // Экранированный символ
                i += 2;
                continue;
            }
            i++;
        }
        return length;
    }

    private static int processCharLiteral(String code, int start, int length, StringBuilder result) {
        result.append(code.charAt(start)); // Добавляем открывающую кавычку
        int i = start + 1;

        while (i < length) {
            char current = code.charAt(i);
            result.append(current);

            if (current == '\'' && code.charAt(i - 1) != '\\') {
                return i + 1; // Закрывающая кавычка
            }
            if (current == '\\' && i + 1 < length) {
                result.append(code.charAt(i + 1)); // Экранированный символ
                i += 2;
                continue;
            }
            i++;
        }
        return length;
    }

    private static String removePackageAndImports(String code) {
        StringBuilder result = new StringBuilder();
        String[] lines = code.split("\n");

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package") && !trimmedLine.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String removeEmptyLinesAndTrim(String text) {
        StringBuilder result = new StringBuilder();
        String[] lines = text.split("\n");

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.isEmpty()) {
                result.append(trimmedLine).append("\n");
            }
        }

        // Удаляем символы с кодом <33 в начале и конце
        String finalText = result.toString();
        int start = 0;
        int end = finalText.length();

        while (start < end && finalText.charAt(start) < 33) {
            start++;
        }
        while (end > start && finalText.charAt(end - 1) < 33) {
            end--;
        }

        return finalText.substring(start, end);
    }
}