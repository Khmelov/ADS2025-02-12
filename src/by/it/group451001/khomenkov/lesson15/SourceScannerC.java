package by.it.group451001.khomenkov.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerC {

    static class FileData {
        String path;
        String content;
        int length;

        FileData(String path, String content) {
            this.path = path;
            this.content = content;
            this.length = content.length();
        }
    }

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<FileData> files = new ArrayList<>();

        // Сбор и обработка файлов
        Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".java")) {
                    processJavaFile(file, srcPath, files);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        // Сортировка файлов по длине содержимого
        files.sort((a, b) -> Integer.compare(a.length, b.length));

        // Поиск копий
        Map<String, List<String>> copies = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {
            FileData file1 = files.get(i);
            for (int j = i + 1; j < files.size(); j++) {
                FileData file2 = files.get(j);
                if (file2.length - file1.length > 9) {
                    break;
                }
                int distance = levenshtein(file1.content, file2.content, 10);
                if (distance < 10) {
                    copies.computeIfAbsent(file1.path, k -> new ArrayList<>()).add(file2.path);
                    copies.computeIfAbsent(file2.path, k -> new ArrayList<>()).add(file1.path);
                }
            }
        }

        // Сбор и сортировка результатов
        List<String> resultFiles = new ArrayList<>(copies.keySet());
        Collections.sort(resultFiles);

        for (String filePath : resultFiles) {
            System.out.println(filePath);
            List<String> copyList = copies.get(filePath);
            Collections.sort(copyList);
            for (String copy : copyList) {
                System.out.println(copy);
            }
        }
    }

    private static void processJavaFile(Path file, Path srcPath, List<FileData> files) {
        try {
            byte[] fileBytes = Files.readAllBytes(file);
            String content;
            try {
                content = new String(fileBytes, StandardCharsets.UTF_8);
            } catch (Exception e) {
                content = new String(fileBytes, StandardCharsets.ISO_8859_1);
            }

            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            content = removePackageAndImports(content);
            content = removeComments(content);
            content = normalizeWhitespace(content);
            content = content.trim();

            String relativePath = srcPath.relativize(file).toString();
            files.add(new FileData(relativePath, content));

        } catch (MalformedInputException e) {
            // Игнорируем файлы с ошибками кодировки
        } catch (IOException e) {
            // Игнорируем файлы, которые не удалось прочитать
        }
    }

    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\r?\n");

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
            } else if (inLineComment) {
                if (current == '\n') {
                    inLineComment = false;
                    result.append(current);
                }
            } else if (inString) {
                result.append(current);
                if (current == '\\' && next == '"') {
                    result.append(next);
                    i++;
                } else if (current == '"') {
                    inString = false;
                }
            } else if (inChar) {
                result.append(current);
                if (current == '\\' && next == '\'') {
                    result.append(next);
                    i++;
                } else if (current == '\'') {
                    inChar = false;
                }
            } else {
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
        }

        return result.toString();
    }

    private static String normalizeWhitespace(String content) {
        StringBuilder sb = new StringBuilder();
        boolean inWhitespace = false;
        for (char c : content.toCharArray()) {
            if (c < 33) {
                if (!inWhitespace) {
                    sb.append(' ');
                    inWhitespace = true;
                }
            } else {
                sb.append(c);
                inWhitespace = false;
            }
        }
        return sb.toString().trim();
    }

    private static int levenshtein(String s1, String s2, int threshold) {
        int n = s1.length();
        int m = s2.length();

        if (n < m) {
            return levenshtein(s2, s1, threshold);
        }

        if (n - m > threshold) {
            return threshold + 1;
        }

        int[] d = new int[m + 1];
        for (int j = 0; j <= m; j++) {
            d[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            int prev = d[0];
            d[0] = i;
            int min = d[0];
            int start = Math.max(1, i - threshold);
            int end = Math.min(m, i + threshold);
            for (int j = start; j <= end; j++) {
                int temp = d[j];
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                d[j] = Math.min(Math.min(d[j - 1] + 1, temp + 1), prev + cost);
                prev = temp;
                min = Math.min(min, d[j]);
            }
            if (min > threshold) {
                return threshold + 1;
            }
        }

        return d[m];
    }
}
