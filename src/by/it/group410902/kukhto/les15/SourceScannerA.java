package by.it.group410902.kukhto.les15;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        String srcPath = System.getProperty("user.dir") + File.separator + "src";
        Path srcDir = Path.of(srcPath);
        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            Files.walk(srcDir)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String relPath = srcDir.relativize(p).toString();
                            String content = Files.readString(p);

                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            String processed = processContent(content);


                            int sizeBytes = processed.getBytes().length;

                            fileInfos.add(new FileInfo(sizeBytes, relPath));

                        } catch (IOException e) {
                            // игнор ошибок чтения файлов
                        }
                    });
        } catch (IOException e) {
            // игнор ошибок обхода директории
        }


        fileInfos.sort(Comparator.comparingInt(FileInfo::getSize)
                .thenComparing(FileInfo::getPath));


        for (FileInfo fi : fileInfos) {
            System.out.println(fi.getSize() + " " + fi.getPath());
        }

        long endTime = System.currentTimeMillis();
        System.err.println("Execution time: " + (endTime - startTime) + "ms");
    }

    private static String processContent(String content) {

        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\r?\n");

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("package") && !trimmed.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        String filteredContent = result.toString();

        // удаление символов с кодом <33 в начале и конце
        return removeControlCharsFromEdges(filteredContent);
    }

    private static String removeControlCharsFromEdges(String text) {
        if (text.isEmpty()) return text;

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
