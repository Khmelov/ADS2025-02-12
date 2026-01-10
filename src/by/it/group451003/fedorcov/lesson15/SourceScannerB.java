package by.it.group451003.fedorcov.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        List<FileData> fileDataList = new ArrayList<>();
        scanJavaFiles(new File(src), fileDataList, src);

        fileDataList.sort((f1, f2) -> {
            int sizeCompare = Integer.compare(f1.size, f2.size);
            if (sizeCompare != 0) {
                return sizeCompare;
            }
            return f1.relativePath.compareTo(f2.relativePath);
        });

        for (FileData fileData : fileDataList) {
            System.out.println(fileData.size + " " + fileData.relativePath);
        }
    }

    private static void scanJavaFiles(File directory, List<FileData> fileDataList, String basePath) {
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                scanJavaFiles(file, fileDataList, basePath);
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                processJavaFile(file, fileDataList, basePath);
            }
        }
    }

    private static void processJavaFile(File file, List<FileData> fileDataList, String basePath) {
        try {
            String content = readFileWithFallback(file.toPath());

            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            String processedContent = processContent(content);

            String relativePath = file.getAbsolutePath().substring(basePath.length());

            int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

            fileDataList.add(new FileData(size, relativePath));

        } catch (IOException e) {
        }
    }

    private static String readFileWithFallback(Path path) throws IOException {
        Charset[] charsets = {StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1, StandardCharsets.UTF_16};

        for (Charset charset : charsets) {
            try {
                return Files.readString(path, charset);
            } catch (IOException e) {

            }
        }

        return "";
    }
    private static String processContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        content = removePackageAndImports(content);

        content = removeComments(content);

        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String processedLine = processLine(line);
            if (!processedLine.isEmpty()) {
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(processedLine);
            }
        }

        return result.toString();
    }

    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package ") && !trimmedLine.startsWith("import ")) {
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(line);
            }
        }

        return result.toString();
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int length = content.length();
        int i = 0;

        while (i < length) {
            if (i + 1 < length && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                while (i < length && content.charAt(i) != '\n') {
                    i++;
                }
            } else if (i + 1 < length && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                i += 2;
                while (i < length - 1 && !(content.charAt(i) == '*' && content.charAt(i + 1) == '/')) {
                    i++;
                }
                i += 2;
            } else {
                result.append(content.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    private static String processLine(String line) {
        line = trimLowChars(line);

        return line;
    }

    private static String trimLowChars(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        int start = 0;
        int end = text.length();

        while (start < end && text.charAt(start) < 33) {
            start++;
        }

        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        return text.substring(start, end);
    }

    static class FileData {
        int size;
        String relativePath;

        FileData(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}