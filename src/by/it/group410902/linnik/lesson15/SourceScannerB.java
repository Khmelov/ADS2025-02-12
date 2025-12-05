package by.it.group410902.linnik.lesson15;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) {
        String srcPath = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            Files.walk(Paths.get(srcPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            processJavaFileOptimized(path, fileInfos, srcPath);
                        } catch (IOException e) {
                            System.err.println("Ошибка: " + path + " - " + e.getMessage());
                        }
                    });

        } catch (IOException e) {
            System.err.println("Ошибка при обходе: " + e.getMessage());
            return;
        }

        // Сортировка
        fileInfos.sort(Comparator.comparingInt((FileInfo a) -> a.size).thenComparing(a -> a.relativePath));

        // Вывод
        for (FileInfo info : fileInfos) {
            System.out.println(info.size + " " + info.relativePath);
        }
    }

    private static void processJavaFileOptimized(Path filePath, List<FileInfo> fileInfos, String srcPath)
            throws IOException {
        String content = readFileSafely(filePath);

        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        String processed = removePackageAndImports(content);
        processed = removeComments(processed);
        processed = processed.replaceAll("(?m)^[ \t]*\r?\n", "");
        processed = processed.trim(); // Удаляем пробелы в начале/конце

        // Получаем размер и путь
        int size = processed.getBytes(StandardCharsets.UTF_8).length;
        String relativePath = filePath.toString().substring(srcPath.length());

        fileInfos.add(new FileInfo(size, relativePath));
    }

    private static String readFileSafely(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);

        // Пробуем UTF-8
        try {
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e1) {
            // Пробуем Windows-1251
            try {
                return new String(bytes, "Windows-1251");
            } catch (Exception e2) {
                // Пробуем ISO-8859-1 (всегда работает для байтов 0-255)
                return new String(bytes, StandardCharsets.ISO_8859_1);
            }
        }
    }

    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("package") && !trimmed.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = content.length();

        while (i < n) {
            if (i + 1 < n && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < n && !(content.charAt(i) == '*' && content.charAt(i + 1) == '/')) {
                    i++;
                }
                i += 2;
            } else if (i + 1 < n && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                // Строчный комментарий
                i += 2;
                while (i < n && content.charAt(i) != '\n') {
                    i++;
                }
            } else if (content.charAt(i) == '"' || content.charAt(i) == '\'') {
                char quote = content.charAt(i);
                result.append(quote);
                i++;

                while (i < n && content.charAt(i) != quote) {
                    if (content.charAt(i) == '\\') {
                        result.append(content.charAt(i));
                        i++;
                        if (i < n) {
                            result.append(content.charAt(i));
                            i++;
                        }
                    } else {
                        result.append(content.charAt(i));
                        i++;
                    }
                }
                if (i < n) {
                    result.append(content.charAt(i));
                    i++;
                }
            } else {
                result.append(content.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    static class FileInfo {
        int size;
        String relativePath;

        FileInfo(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}
