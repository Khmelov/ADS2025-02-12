package by.it.group451002.popeko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SourceScannerC {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<ProcessedFile> files = new ArrayList<>();

        try {
            Files.walk(srcPath)
                    .filter(path -> {
                        // ФИЛЬТР: берем только СВОИ файлы
                        String pathStr = path.toString();
                        return pathStr.endsWith(".java")
                                && pathStr.contains("group451002") // ТОЛЬКО моя группа
                                && !pathStr.contains("test")       // игнорируем тесты
                                && !pathStr.contains("Test");
                    })
                    .forEach(path -> {
                        try {
                            String content = Files.readString(path, StandardCharsets.UTF_8);

                            // Дополнительная проверка на тестовые файлы
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Обрабатываем файл
                            String processedContent = processContent(content);
                            String relativePath = srcPath.relativize(path).toString();

                            files.add(new ProcessedFile(relativePath, processedContent));

                        } catch (IOException e) {
                            System.err.println("Ошибка чтения файла: " + path);
                        }
                    });

            // Находим копии с оптимизацией
            findAndPrintCopiesOptimized(files);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processContent(String content) {
        // Удаляем package и imports
        content = removePackageAndImports(content);
        // Удаляем комментарии
        content = removeComments(content);
        // Заменяем последовательности символов с кодом <33 на пробел
        content = normalizeWhitespace(content);
        // Выполняем trim()
        content = content.trim();

        return content;
    }

    private static String removePackageAndImports(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package") && !trimmedLine.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean inChar = false;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    result.append(c);
                }
                continue;
            }

            if (inBlockComment) {
                if (c == '*' && i + 1 < content.length() && content.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i++; // Пропускаем '/'
                }
                continue;
            }

            if (inString) {
                result.append(c);
                if (c == '\\' && i + 1 < content.length()) {
                    result.append(content.charAt(i + 1));
                    i++;
                } else if (c == '"') {
                    inString = false;
                }
                continue;
            }

            if (inChar) {
                result.append(c);
                if (c == '\\' && i + 1 < content.length()) {
                    result.append(content.charAt(i + 1));
                    i++;
                } else if (c == '\'') {
                    inChar = false;
                }
                continue;
            }

            if (c == '"') {
                inString = true;
                result.append(c);
            } else if (c == '\'') {
                inChar = true;
                result.append(c);
            } else if (c == '/' && i + 1 < content.length()) {
                if (content.charAt(i + 1) == '/') {
                    inLineComment = true;
                    i++;
                } else if (content.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i++;
                } else {
                    result.append(c);
                }
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private static String normalizeWhitespace(String text) {
        StringBuilder result = new StringBuilder();
        boolean lastWasSpace = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!lastWasSpace) {
                    result.append(' ');
                    lastWasSpace = true;
                }
            } else {
                result.append(c);
                lastWasSpace = false;
            }
        }

        return result.toString();
    }

    private static void findAndPrintCopiesOptimized(List<ProcessedFile> files) {
        if (files.isEmpty()) {
            System.out.println("Нет файлов для анализа (фильтр group451002)");
            return;
        }

        Map<String, List<String>> copies = new TreeMap<>();
        int n = files.size();

        // Группируем файлы по длине для оптимизации
        Map<Integer, List<ProcessedFile>> filesByLength = new HashMap<>();
        for (ProcessedFile file : files) {
            int length = file.content.length();
            filesByLength.computeIfAbsent(length, k -> new ArrayList<>()).add(file);
        }

        // Сравниваем только файлы с близкой длиной
        for (int i = 0; i < n; i++) {
            ProcessedFile file1 = files.get(i);
            if (copies.containsKey(file1.path)) {
                continue; // Уже нашли как копию
            }

            List<String> fileCopies = new ArrayList<>();
            int length1 = file1.content.length();

            // Проверяем файлы с длиной в диапазоне ±20 символов
            for (int delta = -20; delta <= 20; delta++) {
                int targetLength = length1 + delta;
                List<ProcessedFile> candidateFiles = filesByLength.get(targetLength);
                if (candidateFiles != null) {
                    for (ProcessedFile file2 : candidateFiles) {
                        if (file1 == file2 || copies.containsKey(file2.path)) {
                            continue;
                        }

                        // Быстрая проверка по хешу для исключения заведомо разных файлов
                        if (Math.abs(file1.content.hashCode() - file2.content.hashCode()) > 1000) {
                            continue;
                        }

                        // Оптимизированное расстояние Левенштейна с ранним выходом
                        int distance = optimizedLevenshtein(file1.content, file2.content, 10);
                        if (distance < 10) {
                            fileCopies.add(file2.path);
                        }
                    }
                }
            }

            if (!fileCopies.isEmpty()) {
                copies.put(file1.path, fileCopies);
            }
        }

        // Вывод результатов
        if (copies.isEmpty()) {
            System.out.println("Дубликаты не найдены");
        } else {
            for (Map.Entry<String, List<String>> entry : copies.entrySet()) {
                System.out.println(entry.getKey());
                for (String copy : entry.getValue()) {
                    System.out.println(copy);
                }
                System.out.println(); // Пустая строка между группами
            }
        }
    }

    // Оптимизированная версия с ранним выходом
    private static int optimizedLevenshtein(String s1, String s2, int threshold) {
        int m = s1.length();
        int n = s2.length();

        // Если разница в длине больше порога, сразу возвращаем большое значение
        if (Math.abs(m - n) > threshold) {
            return threshold + 1;
        }

        // Используем только два массива для экономии памяти
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        for (int j = 0; j <= n; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= m; i++) {
            curr[0] = i;
            int minInRow = Integer.MAX_VALUE;

            for (int j = 1; j <= n; j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(Math.min(
                                curr[j - 1] + 1,
                                prev[j] + 1),
                        prev[j - 1] + cost
                );
                minInRow = Math.min(minInRow, curr[j]);
            }

            // Ранний выход если минимальное значение в строке превышает порог
            if (minInRow > threshold) {
                return threshold + 1;
            }

            // Обмен массивами
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[n];
    }

    private static class ProcessedFile {
        private final String path;
        private final String content;

        public ProcessedFile(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}