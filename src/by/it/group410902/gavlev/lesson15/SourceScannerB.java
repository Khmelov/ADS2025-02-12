package by.it.group410902.gavlev.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<Result> results = new ArrayList<>();

        try (var walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java")).forEach(p -> {
                try {
                    String text;
                    try {
                        text = Files.readString(p);
                    } catch (MalformedInputException e) {
                        return; // корректно игнорируем ошибки
                    }
                    if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                    // удаляем package и import
                    StringBuilder sb = new StringBuilder();
                    for (String line : text.split("\n")) {
                        line = line.trim();
                        if (line.startsWith("package") || line.startsWith("import")) continue;
                        sb.append(line).append("\n");
                    }
                    String cleaned = sb.toString();

                    // удаляем комментарии (однострочные и многострочные)
                    cleaned = cleaned.replaceAll("//.*", "");
                    cleaned = cleaned.replaceAll("/\\*.*?\\*/", "");

                    // удаляем символы <33 в начале и конце
                    int start = 0, end = cleaned.length();
                    while (start < end && cleaned.charAt(start) < 33) start++;
                    while (end > start && cleaned.charAt(end - 1) < 33) end--;
                    cleaned = cleaned.substring(start, end);

                    // удаляем пустые строки
                    StringBuilder sb2 = new StringBuilder();
                    for (String line : cleaned.split("\n")) {
                        if (!line.trim().isEmpty()) sb2.append(line).append("\n");
                    }
                    cleaned = sb2.toString();

                    results.add(new Result(cleaned.getBytes().length, root.relativize(p).toString()));
                } catch (IOException ignored) {}
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        results.sort(Comparator.comparingInt((Result r) -> r.size).thenComparing(r -> r.path));
        for (Result r : results) {
            System.out.println(r.size + " " + r.path);
        }
    }

    static class Result {
        int size;
        String path;
        Result(int s, String p) { size = s; path = p; }
    }
}
