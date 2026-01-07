package by.it.group410901.shaidarov.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path srcPath = Paths.get(src);

        if (!Files.exists(srcPath)) {
            System.err.println("Каталог src не найден: " + src);
            return;
        }

        List<FileContent> fileContents = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(srcPath)) {
            paths.filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            processFile(path, srcPath, fileContents);
                        } catch (IOException e) {
                            // Игнорируем ошибки чтения
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталогов: " + e.getMessage());
            return;
        }

        // Находим копии
        Map<String, Set<String>> copies = findCopies(fileContents);

        // Сортируем и выводим результаты
        List<String> sortedPaths = new ArrayList<>(copies.keySet());
        Collections.sort(sortedPaths);

        for (String path : sortedPaths) {
            System.out.println(path);
            List<String> copyPaths = new ArrayList<>(copies.get(path));
            Collections.sort(copyPaths);
            for (String copyPath : copyPaths) {
                System.out.println(copyPath);
            }
        }
    }

    private static void processFile(Path filePath, Path srcPath, List<FileContent> fileContents)
            throws IOException {
        // Читаем файл с игнорированием MalformedInputException
        String content = readFileIgnoringErrors(filePath);

        // Проверка на тесты
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        // Обработка текста
        String processed = processContent(content);

        // Получаем относительный путь
        String relativePath = srcPath.relativize(filePath).toString();

        fileContents.add(new FileContent(relativePath, processed));
    }

    private static String readFileIgnoringErrors(Path filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(filePath);
        return StandardCharsets.UTF_8
                .newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE)
                .decode(java.nio.ByteBuffer.wrap(bytes))
                .toString();
    }

    private static String processContent(String content) {
        // Шаг 1: Удаление комментариев за O(n)
        String withoutComments = removeComments(content);

        // Шаг 2: Удаление package и imports
        StringBuilder result = new StringBuilder();
        String[] lines = withoutComments.split("\n", -1);

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue;
            }
            result.append(line).append('\n');
        }

        // Шаг 3: Заменить все последовательности символов с кодом <33 на один пробел
        String text = result.toString();
        StringBuilder normalized = new StringBuilder(text.length());
        boolean inWhitespace = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!inWhitespace) {
                    normalized.append(' ');
                    inWhitespace = true;
                }
            } else {
                normalized.append(c);
                inWhitespace = false;
            }
        }

        return normalized.toString().trim();
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder(content.length());

        int i = 0;
        int len = content.length();
        boolean inLineComment = false;
        boolean inBlockComment = false;
        boolean inString = false;
        boolean inChar = false;
        char prevChar = 0;

        while (i < len) {
            char c = content.charAt(i);

            if (!inLineComment && !inBlockComment) {
                if (c == '"' && prevChar != '\\') {
                    inString = !inString;
                    result.append(c);
                    prevChar = c;
                    i++;
                    continue;
                }

                if (c == '\'' && prevChar != '\\') {
                    inChar = !inChar;
                    result.append(c);
                    prevChar = c;
                    i++;
                    continue;
                }
            }

            if (inString || inChar) {
                result.append(c);
                prevChar = c;
                i++;
                continue;
            }

            if (!inLineComment && !inBlockComment && i + 1 < len
                    && c == '/' && content.charAt(i + 1) == '*') {
                inBlockComment = true;
                i += 2;
                prevChar = 0;
                continue;
            }

            if (inBlockComment) {
                if (i + 1 < len && c == '*' && content.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i += 2;
                    prevChar = 0;
                } else {
                    i++;
                }
                continue;
            }

            if (!inLineComment && i + 1 < len
                    && c == '/' && content.charAt(i + 1) == '/') {
                inLineComment = true;
                i += 2;
                prevChar = 0;
                continue;
            }

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    result.append(c);
                }
                prevChar = c;
                i++;
                continue;
            }

            result.append(c);
            prevChar = c;
            i++;
        }

        return result.toString();
    }

    private static Map<String, Set<String>> findCopies(List<FileContent> fileContents) {
        Map<String, Set<String>> copies = new TreeMap<>();
        int n = fileContents.size();

        // Вычисляем хеши для быстрого сравнения
        for (FileContent fc : fileContents) {
            fc.computeHash();
        }

        // Группируем по длине для оптимизации
        Map<Integer, List<FileContent>> lengthBuckets = new HashMap<>();
        for (FileContent fc : fileContents) {
            int bucket = fc.content.length() / 10; // Группы по 10 символов
            lengthBuckets.computeIfAbsent(bucket, k -> new ArrayList<>()).add(fc);
        }

        // Сравниваем только файлы из близких групп
        for (int i = 0; i < n; i++) {
            FileContent file1 = fileContents.get(i);
            int bucket1 = file1.content.length() / 10;

            // Проверяем соседние группы
            for (int b = bucket1 - 1; b <= bucket1 + 1; b++) {
                List<FileContent> candidates = lengthBuckets.get(b);
                if (candidates == null) continue;

                for (FileContent file2 : candidates) {
                    if (file1.path.compareTo(file2.path) >= 0) {
                        continue;
                    }

                    // Быстрая проверка по длине
                    int lenDiff = Math.abs(file1.content.length() - file2.content.length());
                    if (lenDiff >= 10) {
                        continue;
                    }

                    // Быстрая проверка по хешу
                    if (Math.abs(file1.hash - file2.hash) > 10 * 31) {
                        continue;
                    }

                    // Точное вычисление расстояния
                    int distance = levenshteinDistanceFast(file1.content, file2.content);

                    if (distance < 10) {
                        copies.computeIfAbsent(file1.path, k -> new TreeSet<>()).add(file2.path);
                        copies.computeIfAbsent(file2.path, k -> new TreeSet<>()).add(file1.path);
                    }
                }
            }
        }

        return copies;
    }

    private static int levenshteinDistanceFast(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        // Быстрые проверки
        if (s1.equals(s2)) return 0;

        int lengthDiff = Math.abs(len1 - len2);
        if (lengthDiff >= 10) return 10;

        if (len1 == 0) return Math.min(len2, 10);
        if (len2 == 0) return Math.min(len1, 10);

        // Используем только две строки
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

        // Инициализация
        for (int j = 0; j <= len2; j++) {
            prev[j] = Math.min(j, 10);
        }

        // Динамическое программирование с узкой полосой
        for (int i = 1; i <= len1; i++) {
            curr[0] = Math.min(i, 10);

            // Узкая полоса: только ±10 от диагонали
            int minJ = Math.max(1, i - 10);
            int maxJ = Math.min(len2, i + 10);

            int minInRow = curr[0];
            char c1 = s1.charAt(i - 1);

            for (int j = minJ; j <= maxJ; j++) {
                int cost = (c1 == s2.charAt(j - 1)) ? 0 : 1;

                int val = Math.min(
                        Math.min(prev[j] + 1, curr[j - 1] + 1),
                        prev[j - 1] + cost
                );

                curr[j] = Math.min(val, 10);
                minInRow = Math.min(minInRow, curr[j]);
            }

            // Ранний выход
            if (minInRow >= 10) {
                return 10;
            }

            // Swap
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[len2];
    }

    static class FileContent {
        String path;
        String content;
        int hash;

        FileContent(String path, String content) {
            this.path = path;
            this.content = content;
        }

        void computeHash() {
            // Простой rolling hash для быстрого сравнения
            int h = 0;
            int len = Math.min(content.length(), 100); // Берем первые 100 символов
            for (int i = 0; i < len; i++) {
                h = h * 31 + content.charAt(i);
            }
            this.hash = h;
        }
    }
}