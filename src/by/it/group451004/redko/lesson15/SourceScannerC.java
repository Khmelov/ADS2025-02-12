package by.it.group451004.redko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class SourceScannerC {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        
        List<FileData> files = new ArrayList<>();
        
        try (var walk = Files.walk(root)) {
            walk.forEach(p -> {
                if (p.toString().endsWith(".java")) {
                    try {
                        String content = Files.readString(p, StandardCharsets.UTF_8);

                        if (content.contains("@Test") || content.contains("org.junit.Test")) {
                            return;
                        }

                        String processed = processContent(content);

                        if (processed.isEmpty()) {
                            return;
                        }

                        String relativePath = root.relativize(p).toString();
                        
                        files.add(new FileData(relativePath, processed));
                    } catch (IOException e) {
                        // Ignore MalformedInputException and other IO errors
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Set<String>> copies = new TreeMap<>();
        
        for (int i = 0; i < files.size(); i++) {
            FileData file1 = files.get(i);
            String content1 = file1.getContent();
            int len1 = content1.length();
            
            for (int j = i + 1; j < files.size(); j++) {
                FileData file2 = files.get(j);
                String content2 = file2.getContent();
                int len2 = content2.length();

                if (Math.abs(len1 - len2) >= 10) {
                    continue;
                }

                if (content1.equals(content2)) {
                    copies.computeIfAbsent(file1.getPath(), k -> new TreeSet<>()).add(file2.getPath());
                    copies.computeIfAbsent(file2.getPath(), k -> new TreeSet<>()).add(file1.getPath());
                    continue;
                }
                
                int distance = levenshteinDistance(content1, content2);
                
                if (distance < 10) {
                    copies.computeIfAbsent(file1.getPath(), k -> new TreeSet<>()).add(file2.getPath());
                    copies.computeIfAbsent(file2.getPath(), k -> new TreeSet<>()).add(file1.getPath());
                }
            }
        }

        for (Map.Entry<String, Set<String>> entry : copies.entrySet()) {
            System.out.println(entry.getKey());
            for (String copyPath : entry.getValue()) {
                System.out.println(copyPath);
            }
        }
    }
    
    private static String processContent(String content) {
        StringBuilder sb = new StringBuilder();
        String[] lines = content.split("\n", -1);
        boolean inPackageOrImport = true;
        
        for (String line : lines) {
            String trimmed = line.trim();
            
            if (inPackageOrImport) {
                if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                    continue;
                }
                if (!trimmed.isEmpty() && !trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                    inPackageOrImport = false;
                } else {
                    continue;
                }
            }
            
            sb.append(line).append("\n");
        }
        
        String result = sb.toString();

        result = removeComments(result);

        StringBuilder sb2 = new StringBuilder();
        boolean inWhitespace = false;
        for (int i = 0; i < result.length(); i++) {
            char c = result.charAt(i);
            if (c < 33) {
                if (!inWhitespace) {
                    sb2.append(' ');
                    inWhitespace = true;
                }
            } else {
                sb2.append(c);
                inWhitespace = false;
            }
        }
        
        result = sb2.toString();

        result = result.trim();
        
        return result;
    }
    
    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int len = content.length();
        
        while (i < len) {
            if (i < len - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                while (i < len && content.charAt(i) != '\n') {
                    i++;
                }
                if (i < len) {
                    result.append('\n');
                    i++;
                }
            } else if (i < len - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
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
    
    private static int levenshteinDistance(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        if (Math.abs(len1 - len2) >= 10) {
            return 10;
        }

        if (len1 > len2) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
            int tempLen = len1;
            len1 = len2;
            len2 = tempLen;
        }
        
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

        for (int j = 0; j <= len2; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            curr[0] = i;
            int minInRow = Integer.MAX_VALUE;
            boolean allAboveThreshold = true;
            
            for (int j = 1; j <= len2; j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                
                curr[j] = Math.min(
                        Math.min(
                                prev[j] + 1,
                                curr[j - 1] + 1
                        ),
                        prev[j - 1] + cost
                );
                
                minInRow = Math.min(minInRow, curr[j]);
                if (curr[j] < 10) {
                    allAboveThreshold = false;
                }
            }

            if (allAboveThreshold && minInRow >= 10) {
                return 10;
            }

            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        return prev[len2];
    }
    
    private static class FileData {
        private final String path;
        private final String content;
        
        public FileData(String path, String content) {
            this.path = path;
            this.content = content;
        }
        
        public String getPath() {
            return path;
        }
        
        public String getContent() {
            return content;
        }
    }
}
