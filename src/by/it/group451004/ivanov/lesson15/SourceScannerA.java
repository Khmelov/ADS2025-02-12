package by.it.group451004.ivanov.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerA {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        
        List<FileInfo> fileInfos = new ArrayList<>();
        
        try (var walk = Files.walk(root)) {
            walk.forEach(p -> {
                if (p.toString().endsWith(".java")) {
                    try {
                        String content = Files.readString(p, StandardCharsets.UTF_8);

                        if (content.contains("@Test") || content.contains("org.junit.Test")) {
                            return;
                        }

                        String processed = processContent(content);

                        byte[] bytes = processed.getBytes(StandardCharsets.UTF_8);
                        int size = bytes.length;

                        String relativePath = root.relativize(p).toString();
                        
                        fileInfos.add(new FileInfo(size, relativePath));
                    } catch (IOException e) {
                        // Ignore MalformedInputException and other IO errors
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileInfos.sort(Comparator.comparingInt(FileInfo::getSize)
                .thenComparing(FileInfo::getPath));

        for (FileInfo info : fileInfos) {
            System.out.println(info.getSize() + " " + info.getPath());
        }
    }
    
    private static String processContent(String content) {
        StringBuilder sb = new StringBuilder();
        String[] lines = content.split("\n");
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

        int start = 0;
        while (start < result.length() && result.charAt(start) < 33) {
            start++;
        }

        int end = result.length();
        while (end > start && result.charAt(end - 1) < 33) {
            end--;
        }
        
        return result.substring(start, end);
    }
    
    private static class FileInfo {
        private final int size;
        private final String path;
        
        public FileInfo(int size, String path) {
            this.size = size;
            this.path = path;
        }
        
        public int getSize() {
            return size;
        }
        
        public String getPath() {
            return path;
        }
    }
}