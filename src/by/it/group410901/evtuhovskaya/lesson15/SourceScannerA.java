package by.it.group410901.evtuhovskaya.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerA {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Paths.get(src);
        List<FileInfo> fileList = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java")).forEach(p -> {
                try {
                    // Игнорируем MalformedInputException через try-catch
                    String content = Files.readString(p, StandardCharsets.UTF_8);

                    if (content.contains("@Test") || content.contains("org.junit.Test")) {
                        return;
                    }

                    // 1. Удаление package и import
                    String cleaned = removePackageAndImports(content);
                    // 2. Удаление символов < 33 в начале и конце
                    cleaned = trimLowChars(cleaned);

                    if (!cleaned.isEmpty() || !content.isEmpty()) {
                        int size = cleaned.getBytes(StandardCharsets.UTF_8).length;
                        // Используем системный toString(), чтобы слэши совпали с тестом
                        String relPath = root.relativize(p).toString();
                        fileList.add(new FileInfo(relPath, size));
                    }
                } catch (MalformedInputException ignored) {
                    // По условию: найти способ игнорировать MalformedInputException
                } catch (IOException ignored) {}
            });
        } catch (IOException ignored) {}

        // Сортировка по размеру, затем лексикографически
        fileList.sort(Comparator.comparingInt((FileInfo f) -> f.size).thenComparing(f -> f.path));

        for (FileInfo info : fileList) {
            System.out.println(info.size + " " + info.path);
        }
    }

    private static String removePackageAndImports(String content) {
        String[] lines = content.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue;
            }
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    private static String trimLowChars(String s) {
        int start = 0;
        int end = s.length() - 1;
        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;
        return start > end ? "" : s.substring(start, end + 1);
    }

    private static class FileInfo {
        String path;
        int size;
        FileInfo(String path, int size) { this.path = path; this.size = size; }
    }
}