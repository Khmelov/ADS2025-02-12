package by.it.group451001.khomenkov.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerA {

    static class FileData implements Comparable<FileData> {
        String relativePath;
        int size;

        FileData(String relativePath, int size) {
            this.relativePath = relativePath;
            this.size = size;
        }

        @Override
        public int compareTo(FileData other) {
            if (this.size != other.size) {
                return Integer.compare(this.size, other.size);
            }
            return this.relativePath.compareTo(other.relativePath);
        }
    }

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<FileData> fileDataList = new ArrayList<>();

        Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".java")) {
                    processJavaFile(file, srcPath, fileDataList);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        Collections.sort(fileDataList);

        for (FileData fileData : fileDataList) {
            System.out.println(fileData.size + " " + fileData.relativePath);
        }
    }

    private static void processJavaFile(Path file, Path srcPath, List<FileData> fileDataList) {
        try {
            String content = new String(Files.readAllBytes(file));

            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            String processedContent = processContent(content);
            byte[] bytes = processedContent.getBytes();
            String relativePath = srcPath.relativize(file).toString();

            fileDataList.add(new FileData(relativePath, bytes.length));

        } catch (MalformedInputException e) {
            // Игнорируем файлы с ошибками кодировки
        } catch (IOException e) {
            // Игнорируем файлы, которые не удалось прочитать
        }
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\r?\n");

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.startsWith("package") || trimmedLine.startsWith("import")) {
                continue;
            }

            result.append(line).append("\n");
        }

        String processed = result.toString();

        // Удаляем символы с кодом <33 в начале и конце
        processed = removeLowCharsFromStart(processed);
        processed = removeLowCharsFromEnd(processed);

        return processed;
    }

    private static String removeLowCharsFromStart(String str) {
        int i = 0;
        while (i < str.length() && str.charAt(i) < 33) {
            i++;
        }
        return str.substring(i);
    }

    private static String removeLowCharsFromEnd(String str) {
        int i = str.length() - 1;
        while (i >= 0 && str.charAt(i) < 33) {
            i--;
        }
        return str.substring(0, i + 1);
    }
}
