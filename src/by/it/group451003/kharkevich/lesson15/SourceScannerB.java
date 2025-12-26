package by.it.group451003.kharkevich.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) {
        // Формируем путь к папке src текущего проекта
        String src = System.getProperty("user.dir") + File.separator + "src";
        List<FileData> fileDataList = new ArrayList<>();
        Path srcPath = Paths.get(src);

        try {
            // Рекурсивный обход файлов с использованием Stream API
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

        // Выводим отсортированный список
        for (FileData data : fileDataList) {
            System.out.println(data.size + " " + data.relativePath);
        }
    }

    // Обрабатывает отдельный Java-файл
    private static void processJavaFile(Path file, Path srcPath, List<FileData> fileDataList) {
        try {
            // Читаем содержимое файла как строку (упрощенный способ)
            String content = Files.readString(file, StandardCharsets.UTF_8);

            // Пропускаем тестовые файлы
            if (content.contains("@Test") || content.contains("org.junit.Test")) return;

            // Обрабатываем содержимое файла (удаляем комментарии, package, import)
            String processed = processFileContent(content);

            // Получаем относительный путь с использованием Path.relativize()
            String relativePath = srcPath.relativize(file).toString();

            // Вычисляем размер обработанного содержимого
            int size = processed.getBytes(StandardCharsets.UTF_8).length;

            // Добавляем информацию о файле в список
            fileDataList.add(new FileData(size, relativePath));

        } catch (MalformedInputException e) {
            // Игнорируем файлы с некорректной кодировкой
        } catch (IOException e) {
            // Игнорируем ошибки чтения файлов
        }
    }

    // Основной метод обработки содержимого файла
    private static String processFileContent(String content) {
        // Удаляем все комментарии из кода
        String withoutComments = removeComments(content);

        // Разбиваем на строки с поддержкой разных форматов переноса (\n, \r\n, \r)
        String[] lines = withoutComments.split("\\R");
        StringBuilder result = new StringBuilder();

        // Обрабатываем каждую строку
        for (String line : lines) {
            String processedLine = processLine(line);
            if (!processedLine.isEmpty()) result.append(processedLine).append("\n");
        }

        String processed = result.toString();
        // Удаляем начальные и конечные пробельные символы
        processed = removeLowCharsFromStart(processed);
        processed = removeLowCharsFromEnd(processed);
        return processed;
    }

    // Удаляет комментарии из кода: как однострочные (//), так и многострочные (/* */)
    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int length = content.length();
        int i = 0;

        while (i < length) {
            // Обработка однострочных комментариев (//)
            if (i + 1 < length && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                // Пропускаем все символы до конца строки
                while (i < length && content.charAt(i) != '\n' && content.charAt(i) != '\r') i++;
            }
            // Обработка многострочных комментариев (/* */)
            else if (i + 1 < length && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                i += 2; // Пропускаем /*
                // Пропускаем все символы до */
                while (i + 1 < length && !(content.charAt(i) == '*' && content.charAt(i + 1) == '/')) i++;
                i += 2; // Пропускаем */
            }
            // Обычный текст - добавляем в результат
            else {
                result.append(content.charAt(i));
                i++;
            }
        }
        return result.toString();
    }

    // Обрабатывает отдельную строку кода
    private static String processLine(String line) {
        String trimmed = line.trim();

        // Полностью удаляем строки package и import
        if (trimmed.startsWith("package") || trimmed.startsWith("import")) return "";

        // Удаляем пробельные символы с начала и конца строки
        String processed = removeLowCharsFromStart(line);
        processed = removeLowCharsFromEnd(processed);
        return processed;
    }

    // Удаляет пробельные символы (с кодом < 33) из начала строки
    private static String removeLowCharsFromStart(String str) {
        int start = 0;
        while (start < str.length() && str.charAt(start) < 33) start++;
        return start == 0 ? str : str.substring(start);  // Оптимизация: не создаем подстроку если не нужно
    }

    // Удаляет пробельные символы из конца строки
    private static String removeLowCharsFromEnd(String str) {
        int end = str.length();
        while (end > 0 && str.charAt(end - 1) < 33) end--;
        return end == str.length() ? str : str.substring(0, end);  // Оптимизация
    }

    // Класс для хранения информации о файле (immutable - поля final)
    private static class FileData {
        final int size;              // Размер обработанного файла
        final String relativePath;   // Относительный путь

        FileData(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}