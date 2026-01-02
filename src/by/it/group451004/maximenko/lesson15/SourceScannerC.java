package by.it.group451004.maximenko.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class SourceScannerC {
    private static final Set<String> TEST_KEYWORDS = Set.of("@Test", "org.junit.Test");
    private static final int COPY_THRESHOLD = 10;

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src/by/it/group451004" + File.separator;

        try {
            List<ProcessedFile> files = processSourceFiles(src);
            findAndPrintCopies(files);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<ProcessedFile> processSourceFiles(String srcDir) throws IOException {
        Path start = Paths.get(srcDir);
        if (!Files.exists(start)) {
            return Collections.emptyList();
        }

        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        try {
            List<Future<ProcessedFile>> futures = Files.walk(start)
                    .filter(p -> p.toString().endsWith(".java"))
                    .map(p -> executor.submit(() -> processFile(p)))
                    .collect(Collectors.toList());

            List<ProcessedFile> results = new ArrayList<>();
            for (Future<ProcessedFile> future : futures) {
                try {
                    ProcessedFile pf = future.get();
                    if (pf != null) {
                        results.add(pf);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    System.err.println("Failed to process file: " + e.getCause());
                }
            }
            return results;
        } finally {
            executor.shutdown();
        }
    }

    private static ProcessedFile processFile(Path filePath) {
        try {
            String content = readFile(filePath);

            // Проверка на тестовый файл
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return null;
            }

            String processed = processContent(content);
            return new ProcessedFile(filePath.toString(), processed);

        } catch (MalformedInputException e) {
            // Попробуем другие кодировки
            return tryAlternativeEncodings(filePath);
        } catch (IOException e) {
            System.err.println("Cannot read file: " + filePath + " - " + e.getMessage());
            return null;
        }
    }

    private static String readFile(Path path) throws IOException {
        // Читаем с автоопределением кодировки (UTF-8 по умолчанию)
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    private static ProcessedFile tryAlternativeEncodings(Path filePath) {
        Charset[] charsets = {
                StandardCharsets.ISO_8859_1,
                StandardCharsets.US_ASCII,
                Charset.forName("Windows-1251")
        };

        for (Charset charset : charsets) {
            try {
                String content = Files.readString(filePath, charset);
                if (content.contains("@Test") || content.contains("org.junit.Test")) {
                    return null;
                }
                String processed = processContent(content);
                return new ProcessedFile(filePath.toString(), processed);
            } catch (IOException e) {
                continue;
            }
        }

        System.err.println("Failed to read file with any encoding: " + filePath);
        return null;
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder(content.length());
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean inChar = false;
        char prev = 0;

        // Обрабатываем посимвольно за O(n)
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            char next = i + 1 < content.length() ? content.charAt(i + 1) : 0;

            // Обработка строковых и символьных литералов
            if (!inBlockComment && !inLineComment) {
                if (!inString && !inChar && c == '"' && prev != '\\') {
                    inString = true;
                    result.append(' ');
                    prev = c;
                    continue;
                } else if (inString && c == '"' && prev != '\\') {
                    inString = false;
                    result.append(' ');
                    prev = c;
                    continue;
                } else if (!inString && !inChar && c == '\'' && prev != '\\') {
                    inChar = true;
                    result.append(' ');
                    prev = c;
                    continue;
                } else if (inChar && c == '\'' && prev != '\\') {
                    inChar = false;
                    result.append(' ');
                    prev = c;
                    continue;
                }

                if (inString || inChar) {
                    result.append(' ');
                    prev = c;
                    continue;
                }
            }

            // Обработка комментариев
            if (!inString && !inChar) {
                if (!inBlockComment && !inLineComment && c == '/' && next == '*') {
                    inBlockComment = true;
                    i++;
                    continue;
                } else if (inBlockComment && c == '*' && next == '/') {
                    inBlockComment = false;
                    i++;
                    continue;
                } else if (!inBlockComment && c == '/' && next == '/') {
                    inLineComment = true;
                    i++;
                    continue;
                } else if (inLineComment && c == '\n') {
                    inLineComment = false;
                    result.append(' ');
                    prev = c;
                    continue;
                }
            }

            if (inBlockComment || inLineComment) {
                prev = c;
                continue;
            }

            // Добавляем символ
            if (c < 33) {
                result.append(' ');
            } else {
                result.append(c);
            }
            prev = c;
        }

        // Удаляем package и imports
        String processed = result.toString();
        processed = Arrays.stream(processed.split("\n"))
                .filter(line -> !line.trim().startsWith("package") &&
                        !line.trim().startsWith("import"))
                .collect(Collectors.joining("\n"));

        // Заменяем последовательности пробелов на один
        processed = processed.replaceAll("\\s+", " ").trim();

        return processed;
    }

    private static void findAndPrintCopies(List<ProcessedFile> files) {
        if (files.size() < 2) return;

        Map<String, List<String>> copiesMap = new TreeMap<>();

        // Используем оптимизацию: предварительная фильтрация по длине
        List<ProcessedFile> sortedByLength = new ArrayList<>(files);
        sortedByLength.sort(Comparator.comparingInt(pf -> pf.content.length()));

        for (int i = 0; i < sortedByLength.size(); i++) {
            ProcessedFile file1 = sortedByLength.get(i);

            for (int j = i + 1; j < sortedByLength.size(); j++) {
                ProcessedFile file2 = sortedByLength.get(j);

                // Быстрая проверка: если разница в длине слишком большая, пропускаем
                int lengthDiff = Math.abs(file1.content.length() - file2.content.length());
                if (lengthDiff > COPY_THRESHOLD * 10) {
                    continue;
                }

                int distance = levenshteinDistance(file1.content, file2.content);
                if (distance < COPY_THRESHOLD) {
                    copiesMap.computeIfAbsent(file1.path, k -> new ArrayList<>()).add(file2.path);
                    copiesMap.computeIfAbsent(file2.path, k -> new ArrayList<>()).add(file1.path);
                }
            }
        }

        // Вывод результатов
        for (Map.Entry<String, List<String>> entry : copiesMap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                List<String> sortedCopies = new ArrayList<>(entry.getValue());
                Collections.sort(sortedCopies);

                System.out.println(entry.getKey());
                for (String copy : sortedCopies) {
                    System.out.println("  " + copy);
                }
            }
        }
    }

    // Оптимизированное расстояние Левенштейна с отсечением
    private static int levenshteinDistance(String s1, String s2) {
        if (s1.isEmpty()) return s2.length();
        if (s2.isEmpty()) return s1.length();

        // Меняем местами, чтобы s1 была короче (для экономии памяти)
        if (s1.length() > s2.length()) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
        }

        int len1 = s1.length();
        int len2 = s2.length();

        // Используем два массива вместо матрицы для экономии памяти
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

        for (int j = 0; j <= len2; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            curr[0] = i;
            int minRow = i;

            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(curr[j - 1] + 1, prev[j] + 1),
                        prev[j - 1] + cost
                );
                minRow = Math.min(minRow, curr[j]);
            }

            // Отсечение: если минимальное значение в строке больше порога
            if (minRow > COPY_THRESHOLD) {
                return COPY_THRESHOLD + 1;
            }

            // Обмен массивами
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[len2];
    }

    private static class ProcessedFile {
        final String path;
        final String content;

        ProcessedFile(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}
