package by.it.group410902.andala.lesson15;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerA {
    public static void main(String[] args) {
        // Получаем путь к папке src проекта
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileInfo> results = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(Paths.get(src))) {
            stream
                    .filter(p -> !Files.isDirectory(p)) // только файлы
                    .filter(p -> p.toString().endsWith(".java")) // только .java файлы
                    .forEach(path -> {
                        try {
                            // Читаем содержимое файла
                            String text = Files.readString(path, StandardCharsets.UTF_8);

                            // Пропускаем тестовые файлы
                            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                            // Удаляем package и import строки
                            List<String> lines = Arrays.asList(text.split("\n"));
                            List<String> filtered = new ArrayList<>();
                            for (String line : lines) {
                                line = line.replace("\r", "");
                                if (line.trim().startsWith("package ")) continue;
                                if (line.trim().startsWith("import ")) continue;
                                filtered.add(line);
                            }
                            String joined = String.join("\n", filtered);

                            // Удаляем управляющие символы по краям
                            joined = trimControlChars(joined);

                            // Получаем относительный путь
                            String rel = src.endsWith(File.separator)
                                    ? path.toString().substring(src.length())
                                    : path.toString().replace(src, "");

                            // Вычисляем размер в байтах
                            int byteSize = joined.getBytes(StandardCharsets.UTF_8).length;

                            results.add(new FileInfo(rel, byteSize));
                        } catch (MalformedInputException e) {
                            // Игнорируем файлы с некорректной кодировкой
                        } catch (IOException e) {
                            // Игнорируем ошибки чтения
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка обхода файлов: " + e.getMessage());
        }

        // Сортируем по размеру, затем по пути
        results.stream()
                .sorted(Comparator.comparingInt(FileInfo::getSize)
                        .thenComparing(FileInfo::getRelPath))
                .forEach(info ->
                        System.out.println(info.size + " " + info.relPath)
                );
    }

    // Удаляет управляющие символы с начала и конца строки
    private static String trimControlChars(String text) {
        int start = 0;
        int end = text.length();
        while (start < end && text.charAt(start) < 33) start++;
        while (end > start && text.charAt(end - 1) < 33) end--;
        return text.substring(start, end);
    }

    // Хранит информацию о файле
    static class FileInfo {
        final String relPath; // относительный путь
        final int size;       // размер в байтах
        FileInfo(String relPath, int size) {
            this.relPath = relPath;
            this.size = size;
        }
        int getSize() { return size; }
        String getRelPath() { return relPath; }
    }
}