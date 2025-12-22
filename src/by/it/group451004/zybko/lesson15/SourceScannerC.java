package by.it.group451004.zybko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<ProcessedFile> processedFiles = new ArrayList<>();

        try {
            List<Path> javaFiles = findJavaFiles(Paths.get(src));

            for (Path filePath : javaFiles) {
                try {
                    String relativePath = getRelativePath(src, filePath.toString());

                    String content = readFileWithErrorHandling(filePath);

                    if (content.contains("@Test") || content.contains("org.junit.Test")) {
                        continue;
                    }

                    String processedContent = processContent(content);

                    if (!processedContent.isEmpty()) {
                        processedFiles.add(new ProcessedFile(relativePath, processedContent));
                    }

                } catch (IOException e) {
                    System.err.println("Ошибка при обработке файла: " + filePath);
                }
            }

            // Находим копии
            Map<String, List<String>> copiesMap = findCopies(processedFiles, 9); // <10 правок

            // Сортируем пути файлов лексикографически
            List<String> sortedPaths = new ArrayList<>(copiesMap.keySet());
            Collections.sort(sortedPaths);

            // Выводим результаты
            for (String filePath : sortedPaths) {
                System.out.println(filePath);
                List<String> copies = copiesMap.get(filePath);
                Collections.sort(copies);
                for (String copyPath : copies) {
                    System.out.println(copyPath);
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка при поиске файлов: " + e.getMessage());
        }
    }

    private static List<Path> findJavaFiles(Path startPath) throws IOException {
        try (var walk = Files.walk(startPath)) {
            return walk
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .collect(java.util.stream.Collectors.toList());
        }
    }

    private static String readFileWithErrorHandling(Path filePath) throws IOException {
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPLACE)
                .onUnmappableCharacter(CodingErrorAction.REPLACE);

        byte[] fileBytes = Files.readAllBytes(filePath);
        return new String(fileBytes, decoder.charset());
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        boolean inBlockComment = false;
        boolean inString = false;
        boolean inChar = false;
        char stringDelimiter = '"';

        for (String line : lines) {
            StringBuilder lineResult = new StringBuilder();
            boolean skipLine = false;

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                // Проверяем, не начинается ли строка с package или import
                if (i == 0 && line.trim().startsWith("package ")) {
                    skipLine = true;
                    break;
                }
                if (i == 0 && line.trim().startsWith("import ")) {
                    skipLine = true;
                    break;
                }

                if (inBlockComment) {
                    if (c == '*' && i + 1 < line.length() && line.charAt(i + 1) == '/') {
                        inBlockComment = false;
                        i++; // Пропускаем '/'
                    }
                    continue;
                }

                if (inString) {
                    lineResult.append(c);
                    if (c == '\\' && i + 1 < line.length()) {
                        // Экранированный символ
                        lineResult.append(line.charAt(i + 1));
                        i++;
                    } else if (c == stringDelimiter) {
                        inString = false;
                    }
                    continue;
                }

                if (inChar) {
                    lineResult.append(c);
                    if (c == '\\' && i + 1 < line.length()) {
                        // Экранированный символ
                        lineResult.append(line.charAt(i + 1));
                        i++;
                    } else if (c == '\'') {
                        inChar = false;
                    }
                    continue;
                }

                // Проверяем начало блочного комментария
                if (c == '/' && i + 1 < line.length() && line.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i++; // Пропускаем '*'
                    continue;
                }

                // Проверяем однострочный комментарий
                if (c == '/' && i + 1 < line.length() && line.charAt(i + 1) == '/') {
                    break; // Пропускаем остаток строки
                }

                // Проверяем начало строкового литерала
                if (c == '"') {
                    inString = true;
                    stringDelimiter = '"';
                    lineResult.append(c);
                    continue;
                }

                // Проверяем начало символьного литерала
                if (c == '\'') {
                    inChar = true;
                    lineResult.append(c);
                    continue;
                }

                // Заменяем символы с кодом <33 на пробел
                if (c < 33) {
                    lineResult.append(' ');
                } else {
                    lineResult.append(c);
                }
            }

            if (!skipLine && lineResult.length() > 0) {
                String processedLine = lineResult.toString().trim();
                if (!processedLine.isEmpty()) {
                    result.append(processedLine).append(" ");
                }
            }
        }

        String processed = result.toString().trim();
        // Удаляем лишние пробелы
        processed = processed.replaceAll("\\s+", " ");

        return processed;
    }

    private static String getRelativePath(String basePath, String fullPath) {
        return fullPath.substring(basePath.length());
    }

    // Быстрая версия расстояния Левенштейна с отсечением
    private static int levenshteinDistance(String s1, String s2, int threshold) {
        int n = s1.length();
        int m = s2.length();

        // Быстрые проверки
        if (n == 0) return m <= threshold ? m : threshold + 1;
        if (m == 0) return n <= threshold ? n : threshold + 1;
        if (Math.abs(n - m) > threshold) return threshold + 1;

        // Убедимся, что s1 короче или равен s2
        if (n > m) {
            // Меняем местами
            String temp = s1;
            s1 = s2;
            s2 = temp;
            n = s1.length();
            m = s2.length();
        }

        int[] current = new int[n + 1];
        int[] previous = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            previous[i] = i;
        }

        for (int j = 1; j <= m; j++) {
            current[0] = j;

            // Вычисляем только в окне
            int start = Math.max(1, j - threshold);
            int end = Math.min(n, j + threshold);

            boolean anyInThreshold = false;

            for (int i = start; i <= end; i++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                int min = Math.min(
                        Math.min(current[i - 1] + 1, previous[i] + 1),
                        previous[i - 1] + cost
                );
                current[i] = min;

                if (min <= threshold) {
                    anyInThreshold = true;
                }
            }

            // Если все значения за пределами порога, прерываем
            if (!anyInThreshold) {
                return threshold + 1;
            }

            // Копируем текущую строку в предыдущую
            int[] temp = previous;
            previous = current;
            current = temp;
        }

        int result = previous[n];
        return result <= threshold ? result : threshold + 1;
    }

    private static Map<String, List<String>> findCopies(List<ProcessedFile> files, int threshold) {
        Map<String, List<String>> copiesMap = new TreeMap<>(); // TreeMap автоматически сортирует ключи
        int n = files.size();

        // Создаем массив хешей для быстрой предварительной проверки
        long[] hashes = new long[n];
        for (int i = 0; i < n; i++) {
            hashes[i] = computeSimpleHash(files.get(i).content);
        }

        for (int i = 0; i < n; i++) {
            ProcessedFile file1 = files.get(i);
            List<String> copies = new ArrayList<>();

            for (int j = i + 1; j < n; j++) { // Начинаем с i+1, чтобы избежать дублирования
                ProcessedFile file2 = files.get(j);

                // Быстрая проверка по хешу
                if (Math.abs(hashes[i] - hashes[j]) > 1000) {
                    continue;
                }

                // Проверка по длине
                if (Math.abs(file1.content.length() - file2.content.length()) > threshold) {
                    continue;
                }

                int distance = levenshteinDistance(file1.content, file2.content, threshold);

                if (distance <= threshold) {
                    copies.add(file2.path);
                }
            }

            if (!copies.isEmpty()) {
                // Добавляем текущий файл в список копий других файлов
                for (String copyPath : copies) {
                    copiesMap.computeIfAbsent(copyPath, k -> new ArrayList<>()).add(file1.path);
                }
                // И добавляем текущий файл с его копиями
                copiesMap.put(file1.path, copies);
            }
        }

        // Удаляем дубликаты из списков
        for (List<String> list : copiesMap.values()) {
            Collections.sort(list);
        }

        return copiesMap;
    }

    private static long computeSimpleHash(String str) {
        long hash = 0;
        for (int i = 0; i < Math.min(str.length(), 100); i++) {
            hash = 31 * hash + str.charAt(i);
        }
        return hash;
    }

    private static class ProcessedFile {
        String path;
        String content;

        ProcessedFile(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}