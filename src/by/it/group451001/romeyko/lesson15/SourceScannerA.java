package by.it.group451001.romeyko.lesson15;

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
import java.util.stream.Collectors;

public class SourceScannerA {

    // структура для хранения результатов
    static class FileInfo {
        final int size;
        final String path;

        FileInfo(int size, String path) {
            this.size = size;
            this.path = path;
        }

        public int size() { return size; }
        public String path() { return path; }
    }

    public static void main(String[] args) throws IOException {

        // получаем путь src/
        String srcRoot = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Paths.get(srcRoot);
        if (!Files.exists(root)) {
            System.err.println("Каталог src не найден: " + root);
            return;
        }

        // соберём все java файлы
        List<Path> javaFiles;
        try (var s = Files.walk(root)) {
            javaFiles = s.filter(p -> p.toString().endsWith(".java"))
                    .collect(Collectors.toList());
        }

        List<FileInfo> results = new ArrayList<>();
        for (Path file : javaFiles) {
            String text;
            try {
                text = Files.readString(file, StandardCharsets.UTF_8);
            } catch (MalformedInputException e) {
                continue;
            } catch (IOException e) {
                continue;
            }

            // пропускаем тестовые файлы
            if (text.contains("@Test") || text.contains("org.junit.Test")) {
                continue;
            }

            // удаляем package и import
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new StringReader(text))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String trim = line.trim();
                    if (trim.startsWith("package ")) continue;
                    if (trim.startsWith("import ")) continue;
                    sb.append(line).append('\n');
                }
            }

            String cleaned = sb.toString();
            int start = 0;
            int end = cleaned.length();
            while (start < end && cleaned.charAt(start) < 33) start++;
            while (end > start && cleaned.charAt(end - 1) < 33) end--;
            cleaned = cleaned.substring(start, end);

            // вычисляем размер в байтах (UTF-8)
            int sizeBytes = cleaned.getBytes(StandardCharsets.UTF_8).length;

            // относительный путь
            String relPath = root.relativize(file).toString();

            results.add(new FileInfo(sizeBytes, relPath));
        }

        // сортировка по размеру и пути
        results.sort(Comparator.comparing(FileInfo::size).thenComparing(FileInfo::path));
        for (FileInfo fi : results) {
            System.out.println(fi.size + "  " + fi.path);
        }
    }
}

