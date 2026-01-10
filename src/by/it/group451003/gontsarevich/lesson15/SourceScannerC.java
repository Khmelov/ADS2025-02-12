package by.it.group451003.gontsarevich.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SourceScannerC {
    static final int NORMAL_DISTANCE = 9;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    // Оптимизированное вычисление расстояния Левенштейна (ограниченное окном)
    private static int levenshteinDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        // Быстрая проверка - если разница длин больше порога, сразу возвращаем большое число
        if (Math.abs(m - n) > NORMAL_DISTANCE) {
            return Integer.MAX_VALUE;
        }

        // Используем два массива для экономии памяти
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        // Инициализация первого ряда
        for (int j = 0; j <= n; j++) {
            prev[j] = j;
        }

        // Заполнение матрицы
        for (int i = 1; i <= m; i++) {
            curr[0] = i;
            for (int j = 1; j <= n; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(prev[j] + 1, curr[j - 1] + 1),
                        prev[j - 1] + cost
                );
            }

            // Обмен массивами
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[n];
    }

    // Проверка, являются ли тексты копиями с предварительными оптимизациями
    private static boolean checkDistance(String text1, String text2) {
        // Быстрая проверка 1: разница длин
        int lengthDiff = Math.abs(text1.length() - text2.length());
        if (lengthDiff > NORMAL_DISTANCE) {
            return false;
        }

        // Быстрая проверка 2: хеши
        if (text1.hashCode() == text2.hashCode() && text1.equals(text2)) {
            return true;
        }

        // Быстрая проверка 3: разбиение на чанки для больших текстов
        if (text1.length() > 1000 || text2.length() > 1000) {
            return checkDistanceLarge(text1, text2);
        }

        // Для небольших текстов используем полный алгоритм
        int distance = levenshteinDistance(text1, text2);
        return distance <= NORMAL_DISTANCE;
    }

    // Проверка для больших текстов (оптимизированная)
    private static boolean checkDistanceLarge(String text1, String text2) {
        // Разбиваем на слова и сравниваем количество слов
        String[] words1 = text1.split("\\s+");
        String[] words2 = text2.split("\\s+");

        if (Math.abs(words1.length - words2.length) > NORMAL_DISTANCE) {
            return false;
        }

        // Используем алгоритм с окном
        int m = Math.min(words1.length, 100); // ограничиваем количество сравнений
        int n = Math.min(words2.length, 100);

        int[][] dp = new int[2][n + 1];

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            dp[1][0] = i;
            for (int j = 1; j <= n; j++) {
                int cost = words1[i - 1].equals(words2[j - 1]) ? 0 : 1;
                dp[1][j] = Math.min(
                        Math.min(dp[0][j] + 1, dp[1][j - 1] + 1),
                        dp[0][j - 1] + cost
                );
            }

            // Сдвигаем строки
            System.arraycopy(dp[1], 0, dp[0], 0, n + 1);
        }

        return dp[1][n] <= NORMAL_DISTANCE;
    }

    // Обработка содержимого файла
    private static String processFileContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        // Удаление package и imports
        String[] lines = content.split("\\R");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            line = line.trim();

            // Пропускаем пустые строки, package и импорты
            if (line.isEmpty() ||
                    line.startsWith("package ") ||
                    line.startsWith("import ")) {
                continue;
            }

            // Удаление однострочных комментариев
            int commentIndex = line.indexOf("//");
            if (commentIndex != -1) {
                line = line.substring(0, commentIndex);
            }

            // Удаление многострочных комментариев (упрощенно)
            line = line.replaceAll("/\\*.*", "");

            if (!line.isEmpty()) {
                // Замена символов с кодом < 32 на пробел
                StringBuilder processedLine = new StringBuilder();
                for (char c : line.toCharArray()) {
                    if (c < 32) {
                        processedLine.append(' ');
                    } else {
                        processedLine.append(c);
                    }
                }
                result.append(processedLine.toString().trim()).append(" ");
            }
        }

        // Финальная обработка
        String finalText = result.toString()
                .replaceAll("/\\*.*?\\*/", "") // окончательное удаление многострочных комментариев
                .replaceAll("\\s+", " ")
                .trim();

        return finalText;
    }

    // Получение обработанного содержимого файла
    private static String getFileContent(Path filePath) throws IOException {
        try {
            String content = Files.readString(filePath, CHARSET);
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return null;
            }
            return processFileContent(content);
        } catch (IOException e) {
            try {
                String content = Files.readString(filePath, StandardCharsets.ISO_8859_1);
                if (content.contains("@Test") || content.contains("org.junit.Test")) {
                    return null;
                }
                return processFileContent(content);
            } catch (IOException ex) {
                return null;
            }
        }
    }

    // Поиск копий с оптимизацией производительности
    private static Map<Path, List<Path>> findCopies(List<Path> allFiles, Map<Path, String> fileContents) {
        Map<Path, List<Path>> copies = new TreeMap<>();
        Set<Path> processed = new HashSet<>();

        // Группируем файлы по длине текста для оптимизации
        Map<Integer, List<Path>> lengthGroups = new HashMap<>();
        for (Path file : allFiles) {
            String content = fileContents.get(file);
            if (content != null && !content.isEmpty()) {
                int length = content.length();
                lengthGroups.computeIfAbsent(length, k -> new ArrayList<>()).add(file);
            }
        }

        // Сравниваем только файлы с близкими длинами
        for (List<Path> group : lengthGroups.values()) {
            if (group.size() < 2) continue;

            for (int i = 0; i < group.size(); i++) {
                Path file1 = group.get(i);
                if (processed.contains(file1)) continue;

                String content1 = fileContents.get(file1);
                if (content1 == null || content1.isEmpty()) continue;

                List<Path> similarFiles = new ArrayList<>();
                similarFiles.add(file1);

                for (int j = i + 1; j < group.size(); j++) {
                    Path file2 = group.get(j);
                    if (processed.contains(file2)) continue;

                    String content2 = fileContents.get(file2);
                    if (content2 == null || content2.isEmpty()) continue;

                    // Быстрая проверка по хешу
                    if (content1.hashCode() == content2.hashCode() && content1.equals(content2)) {
                        similarFiles.add(file2);
                        processed.add(file2);
                    } else if (checkDistance(content1, content2)) {
                        similarFiles.add(file2);
                        processed.add(file2);
                    }
                }

                if (similarFiles.size() > 1) {
                    similarFiles.sort(Comparator.naturalOrder());
                    copies.put(similarFiles.get(0), similarFiles.subList(1, similarFiles.size()));
                    processed.add(file1);
                }
            }
        }

        return copies;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        try {
            String src = System.getProperty("user.dir") + File.separator + "src";
            Path srcPath = Path.of(src);

            if (!Files.exists(srcPath)) {
                System.err.println("Directory 'src' not found: " + srcPath);
                return;
            }

            // Собираем все .java файлы
            List<Path> javaFiles = new ArrayList<>();
            try (var walk = Files.walk(srcPath)) {
                walk.filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".java"))
                        .forEach(javaFiles::add);
            }

            // Сортируем файлы
            javaFiles.sort(Comparator.naturalOrder());

            // Читаем и обрабатываем содержимое файлов
            Map<Path, String> fileContents = new HashMap<>();
            for (Path file : javaFiles) {
                String content = getFileContent(file);
                if (content != null && !content.isEmpty()) {
                    fileContents.put(file, content);
                }
            }

            // Находим копии
            Map<Path, List<Path>> copies = findCopies(
                    new ArrayList<>(fileContents.keySet()),
                    fileContents
            );

            // Выводим результат
            if (copies.isEmpty()) {
                System.out.println("No copies found.");
            } else {
                for (Map.Entry<Path, List<Path>> entry : copies.entrySet()) {
                    System.out.println("Original: " + srcPath.relativize(entry.getKey()));
                    for (Path copy : entry.getValue()) {
                        System.out.println("  Copy:   " + srcPath.relativize(copy));
                    }
                    System.out.println();
                }
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + " ms");

        } catch (IOException e) {
            System.err.println("Error scanning files: " + e.getMessage());
        }
    }
}