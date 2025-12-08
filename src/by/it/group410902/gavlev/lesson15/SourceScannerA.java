package by.it.group410902.gavlev.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<Result> results = new ArrayList<>();

        try (var walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java")).forEach(p -> {
                try {
                    String text;
                    try {
                        text = Files.readString(p); // читаем весь файл сразу
                    } catch (MalformedInputException e) {
                        return; // игнорируем ошибки кодировки
                    }
                    if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                    // удаляем package и import за O(n)
                    StringBuilder sb = new StringBuilder(text.length());
                    int idx = 0;
                    int len = text.length();
                    while (idx < len) {
                        int lineEnd = text.indexOf('\n', idx);
                        if (lineEnd == -1) lineEnd = len;
                        String line = text.substring(idx, lineEnd);
                        String trim = line.trim();
                        if (!trim.startsWith("package") && !trim.startsWith("import")) {
                            sb.append(line).append('\n');
                        }
                        idx = lineEnd + 1;
                    }

                    // удаляем символы <33 в начале и конце
                    char[] arr = sb.toString().toCharArray();
                    int start = 0, end = arr.length;
                    while (start < end && arr[start] < 33) start++;
                    while (end > start && arr[end - 1] < 33) end--;

                    int size = end - start; // размер в байтах ~ длина строки (ASCII)
                    results.add(new Result(size, root.relativize(p).toString()));
                } catch (IOException ignored) {}
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // сортировка по размеру, затем по пути
        results.sort((a, b) -> {
            if (a.size != b.size) return Integer.compare(a.size, b.size);
            return a.path.compareTo(b.path);
        });

        StringBuilder out = new StringBuilder();
        for (Result r : results) {
            out.append(r.size).append(" ").append(r.path).append('\n');
        }
        System.out.print(out);
    }

    static class Result {
        int size;
        String path;
        Result(int s, String p) { size = s; path = p; }
    }
}
