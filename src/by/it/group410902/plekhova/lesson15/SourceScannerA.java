package by.it.group410902.plekhova.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) {
        String srcDir = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Path.of(srcDir);

        List<Result> results = new ArrayList<>();

        try (var walk = Files.walk(root)) {
            walk.forEach(path -> {
                if (path.toString().endsWith(".java")) {
                    processFile(path, root).ifPresent(results::add);
                }
            });
        } catch (IOException e) {

            e.printStackTrace();
        }

        // сортировка: размер -> путь
        results.sort(Comparator
                .comparingLong((Result r) -> r.size)
                .thenComparing(r -> r.relative)
        );

        // вывод
        for (Result r : results) {
            System.out.println(r.size + " " + r.relative);
        }
    }


     // Обработка одного файла. Возвращает Optional.empty() если файл надо пропустить.

    private static Optional<Result> processFile(Path file, Path root) {
        String text;
        try {
            // безопасное чтение, игнорируем MalformedInputException
            text = Files.readString(file);
        } catch (MalformedInputException malformed) {
            return Optional.empty(); // просто пропускаем такой файл
        } catch (IOException e) {
            return Optional.empty(); // другие ошибки тоже пропускаем
        }

        // пропустить файлы, содержащие тесты
        if (text.contains("@Test") || text.contains("org.junit.Test")) {
            return Optional.empty();
        }

        // удалить package и import
        StringBuilder sb = new StringBuilder(text.length());
        String[] lines = text.split("\n", -1);

        for (String line : lines) {
            String trim = line.trim();
            if (trim.startsWith("package ") || trim.startsWith("import ")) {
                continue; // пропускаем
            }
            sb.append(line).append("\n");
        }

        String cleaned = sb.toString();

        // обрезать символы < 33 в начале и конце
        cleaned = trimLowAscii(cleaned);

        // размер в байтах в UTF-8
        long size = cleaned.getBytes(Charset.forName("UTF-8")).length;

        String relative = root.relativize(file).toString();

        return Optional.of(new Result(relative, size));
    }


     //Удаляет символы с кодом <33 в начале и конце строки.

    private static String trimLowAscii(String s) {
        int start = 0;
        int end = s.length();

        while (start < end && s.charAt(start) < 33) start++;
        while (end > start && s.charAt(end - 1) < 33) end--;

        return s.substring(start, end);
    }

    private static class Result {
        final String relative;
        final long size;

        Result(String relative, long size) {
            this.relative = relative;
            this.size = size;
        }
    }
}
