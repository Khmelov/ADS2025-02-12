package by.it.group410901.danilova.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        try {
            List<FileContent> fileContents = processJavaFiles(src);
            findAndPrintDuplicates(fileContents);
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }

    private static List<FileContent> processJavaFiles(String srcDir) throws IOException {
        Path root = Paths.get(srcDir);
        List<FileContent> result = new ArrayList<>();

        if (!Files.exists(root)) {
            return result;
        }

        Files.walk(root)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        String content = readFileWithFallback(path);

                        if (content.contains("@Test") || content.contains("org.junit.Test")) {
                            return;
                        }

                        String processedContent = processContent(content);
                        String relativePath = root.relativize(path).toString();

                        result.add(new FileContent(relativePath, processedContent));

                    } catch (IOException e) {
                        // Ignore files with read errors
                    }
                });

        return result;
    }

    private static String readFileWithFallback(Path path) throws IOException {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            try {
                return Files.readString(path, Charset.defaultCharset());
            } catch (IOException e2) {
                return "";
            }
        }
    }

    private static String processContent(String content) {
        content = removePackageAndImports(content);
        content = removeComments(content);
        content = normalizeWhitespace(content);
        content = content.trim();
        return content;
    }

    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package ") && !trimmedLine.startsWith("import ")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int length = content.length();
        int i = 0;

        while (i < length) {
            if (i < length - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                // Однострочный комментарий
                while (i < length && content.charAt(i) != '\n') {
                    i++;
                }
            } else if (i < length - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                // Многострочный комментарий
                i += 2;
                while (i < length - 1 && !(content.charAt(i) == '*' && content.charAt(i + 1) == '/')) {
                    i++;
                }
                i += 2;
            } else {
                result.append(content.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    private static String normalizeWhitespace(String content) {
        StringBuilder result = new StringBuilder();
        boolean inWhitespace = false;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c < 33) {
                if (!inWhitespace) {
                    result.append(' ');
                    inWhitespace = true;
                }
            } else {
                result.append(c);
                inWhitespace = false;
            }
        }

        return result.toString();
    }

    private static void findAndPrintDuplicates(List<FileContent> fileContents) {
        int n = fileContents.size();
        Map<String, List<String>> duplicatesMap = new TreeMap<>();

        // Оптимизация: сначала группируем по длине для уменьшения сравнений
        Map<Integer, List<FileContent>> lengthGroups = new HashMap<>();
        for (FileContent fc : fileContents) {
            int len = fc.content.length();
            lengthGroups.computeIfAbsent(len, k -> new ArrayList<>()).add(fc);
        }

        // Сравниваем файлы в группах с похожей длиной
        for (List<FileContent> group : lengthGroups.values()) {
            if (group.size() < 2) continue;

            for (int i = 0; i < group.size(); i++) {
                FileContent fc1 = group.get(i);
                List<String> copies = new ArrayList<>();

                for (int j = i + 1; j < group.size(); j++) {
                    FileContent fc2 = group.get(j);

                    // Быстрая проверка: если длины сильно отличаются, пропускаем
                    if (Math.abs(fc1.content.length() - fc2.content.length()) > 20) {
                        continue;
                    }

                    int distance = optimizedLevenshtein(fc1.content, fc2.content);
                    if (distance < 10) {
                        copies.add(fc2.path);
                    }
                }

                if (!copies.isEmpty()) {
                    Collections.sort(copies);
                    duplicatesMap.put(fc1.path, copies);
                }
            }
        }

        // Вывод результатов
        for (Map.Entry<String, List<String>> entry : duplicatesMap.entrySet()) {
            System.out.println(entry.getKey());
            for (String copy : entry.getValue()) {
                System.out.println(copy);
            }
        }
    }

    // Оптимизированная версия расстояния Левенштейна с отсечением
    private static int optimizedLevenshtein(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        // Быстрая проверка на идентичность
        if (s1.equals(s2)) return 0;

        // Используем только два ряда для экономии памяти
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

        // Инициализация первого ряда
        for (int j = 0; j <= len2; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            curr[0] = i;
            int minInRow = i;

            for (int j = 1; j <= len2; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(Math.min(prev[j], curr[j - 1]), prev[j - 1]);
                }
                minInRow = Math.min(minInRow, curr[j]);
            }

            // Отсечение: если минимальное расстояние в строке уже превышает порог
            if (minInRow > 10) {
                return minInRow;
            }

            // Swap arrays
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[len2];
    }

    private static class FileContent {
        private final String path;
        private final String content;

        public FileContent(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}