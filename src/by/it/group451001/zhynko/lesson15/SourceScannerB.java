package by.it.group451001.zhynko.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) {

        String src = System.getProperty("user.dir") + File.separator + "src";
        List<by.it.group451001.zhynko.lesson15.SourceScannerB.FileData> fileDataList = new ArrayList<>();
        Path srcPath = Paths.get(src);

        try {

            Files.walk(srcPath)
                    .filter(p -> p.toString().endsWith(".java"))  // Фильтруем только Java-файлы
                    .forEach(file -> processJavaFile(file, srcPath, fileDataList));  // Обрабатываем каждый файл
        } catch (IOException e) {
            // Игнорируем ошибки ввода-вывода
        }

        // Сортируем список файлов: сначала по размеру, потом по пути
        fileDataList.sort((f1, f2) -> {
            int sizeCompare = Integer.compare(f1.size, f2.size);
            return sizeCompare != 0 ? sizeCompare : f1.relativePath.compareTo(f2.relativePath);
        });

        for (by.it.group451001.zhynko.lesson15.SourceScannerB.FileData data : fileDataList) {
            System.out.println(data.size + " " + data.relativePath);
        }
    }

    private static void processJavaFile(Path file, Path srcPath, List<by.it.group451001.zhynko.lesson15.SourceScannerB.FileData> fileDataList) {
        try {
            String content = Files.readString(file, StandardCharsets.UTF_8);
            if (content.contains("@Test") || content.contains("org.junit.Test")) return;
            String processed = processFileContent(content);
            String relativePath = srcPath.relativize(file).toString();
            int size = processed.getBytes(StandardCharsets.UTF_8).length;
            fileDataList.add(new by.it.group451001.zhynko.lesson15.SourceScannerB.FileData(size, relativePath));

        } catch (MalformedInputException e) {
            // Игнорируем файлы с некорректной кодировкой
        } catch (IOException e) {
            // Игнорируем ошибки чтения файлов
        }
    }

    private static String processFileContent(String content) {
        String withoutComments = removeComments(content);
        String[] lines = withoutComments.split("\\R");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String processedLine = processLine(line);
            if (!processedLine.isEmpty()) result.append(processedLine).append("\n");
        }

        String processed = result.toString();
        processed = removeLowCharsFromStart(processed);
        processed = removeLowCharsFromEnd(processed);
        return processed;
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int length = content.length();
        int i = 0;

        while (i < length) {
            if (i + 1 < length && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                while (i < length && content.charAt(i) != '\n' && content.charAt(i) != '\r') i++;
            }
            else if (i + 1 < length && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                i += 2; // Пропускаем /*
                while (i + 1 < length && !(content.charAt(i) == '*' && content.charAt(i + 1) == '/')) i++;
                i += 2; // Пропускаем */
            }
            else {
                result.append(content.charAt(i));
                i++;
            }
        }
        return result.toString();
    }

    private static String processLine(String line) {
        String trimmed = line.trim();

        if (trimmed.startsWith("package") || trimmed.startsWith("import")) return "";

        String processed = removeLowCharsFromStart(line);
        processed = removeLowCharsFromEnd(processed);
        return processed;
    }

    private static String removeLowCharsFromStart(String str) {
        int start = 0;
        while (start < str.length() && str.charAt(start) < 33) start++;
        return start == 0 ? str : str.substring(start);  // Оптимизация: не создаем подстроку если не нужно
    }

    private static String removeLowCharsFromEnd(String str) {
        int end = str.length();
        while (end > 0 && str.charAt(end - 1) < 33) end--;
        return end == str.length() ? str : str.substring(0, end);  // Оптимизация
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