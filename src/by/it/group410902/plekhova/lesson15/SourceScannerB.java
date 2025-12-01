package by.it.group410902.plekhova.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {

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

        // сортировка: сначала по размеру, затем по пути
        results.sort(Comparator
                .comparingLong((Result r) -> r.size)
                .thenComparing(r -> r.relative)
        );

        for (Result r : results) {
            System.out.println(r.size + " " + r.relative);
        }
    }

    private static Optional<Result> processFile(Path file, Path root) {
        String text;
        try {
            text = Files.readString(file);
        } catch (MalformedInputException malformed) {
            return Optional.empty();  // корректно пропускаем "битые" файлы
        } catch (IOException e) {
            return Optional.empty();
        }

        // пропуск тестов
        if (text.contains("@Test") || text.contains("org.junit.Test")) {
            return Optional.empty();
        }

        // Удаление package и import — O(n), построчно
        String[] lines = text.split("\n", -1);
        StringBuilder sb = new StringBuilder(text.length());

        for (String line : lines) {
            String t = line.trim();
            if (t.startsWith("package ") || t.startsWith("import ")) {
                continue;
            }
            sb.append(line).append("\n");
        }

        String noPkgImports = sb.toString();

        // Удаление всех комментариев за O(n)
        String noComments = removeComments(noPkgImports);

        // Удалить управляющие символы <33 в начале и конце
        String trimmed = trimLowAscii(noComments);

        // Удалить пустые строки
        String noEmptyLines = removeEmptyLines(trimmed);

        // вычисление размера в UTF-8
        long size = noEmptyLines.getBytes(Charset.forName("UTF-8")).length;

        String relative = root.relativize(file).toString();
        return Optional.of(new Result(relative, size));
    }


     //Удаление комментариев

    private static String removeComments(String s) {
        StringBuilder out = new StringBuilder(s.length());
        int n = s.length();
        boolean inBlock = false;
        int i = 0;

        while (i < n) {
            if (!inBlock) {
                // начало блокового комментария
                if (i + 1 < n && s.charAt(i) == '/' && s.charAt(i + 1) == '*') {
                    inBlock = true;
                    i += 2;
                }
                // однострочный комментарий
                else if (i + 1 < n && s.charAt(i) == '/' && s.charAt(i + 1) == '/') {
                    // пропустить до конца строки
                    i += 2;
                    while (i < n && s.charAt(i) != '\n') i++;
                }
                else {
                    out.append(s.charAt(i));
                    i++;
                }
            } else {
                // мы внутри блокового комментария /* ... */
                if (i + 1 < n && s.charAt(i) == '*' && s.charAt(i + 1) == '/') {
                    inBlock = false;
                    i += 2;
                } else {
                    i++;
                }
            }
        }
        return out.toString();
    }


     // Удаляет символы с кодом <33 в начале и конце строки.

    private static String trimLowAscii(String s) {
        int start = 0;
        int end = s.length();

        while (start < end && s.charAt(start) < 33) start++;
        while (end > start && s.charAt(end - 1) < 33) end--;

        return s.substring(start, end);
    }


     //Удаляет пустые строки (те, где trim().isEmpty()).

    private static String removeEmptyLines(String s) {
        StringBuilder out = new StringBuilder(s.length());
        String[] lines = s.split("\n", -1);

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                out.append(line).append("\n");
            }
        }
        return out.toString();
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
