package by.it.group410901.zaythev.lesson15;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerA {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileInfo> results = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(Paths.get(src))) {
            stream
                    .filter(p -> !Files.isDirectory(p))
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            // Пробуем читать содержимое
                            String text = Files.readString(path, StandardCharsets.UTF_8);

                            // Пропускаем файлы-тесты
                            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                            // Удаляем строку package и все импорты за O(n)
                            List<String> lines = Arrays.asList(text.split("\n"));
                            List<String> filtered = new ArrayList<>();
                            for (String line : lines) {
                                line = line.replace("\r", "");
                                if (line.trim().startsWith("package ")) continue;
                                if (line.trim().startsWith("import ")) continue;
                                filtered.add(line);
                            }
                            String joined = String.join("\n", filtered);

                            // Удаляем символы с кодом <33 в начале и конце
                            joined = trimControlChars(joined);

                            // Получаем относительный путь от src
                            String rel = src.endsWith(File.separator)
                                    ? path.toString().substring(src.length())
                                    : path.toString().replace(src, "");

                            // Вычисляем размер в байтах
                            int byteSize = joined.getBytes(StandardCharsets.UTF_8).length;

                            results.add(new FileInfo(rel, byteSize));
                        } catch (MalformedInputException e) {
                            // Просто игнорируем некорректные файлы
                        } catch (IOException e) {
                            // Игнорируем другие IO ошибки чтения
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка обхода файлов: " + e.getMessage());
        }

        // Сортировка: сначала по размеру, затем по лексикографическому пути
        results.stream()
                .sorted(Comparator.comparingInt(FileInfo::getSize)
                        .thenComparing(FileInfo::getRelPath))
                .forEach(info ->
                        System.out.println(info.size + " " + info.relPath)
                );
    }

    // Вспомогательный метод для удаления управляющих символов < 33
    private static String trimControlChars(String text) {
        int start = 0;
        int end = text.length();
        while (start < end && text.charAt(start) < 33) start++;
        while (end > start && text.charAt(end - 1) < 33) end--;
        return text.substring(start, end);
    }

    // Класс для хранения информации о файле
    static class FileInfo {
        final String relPath;
        final int size;
        FileInfo(String relPath, int size) {
            this.relPath = relPath;
            this.size = size;
        }
        int getSize() { return size; }
        String getRelPath() { return relPath; }
    }
}
