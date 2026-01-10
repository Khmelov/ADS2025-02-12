package by.it.group451002.shandr.lesson15;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {
    public static void main(String[] args) {
        // Засекаем время начала выполнения программы
        long startTime = System.currentTimeMillis();

        // Определяем путь к директории src текущего проекта
        String srcPath = System.getProperty("user.dir") + File.separator + "src";
        Path srcDir = Path.of(srcPath);

        // Список для хранения информации о файлах
        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            // Рекурсивно обходим все файлы и поддиректории в src
            Files.walk(srcDir)
                    // Фильтруем только Java файлы
                    .filter(p -> p.toString().endsWith(".java"))
                    // Для каждого Java файла
                    .forEach(p -> {
                        try {
                            // Получаем относительный путь файла (относительно src)
                            String relPath = srcDir.relativize(p).toString();

                            // Читаем содержимое файла
                            String content = Files.readString(p);

                            // Пропускаем тестовые файлы (содержат аннотации @Test)
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return; // переходим к следующему файлу
                            }

                            // Обрабатываем содержимое файла (удаляем package/import и т.д.)
                            String processed = processContent(content);

                            // Вычисляем размер обработанного содержимого в байтах
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


        // Сортируем файлы по двум критериям:
        // 1. По размеру (возрастание)
        // 2. По пути (лексикографически)
        fileInfos.sort(Comparator.comparingInt(FileInfo::getSize)
                .thenComparing(FileInfo::getPath));


        // Выводим результат: размер и путь для каждого файла
        for (FileInfo fi : fileInfos) {
            System.out.println(fi.getSize() + " " + fi.getPath());
        }

        // Засекаем время окончания и выводим общее время выполнения
        long endTime = System.currentTimeMillis();
        System.err.println("Execution time: " + (endTime - startTime) + "ms");
    }

    // Метод для обработки содержимого Java файла
    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();

        // Разделяем содержимое на строки
        String[] lines = content.split("\r?\n");

        // Обрабатываем каждую строку
        for (String line : lines) {
            String trimmed = line.trim(); // Убираем пробелы по краям

            // Пропускаем строки, начинающиеся с package или import
            if (!trimmed.startsWith("package") && !trimmed.startsWith("import")) {
                result.append(line).append("\n"); // Сохраняем исходную строку с переносом
            }
        }

        String filteredContent = result.toString();

        // Удаляем управляющие символы (с кодом < 33) с начала и конца
        return removeControlCharsFromEdges(filteredContent);
    }

    // Метод удаления управляющих символов с краев строки
    private static String removeControlCharsFromEdges(String text) {
        if (text.isEmpty()) return text;

        int start = 0;
        int end = text.length();

        // Удаляем управляющие символы с начала строки
        // ASCII символы с кодом < 33: пробелы, табуляции, переносы строк и т.д.
        while (start < end && text.charAt(start) < 33) {
            start++;
        }

        // Удаляем управляющие символы с конца строки
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        // Возвращаем подстроку без управляющих символов по краям
        return text.substring(start, end);
    }

    // Внутренний класс для хранения информации о файле
    private static class FileInfo {
        private final int size;      // Размер обработанного содержимого в байтах
        private final String path;   // Относительный путь к файлу

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