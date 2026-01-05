package by.it.group410902.varava.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerB {

    public static void main(String[] args) {

        // Путь к каталогу src
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Path.of(src);

        List<Result> results = new ArrayList<>();

        try (var walk = Files.walk(root)) {
            walk.forEach(path -> {

                // Берём только файлы .java
                if (!path.toString().endsWith(".java"))
                    return;

                String text;

                try {
                    text = Files.readString(path);
                } catch (MalformedInputException e) {
                    // плохая кодировка → пропускаем
                    return;
                } catch (IOException e) {
                    return;
                }

                // Пропускаем тестовые файлы
                if (text.contains("@Test") || text.contains("org.junit.Test"))
                    return;

                // Удаляем package, import и комментарии
                String cleaned = cleanText(text);

                // Удаляем символы <33 в начале и конце
                cleaned = trimLowAscii(cleaned);

                // Удаляем пустые строки
                cleaned = removeEmptyLines(cleaned);

                // Размер в байтах
                int size = cleaned.getBytes().length;

                // Относительный путь src → файл
                String rel = root.relativize(path).toString();

                results.add(new Result(size, rel));
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // сортировка по размеру, затем по пути
        results.sort(
                Comparator.comparingInt((Result r) -> r.size)
                        .thenComparing(r -> r.path)
        );

        // вывод
        for (Result r : results) {
            System.out.println(r.size + " " + r.path);
        }
    }

    // ---------------------------
    // УДАЛЕНИЕ package, import, комментариев
    // ---------------------------
    private static String cleanText(String text) {

        StringBuilder out = new StringBuilder();
        char[] c = text.toCharArray();
        int n = c.length;

        boolean inLineComment = false;   // //
        boolean inBlockComment = false;  /* ... */
        boolean inString = false;        // "text"

        // Сначала выбрасываем package/import построчно за O(n)
        String[] lines = text.split("\n");
        StringBuilder filtered = new StringBuilder();
        for (String line : lines) {
            String t = line.trim();

            if (t.startsWith("package ") || t.startsWith("import ")) {
                continue; // пропускаем
            }

            filtered.append(line).append("\n");
        }

        // Теперь удаляем комментарии (новая строка → новый проход)
        c = filtered.toString().toCharArray();
        n = c.length;

        for (int i = 0; i < n; i++) {

            if (inLineComment) {
                if (c[i] == '\n') {
                    inLineComment = false;
                    out.append(c[i]);
                }
                continue;
            }

            if (inBlockComment) {
                if (c[i] == '*' && i + 1 < n && c[i + 1] == '/') {
                    inBlockComment = false;
                    i++;
                }
                continue;
            }

            // Строки нужно оставить (чтобы не удалять // внутри кавычек)
            if (c[i] == '"' && !inString) {
                inString = true;
                out.append(c[i]);
                continue;
            } else if (c[i] == '"' && inString) {
                inString = false;
                out.append(c[i]);
                continue;
            }

            if (!inString) {
                // Однострочный комментарий //
                if (c[i] == '/' && i + 1 < n && c[i + 1] == '/') {
                    inLineComment = true;
                    i++;
                    continue;
                }

                // Многострочный комментарий /* ... */
                if (c[i] == '/' && i + 1 < n && c[i + 1] == '*') {
                    inBlockComment = true;
                    i++;
                    continue;
                }
            }

            out.append(c[i]);
        }

        return out.toString();
    }

    // удаление символов с кодом < 33 по краям
    private static String trimLowAscii(String s) {
        int start = 0;
        int end = s.length() - 1;

        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;

        if (start > end) return "";
        return s.substring(start, end + 1);
    }

    // удаление пустых строк
    private static String removeEmptyLines(String s) {
        StringBuilder sb = new StringBuilder();
        for (String line : s.split("\n")) {
            if (line.trim().isEmpty()) continue;
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    private record Result(int size, String path) {}
}
