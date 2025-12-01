package by.it.group451004.kozlov.lesson15;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        try {
            List<FileData> fileDataList = new ArrayList<>();
            scanJavaFiles(new File(src), new File(src), fileDataList);
            printResults(fileDataList);
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    private static void scanJavaFiles(File currentDir, File baseDir, List<FileData> result) {
        if (!currentDir.exists() || !currentDir.isDirectory()) {
            return;
        }

        File[] files = currentDir.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                scanJavaFiles(file, baseDir, result);
            } else if (file.getName().endsWith(".java")) {
                processFileFast(file, baseDir, result);
            }
        }
    }

    private static void processFileFast(File file, File baseDir, List<FileData> result) {
        try {
            if (file.getName().contains("Test") || file.getPath().contains("test")) {
                return;
            }

            String content = readWithTimeout(file, 1000); // 1 секунда таймаут
            if (content == null) {
                return;
            }

            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            String processed = processFast(content);
            int size = processed.getBytes(StandardCharsets.UTF_8).length;

            String relativePath = baseDir.toPath().relativize(file.toPath()).toString();
            result.add(new FileData(relativePath, size));

        } catch (Exception e) {
            //scip
        }
    }

    private static String readWithTimeout(File file, long timeoutMs) {
        StringBuilder content = new StringBuilder();
        Thread readThread = new Thread(() -> {
            try (FileInputStream fis = new FileInputStream(file);
                 InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(isr)) {

                char[] buffer = new char[4096];
                int read;
                while ((read = reader.read(buffer)) != -1) {
                    content.append(buffer, 0, read);
                    // Прерываем если файл слишком большой
                    if (content.length() > 100000) {
                        break;
                    }
                }
            } catch (Exception e) {
                //scip
            }
        });

        readThread.start();
        try {
            readThread.join(timeoutMs);
            if (readThread.isAlive()) {
                readThread.interrupt();
                return null;
            }
        } catch (InterruptedException e) {
            readThread.interrupt();
            return null;
        }

        return content.toString();
    }

    private static String processFast(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("package") && !trimmed.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        String output = result.toString();
        output = output.replaceAll("^\\s+", "");
        output = output.replaceAll("\\s+$", "");

        return output;
    }

    private static void printResults(List<FileData> fileDataList) {
        fileDataList.sort((f1, f2) -> {
            int sizeCompare = Integer.compare(f1.size, f2.size);
            return sizeCompare != 0 ? sizeCompare : f1.path.compareTo(f2.path);
        });

        for (FileData fileData : fileDataList) {
            System.out.println(fileData.size + " " + fileData.path);
        }
    }

    private static class FileData {
        String path;
        int size;

        FileData(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }
}