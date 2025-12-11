package by.it.group451001.puzik.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerB {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        
        Path root = Path.of(src);
        List<FileInfo> fileInfos = new ArrayList<>();
        
        try (var walk = Files.walk(root)) {
            walk.forEach(p -> {
                if (p.toString().endsWith(".java")) {
                    String content = readFileContent(p);
                    if (content != null && !content.contains("@Test") && !content.contains("org.junit.Test")) {
                        String processed = processFile(content);
                        int size = processed.getBytes(StandardCharsets.UTF_8).length;
                        String relativePath = root.relativize(p).toString().replace(File.separator, "/");
                        fileInfos.add(new FileInfo(relativePath, size));
                    }
                }
            });
        } catch (IOException e) {
            // Ignore
        }
        
        // Sort by size, then lexicographically by path
        fileInfos.sort(Comparator.comparingInt(FileInfo::getSize)
                .thenComparing(FileInfo::getPath));
        
        // Output results
        for (FileInfo info : fileInfos) {
            System.out.println(info.getSize() + " " + info.getPath());
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
        
        // Remove characters with code <33 at beginning and end
        int start = 0;
        while (start < result.length() && result.charAt(start) < 33) {
            start++;
        }
        int end = result.length();
        while (end > start && result.charAt(end - 1) < 33) {
            end--;
        }
        result = result.substring(start, end);
        
        // Remove empty lines
        StringBuilder noEmptyLines = new StringBuilder();
        String[] resultLines = result.split("\n");
        for (String line : resultLines) {
            if (!line.trim().isEmpty()) {
                noEmptyLines.append(line).append("\n");
            }
        }
        
        return noEmptyLines.toString();
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
    
    private static class FileInfo {
        private final String path;
        private final int size;
        
        public FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
        }
        
        public String getPath() {
            return path;
        }
        
        public int getSize() {
            return size;
        }
    }
}

