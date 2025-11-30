package by.it.group410902.jalilova.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) {

        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Paths.get(src);

        List<Result> results = new ArrayList<>();

        try {
            Files.walk(root)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        String text;
                        try {
                            try {
                                text = Files.readString(p);
                            } catch (MalformedInputException e) {
                                return; // игнорируем битые файлы
                            }

                            // пропускаем тестовые файлы
                            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                            // удаляем package/import и комментарии
                            String cleaned = removePackageImportAndComments(text);

                            // обрезаем символы <33
                            cleaned = trimNonPrintable(cleaned);

                            if (cleaned.isEmpty()) return;

                            // размер в байтах
                            int size = cleaned.getBytes().length;

                            // относительный путь
                            String relPath = root.relativize(p).toString();

                            results.add(new Result(size, relPath));

                        } catch (IOException ignored) {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // сортировка: сначала по размеру, потом по пути
        results.sort(Comparator
                .comparingInt((Result r) -> r.size)
                .thenComparing(r -> r.path));

        // вывод
        for (Result r : results) {
            System.out.println(r.size + " " + r.path);
        }
    }

    private static String removePackageImportAndComments(String text) {
        StringBuilder sb = new StringBuilder();
        boolean inBlockComment = false;

        String[] lines = text.split("\n");
        for (String line : lines) {
            String s = line;

            // блок комментария /* ... */
            if (inBlockComment) {
                if (s.contains("*/")) {
                    s = s.substring(s.indexOf("*/") + 2);
                    inBlockComment = false;
                } else continue;
            }

            // удаляем все блоки /* ... */
            while (s.contains("/*")) {
                int start = s.indexOf("/*");
                int end = s.indexOf("*/", start + 2);
                if (end >= 0) {
                    s = s.substring(0, start) + s.substring(end + 2);
                } else {
                    s = s.substring(0, start);
                    inBlockComment = true;
                    break;
                }
            }

            // удаляем однострочные комментарии //
            int idx = s.indexOf("//");
            if (idx >= 0) s = s.substring(0, idx);

            // убираем package/import и пустые строки
            String t = s.strip();
            if (t.isEmpty()) continue;
            if (t.startsWith("package ") || t.startsWith("import ")) continue;

            sb.append(t).append("\n");
        }

        return sb.toString();
    }

    private static String trimNonPrintable(String s) {
        int start = 0;
        int end = s.length() - 1;
        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;
        return (start > end) ? "" : s.substring(start, end + 1);
    }

    private record Result(int size, String path) {}
}
