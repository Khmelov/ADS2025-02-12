package by.it.group410902.harkavy.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerA {

    private static class Result {
        final String path;
        final int size;

        Result(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Paths.get(src);

        List<Result> results = new ArrayList<>();

        try {
            Files.walk(root)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> processFile(p, root, results));
        } catch (IOException e) {

        }

        // сортировка: сначала по размеру, при равном размере — по пути
        results.sort(
                Comparator.comparingInt((Result r) -> r.size)
                        .thenComparing(r -> r.path)
        );

        // вывод: "<size> <relativePath>"
        for (Result r : results) {
            System.out.println(r.size + " " + r.path);
        }
    }

    private static void processFile(Path file, Path root, List<Result> results) {
        List<String> lines;

        try {
            // Явно задаём UTF-8 и игнорируем MalformedInputException
            lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            // по заданию — игнорируем такой файл
            return;
        } catch (IOException e) {
            // другие IO-ошибки тоже просто пропускаем
            return;
        }

        // игнорируем тестовые файлы
        boolean isTest = false;
        for (String line : lines) {
            if (line.contains("@Test") || line.contains("org.junit.Test")) {
                isTest = true;
                break;
            }
        }
        if (isTest) {
            return;
        }

        // 1. Удаляем строку package и все import'ы за O(n)
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("package ")
                    || trimmed.startsWith("import ")) {
                continue; // просто не добавляем в результат
            }
            sb.append(line).append('\n');
        }

        // 2. Удаляем все символы с кодом < 33 в начале и конце текста
        String text = sb.toString();
        int start = 0;
        int end = text.length() - 1;

        while (start <= end && text.charAt(start) < 33) {
            start++;
        }
        while (end >= start && text.charAt(end) < 33) {
            end--;
        }

        String cleaned;
        if (start > end) {
            cleaned = "";
        } else {
            cleaned = text.substring(start, end + 1);
        }

        // размер в байтах (возьмём UTF-8 — нормальный вариант для тестов)
        int size = cleaned.getBytes(StandardCharsets.UTF_8).length;

        String relativePath = root.relativize(file).toString();

        results.add(new Result(relativePath, size));
    }
}
