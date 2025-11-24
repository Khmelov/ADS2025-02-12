package by.it.group451002.kureichuk.lesson15;

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
        Path rootPath = Paths.get(src);

        List<ProcessedFile> processedFiles = new ArrayList<>();

        try {
            // Собираем и обрабатываем все Java файлы
            Files.walk(rootPath)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String content = readFileWithFallback(path);

                            // Пропускаем тестовые файлы
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Обработка содержимого файла
                            String processedContent = processFileContent(content);
                            String relativePath = rootPath.relativize(path).toString();

                            processedFiles.add(new ProcessedFile(relativePath, processedContent));

                        } catch (IOException e) {
                            // Игнорируем ошибки чтения файлов
                        }
                    });

            // Используем быстрые методы сравнения
            Map<String, List<String>> copies = findCopiesFast(processedFiles);

            // Выводим результаты
            printResults(copies);

        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталога: " + e.getMessage());
        }
    }

    /**
     * Чтение файла с обработкой различных кодировок
     */
    private static String readFileWithFallback(Path path) throws IOException {
        Charset[] charsets = {
                StandardCharsets.UTF_8,
                Charset.forName("Windows-1251"),
                StandardCharsets.ISO_8859_1,
                StandardCharsets.US_ASCII
        };

        for (Charset charset : charsets) {
            try {
                return Files.readString(path, charset);
            } catch (IOException e) {
                // Пробуем следующую кодировку
            }
        }
        return "";
    }

    /**
     * Обработка содержимого файла
     */
    private static String processFileContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        // Удаляем комментарии
        String withoutComments = removeComments(content);

        // Удаляем package и imports
        String withoutPackageImports = removePackageAndImports(withoutComments);

        // Заменяем последовательности символов с кодом <33 на пробел и делаем trim
        return normalizeWhitespace(withoutPackageImports);
    }

    /**
     * Удаление комментариев за O(n)
     */
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
                if (c == '"' && (i == 0 || content.charAt(i - 1) != '\\')) {
                    inString = false;
                }
                continue;
            }

            if (inChar) {
                result.append(c);
                if (c == '\'' && (i == 0 || content.charAt(i - 1) != '\\')) {
                    inChar = false;
                }
                continue;
            }

            if (c == '"') {
                result.append(c);
                inString = true;
            } else if (c == '\'') {
                result.append(c);
                inChar = true;
            } else if (c == '/' && i + 1 < content.length()) {
                if (content.charAt(i + 1) == '/') {
                    inLineComment = true;
                    i++; // Пропускаем второй '/'
                } else if (content.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i++; // Пропускаем '*'
                } else {
                    result.append(c);
                }
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * Удаление package и import statements
     */
    private static String removePackageAndImports(String content) {
        String[] lines = content.split("\\r?\\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    /**
     * Нормализация пробельных символов
     */
    private static String normalizeWhitespace(String text) {
        StringBuilder result = new StringBuilder();
        boolean lastWasWhitespace = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!lastWasWhitespace) {
                    result.append(' ');
                    lastWasWhitespace = true;
                }
            } else {
                result.append(c);
                lastWasWhitespace = false;
            }
        }

        return result.toString().trim();
    }

    /**
     * Быстрый поиск копий с использованием хешей и быстрого сравнения
     */
    private static Map<String, List<String>> findCopiesFast(List<ProcessedFile> files) {
        Map<String, List<String>> copies = new HashMap<>();
        int n = files.size();

        // Группируем файлы по длине для оптимизации
        Map<Integer, List<ProcessedFile>> lengthGroups = new HashMap<>();
        for (ProcessedFile file : files) {
            int length = file.content.length();
            lengthGroups.computeIfAbsent(length, k -> new ArrayList<>()).add(file);
        }

        // Сравниваем только файлы похожей длины
        for (List<ProcessedFile> group : lengthGroups.values()) {
            if (group.size() < 2) continue;

            for (int i = 0; i < group.size(); i++) {
                ProcessedFile file1 = group.get(i);

                for (int j = i + 1; j < group.size(); j++) {
                    ProcessedFile file2 = group.get(j);

                    // Быстрое сравнение с использованием хешей
                    if (isCopyFast(file1.content, file2.content)) {
                        copies.computeIfAbsent(file1.path, k -> new ArrayList<>()).add(file2.path);
                        copies.computeIfAbsent(file2.path, k -> new ArrayList<>()).add(file1.path);
                    }
                }
            }
        }

        // Также проверяем файлы с небольшой разницей в длине (±10 символов)
        for (int i = 0; i < n; i++) {
            ProcessedFile file1 = files.get(i);
            int len1 = file1.content.length();

            for (int j = i + 1; j < n; j++) {
                ProcessedFile file2 = files.get(j);
                int len2 = file2.content.length();

                // Пропускаем если разница в длине слишком большая
                if (Math.abs(len1 - len2) > 10) continue;

                // Пропускаем если уже нашли как копии
                if (copies.containsKey(file1.path) && copies.get(file1.path).contains(file2.path)) {
                    continue;
                }

                // Быстрое сравнение
                if (isCopyFast(file1.content, file2.content)) {
                    copies.computeIfAbsent(file1.path, k -> new ArrayList<>()).add(file2.path);
                    copies.computeIfAbsent(file2.path, k -> new ArrayList<>()).add(file1.path);
                }
            }
        }

        return copies;
    }

    /**
     * Быстрая проверка на копии (менее 10 правок)
     */
    private static boolean isCopyFast(String s1, String s2) {
        // Если строки идентичны - точно копии
        if (s1.equals(s2)) {
            return true;
        }

        int len1 = s1.length();
        int len2 = s2.length();

        // Если разница в длине больше 10 - не копии
        if (Math.abs(len1 - len2) > 10) {
            return false;
        }

        // Быстрая проверка по первым и последним символам
        int minLen = Math.min(len1, len2);
        int differences = 0;

        // Проверяем начало строки
        for (int i = 0; i < Math.min(50, minLen); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                differences++;
                if (differences >= 10) return false;
            }
        }

        // Проверяем конец строки
        for (int i = 1; i <= Math.min(50, minLen); i++) {
            if (s1.charAt(len1 - i) != s2.charAt(len2 - i)) {
                differences++;
                if (differences >= 10) return false;
            }
        }

        // Если строки короткие, используем упрощенное сравнение
        if (minLen <= 200) {
            return computeSimpleDifference(s1, s2) < 10;
        }

        // Для длинных строк используем выборку
        return computeSampledDifference(s1, s2) < 10;
    }

    /**
     * Упрощенное вычисление разницы для коротких строк
     */
    private static int computeSimpleDifference(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int maxLen = Math.max(len1, len2);
        int differences = 0;

        for (int i = 0; i < maxLen; i++) {
            char c1 = i < len1 ? s1.charAt(i) : 0;
            char c2 = i < len2 ? s2.charAt(i) : 0;

            if (c1 != c2) {
                differences++;
                if (differences >= 10) break;
            }
        }

        return differences;
    }

    /**
     * Вычисление разницы по выборке для длинных строк
     */
    private static int computeSampledDifference(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int sampleSize = Math.min(1000, Math.min(len1, len2));
        int differences = 0;

        // Выборка равномерно распределенных позиций
        for (int i = 0; i < sampleSize; i++) {
            int pos1 = i * len1 / sampleSize;
            int pos2 = i * len2 / sampleSize;

            if (s1.charAt(pos1) != s2.charAt(pos2)) {
                differences++;
                if (differences >= 10) break;
            }
        }

        // Также проверяем общие подстроки
        int commonLength = findCommonSubstringLength(s1, s2);
        int maxLen = Math.max(len1, len2);
        int estimatedDiff = maxLen - commonLength;

        return Math.min(differences, estimatedDiff);
    }

    /**
     * Находит длину наибольшей общей подстроки (упрощенная версия)
     */
    private static int findCommonSubstringLength(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int maxCommon = 0;

        // Проверяем только начало и конец для скорости
        int startCommon = 0;
        while (startCommon < len1 && startCommon < len2 &&
                s1.charAt(startCommon) == s2.charAt(startCommon)) {
            startCommon++;
        }

        int endCommon = 0;
        while (endCommon < len1 && endCommon < len2 &&
                s1.charAt(len1 - 1 - endCommon) == s2.charAt(len2 - 1 - endCommon)) {
            endCommon++;
        }

        return Math.max(startCommon, endCommon);
    }

    /**
     * Вывод результатов
     */
    private static void printResults(Map<String, List<String>> copies) {
        // Собираем все файлы, у которых есть копии
        List<String> filesWithCopies = new ArrayList<>(copies.keySet());
        Collections.sort(filesWithCopies);

        // Убираем дубликаты (если A копия B, то B тоже копия A)
        Set<String> printed = new HashSet<>();

        for (String filePath : filesWithCopies) {
            if (printed.contains(filePath)) continue;

            System.out.println(filePath);
            printed.add(filePath);

            List<String> copyPaths = copies.get(filePath);
            Collections.sort(copyPaths);

            for (String copyPath : copyPaths) {
                System.out.println(copyPath);
                printed.add(copyPath);
            }

            System.out.println(); // Пустая строка между группами
        }
    }

    /**
     * Класс для хранения обработанного файла
     */
    static class ProcessedFile {
        String path;
        String content;

        ProcessedFile(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}