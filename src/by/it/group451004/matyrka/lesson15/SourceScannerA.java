package by.it.group451004.matyrka.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src";
        List<FileData> fileDataList = new ArrayList<>();
        Path srcPath = Paths.get(src);

        try {
            Files.walk(srcPath)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(file -> processJavaFile(file, srcPath, fileDataList));
        } catch (IOException e) {
            // игнорируем ошибки обхода
        }

        fileDataList.sort((f1, f2) -> {
            int sizeCompare = Integer.compare(f1.size, f2.size);
            return sizeCompare != 0 ? sizeCompare : f1.relativePath.compareTo(f2.relativePath);
        });

        for (FileData data : fileDataList) {
            System.out.println(data.size + " " + data.relativePath);
        }
    }

    private static void processJavaFile(Path file, Path srcPath, List<FileData> fileDataList) {
        try {
            String content = Files.readString(file, StandardCharsets.UTF_8);

            // Пропускаем тестовые файлы
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            String processed = processFileContent(content);

            // Удаляем символы с кодом <33 в начале и конце всего текста
            processed = removeLowCharsFromStart(processed);
            processed = removeLowCharsFromEnd(processed);

            String relativePath = srcPath.relativize(file).toString();

            // Размер в байтах (UTF-8)
            int size = processed.getBytes(StandardCharsets.UTF_8).length;

            fileDataList.add(new FileData(size, relativePath));

        } catch (MalformedInputException e) {
            // игнорируем ошибки кодировки
        } catch (IOException e) {
            // игнорируем ошибки чтения файлов
        }
    }

    private static String processFileContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\\R");

        for (String line : lines) {
            String processedLine = processLine(line);
            if (!processedLine.isEmpty()) {
                result.append(processedLine).append("\n");
            }
        }

        return result.toString();
    }

    private static String processLine(String line) {
        // Сначала удаляем символы с кодом <33 в начале и конце строки
        String processed = removeLowCharsFromStart(line);
        processed = removeLowCharsFromEnd(processed);

        // Затем проверяем, не является ли строка package или import
        String trimmed = processed.trim();
        if (trimmed.startsWith("package") || trimmed.startsWith("import")) {
            return "";
        }

        return processed;
    }

    private static String removeLowCharsFromStart(String str) {
        int start = 0;
        while (start < str.length() && str.charAt(start) < 33) {
            start++;
        }
        return start == 0 ? str : str.substring(start);
    }

    private static String removeLowCharsFromEnd(String str) {
        int end = str.length();
        while (end > 0 && str.charAt(end - 1) < 33) {
            end--;
        }
        return end == str.length() ? str : str.substring(0, end);
    }

    private static class FileData {
        final int size;
        final String relativePath;

        FileData(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}