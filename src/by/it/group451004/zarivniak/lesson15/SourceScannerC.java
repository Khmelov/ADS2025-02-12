package by.it.group451004.zarivniak.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src";
        List<FileData> fileDataList = new ArrayList<>();
        Path srcPath = Paths.get(src);

        try {
            Files.walk(srcPath)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(file -> processJavaFile(file, srcPath, fileDataList));
        } catch (IOException e) {}

        Map<String, List<String>> copies = findCopiesByHashing(fileDataList);

        List<String> sortedFiles = new ArrayList<>(copies.keySet());
        Collections.sort(sortedFiles);

        for (String filePath : sortedFiles) {
            System.out.println(filePath);
            List<String> copyPaths = copies.get(filePath);
            Collections.sort(copyPaths);
            for (String copyPath : copyPaths) {
                System.out.println(copyPath);
            }
        }
    }

    private static void processJavaFile(Path file, Path srcPath, List<FileData> fileDataList) {
        try {
            String content = Files.readString(file, StandardCharsets.UTF_8);
            if (content.contains("@Test") || content.contains("org.junit.Test")) return;
            String processed = processFileContent(content);
            String relativePath = srcPath.relativize(file).toString();
            fileDataList.add(new FileData(relativePath, processed));

        } catch (MalformedInputException e) {
        } catch (IOException e) {}
    }

    private static String processFileContent(String content) {
        String withoutComments = removeComments(content);
        String[] lines = withoutComments.split("\\R");
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            String processedLine = processLine(line);
            if (!processedLine.isEmpty()) {
                result.append(processedLine).append(" ");
            }
        }

        String processed = result.toString();
        processed = processed.replaceAll("\\s+", " ").trim();
        return processed;
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int length = content.length();
        int i = 0;

        while (i < length) {
            if (i + 1 < length && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                while (i < length && content.charAt(i) != '\n' && content.charAt(i) != '\r') i++;
            }
            else if (i + 1 < length && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < length && !(content.charAt(i) == '*' && content.charAt(i + 1) == '/')) i++;
                i += 2;
            }
            else {
                result.append(content.charAt(i));
                i++;
            }
        }
        return result.toString();
    }

    private static String processLine(String line) {
        String trimmed = line.trim();
        if (trimmed.startsWith("package") || trimmed.startsWith("import")) return "";
        return trimmed;
    }

    private static Map<String, List<String>> findCopiesByHashing(List<FileData> fileDataList) {
        Map<String, List<String>> copies = new HashMap<>();
        Map<FileSignature, List<FileData>> groups = new HashMap<>();
        for (FileData file : fileDataList) {
            FileSignature signature = new FileSignature(file.content);
            groups.computeIfAbsent(signature, k -> new ArrayList<>()).add(file);
        }

        for (List<FileData> group : groups.values()) {
            if (group.size() > 1) {
                for (int i = 0; i < group.size(); i++) {
                    FileData file1 = group.get(i);
                    List<String> fileCopies = new ArrayList<>();
                    for (int j = i + 1; j < group.size(); j++) {
                        FileData file2 = group.get(j);
                        if (isCopy(file1.content, file2.content)) {
                            fileCopies.add(file2.path);
                        }
                    }
                    if (!fileCopies.isEmpty()) {
                        copies.put(file1.path, fileCopies);
                    }
                }
            }
        }

        return copies;
    }

    private static boolean isCopy(String s1, String s2) {
        if (s1.equals(s2)) return true;
        if (Math.abs(s1.length() - s2.length()) > 20) return false;
        return calculateSimilarity(s1, s2) > 0.95;
    }

    private static double calculateSimilarity(String s1, String s2) {
        Set<String> ngrams1 = getNgrams(s1, 3);
        Set<String> ngrams2 = getNgrams(s2, 3);
        int intersection = 0;
        for (String ngram : ngrams1) {
            if (ngrams2.contains(ngram)) {
                intersection++;
            }
        }

        int union = ngrams1.size() + ngrams2.size() - intersection;
        return union == 0 ? 0 : (double) intersection / union;
    }

    private static Set<String> getNgrams(String str, int n) {
        Set<String> ngrams = new HashSet<>();
        for (int i = 0; i <= str.length() - n; i++) {
            ngrams.add(str.substring(i, i + n));
        }
        return ngrams;
    }

    private static class FileData {
        final String path;
        final String content;
        FileData(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }

    private static class FileSignature {
        final int length;
        final long hash;

        FileSignature(String content) {
            this.length = content.length();
            this.hash = calculateContentHash(content);
        }

        private long calculateContentHash(String content) {
            if (content.length() == 0) return 0;
            long hash = 0;
            int step = Math.max(1, content.length() / 10);
            for (int i = 0; i < content.length(); i += step) {
                hash = hash * 31 + content.charAt(i);
            }
            return hash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FileSignature that = (FileSignature) o;
            return length == that.length && Math.abs(hash - that.hash) < 1000;
        }

        @Override
        public int hashCode() {
            return Objects.hash(length, hash / 1000);
        }
    }
}