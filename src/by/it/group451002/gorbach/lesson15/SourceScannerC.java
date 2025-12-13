package by.it.group451002.gorbach.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {
    private static final int COPY_THRESHOLD = 10;
    private static final int HASH_WINDOW = 20;

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<ProcessedFile> processedFiles = new ArrayList<>();

        Files.walk(Paths.get(src))
                .filter(path -> path.toString().endsWith(".java"))
                .parallel()
                .forEach(path -> {
                    try {
                        processJavaFile(path, src, processedFiles);
                    } catch (IOException e) {
                        System.err.println("Ошибка обработки файла " + path + ": " + e.getMessage());
                    }
                });

        Map<String, List<String>> copiesMap = findCopies(processedFiles);

        List<String> sortedPaths = new ArrayList<>(copiesMap.keySet());
        Collections.sort(sortedPaths);

        for (String path : sortedPaths) {
            List<String> copies = copiesMap.get(path);
            if (!copies.isEmpty()) {
                System.out.println(path);
                for (String copyPath : copies) {
                    System.out.println(copyPath);
                }
            }
        }
    }

    private static synchronized void processJavaFile(Path filePath, String srcRoot,
                                                     List<ProcessedFile> result) throws IOException {
        String content = readFileWithErrorHandling(filePath);

        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        String processedContent = processContent(content);

        if (processedContent.length() > 1000) {
            processedContent = processedContent.substring(0, 1000);
        }

        String relativePath = Paths.get(srcRoot).relativize(filePath).toString();

        int hash = calculateFingerprint(processedContent);

        ProcessedFile file = new ProcessedFile(relativePath, processedContent, hash);
        result.add(file);
    }

    private static String readFileWithErrorHandling(Path path) throws IOException {
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPLACE)
                .onUnmappableCharacter(CodingErrorAction.REPLACE);

        return Files.readString(path, Charset.forName(decoder.charset().name()));
    }

    private static String processContent(String content) {
        String withoutComments = removeComments(content);

        String[] lines = withoutComments.split("\n");
        StringBuilder result = new StringBuilder();

        boolean insideImports = true;

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.isEmpty()) {
                continue;
            }

            if (insideImports && (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import "))) {
                continue;
            }

            if (insideImports && !trimmedLine.isEmpty() &&
                    !trimmedLine.startsWith("package ") &&
                    !trimmedLine.startsWith("import ")) {
                insideImports = false;
            }

            if (!insideImports || (!trimmedLine.startsWith("package ") && !trimmedLine.startsWith("import "))) {
                result.append(line).append("\n");
            }
        }

        String processed = result.toString();

        processed = normalizeWhitespace(processed);

        return processed.trim();
    }

    private static String removeComments(String source) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = source.length();

        while (i < n) {
            if (i + 1 < n && source.charAt(i) == '/' && source.charAt(i + 1) == '/') {
                while (i < n && source.charAt(i) != '\n') {
                    i++;
                }
                if (i < n && source.charAt(i) == '\n') {
                    result.append('\n');
                    i++;
                }
            }
            else if (i + 1 < n && source.charAt(i) == '/' && source.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < n && !(source.charAt(i) == '*' && source.charAt(i + 1) == '/')) {
                    i++;
                }
                if (i + 1 < n) {
                    i += 2;
                }
            }
            else {
                result.append(source.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    private static String normalizeWhitespace(String str) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = str.length();

        while (i < n) {
            char c = str.charAt(i);
            if (c < 33) {
                if (result.length() > 0 && result.charAt(result.length() - 1) != ' ') {
                    result.append(' ');
                }
                i++;
                while (i < n && str.charAt(i) < 33) {
                    i++;
                }
            } else {
                result.append(c);
                i++;
            }
        }

        if (result.length() > 0 && result.charAt(result.length() - 1) == ' ') {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    private static int calculateFingerprint(String content) {
        int hash = 0;
        int n = content.length();

        for (int i = 0; i < n; i++) {
            hash = 31 * hash + content.charAt(i);
        }

        return hash;
    }

    private static Map<String, List<String>> findCopies(List<ProcessedFile> files) {
        Map<String, List<String>> copiesMap = new HashMap<>();
        int n = files.size();

        Collections.sort(files, Comparator.comparingInt(ProcessedFile::hash));

        for (int i = 0; i < n; i++) {
            ProcessedFile file1 = files.get(i);
            List<String> copies = new ArrayList<>();

            int j = i + 1;
            while (j < n && files.get(j).hash() == file1.hash()) {
                ProcessedFile file2 = files.get(j);

                if (isCopy(file1.content(), file2.content())) {
                    copies.add(file2.path());
                }
                j++;
            }

            if (!copies.isEmpty()) {
                copiesMap.put(file1.path(), copies);
            }

            i = j - 1;
        }

        return copiesMap;
    }

    private static boolean isCopy(String s1, String s2) {
        if (s1.equals(s2)) {
            return true;
        }

        int len1 = s1.length();
        int len2 = s2.length();

        if (Math.abs(len1 - len2) >= COPY_THRESHOLD) {
            return false;
        }

        int maxLen = Math.max(len1, len2);
        int minLen = Math.min(len1, len2);

        if (maxLen > 0 && (float)minLen / maxLen < 0.9f) {
            return false;
        }

        return quickLevenshtein(s1, s2) < COPY_THRESHOLD;
    }

    private static int quickLevenshtein(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        if (len1 == 0) return len2;
        if (len2 == 0) return len1;

        int limit = Math.min(Math.max(len1, len2), COPY_THRESHOLD * 2);

        int diff = 0;
        int i = 0, j = 0;

        while (i < len1 && j < len2 && diff <= COPY_THRESHOLD) {
            if (s1.charAt(i) == s2.charAt(j)) {
                i++;
                j++;
            } else {
                diff++;
                if (i + 1 < len1 && s1.charAt(i + 1) == s2.charAt(j)) {
                    i++;
                } else if (j + 1 < len2 && s1.charAt(i) == s2.charAt(j + 1)) {
                    j++;
                } else {
                    i++;
                    j++;
                }
            }
        }

        diff += Math.abs((len1 - i) - (len2 - j));

        return diff;
    }

    private record ProcessedFile(String path, String content, int hash) {}
}