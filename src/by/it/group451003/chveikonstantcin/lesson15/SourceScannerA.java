package by.it.group451003.chveikonstantcin.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path rootPath = Path.of(src);

        List<FileData> fileDataList = new ArrayList<>();
        scanJavaFiles(new File(src), fileDataList, rootPath);

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

    private static void scanJavaFiles(File directory, List<FileData> fileDataList, Path rootPath) {
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                scanJavaFiles(file, fileDataList, rootPath);
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                processJavaFile(file, fileDataList, rootPath);
            }
        }
    }

    private static void processJavaFile(File file, List<FileData> fileDataList, Path rootPath) {
        try {
            String content = readFileWithFallback(file.toPath());

            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            String processedContent = processContent(content);
            String relativePath = getRelativePath(file.toPath(), rootPath);
            int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

            fileDataList.add(new FileData(size, relativePath));

        } catch (IOException e) {
        }
    }

    private static String readFileWithFallback(Path path) throws IOException {
        Charset[] charsets = {StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1, Charset.defaultCharset()};

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

        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")) {
                continue;
            }

            if (result.length() > 0) {
                result.append("\n");
            }
            result.append(trimmedLine);
        }

        return trimLowChars(result.toString());
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

    private static String getRelativePath(Path filePath, Path rootPath) {
        return rootPath.relativize(filePath).toString().replace(File.separatorChar, '\\');
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