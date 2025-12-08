package by.it.group451004.kozlov.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        try {
            List<FileData> processedFiles = processJavaFiles(src);
            printResults(processedFiles);
        } catch (IOException e) {
            System.err.println("Ошибка при обработке файлов: " + e.getMessage());
        }
    }

    private static List<FileData> processJavaFiles(String srcDir) throws IOException {
        List<FileData> result = new ArrayList<>();
        Path startPath = Paths.get(srcDir);

        if (!Files.exists(startPath)) {
            System.err.println("Каталог src не найден: " + srcDir);
            return result;
        }

        // Рекурсивный обход всех файлов .java
        Files.walk(startPath)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        processFile(path, result, startPath);
                    } catch (IOException e) {
                        System.err.println("Ошибка при обработке файла " + path + ": " + e.getMessage());
                    }
                });

        return result;
    }

    private static void processFile(Path filePath, List<FileData> result, Path startPath) throws IOException {
        // Чтение файла с обработкой ошибок кодировки
        String content = readFileWithFallback(filePath);

        // Пропускаем тестовые файлы
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        // Обработка содержимого
        String processedContent = processContent(content);

        // Получаем относительный путь
        String relativePath = startPath.relativize(filePath).toString();

        // Сохраняем данные файла
        result.add(new FileData(relativePath, processedContent.getBytes(StandardCharsets.UTF_8).length));
    }

    private static String readFileWithFallback(Path filePath) throws IOException {
        // Пробуем стандартную кодировку UTF-8
        try {
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            // Если UTF-8 не работает, пробуем Windows-1251 (кириллица)
            try {
                return Files.readString(filePath, Charset.forName("Windows-1251"));
            } catch (MalformedInputException e2) {
                // Если и это не работает, пробуем ISO-8859-1
                return Files.readString(filePath, StandardCharsets.ISO_8859_1);
            }
        }
    }

    private static String processContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        // Разбиваем на строки для обработки
        String[] lines = content.split("\n");
        List<String> processedLines = new ArrayList<>();

        for (String line : lines) {
            String trimmedLine = line.trim();

            // Пропускаем package и import строки
            if (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")) {
                continue;
            }

            // Удаляем комментарии
            String withoutComments = removeComments(trimmedLine);

            // Пропускаем пустые строки после удаления комментариев
            if (!withoutComments.isEmpty()) {
                processedLines.add(withoutComments);
            }
        }

        // Собираем обратно в текст
        String result = String.join("\n", processedLines);

        // Удаляем символы с кодом < 33 в начале и конце
        result = trimLowChars(result);

        return result;
    }

    private static String removeComments(String line) {
        StringBuilder result = new StringBuilder();
        boolean inString = false;
        char stringChar = '"';
        boolean inChar = false;
        boolean inLineComment = false;
        boolean inBlockComment = false;

        for (int i = 0; i < line.length(); i++) {
            char current = line.charAt(i);
            char next = (i < line.length() - 1) ? line.charAt(i + 1) : '\0';

            if (inLineComment) {
                // Пропускаем всё до конца строки
                continue;
            } else if (inBlockComment) {
                // Проверяем конец блочного комментария
                if (current == '*' && next == '/') {
                    inBlockComment = false;
                    i++; // Пропускаем следующий символ
                }
                continue;
            } else if (inString) {
                result.append(current);
                if (current == '\\' && next == stringChar) {
                    result.append(next);
                    i++; // Пропускаем экранированный символ
                } else if (current == stringChar) {
                    inString = false;
                }
            } else if (inChar) {
                result.append(current);
                if (current == '\\' && next == '\'') {
                    result.append(next);
                    i++; // Пропускаем экранированный символ
                } else if (current == '\'') {
                    inChar = false;
                }
            } else {
                // Проверяем начало комментариев и строк
                if (current == '"') {
                    inString = true;
                    stringChar = '"';
                    result.append(current);
                } else if (current == '\'') {
                    inChar = true;
                    result.append(current);
                } else if (current == '/' && next == '/') {
                    inLineComment = true;
                    i++; // Пропускаем следующий символ
                } else if (current == '/' && next == '*') {
                    inBlockComment = true;
                    i++; // Пропускаем следующий символ
                } else {
                    result.append(current);
                }
            }
        }

        return result.toString().trim();
    }

    private static String trimLowChars(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Удаляем в начале
        int start = 0;
        while (start < text.length() && text.charAt(start) < 33) {
            start++;
        }

        // Удаляем в конце
        int end = text.length();
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        return text.substring(start, end);
    }

    private static void printResults(List<FileData> files) {
        // Сортировка: сначала по размеру (по возрастанию), затем по пути (лексикографически)
        files.sort((f1, f2) -> {
            int sizeCompare = Integer.compare(f1.size, f2.size);
            if (sizeCompare != 0) {
                return sizeCompare;
            }
            return f1.path.compareTo(f2.path);
        });

        // Вывод результатов
        for (FileData file : files) {
            System.out.println(file.size + " " + file.path);
        }
    }

    // Вспомогательный класс для хранения данных файла
    private static class FileData {
        String path;
        int size;

        FileData(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }
}