package by.it.group410901.evtuhovskaya.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) {
        // Создаем final переменные для использования в лямбде
        final Path root = getRootDirectory();
        final List<FileInfo> fileList = new ArrayList<>();

        try {
            Files.walk(root)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        // Используем final переменные из внешней области
                        processFile(path, root, fileList);
                    });
        } catch (IOException e) {
            // Игнорируем ошибки обхода директории
        }

        // Сортировка
        fileList.sort((f1, f2) -> {
            int sizeCompare = Integer.compare(f1.size, f2.size);
            return sizeCompare != 0 ? sizeCompare : f1.relativePath.compareTo(f2.relativePath);
        });

        // Вывод результатов
        for (FileInfo fileInfo : fileList) {
            System.out.println(fileInfo.size + " " + fileInfo.relativePath);
        }
    }

    private static Path getRootDirectory() {
        // Пробуем разные возможные пути
        String[] possiblePaths = {
                "D:/ASD/ADS2025-02-12/src",
                "D:/ASD/src",
                System.getProperty("user.dir") + File.separator + "src",
                System.getProperty("user.dir")
        };

        for (String path : possiblePaths) {
            Path testPath = Paths.get(path);
            if (Files.exists(testPath) && Files.isDirectory(testPath)) {
                return testPath;
            }
        }
        return Paths.get(System.getProperty("user.dir"));
    }

    private static void processFile(Path file, Path root, List<FileInfo> fileList) {
        String content;
        try {
            content = Files.readString(file, Charset.defaultCharset());
        } catch (MalformedInputException e) {
            return;
        } catch (IOException e) {
            return;
        }

        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        String cleanedContent = removePackageAndImports(content);
        cleanedContent = trimLowChars(cleanedContent);
        int size = cleanedContent.getBytes().length;

        String relativePath;
        try {
            relativePath = root.relativize(file).toString();
        } catch (IllegalArgumentException e) {
            relativePath = file.getFileName().toString();
        }

        fileList.add(new FileInfo(relativePath, size));
    }

    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n", -1);

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package ") && !trimmedLine.startsWith("import ")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String trimLowChars(String s) {
        if (s == null || s.isEmpty()) return s;

        int start = 0;
        int end = s.length() - 1;

        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;

        return start > end ? "" : s.substring(start, end + 1);
    }

    static class FileInfo {
        String relativePath;
        int size;

        FileInfo(String relativePath, int size) {
            this.relativePath = relativePath;
            this.size = size;
        }
    }
}