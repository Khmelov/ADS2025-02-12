package by.it.group410902.shahov.lesson15;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // Получаем путь к директории src текущего проекта
        String srcPath = System.getProperty("user.dir") + File.separator + "src";
        Path srcDir = Path.of(srcPath);
        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            // Рекурсивно обходим все файлы в директории src
            Files.walk(srcDir)
                    // Фильтруем только Java файлы
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            // Получаем относительный путь файла
                            String relPath = srcDir.relativize(p).toString();
                            // Читаем содержимое файла
                            String content = Files.readString(p);

                            // Пропускаем тестовые файлы (содержащие аннотации @Test)
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Обрабатываем содержимое файла
                            String processed = processContent(content);

                            // Вычисляем размер обработанного содержимого в байтах
                            int sizeBytes = processed.getBytes().length;

                            // Сохраняем информацию о файле
                            fileInfos.add(new FileInfo(sizeBytes, relPath));

                        } catch (IOException e) {
                            // Игнорируем ошибки чтения отдельных файлов
                        }
                    });
        } catch (IOException e) {
            // Игнорируем ошибки обхода директории
        }

        // Сортируем файлы по размеру (по возрастанию), а при равных размерах - по пути
        fileInfos.sort(Comparator.comparingInt(FileInfo::getSize)
                .thenComparing(FileInfo::getPath));

        // Выводим отсортированный список файлов
        for (FileInfo fi : fileInfos) {
            System.out.println(fi.getSize() + " " + fi.getPath());
        }

        // Выводим время выполнения в stderr
        long endTime = System.currentTimeMillis();
        System.err.println("Execution time: " + (endTime - startTime) + "ms");
    }

    /**
     * Обрабатывает содержимое Java файла:
     * 1. Удаляет строки с package и import
     * 2. Удаляет управляющие символы с начала и конца
     */
    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        // Разбиваем содержимое на строки (поддерживает Windows и Unix форматы)
        String[] lines = content.split("\r?\n");

        for (String line : lines) {
            String trimmed = line.trim();
            // Сохраняем только строки, не начинающиеся с package или import
            if (!trimmed.startsWith("package") && !trimmed.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        String filteredContent = result.toString();

        // Удаляем управляющие символы (с кодом < 33) с начала и конца
        return removeControlCharsFromEdges(filteredContent);
    }

    /**
     * Удаляет управляющие символы (пробелы, табуляции, переносы строк и т.д.)
     * с начала и конца строки
     */
    private static String removeControlCharsFromEdges(String text) {
        if (text.isEmpty()) return text;

        int start = 0;
        int end = text.length();

        // Находим первый непробельный символ
        while (start < end && text.charAt(start) < 33) {
            start++;
        }

        // Находим последний непробельный символ
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        return text.substring(start, end);
    }

    /**
     * Вспомогательный класс для хранения информации о файле
     */
    private static class FileInfo {
        private final int size;    // Размер файла в байтах после обработки
        private final String path; // Относительный путь к файлу

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
