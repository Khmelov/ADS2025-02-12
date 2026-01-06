package by.it.group410902.andala.lesson15;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerB {
    public static void main(String[] args) {
        // Получаем путь к папке src проекта
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileInfo> results = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(Paths.get(src))) {
            stream
                    .filter(p -> !Files.isDirectory(p)) // только файлы
                    .filter(p -> p.toString().endsWith(".java")) // только .java
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
                                String trimmed = line.trim();
                                if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) continue;
                                filtered.add(line);
                            }
                            String joined = String.join("\n", filtered);

                            // Удаляем комментарии
                            joined = removeComments(joined);

                            // Удаляем управляющие символы по краям
                            joined = trimControlChars(joined);

                            // Удаляем пустые строки
                            lines = Arrays.asList(joined.split("\n"));
                            filtered = new ArrayList<>();
                            for (String line : lines) {
                                if (line.trim().isEmpty()) continue;
                                filtered.add(line);
                            }
                            joined = String.join("\n", filtered);

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

    // Удаляет все комментарии из кода
    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder();
        int len = text.length();
        boolean isBlock = false, isLine = false; // флаги для комментариев
        for (int i = 0; i < len; ) {
            // Начало блочного комментария
            if (!isBlock && !isLine && i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '*') {
                isBlock = true; i += 2; continue;
            }
            // Конец блочного комментария
            if (isBlock && i + 1 < len && text.charAt(i) == '*' && text.charAt(i + 1) == '/') {
                isBlock = false; i += 2; continue;
            }
            // Начало строчного комментария
            if (!isBlock && !isLine && i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '/') {
                isLine = true; i += 2; continue;
            }
            // Конец строчного комментария
            if (isLine && (text.charAt(i) == '\n' || text.charAt(i) == '\r')) {
                isLine = false; sb.append(text.charAt(i)); i++; continue;
            }
            // Пропускаем символы внутри комментариев
            if (isBlock || isLine) { i++; continue; }
            // Добавляем обычные символы
            sb.append(text.charAt(i)); i++;
        }
        return sb.toString();
    }

    // Удаляет управляющие символы с начала и конца
    private static String trimControlChars(String text) {
        int start = 0, end = text.length();
        while (start < end && text.charAt(start) < 33) ++start;
        while (end > start && text.charAt(end - 1) < 33) --end;
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