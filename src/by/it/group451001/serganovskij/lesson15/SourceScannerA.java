package by.it.group451001.serganovskij.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerA {
    public static void main(String[] args) {
        // Получаем путь к каталогу src
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        List<FileData> fileDataList = new ArrayList<>();

        try {
            // Рекурсивно обходим все файлы в каталоге src
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    // Обрабатываем только Java файлы
                    if (file.toString().endsWith(".java")) {
                        processJavaFile(file, fileDataList);
                    }
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    // Пропускаем файлы с ошибками доступа
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {}

        // Сортируем файлы по размеру, затем по пути
        fileDataList.sort((f1, f2) -> {
            int sizeCompare = Integer.compare(f1.size, f2.size);
            if (sizeCompare != 0) return sizeCompare;
            return f1.relativePath.compareTo(f2.relativePath);
        });

        // Выводим результаты
        for (FileData data : fileDataList) {
            System.out.println(data.size + " " + data.relativePath);
        }
    }

    // Обрабатывает отдельный Java файл
    private static void processJavaFile(Path file, List<FileData> fileDataList) {
        try {
            String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
            // Пропускаем тестовые файлы
            if (content.contains("@Test") || content.contains("org.junit.Test")) return;

            String processedContent = processFileContent(content);
            String srcPath = System.getProperty("user.dir") + File.separator + "src" + File.separator;
            // Получаем относительный путь
            String relativePath = file.toString().substring(srcPath.length());
            int size = processedContent.getBytes(StandardCharsets.UTF_8).length;
            fileDataList.add(new FileData(size, relativePath));
        } catch (MalformedInputException e) {
            // Игнорируем файлы с ошибками кодировки
        } catch (IOException e) {}
    }

    // Обрабатывает содержимое файла: удаляет package и импорты
    private static String processFileContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");
        for (String line : lines) {
            // Пропускаем строки package и import
            if (line.trim().startsWith("package") || line.trim().startsWith("import")) continue;
            result.append(line).append("\n");
        }
        String processed = result.toString();
        // Удаляем служебные символы в начале и конце
        processed = removeLowCharsFromStart(processed);
        processed = removeLowCharsFromEnd(processed);
        return processed;
    }

    // Удаляет символы с кодом <33 из начала строки
    private static String removeLowCharsFromStart(String str) {
        int start = 0;
        while (start < str.length() && str.charAt(start) < 33) start++;
        return str.substring(start);
    }

    // Удаляет символы с кодом <33 из конца строки
    private static String removeLowCharsFromEnd(String str) {
        int end = str.length();
        while (end > 0 && str.charAt(end - 1) < 33) end--;
        return str.substring(0, end);
    }

    // Класс для хранения данных о файле
    private static class FileData {
        int size;
        String relativePath;
        FileData(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}