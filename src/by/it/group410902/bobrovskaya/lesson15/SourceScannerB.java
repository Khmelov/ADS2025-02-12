package by.it.group410902.bobrovskaya.lesson15;

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

public class SourceScannerB {

    public static void main(String[] args) throws Exception {

        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);
        List<Result> results = new ArrayList<>();

        // Рекурсивный обход
        try (Stream<Path> stream = Files.walk(srcPath)) {
            stream.filter(p -> p.toString().endsWith(".java")).forEach(p -> processFile(p, srcPath, results));
        }

        // Сортировка
        results.sort(Comparator.comparingLong((Result r) -> r.size).thenComparing(r -> r.relPath));

        // Вывод
        for (Result r : results) {
            System.out.println(r.size + " " + r.relPath);
        }
    }

    private static void processFile(Path file, Path srcPath, List<Result> results) {

        String text; // для содержимого файла

        try {
            byte[] bytes = Files.readAllBytes(file); // считываем как массив байтов
            text = new String(bytes, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            return; // пропускаем
        } catch (IOException e) {
            return;
        }

        // Пропускаем тесты
        if (text.contains("@Test") || text.contains("org.junit.Test"))
            return;

        // Удаляем package и import (O(n))
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if ((trimmed.startsWith("package ") && trimmed.endsWith(";")) ||
                        (trimmed.startsWith("import ") && trimmed.endsWith(";"))) {
                    continue;
                }
                sb.append(line).append('\n');
            }
        } catch (IOException ignored) {}

        // Удаляем комментарии O(n)
        String noComments = removeComments(sb.toString());

        // Удаляем символы <33 в начале и конце
        String cleaned = trimControl(noComments);

        // Удаляем пустые строки
        StringBuilder noEmpty = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new StringReader(cleaned))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    noEmpty.append(line).append('\n');
                }
            }
        } catch (IOException ignored) {}

        String finalText = noEmpty.toString();
        long size = finalText.getBytes(StandardCharsets.UTF_8).length;

        Path rel = srcPath.relativize(file);
        results.add(new Result(rel.toString(), size));
    }

    private static String removeComments(String s) {
        StringBuilder out = new StringBuilder();
        int n = s.length();
        boolean inLine = false;
        boolean inBlock = false;

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);

            if (inLine) {
                if (c == '\n') {
                    inLine = false;
                    out.append(c);
                }
                continue;
            }

            if (inBlock) {
                if (c == '*' && i + 1 < n && s.charAt(i + 1) == '/') {
                    inBlock = false;
                    i++; // пропустить '/'
                }
                continue;
            }

            // начало // комментария
            if (c == '/' && i + 1 < n && s.charAt(i + 1) == '/') {
                inLine = true;
                i++;
                continue;
            }

            // начало /* ... */ комментария
            if (c == '/' && i + 1 < n && s.charAt(i + 1) == '*') {
                inBlock = true;
                i++;
                continue;
            }

            out.append(c);
        }

        return out.toString();
    }

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