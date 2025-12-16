package by.it.group410902.derzhavskaya_e.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Paths.get(src);

        if (!Files.exists(root)) {
            System.out.println("src folder not found: " + root);
            return;
        }

        List<FileInfo> result = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(root)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .filter(p -> !isTestFile(p))            // исключаем junit тесты
                    .forEach(p -> processFile(p, root, result));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // сортировка
        result.sort((a, b) -> {
            if (a.size != b.size)
                return Long.compare(a.size, b.size);
            return a.path.compareTo(b.path);
        });

        // вывод
        for (FileInfo fi : result) {
            System.out.println(fi.size + " " + fi.path);
        }
    }

    // Проверяем, что файл является тестом
    private static boolean isTestFile(Path p) {
        try {
            String text = Files.readString(p);
            return text.contains("@Test") || text.contains("org.junit.Test");
        } catch (IOException e) {
            return true; // лучше исключить сомнительный файл
        }
    }

    // Обработка одного файла
    private static void processFile(Path file, Path root, List<FileInfo> result) {
        String text;

        // безопасное чтение с защитой от MalformedInputException
        try {
            byte[] raw = Files.readAllBytes(file);
            text = new String(raw, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            // читаем в ISO-8859-1, вдруг файл кривой
            try {
                byte[] raw = Files.readAllBytes(file);
                text = new String(raw, StandardCharsets.ISO_8859_1);
            } catch (IOException ex) {
                return;
            }
        } catch (IOException e) {
            return;
        }

        String processed = cleanCode(text);

        // расчет относительного пути
        Path relative = root.relativize(file);
        String outPath = relative.toString().replace("/", "\\");

        result.add(new FileInfo(outPath, processed.getBytes(StandardCharsets.UTF_8).length));
    }

    // Удаление:
    // 1) package
    // 2) import
    // 3) комментариев
    // 4) пробелов <33 с краёв
    // 5) пустых строк
    private static String cleanCode(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        String[] lines = text.split("\n");

        boolean inBlockComment = false;

        for (String line : lines) {

            // удаляем package и import
            String trimmed = line.trim();
            if (trimmed.startsWith("package ") || trimmed.startsWith("import "))
                continue;

            StringBuilder cleared = new StringBuilder();

            int i = 0;
            while (i < line.length()) {

                // конец блочного комментария
                if (inBlockComment) {
                    int end = line.indexOf("*/", i);
                    if (end == -1) {
                        i = line.length();
                        continue;
                    } else {
                        inBlockComment = false;
                        i = end + 2;
                        continue;
                    }
                }

                // начало блочного комментария
                if (i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i += 2;
                    continue;
                }

                // начало однострочного комментария
                if (i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                    break; // остаток строки выбрасываем
                }

                cleared.append(line.charAt(i));
                i++;
            }

            String cleaned = trimLowChars(cleared.toString());

            if (!cleaned.isEmpty())
                sb.append(cleaned).append("\n");
        }

        return sb.toString();
    }

    // удаление символов с кодом <33 в начале и конце строки
    private static String trimLowChars(String s) {
        int start = 0;
        int end = s.length() - 1;

        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;

        if (start > end) return "";
        return s.substring(start, end + 1);
    }

    // контейнер результата
    static class FileInfo {
        String path;
        long size;

        FileInfo(String path, long size) {
            this.path = path;
            this.size = size;
        }
    }
}
