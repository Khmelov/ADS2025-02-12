package by.it.group451001.kolosun.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SourceScannerC {
    private static final int LEVENSHTEIN_COPY_THRESHOLD = 10;
    private static final int SIZE_TOLERANCE_PERCENT = 5;
    private static final boolean DEBUG = false;

    private static class JavaFile {
        String path;
        String cleanContent;
        int length;
        int hash;

        JavaFile(String path, String cleanContent) {
            this.path = path;
            this.cleanContent = cleanContent;
            this.length = cleanContent.length();
            this.hash = cleanContent.hashCode();
        }
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        log("=== SourceScannerC начало ===");

        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        if (!Files.exists(srcPath)) {
            log("Каталог src не найден");
            return;
        }

        List<JavaFile> javaFiles = scanAndCleanFiles(srcPath);
        log("Найдено файлов: " + javaFiles.size());

        if (javaFiles.isEmpty()) {
            log("Java файлы не найдены");
            return;
        }

        javaFiles.sort((a, b) -> Integer.compare(a.length, b.length));
        log("Файлы отсортированы по размеру");

        findAndPrintCopies(javaFiles);

        long elapsed = System.currentTimeMillis() - startTime;
        log("=== Завершено за " + elapsed + "ms ===");
    }

    private static void log(String msg) {
        if (DEBUG) {
            System.err.println("[LOG] " + msg);
        }
    }

    private static List<JavaFile> scanAndCleanFiles(Path srcPath) throws IOException {
        List<JavaFile> result = Collections.synchronizedList(new ArrayList<>());

        try (var stream = Files.walk(srcPath)) {
            stream.filter(path -> path.toString().endsWith(".java")).parallel().forEach(path -> {
                try {
                    String content = readFileWithErrorHandling(path);
                    if (content != null && !content.contains("@Test") && !content.contains("org.junit.Test")) {
                        String cleaned = cleanContent(content);
                        String relativePath = srcPath.relativize(path).toString();
                        result.add(new JavaFile(relativePath, cleaned));
                    }
                } catch (Exception e) { }});
        }

        return result;
    }

    private static String readFileWithErrorHandling(Path path) {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (java.nio.charset.MalformedInputException e) {
            try {
                return Files.readString(path, StandardCharsets.ISO_8859_1);
            } catch (IOException e2) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    private static String cleanContent(String content) {
        content = removePackageAndImports(content);
        content = removeComments(content);
        content = normalizeWhitespace(content);
        return content.trim();
    }

    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = content.length();

        while (i < n) {
            if (i + 1 < n && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                i += 2;
                while (i < n && content.charAt(i) != '\n') {
                    i++;
                }
                if (i < n) {
                    result.append('\n');
                    i++;
                }
            } else if (i + 1 < n && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < n) {
                    if (content.charAt(i) == '*' && content.charAt(i + 1) == '/') {
                        i += 2;
                        break;
                    }
                    if (content.charAt(i) == '\n') {
                        result.append('\n');
                    }
                    i++;
                }
            } else if (content.charAt(i) == '"' || content.charAt(i) == '\'') {
                char quote = content.charAt(i);
                result.append(content.charAt(i));
                i++;
                while (i < n) {
                    char c = content.charAt(i);
                    result.append(c);
                    if (c == quote && (i == 0 || content.charAt(i - 1) != '\\')) {
                        i++;
                        break;
                    }
                    i++;
                }
            } else {
                result.append(content.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    private static String normalizeWhitespace(String content) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c < 33) {
                if (result.length() == 0 || result.charAt(result.length() - 1) != ' ') {
                    result.append(' ');
                }
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private static int levenshteinDistance(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        int lenDiff = Math.abs(len1 - len2);
        if (lenDiff >= LEVENSHTEIN_COPY_THRESHOLD) {
            return Integer.MAX_VALUE;
        }

        if (len1 == 0) return len2;
        if (len2 == 0) return len1;

        if (len1 < len2) {
            String tmp = s1;
            s1 = s2;
            s2 = tmp;
            len1 = s1.length();
            len2 = s2.length();
        }

        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

        for (int j = 0; j <= len2; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            curr[0] = i;
            int minInRow = Integer.MAX_VALUE;

            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                int val = Math.min(
                        Math.min(curr[j - 1] + 1, prev[j] + 1),
                        prev[j - 1] + cost
                );
                curr[j] = val;

                if (val < minInRow) {
                    minInRow = val;
                }
            }

            if (minInRow >= LEVENSHTEIN_COPY_THRESHOLD) {
                return Integer.MAX_VALUE;
            }

            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[len2];
    }

    private static void findAndPrintCopies(List<JavaFile> files) {
        Map<Integer, Set<Integer>> copies = new HashMap<>();
        int n = files.size();
        int totalComparisons = 0;

        log("Начало поиска копий для " + n + " файлов");

        for (int i = 0; i < n; i++) {
            JavaFile fileI = files.get(i);

            for (int j = i + 1; j < n; j++) {
                JavaFile fileJ = files.get(j);

                int maxSize = Math.max(fileI.length, fileJ.length);
                int minSize = Math.min(fileI.length, fileJ.length);


                if ((maxSize - minSize) * 100 > maxSize * SIZE_TOLERANCE_PERCENT) {
                    break;
                }

                if (fileI.hash == fileJ.hash && fileI.cleanContent.equals(fileJ.cleanContent)) {
                    copies.computeIfAbsent(i, k -> new TreeSet<>()).add(j);
                    copies.computeIfAbsent(j, k -> new TreeSet<>()).add(i);
                    totalComparisons++;
                    continue;
                }

                int distance = levenshteinDistance(fileI.cleanContent, fileJ.cleanContent);

                if (distance < LEVENSHTEIN_COPY_THRESHOLD) {
                    copies.computeIfAbsent(i, k -> new TreeSet<>()).add(j);
                    copies.computeIfAbsent(j, k -> new TreeSet<>()).add(i);
                }

                totalComparisons++;
            }

            if ((i + 1) % 100 == 0) {
                log("Обработано файлов: " + (i + 1) + "/" + n);
            }
        }

        log("Всего сравнений: " + totalComparisons);

        List<Integer> filesWithCopies = new ArrayList<>(copies.keySet());
        filesWithCopies.sort((a, b) -> files.get(a).path.compareTo(files.get(b).path));

        for (int i : filesWithCopies) {
            System.out.println(files.get(i).path);
            Set<Integer> copyIndices = new TreeSet<>(copies.get(i));
            List<String> copyPaths = new ArrayList<>();
            for (int copyIdx : copyIndices) {
                copyPaths.add(files.get(copyIdx).path);
            }
            copyPaths.sort(String::compareTo);
            for (String copyPath : copyPaths) {
                System.out.println(copyPath);
            }
        }
    }
}