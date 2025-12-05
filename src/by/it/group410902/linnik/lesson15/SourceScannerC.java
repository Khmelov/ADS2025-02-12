package by.it.group410902.linnik.lesson15;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;

public class SourceScannerC {

    static class ProcessedFile {
        String path;
        String content;
        int hash;

        ProcessedFile(String path, String content) {
            this.path = path;
            this.content = content;
            this.hash = content.hashCode();
        }
    }

    public static int fastLevenshtein(String s1, String s2, int threshold) {
        int n = s1.length();
        int m = s2.length();

        if (Math.abs(n - m) > threshold) {
            return threshold + 1;
        }

        if (n > m) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
            n = s1.length();
            m = s2.length();
        }

        int[] current = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            current[i] = i;
        }

        for (int j = 1; j <= m; j++) {
            int previous = current[0];
            current[0] = j;

            for (int i = 1; i <= n; i++) {
                int temp = current[i];
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                current[i] = Math.min(
                        Math.min(current[i - 1] + 1, current[i] + 1),
                        previous + cost
                );
                previous = temp;

                if (i == n && current[i] > threshold) {
                    return threshold + 1;
                }
            }
        }

        return current[n] <= threshold ? current[n] : threshold + 1;
    }

    public static void main(String[] args) {
        String srcPath = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        List<ProcessedFile> files = new ArrayList<>();

        try (var paths = Files.walk(Paths.get(srcPath))) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .parallel()
                    .forEach(p -> {
                        try {
                            String content = readFileFast(p);
                            if (isTestFile(content)) return;

                            String processed = processFileFast(content);
                            if (!processed.isEmpty()) {
                                String relPath = p.toString().substring(srcPath.length());
                                files.add(new ProcessedFile(relPath, processed));
                            }
                        } catch (Exception e) { }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
            return;
        }

        Map<Integer, List<ProcessedFile>> hashGroups = new HashMap<>();
        Map<Integer, List<ProcessedFile>> lengthGroups = new HashMap<>();

        for (ProcessedFile file : files) {
            hashGroups.computeIfAbsent(file.hash, k -> new ArrayList<>()).add(file);
            lengthGroups.computeIfAbsent(file.content.length(), k -> new ArrayList<>()).add(file);
        }

        Map<String, List<String>> results = new TreeMap<>();
        Set<String> alreadyProcessed = new HashSet<>();

        for (ProcessedFile file1 : files) {
            if (alreadyProcessed.contains(file1.path)) continue;

            List<String> copies = new ArrayList<>();

            for (ProcessedFile file2 : hashGroups.get(file1.hash)) {
                if (!file1.path.equals(file2.path) && file1.content.equals(file2.content)) {
                    copies.add(file2.path);
                    alreadyProcessed.add(file2.path);
                }
            }

            int len = file1.content.length();
            for (int targetLen = Math.max(1, len - 100); targetLen <= len + 100; targetLen++) {
                List<ProcessedFile> candidates = lengthGroups.get(targetLen);
                if (candidates == null) continue;

                for (ProcessedFile file2 : candidates) {
                    if (file1.path.equals(file2.path) || alreadyProcessed.contains(file2.path)) {
                        continue;
                    }

                    if (!quickCheck(file1.content, file2.content)) {
                        continue;
                    }

                    int distance = fastLevenshtein(file1.content, file2.content, 9);
                    if (distance < 10) {
                        copies.add(file2.path);
                        alreadyProcessed.add(file2.path);
                    }
                }
            }

            if (!copies.isEmpty()) {
                Collections.sort(copies);
                results.put(file1.path, copies);
                alreadyProcessed.add(file1.path);
            }
        }

        for (Map.Entry<String, List<String>> entry : results.entrySet()) {
            System.out.println(entry.getKey());
            for (String copy : entry.getValue()) {
                System.out.println(copy);
            }
        }
    }

    private static String readFileFast(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        try {
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                if (b >= 32 && b < 127 || b == 9 || b == 10 || b == 13) {
                    sb.append((char) b);
                } else if (b < 0) {

                } else {
                    sb.append(' ');
                }
            }
            return sb.toString();
        }
    }

    private static boolean isTestFile(String content) {
        return content.contains("@Test") || content.contains("org.junit.Test");
    }

    private static String processFileFast(String content) {
        StringBuilder result = new StringBuilder();
        boolean inComment = false;
        boolean inString = false;
        int i = 0;

        while (i < content.length()) {
            char c = content.charAt(i);

            if (!inComment && !inString) {
                if (i == 0 || content.charAt(i - 1) == '\n') {
                    int start = i;
                    while (start < content.length() &&
                            (content.charAt(start) == ' ' || content.charAt(start) == '\t')) {
                        start++;
                    }
                    if (start + 6 < content.length() &&
                            content.startsWith("package", start)) {
                        i = skipToNewLine(content, i);
                        continue;
                    }
                    if (start + 5 < content.length() &&
                            content.startsWith("import", start)) {
                        i = skipToNewLine(content, i);
                        continue;
                    }
                }
                if (c == '/' && i + 1 < content.length()) {
                    char next = content.charAt(i + 1);
                    if (next == '/') {
                        i = skipToNewLine(content, i);
                        continue;
                    } else if (next == '*') {
                        inComment = true;
                        i += 2;
                        continue;
                    }
                }
                if (c == '"' || c == '\'') {
                    inString = true;
                    result.append(c);
                    i++;
                    continue;
                }
            }

            if (inComment) {
                if (c == '*' && i + 1 < content.length() && content.charAt(i + 1) == '/') {
                    inComment = false;
                    i += 2;
                } else {
                    i++;
                }
                continue;
            }

            if (inString) {
                result.append(c);
                if (c == '\\' && i + 1 < content.length()) {
                    result.append(content.charAt(i + 1));
                    i += 2;
                } else if (c == '"' || c == '\'') {
                    inString = false;
                    i++;
                } else {
                    i++;
                }
                continue;
            }
            result.append(c < 33 ? ' ' : c);
            i++;
        }
        return result.toString().replaceAll("\\s+", " ").trim();
    }

    private static int skipToNewLine(String content, int i) {
        while (i < content.length() && content.charAt(i) != '\n') {
            i++;
        }
        return i < content.length() ? i + 1 : i;
    }

    private static boolean quickCheck(String s1, String s2) {
        int n = Math.min(100, Math.min(s1.length(), s2.length()));
        int mismatches = 0;

        for (int i = 0; i < n; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                mismatches++;
                if (mismatches > 5) return false;
            }
        }
        return true;
    }
}