package by.it.group451001.serganovskij.lesson15;
import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {
    public static void main(String[] args) {
        // Получаем путь к каталогу src
        String src = System.getProperty("user.dir") + File.separator + "src";
        List<FileData> fileDataList = new ArrayList<>();
        Path srcPath = Paths.get(src);

        try {
            // Рекурсивно обходим все Java файлы
            Files.walk(srcPath)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(file -> processJavaFile(file, srcPath, fileDataList));
        } catch (IOException e) {}

        // Сортируем по размеру и пути
        fileDataList.sort((f1, f2) -> {
            int sizeCompare = Integer.compare(f1.size, f2.size);
            return sizeCompare != 0 ? sizeCompare : f1.relativePath.compareTo(f2.relativePath);
        });

        // Выводим результаты
        for (FileData data : fileDataList) {
            System.out.println(data.size + " " + data.relativePath);
        }
    }

    // Обрабатывает отдельный Java файл
    private static void processJavaFile(Path file, Path srcPath, List<FileData> fileDataList) {
        try {
            String content = Files.readString(file, StandardCharsets.UTF_8);
            // Пропускаем тестовые файлы
            if (content.contains("@Test") || content.contains("org.junit.Test")) return;

            String processed = processFileContent(content);
            String relativePath = srcPath.relativize(file).toString();
            int size = processed.getBytes(StandardCharsets.UTF_8).length;
            fileDataList.add(new FileData(size, relativePath));
        } catch (MalformedInputException e) {
            // Игнорируем файлы с ошибками кодировки
        } catch (IOException e) {}
    }

    // Основная обработка содержимого файла
    private static String processFileContent(String content) {
        // Удаляем комментарии
        String withoutComments = removeComments(content);
        String[] lines = withoutComments.split("\\R");
        StringBuilder result = new StringBuilder();

        // Обрабатываем каждую строку
        for (String line : lines) {
            String processedLine = processLine(line);
            if (!processedLine.isEmpty()) result.append(processedLine).append("\n");
        }

        String processed = result.toString();
        // Удаляем служебные символы в начале и конце
        processed = removeLowCharsFromStart(processed);
        processed = removeLowCharsFromEnd(processed);
        return processed;
    }

    // Удаляет однострочные и многострочные комментарии
    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int length = content.length();
        int i = 0;

        while (i < length) {
            // Обработка однострочных комментариев
            if (i + 1 < length && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                while (i < length && content.charAt(i) != '\n' && content.charAt(i) != '\r') i++;
            }
            // Обработка многострочных комментариев
            else if (i + 1 < length && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < length && !(content.charAt(i) == '*' && content.charAt(i + 1) == '/')) i++;
                i += 2;
            } else {
                result.append(content.charAt(i));
                i++;
            }
        }
        return result.toString();
    }

    // Обрабатывает отдельную строку
    private static String processLine(String line) {
        String trimmed = line.trim();
        // Удаляем строки package и import
        if (trimmed.startsWith("package") || trimmed.startsWith("import")) return "";

        // Удаляем служебные символы
        String processed = removeLowCharsFromStart(line);
        processed = removeLowCharsFromEnd(processed);
        return processed;
    }

    // Удаляет символы с кодом <33 из начала строки
    private static String removeLowCharsFromStart(String str) {
        int start = 0;
        while (start < str.length() && str.charAt(start) < 33) start++;
        return start == 0 ? str : str.substring(start);
    }

    // Удаляет символы с кодом <33 из конца строки
    private static String removeLowCharsFromEnd(String str) {
        int end = str.length();
        while (end > 0 && str.charAt(end - 1) < 33) end--;
        return end == str.length() ? str : str.substring(0, end);
    }

    // Класс для хранения данных о файле
    private static class FileData {
        final int size;
        final String relativePath;
        FileData(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}