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

public class SourceScannerA {

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

        // Используем walk для обхода всех файлов
        Files.walk(root)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        // Читаем файл с обработкой ошибок кодировки
                        String content = readFileWithFallback(path);

                        // Пропускаем тестовые файлы
                        if (content.contains("@Test") || content.contains("org.junit.Test")) {
                            return;
                        }

                        // Обрабатываем содержимое
                        String processedContent = processContent(content);

                        // Получаем относительный путь
                        String relativePath = root.relativize(path).toString();

                        // Рассчитываем размер в байтах
                        int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

                        result.add(new FileInfo(relativePath, size));

                    } catch (IOException e) {
                        // Игнорируем файлы с ошибками чтения
                    }
                });

        return result;
    }

    private static String readFileWithFallback(Path path) throws IOException {
        // Пробуем UTF-8, если ошибка - пробуем другие кодировки
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Пробуем стандартную кодировку платформы
            return Files.readString(path, Charset.defaultCharset());
        }
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            // Пропускаем package и import строки
            if (line.trim().startsWith("package ") || line.trim().startsWith("import ")) {
                continue;
            }
            result.append(line).append("\n");
        }

        // Удаляем символы с кодом <33 в начале и конце
        String processed = result.toString();
        processed = removeLowCharsFromStart(processed);
        processed = removeLowCharsFromEnd(processed);

        return processed;
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
        // Сортируем по размеру (по возрастанию), затем по пути
        List<FileInfo> sorted = fileInfos.stream()
                .sorted(Comparator
                        .comparingInt(FileInfo::getSize)
                        .thenComparing(FileInfo::getPath))
                .collect(Collectors.toList());

        // Выводим результаты
        for (FileInfo fileInfo : sorted) {
            System.out.println(fileInfo.getSize() + " " + fileInfo.getPath());
        }
    }

    // Вспомогательный класс для хранения информации о файле
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