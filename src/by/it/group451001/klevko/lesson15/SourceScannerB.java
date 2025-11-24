package by.it.group451001.klevko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<FileInfo> fileInfoList = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(srcPath)) {
            walk.filter(p -> p.toString().endsWith(".java")).forEach(p -> processFile(p, srcPath, fileInfoList));
        } catch (IOException e) {
            System.err.println("Error walking directory: " + e.getMessage());
        }

        fileInfoList.sort(Comparator.comparingLong(FileInfo::getSize).thenComparing(FileInfo::getRelativePath));

        for (FileInfo info : fileInfoList) {
            System.out.println(info.getSize() + " " + info.getRelativePath());
        }
    }

    private static void processFile(Path filePath, Path srcPath, List<FileInfo> fileInfoList) {
        String content = readFileWithErrorHandling(filePath);

        if (content == null) return;

        if (content.contains("@Test") || content.contains("org.junit.Test")) return;

        String processed = removePackageAndImports(content);
        processed = removeComments(processed);
        processed = removeControlCharacters(processed);
        processed = removeEmptyLines(processed);

        long size = processed.getBytes(StandardCharsets.UTF_8).length;

        String relativePath = srcPath.relativize(filePath).toString();

        fileInfoList.add(new FileInfo(size, relativePath));
    }

    private static String readFileWithErrorHandling(Path filePath) {
        try {
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (java.nio.charset.MalformedInputException e) {
            try {
                return Files.readString(filePath, StandardCharsets.ISO_8859_1);
            } catch (IOException ex) {
                try {
                    return Files.readString(filePath, StandardCharsets.US_ASCII);
                } catch (IOException exc) {
                    System.err.println("Cannot read file " + filePath + " with any encoding");
                    return null;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
            return null;
        }
    }

    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\\n", -1);

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
        int len = content.length();

        while (i < len) {
            if (i < len - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                while (i < len && content.charAt(i) != '\n') {
                    i++;
                }
                if (i < len && content.charAt(i) == '\n') {
                    result.append('\n');
                    i++;
                }
            }
            else if (i < len - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                i += 2;
                while (i < len - 1) {
                    if (content.charAt(i) == '*' && content.charAt(i + 1) == '/') {
                        i += 2;
                        break;
                    }
                    if (content.charAt(i) == '\n') {
                        result.append('\n');
                    }
                    i++;
                }
            }
            else if (content.charAt(i) == '"') {
                result.append(content.charAt(i));
                i++;
                while (i < len) {
                    char c = content.charAt(i);
                    result.append(c);
                    if (c == '"' && (i == 0 || content.charAt(i - 1) != '\\')) {
                        i++;
                        break;
                    }
                    i++;
                }
            }
            else if (content.charAt(i) == '\'') {
                result.append(content.charAt(i));
                i++;
                while (i < len) {
                    char c = content.charAt(i);
                    result.append(c);
                    if (c == '\'' && (i == 0 || content.charAt(i - 1) != '\\')) {
                        i++;
                        break;
                    }
                    i++;
                }
            }
            else {
                result.append(content.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    private static String removeControlCharacters(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n", -1);

        for (String line : lines) {
            int start = 0;
            while (start < line.length() && line.charAt(start) < 33) {
                start++;
            }

            int end = line.length() - 1;
            while (end >= start && line.charAt(end) < 33) {
                end--;
            }

            if (start <= end) {
                result.append(line.substring(start, end + 1)).append("\n");
            } else {
                result.append("\n");
            }
        }

        return result.toString();
    }

    private static String removeEmptyLines(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n", -1);

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        String res = result.toString();
        if (res.endsWith("\n")) {
            res = res.substring(0, res.length() - 1);
        }

        return res;
    }

    private static class FileInfo {
        private final long size;
        private final String relativePath;

        public FileInfo(long size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }

        public long getSize() {
            return size;
        }

        public String getRelativePath() {
            return relativePath;
        }
    }
}
