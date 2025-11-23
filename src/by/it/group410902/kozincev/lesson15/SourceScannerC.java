package by.it.group410902.kozincev.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SourceScannerC {

    static class ProcessedFile {
        final String path;
        final String content;
        final int length;
        final int hash;

        ProcessedFile(String path, String content) {
            this.path = path;
            this.content = content;
            this.length = content.length();
            this.hash = content.hashCode();
        }
    }

    public static void main(String[] args) {
        String sourceRoot = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path basePath = Path.of(sourceRoot);
        List<ProcessedFile> processedFiles = new ArrayList<>();

        // Чтение и обработка файлов
        try {
            Files.walk(basePath)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        processJavaFile(path, basePath, processedFiles);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортируем по длине для оптимизации
        processedFiles.sort(Comparator.comparingInt(f -> f.length));

        // Поиск копий
        Map<String, List<String>> copiesMap = findCopiesOptimized(processedFiles);

        // Вывод результатов
        printResults(copiesMap);
    }

    private static void processJavaFile(Path javaFile, Path rootDir, List<ProcessedFile> resultList) {
        try {
            String fileContent = readFileSafely(javaFile);

            if (fileContent.contains("@Test") || fileContent.contains("org.junit.Test")) {
                return;
            }

            String processedContent = processJavaSource(fileContent);
            String relativePath = rootDir.relativize(javaFile).toString();

            resultList.add(new ProcessedFile(relativePath, processedContent));

        } catch (IOException e) {
            // Игнорируем файлы с ошибками чтения
        }
    }

    private static String readFileSafely(Path filePath) throws IOException {
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
        decoder.onMalformedInput(CodingErrorAction.IGNORE);
        decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);

        byte[] fileBytes = Files.readAllBytes(filePath);
        return new String(fileBytes, StandardCharsets.UTF_8);
    }

    private static String processJavaSource(String sourceCode) {
        String withoutComments = removeAllComments(sourceCode);
        String withoutPackages = removePackageAndImports(withoutComments);
        return normalizeWhitespace(withoutPackages);
    }

    private static String removeAllComments(String code) {
        StringBuilder result = new StringBuilder();
        int length = code.length();
        int i = 0;

        while (i < length) {
            if (i + 1 < length && code.charAt(i) == '/' && code.charAt(i + 1) == '/') {
                i = skipSingleLineComment(code, i, length);
                continue;
            }

            if (i + 1 < length && code.charAt(i) == '/' && code.charAt(i + 1) == '*') {
                i = skipMultiLineComment(code, i, length);
                continue;
            }

            if (code.charAt(i) == '"') {
                int stringEnd = processStringLiteral(code, i, length, result);
                if (stringEnd > i) {
                    i = stringEnd;
                    continue;
                }
            }

            if (code.charAt(i) == '\'') {
                int charEnd = processCharLiteral(code, i, length, result);
                if (charEnd > i) {
                    i = charEnd;
                    continue;
                }
            }

            result.append(code.charAt(i));
            i++;
        }

        return result.toString();
    }

    private static int skipSingleLineComment(String code, int start, int length) {
        int i = start + 2;
        while (i < length && code.charAt(i) != '\n') {
            i++;
        }
        return i;
    }

    private static int skipMultiLineComment(String code, int start, int length) {
        int i = start + 2;
        while (i < length - 1) {
            if (code.charAt(i) == '*' && code.charAt(i + 1) == '/') {
                return i + 2;
            }
            i++;
        }
        return length;
    }

    private static int processStringLiteral(String code, int start, int length, StringBuilder result) {
        result.append(code.charAt(start));
        int i = start + 1;

        while (i < length) {
            char current = code.charAt(i);
            result.append(current);

            if (current == '"' && code.charAt(i - 1) != '\\') {
                return i + 1;
            }
            if (current == '\\' && i + 1 < length) {
                result.append(code.charAt(i + 1));
                i += 2;
                continue;
            }
            i++;
        }
        return length;
    }

    private static int processCharLiteral(String code, int start, int length, StringBuilder result) {
        result.append(code.charAt(start));
        int i = start + 1;

        while (i < length) {
            char current = code.charAt(i);
            result.append(current);

            if (current == '\'' && code.charAt(i - 1) != '\\') {
                return i + 1;
            }
            if (current == '\\' && i + 1 < length) {
                result.append(code.charAt(i + 1));
                i += 2;
                continue;
            }
            i++;
        }
        return length;
    }

    private static String removePackageAndImports(String code) {
        StringBuilder result = new StringBuilder();
        String[] lines = code.split("\n");

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package") && !trimmedLine.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String normalizeWhitespace(String text) {
        StringBuilder result = new StringBuilder();
        boolean inWhitespace = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
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

        return result.toString().trim();
    }

    private static Map<String, List<String>> findCopiesOptimized(List<ProcessedFile> files) {
        Map<String, List<String>> copies = new TreeMap<>();
        int n = files.size();

        for (int i = 0; i < n; i++) {
            ProcessedFile file1 = files.get(i);
            List<String> fileCopies = new ArrayList<>();

            for (int j = i + 1; j < n; j++) {
                ProcessedFile file2 = files.get(j);

                if (Math.abs(file1.length - file2.length) > 20) {
                    continue;
                }

                if (file1.hash == file2.hash && file1.content.equals(file2.content)) {
                    fileCopies.add(file2.path);
                    continue;
                }

                // Проверка расстояния Левенштейна
                int distance = calculateOptimizedLevenshtein(file1.content, file2.content);
                if (distance < 10) {
                    fileCopies.add(file2.path);
                }
            }

            if (!fileCopies.isEmpty()) {
                copies.put(file1.path, fileCopies);
            }
        }

        return copies;
    }

    private static int calculateOptimizedLevenshtein(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        if (Math.abs(len1 - len2) > 10) {
            return Integer.MAX_VALUE;
        }

        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

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
                    curr[j] = 1 + Math.min(
                            Math.min(prev[j], curr[j - 1]),
                            prev[j - 1]
                    );
                }
                minInRow = Math.min(minInRow, curr[j]);

                if (minInRow >= 10) {
                    break;
                }
            }

            if (minInRow >= 10) {
                return minInRow;
            }

            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[len2];
    }

    private static void printResults(Map<String, List<String>> copiesMap) {
        for (Map.Entry<String, List<String>> entry : copiesMap.entrySet()) {
            System.out.println(entry.getKey());
            entry.getValue().sort(String::compareTo);
            for (String copyPath : entry.getValue()) {
                System.out.println(copyPath);
            }
        }
    }
}