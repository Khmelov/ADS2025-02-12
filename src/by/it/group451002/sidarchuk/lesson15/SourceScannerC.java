package by.it.group451002.sidarchuk.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        try {
            List<FileContent> fileContents = processSourceFiles(src);
            Map<String, List<String>> similarities = findSimilarFiles(fileContents);
            printResults(similarities);
        } catch (Exception e) {
            System.err.println("Ошибка при обработке файлов: " + e.getMessage());
        }
    }

    private static List<FileContent> processSourceFiles(String srcDir) throws IOException {
        List<Path> javaFiles = findJavaFiles(srcDir);
        List<FileContent> result = new ArrayList<>();

        for (Path file : javaFiles) {
            FileContent processed = processFile(file);
            if (processed != null) {
                result.add(processed);
            }
        }

        return result;
    }

    private static List<Path> findJavaFiles(String srcDir) throws IOException {
        List<Path> javaFiles = new ArrayList<>();
        Path startDir = Paths.get(srcDir);

        if (!Files.exists(startDir)) {
            return javaFiles;
        }

        Files.walkFileTree(startDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().endsWith(".java")) {
                    javaFiles.add(file);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });

        return javaFiles;
    }

    private static FileContent processFile(Path file) {
        try {
            if (isTestFile(file)) {
                return null;
            }

            String content = readFileContent(file);
            if (content == null) {
                return null;
            }

            String processedContent = processContent(content);
            if (processedContent == null || processedContent.trim().isEmpty()) {
                return null;
            }

            return new FileContent(file.toString(), processedContent);

        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isTestFile(Path file) {
        try {
            String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
            return content.contains("@Test") || content.contains("org.junit.Test");
        } catch (Exception e) {
            return false;
        }
    }

    private static String readFileContent(Path file) {
        try {
            return new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            return readWithAlternativeEncodings(file);
        } catch (Exception e) {
            return null;
        }
    }

    private static String readWithAlternativeEncodings(Path file) {
        String[] encodings = {"ISO-8859-1", "Windows-1251", "Cp1252"};
        for (String encoding : encodings) {
            try {
                return new String(Files.readAllBytes(file), encoding);
            } catch (Exception e) {
                // continue
            }
        }
        return null;
    }

    private static String processContent(String content) {
        if (content == null) return null;

        content = removePackageAndImports(content);
        content = removeComments(content);
        content = normalizeWhitespace(content);
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
        int length = content.length();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean inChar = false;

        for (int i = 0; i < length; i++) {
            char current = content.charAt(i);
            char next = (i < length - 1) ? content.charAt(i + 1) : 0;

            if (inBlockComment) {
                if (current == '*' && next == '/') {
                    inBlockComment = false;
                    i++;
                }
                continue;
            }

            if (inLineComment) {
                if (current == '\n') {
                    inLineComment = false;
                    result.append(current);
                }
                continue;
            }

            if (inString) {
                result.append(current);
                if (current == '\\' && next == '"') {
                    result.append(next);
                    i++;
                } else if (current == '"') {
                    inString = false;
                }
                continue;
            }

            if (inChar) {
                result.append(current);
                if (current == '\\' && next == '\'') {
                    result.append(next);
                    i++;
                } else if (current == '\'') {
                    inChar = false;
                }
                continue;
            }

            if (current == '"') {
                inString = true;
                result.append(current);
            } else if (current == '\'') {
                inChar = true;
                result.append(current);
            } else if (current == '/' && next == '*') {
                inBlockComment = true;
                i++;
            } else if (current == '/' && next == '/') {
                inLineComment = true;
                i++;
            } else {
                result.append(current);
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

    private static Map<String, List<String>> findSimilarFiles(List<FileContent> fileContents) {
        Map<String, List<String>> similarities = new HashMap<>();
        int fileCount = fileContents.size();

        // Используем хеши для быстрого сравнения
        Map<String, String> contentHashes = new HashMap<>();
        for (FileContent fc : fileContents) {
            // Используем короткий хеш для быстрого сравнения
            String shortHash = generateShortHash(fc.content);
            contentHashes.put(fc.filePath, shortHash);
        }

        // Сравниваем только файлы с одинаковыми хешами
        for (int i = 0; i < fileCount; i++) {
            FileContent file1 = fileContents.get(i);
            String hash1 = contentHashes.get(file1.filePath);

            for (int j = i + 1; j < fileCount; j++) {
                FileContent file2 = fileContents.get(j);
                String hash2 = contentHashes.get(file2.filePath);

                // Быстрая проверка по хешу
                if (!hash1.equals(hash2)) {
                    continue;
                }

                // Если хеши совпали, проверяем более тщательно
                if (isSimilar(file1.content, file2.content)) {
                    similarities.computeIfAbsent(file1.filePath, k -> new ArrayList<>()).add(file2.filePath);
                    similarities.computeIfAbsent(file2.filePath, k -> new ArrayList<>()).add(file1.filePath);
                }
            }
        }

        return similarities;
    }

    private static String generateShortHash(String content) {
        // Генерируем короткий хеш на основе первых 200 символов и длины
        String sample = content.length() > 200 ? content.substring(0, 200) : content;
        return sample.length() + "_" + Math.abs(sample.hashCode());
    }

    private static boolean isSimilar(String s1, String s2) {
        // Быстрая проверка длины
        if (Math.abs(s1.length() - s2.length()) > 50) {
            return false;
        }

        // Проверка по первым 100 символам
        if (s1.length() > 100 && s2.length() > 100) {
            String start1 = s1.substring(0, 100);
            String start2 = s2.substring(0, 100);
            if (!start1.equals(start2)) {
                return false;
            }
        }

        // Упрощенная проверка различий
        return calculateSimpleDifference(s1, s2) < 10;
    }

    private static int calculateSimpleDifference(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int minLen = Math.min(len1, len2);
        int maxLen = Math.max(len1, len2);

        // Быстрый подсчет различий в начале строки
        int differences = 0;
        for (int i = 0; i < minLen && differences < 10; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                differences++;
            }
        }

        // Добавляем разницу в длине
        differences += (maxLen - minLen);

        return differences;
    }

    private static void printResults(Map<String, List<String>> similarities) {
        // Собираем все файлы с копиями и сортируем
        List<String> filesWithCopies = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : similarities.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                filesWithCopies.add(entry.getKey());
            }
        }
        Collections.sort(filesWithCopies);

        // Убираем дубликаты в выводе
        Set<String> alreadyPrinted = new HashSet<>();

        for (String filePath : filesWithCopies) {
            if (alreadyPrinted.contains(filePath)) {
                continue;
            }

            List<String> similarFiles = similarities.get(filePath);
            if (!similarFiles.isEmpty()) {
                // Сортируем похожие файлы
                Collections.sort(similarFiles);

                System.out.println(filePath);
                for (String similarFile : similarFiles) {
                    System.out.println(similarFile);
                    alreadyPrinted.add(similarFile);
                }

                // Пустая строка между группами, кроме последней
                if (filesWithCopies.indexOf(filePath) < filesWithCopies.size() - 1) {
                    System.out.println();
                }
            }

            alreadyPrinted.add(filePath);
        }

        if (filesWithCopies.isEmpty()) {
            System.out.println("Копий не найдено");
        }
    }

    // Вспомогательный класс
    static class FileContent {
        String filePath;
        String content;

        FileContent(String filePath, String content) {
            this.filePath = filePath;
            this.content = content;
        }
    }
}
