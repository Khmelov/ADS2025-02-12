package by.it.group410902.dziatko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {
    public static void main(String[] args) {
        Path srcPath = Paths.get(System.getProperty("user.dir"), "src");

        List<FileInfo> results = new ArrayList<>();

        try {
            Files.walk(srcPath)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            List<String> lines;
                            try {
                                lines = Files.readAllLines(path, Charset.defaultCharset());
                            } catch (MalformedInputException e) {
                                lines = Files.readAllLines(path, Charset.forName("ISO-8859-1"));
                            }

                            String joined = String.join("\n", lines);
                            if (joined.contains("@Test") || joined.contains("org.junit.Test")) {
                                return;
                            }

                            StringBuilder sb = new StringBuilder();
                            for (String line : lines) {
                                if (line.startsWith("package ") || line.startsWith("import ")) {
                                    continue;
                                }
                                sb.append(line).append("\n");
                            }

                            String processed = sb.toString();

                            int start = 0, end = processed.length();
                            while (start < end && processed.charAt(start) < 33) start++;
                            while (end > start && processed.charAt(end - 1) < 33) end--;
                            processed = processed.substring(start, end);

                            int size = processed.getBytes(Charset.defaultCharset()).length;

                            String relative = srcPath.relativize(path.toAbsolutePath()).toString();

                            results.add(new FileInfo(size, relative));
                        } catch (IOException e) {
                        }
                    });
        } catch (IOException e) {
            System.out.println("Ошибка обхода каталога: " + e.getMessage());
        }

        results.sort(Comparator
                .comparingInt(FileInfo::size)
                .thenComparing(FileInfo::path));

        if (results.isEmpty()) {
            System.out.println("Файлы не найдены или все были исключены.");
        } else {
            System.out.println("Результаты обработки:");
            for (FileInfo fi : results) {
                System.out.println(fi.size + " байт\t" + fi.path);
            }
        }
    }

    static class FileInfo {
        final int size;
        final String path;

        FileInfo(int size, String path) {
            this.size = size;
            this.path = path;
        }

        int size() { return size; }
        String path() { return path; }
    }
}
