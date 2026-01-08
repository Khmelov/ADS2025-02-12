package by.it.group451001.drzhevetskiy.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) {
        Path baseDir = Paths.get(System.getProperty("user.dir"), "src");
        List<Record> data = new ArrayList<>();

        try {
            Files.walk(baseDir)
                    .filter(SourceScannerA::isValidJavaFile)
                    .forEach(path -> handleFile(baseDir, path, data));
        } catch (IOException e) {
            System.err.println("Не удалось обойти каталог: " + e.getMessage());
            return;
        }

        data.sort((a, b) -> {
            int c = Integer.compare(a.bytes, b.bytes);
            return c != 0 ? c : a.name.compareTo(b.name);
        });

        for (Record r : data) {
            System.out.println(r.bytes + " " + r.name);
        }

        System.err.println("Обработано файлов: " + data.size() + " (ТОЛЬКО ЧТЕНИЕ)");
    }

    // -------- file processing --------

    private static boolean isValidJavaFile(Path p) {
        return Files.isRegularFile(p)
                && Files.isReadable(p)
                && p.toString().endsWith(".java");
    }

    private static void handleFile(Path root, Path file, List<Record> out) {
        try {
            String text = Files.readString(file, StandardCharsets.UTF_8);

            if (containsTests(text)) return;

            String cleared = stripServiceLines(text);
            cleared = trimInvisible(cleared);

            int size = cleared.getBytes(StandardCharsets.UTF_8).length;
            String rel = root.relativize(file).toString();

            out.add(new Record(rel, size));

        } catch (IOException e) {
            System.err.println("Пропущен файл: " + file);
        }
    }

    // -------- content processing --------

    private static boolean containsTests(String s) {
        return s.contains("@Test") || s.contains("org.junit.Test");
    }

    private static String stripServiceLines(String src) {
        StringBuilder sb = new StringBuilder();
        for (String line : src.split("\n")) {
            String t = line.trim();
            if (t.startsWith("package") || t.startsWith("import")) continue;
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

    private static String trimInvisible(String s) {
        int l = 0, r = s.length() - 1;

        while (l <= r && s.charAt(l) <= 32) l++;
        while (r >= l && s.charAt(r) <= 32) r--;

        return (l <= r) ? s.substring(l, r + 1) : "";
    }

    // -------- helper structure --------

    private static class Record {
        String name;
        int bytes;

        Record(String name, int bytes) {
            this.name = name;
            this.bytes = bytes;
        }
    }
}
