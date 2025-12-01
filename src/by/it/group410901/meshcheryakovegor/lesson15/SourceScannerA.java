package by.it.group410901.meshcheryakovegor.lesson15;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerA {

    public static void main(String[] args) throws Exception {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path srcPath = Paths.get(src);

        if (!Files.exists(srcPath)) {
            System.err.println("Каталог src не найден: " + src);
            return;
        }

        List<Result> results = new ArrayList<>();

        // Рекурсивный обход каталога
        try (Stream<Path> stream = Files.walk(srcPath)) {
            stream.filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> processFile(p, srcPath, results));
        }

        // Сортировка результатов
        results.sort(Comparator
                .comparingLong((Result r) -> r.size)
                .thenComparing(r -> r.relPath));

        // Вывод
        for (Result r : results) {
            System.out.println(r.size + " " + r.relPath);
        }
    }

    private static void processFile(Path file, Path srcPath, List<Result> results) {
        String text;

        // Чтение файла (игнорируем неправильную кодировку)
        try {
            byte[] bytes = Files.readAllBytes(file);
            text = new String(bytes, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            return; // просто пропускаем файл
        } catch (IOException e) {
            return; // пропуск при любой проблеме чтения
        }

        // Фильтр тестов
        if (text.contains("@Test") || text.contains("org.junit.Test"))
            return;

        // Удаляем package и imports — однопроходно
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.startsWith("package ") && trimmed.endsWith(";"))
                    continue;
                if (trimmed.startsWith("import ") && trimmed.endsWith(";"))
                    continue;
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            return; // не должно произойти
        }

        // Удаление символов <33 на концах текста
        String cleaned = trimControl(sb.toString());

        long size = cleaned.getBytes(StandardCharsets.UTF_8).length;

        Path rel = srcPath.relativize(file);
        results.add(new Result(rel.toString(), size));
    }

    // Удаление всех символов с кодом < 33 в начале и конце текста
    private static String trimControl(String s) {
        int start = 0;
        int end = s.length();

        while (start < end && s.charAt(start) < 33)
            start++;
        while (end > start && s.charAt(end - 1) < 33)
            end--;

        return s.substring(start, end);
    }

    private static class Result {
        final String relPath;
        final long size;

        Result(String relPath, long size) {
            this.relPath = relPath;
            this.size = size;
        }
    }
}

