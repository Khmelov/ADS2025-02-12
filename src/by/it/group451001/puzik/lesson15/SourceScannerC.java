package by.it.group451001.puzik.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SourceScannerC {
    private static final int COPY_THRESHOLD = 10;
    
    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        
        Path root = Path.of(src);
        List<ProcessedFile> processedFiles = new ArrayList<>();
        
        // Process all files
        try (var walk = Files.walk(root)) {
            walk.forEach(p -> {
                if (p.toString().endsWith(".java")) {
                    String content = readFileContent(p);
                    if (content != null && !content.contains("@Test") && !content.contains("org.junit.Test")) {
                        String processed = processFile(content);
                        String relativePath = root.relativize(p).toString().replace(File.separator, "/");
                        processedFiles.add(new ProcessedFile(relativePath, processed));
                    }
                }
            });
        } catch (IOException e) {
            // Ignore
        }
        
        // Find copies using Levenshtein distance
        Map<String, List<String>> copiesMap = new TreeMap<>();
        
        for (int i = 0; i < processedFiles.size(); i++) {
            ProcessedFile file1 = processedFiles.get(i);
            List<String> copies = new ArrayList<>();
            
            for (int j = 0; j < processedFiles.size(); j++) {
                if (i == j) continue;
                ProcessedFile file2 = processedFiles.get(j);
                int distance = levenshteinDistance(file1.content, file2.content);
                
                if (distance < COPY_THRESHOLD) {
                    copies.add(file2.path);
                }
            }
            
            if (!copies.isEmpty()) {
                copies.sort(String::compareTo);
                copiesMap.put(file1.path, copies);
            }
        }
        
        // Output results
        for (Map.Entry<String, List<String>> entry : copiesMap.entrySet()) {
            System.out.println(entry.getKey());
            for (String copy : entry.getValue()) {
                System.out.println(copy);
            }
        }
    }
    
    private static String readFileContent(Path path) {
        Charset[] charsets = {StandardCharsets.UTF_8, Charset.forName("ISO-8859-1"), Charset.defaultCharset()};
        for (Charset charset : charsets) {
            try {
                return Files.readString(path, charset);
            } catch (IOException e) {
                // Try next charset
            }
        }
        return null;
    }
    
    private static String processFile(String content) {
        // Remove package and imports
        StringBuilder sb = new StringBuilder();
        String[] lines = content.split("\n");
        boolean inPackageOrImport = false;
        
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                inPackageOrImport = true;
                continue;
            }
            if (inPackageOrImport && !trimmed.isEmpty() && !trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                inPackageOrImport = false;
            }
            if (!inPackageOrImport) {
                sb.append(line).append("\n");
            }
        }
        
        String result = sb.toString();
        
        // Remove comments O(n)
        result = removeComments(result);
        
        // Replace all sequences of characters with code <33 with 32 (space)
        StringBuilder normalized = new StringBuilder();
        for (int i = 0; i < result.length(); i++) {
            char c = result.charAt(i);
            if (c < 33) {
                normalized.append(' ');
            } else {
                normalized.append(c);
            }
        }
        
        result = normalized.toString().trim();
        
        return result;
    }
    
    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int len = content.length();
        
        while (i < len) {
            if (i < len - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                // Single-line comment
                while (i < len && content.charAt(i) != '\n') {
                    i++;
                }
            } else if (i < len - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                // Multi-line comment
                i += 2;
                while (i < len - 1) {
                    if (content.charAt(i) == '*' && content.charAt(i + 1) == '/') {
                        i += 2;
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
    
    // Optimized Levenshtein distance with early termination
    private static int levenshteinDistance(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        
        // Early termination if difference in length is >= threshold
        if (Math.abs(len1 - len2) >= COPY_THRESHOLD) {
            return COPY_THRESHOLD;
        }
        
        // Use space-optimized version with early termination
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];
        
        // Initialize first row
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
                    curr[j] = 1 + Math.min(Math.min(prev[j], curr[j - 1]), prev[j - 1]);
                }
                minInRow = Math.min(minInRow, curr[j]);
            }
            
            // Early termination if minimum in row is >= threshold
            if (minInRow >= COPY_THRESHOLD) {
                return COPY_THRESHOLD;
            }
            
            // Swap arrays
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        return prev[len2];
    }
    
    private static class ProcessedFile {
        final String path;
        final String content;
        
        ProcessedFile(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}

