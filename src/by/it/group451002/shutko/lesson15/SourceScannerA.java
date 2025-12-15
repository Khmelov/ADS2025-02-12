package by.it.group451002.shutko.lesson15;

import java.io.*;
import java.nio.file.*;
import java.util.*;

// Просканировать все Java-файлы в директории src, удалить package/import
// строки и управляющие символы, вычислить размеры обработанных файлов и
// вывести отсортированный список (размер + путь), исключая файлы с тестами.
public class SourceScannerA {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // Получаем путь к директории src (относительно текущей рабочей директории)
        String srcPath = System.getProperty("user.dir") + File.separator + "src";
        Path srcDir = Path.of(srcPath);
        List<FileInfo> fileInfos = new ArrayList<>(); // Список для хранения информации о файлах

        try {
            // Рекурсивный обход всех файлов в директории src и её поддиректориях
            Files.walk(srcDir)
                    .filter(p -> p.toString().endsWith(".java")) // Фильтр: только .java файлы
                    .forEach(p -> {
                        try {
                            // Получаем относительный путь (относительно src)
                            String relPath = srcDir.relativize(p).toString();
                            // Читаем всё содержимое файла как строку
                            String content = Files.readString(p);

                            // Пропускаем файлы, содержащие тесты
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Обрабатываем содержимое файла
                            String processed = processContent(content);

                            // Вычисляем размер в байтах (после обработки)
                            int sizeBytes = processed.getBytes().length;

                            // Сохраняем информацию о файле
                            fileInfos.add(new FileInfo(sizeBytes, relPath));

                        } catch (IOException e) {
                            // Игнорируем ошибки чтения файлов
                        }
                    });
        } catch (IOException e) {
            // Игнорируем ошибки обхода директории
        }

        // Сортировка: сначала по размеру (возрастание), затем по пути (лексикографически)
        fileInfos.sort(Comparator.comparingInt(FileInfo::getSize)
                .thenComparing(FileInfo::getPath));

        // Вывод результатов
        for (FileInfo fi : fileInfos) {
            System.out.println(fi.getSize() + " " + fi.getPath());
        }

        // Вывод времени выполнения (в stderr)
        long endTime = System.currentTimeMillis();
        System.err.println("Execution time: " + (endTime - startTime) + "ms");
    }

    // Метод обработки содержимого файла
    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        // Разделяем содержимое на строки (поддерживает разные форматы переноса строк)
        String[] lines = content.split("\r?\n");

        for (String line : lines) {
            String trimmed = line.trim();
            // Удаляем строки package и import (O(n) - один проход по строкам)
            if (!trimmed.startsWith("package") && !trimmed.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        String filteredContent = result.toString();

        // Удаление управляющих символов (с кодом < 33) в начале и конце текста
        return removeControlCharsFromEdges(filteredContent);
    }

    // Удаление символов с кодом < 33 только в начале и конце строки
    private static String removeControlCharsFromEdges(String text) {
        if (text.isEmpty()) return text;

        int start = 0;
        int end = text.length();

        // Находим первый символ с кодом >= 33 (не управляющий)
        while (start < end && text.charAt(start) < 33) {
            start++;
        }

        // Находим последний символ с кодом >= 33
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        // Возвращаем подстроку без управляющих символов по краям
        return text.substring(start, end);
    }

    // Внутренний класс для хранения информации о файле
    private static class FileInfo {
        private final int size;    // Размер в байтах
        private final String path; // Относительный путь

        public FileInfo(int size, String path) {
            this.size = size;
            this.path = path;
        }

        public int getSize() {
            return size;
        }

        public String getPath() {
            return path;
        }
    }
}