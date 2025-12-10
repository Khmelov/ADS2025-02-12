package by.it.group451004.kozlov.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        try {
            List<FileData> processedFiles = processJavaFiles(src);
            findAndPrintDuplicates(processedFiles);
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
        String content = readFileWithFallback(filePath);

        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        String processedContent = processContent(content);
        String relativePath = startPath.relativize(filePath).toString();

        result.add(new FileData(relativePath, processedContent));
    }

    private static String readFileWithFallback(Path filePath) throws IOException {
        try {
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            try {
                return Files.readString(filePath, Charset.forName("Windows-1251"));
            } catch (MalformedInputException e2) {
                return Files.readString(filePath, StandardCharsets.ISO_8859_1);
            }
        }
    }

    private static String processContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")) {
                continue;
            }

            String withoutComments = removeComments(trimmedLine);
            if (!withoutComments.isEmpty()) {
                // Заменяем последовательности символов с кодом <33 на пробел
                String normalized = normalizeWhitespace(withoutComments);
                result.append(normalized).append(" ");
            }
        }

        return result.toString().trim();
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
                continue;
            } else if (inBlockComment) {
                if (current == '*' && next == '/') {
                    inBlockComment = false;
                    i++;
                }
                continue;
            } else if (inString) {
                result.append(current);
                if (current == '\\' && next == stringChar) {
                    result.append(next);
                    i++;
                } else if (current == stringChar) {
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
                    stringChar = '"';
                    result.append(current);
                } else if (current == '\'') {
                    inChar = true;
                    result.append(current);
                } else if (current == '/' && next == '/') {
                    inLineComment = true;
                    i++;
                } else if (current == '/' && next == '*') {
                    inBlockComment = true;
                    i++;
                } else {
                    result.append(current);
                }
            }
        }

        return result.toString();
    }

    private static String normalizeWhitespace(String text) {
        StringBuilder result = new StringBuilder();
        boolean lastWasSpace = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!lastWasSpace) {
                    result.append(' ');
                    lastWasSpace = true;
                }
            } else {
                result.append(c);
                lastWasSpace = false;
            }
        }

        return result.toString();
    }

    private static void findAndPrintDuplicates(List<FileData> files) {
        // Оптимизация: группируем файлы по длине для уменьшения количества сравнений
        Map<Integer, List<FileData>> filesByLength = new HashMap<>();
        for (FileData file : files) {
            int length = file.content.length();
            filesByLength.computeIfAbsent(length, k -> new ArrayList<>()).add(file);
        }

        Map<String, List<String>> duplicates = new TreeMap<>(); // Автоматическая сортировка по ключу

        // Сравниваем только файлы с близкой длиной
        for (List<FileData> sameLengthFiles : filesByLength.values()) {
            if (sameLengthFiles.size() < 2) continue;

            for (int i = 0; i < sameLengthFiles.size(); i++) {
                FileData file1 = sameLengthFiles.get(i);
                List<String> copies = new ArrayList<>();

                for (int j = i + 1; j < sameLengthFiles.size(); j++) {
                    FileData file2 = sameLengthFiles.get(j);

                    int distance = optimizedLevenshtein(file1.content, file2.content);
                    if (distance < 10) {
                        copies.add(file2.path);
                    }
                }

                if (!copies.isEmpty()) {
                    // Сортируем пути копий лексикографически
                    copies.sort(String::compareTo);
                    duplicates.put(file1.path, copies);
                }
            }
        }

        // Вывод результатов
        for (Map.Entry<String, List<String>> entry : duplicates.entrySet()) {
            System.out.println(entry.getKey());
            for (String copyPath : entry.getValue()) {
                System.out.println(copyPath);
            }
            System.out.println(); // Пустая строка между группами
        }
    }

    // Оптимизированная версия расстояния Левенштейна с отсечением
    private static int optimizedLevenshtein(String s1, String s2) {
        if (s1.equals(s2)) return 0;

        int len1 = s1.length();
        int len2 = s2.length();

        // Быстрая проверка на большую разницу в длинах
        if (Math.abs(len1 - len2) >= 10) {
            return Math.max(len1, len2);
        }

        // Используем только два ряда для экономии памяти
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

        for (int j = 0; j <= len2; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            curr[0] = i;

            int minInRow = Integer.MAX_VALUE;
            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(curr[j - 1] + 1, prev[j] + 1),
                        prev[j - 1] + cost
                );
                minInRow = Math.min(minInRow, curr[j]);
            }

            // Отсечение: если минимальное расстояние в строке уже превышает порог
            if (minInRow >= 10) {
                return minInRow;
            }

            // Обмен массивами
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[len2];
    }

    private static class FileData {
        String path;
        String content;

        FileData(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}