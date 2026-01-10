package by.it.group451004.zybko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            // Рекурсивно находим все .java файлы
            List<Path> javaFiles = findJavaFiles(Paths.get(src));

            // Обрабатываем каждый файл
            for (Path filePath : javaFiles) {
                try {
                    String relativePath = getRelativePath(src, filePath.toString());

                    // Читаем содержимое файла с обработкой ошибок кодировки
                    String content = readFileWithErrorHandling(filePath);

                    // Пропускаем тестовые файлы
                    if (content.contains("@Test") || content.contains("org.junit.Test")) {
                        continue;
                    }

                    // Обрабатываем содержимое
                    String processedContent = processContent(content);

                    // Получаем размер в байтах
                    int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

                    // Добавляем информацию о файле
                    fileInfos.add(new FileInfo(relativePath, size));

                } catch (IOException e) {
                    System.err.println("Ошибка при обработке файла: " + filePath);
                    e.printStackTrace();
                }
            }

            // Сортируем файлы по размеру, а при равенстве - по пути
            Collections.sort(fileInfos);

            // Выводим результаты
            for (FileInfo fileInfo : fileInfos) {
                System.out.println(fileInfo.size + " " + fileInfo.path);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при поиске файлов: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Метод для поиска всех .java файлов рекурсивно
    private static List<Path> findJavaFiles(Path startPath) throws IOException {
        try (Stream<Path> walk = Files.walk(startPath)) {
            return walk
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .collect(Collectors.toList());
        }
    }

    // Метод для чтения файла с обработкой ошибок кодировки
    private static String readFileWithErrorHandling(Path filePath) throws IOException {
        // Создаем декодер, который заменяет некорректные символы
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPLACE)
                .onUnmappableCharacter(CodingErrorAction.REPLACE);

        // Читаем все строки файла
        List<String> lines = Files.readAllLines(filePath, Charset.forName("UTF-8"));
        return String.join(System.lineSeparator(), lines);
    }

    // Метод для обработки содержимого файла за O(n)
    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            String trimmedLine = line.trim();

            // Пропускаем строки package и import
            if (trimmedLine.startsWith("package ") ||
                    trimmedLine.startsWith("import ")) {
                continue;
            }

            result.append(line).append("\n");
        }

        // Удаляем символы с кодом <33 в начале и конце
        String processed = result.toString();

        // Удаляем в начале
        int start = 0;
        while (start < processed.length() && processed.charAt(start) < 33) {
            start++;
        }

        // Удаляем в конце
        int end = processed.length() - 1;
        while (end >= start && processed.charAt(end) < 33) {
            end--;
        }

        return processed.substring(start, end + 1);
    }

    // Метод для получения относительного пути
    private static String getRelativePath(String basePath, String fullPath) {
        return fullPath.substring(basePath.length());
    }

    // Вспомогательный класс для хранения информации о файле
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