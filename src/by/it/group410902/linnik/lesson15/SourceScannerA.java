package by.it.group410902.linnik.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) {
        String srcPath = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        File srcDir = new File(srcPath);

        if (!srcDir.exists() || !srcDir.isDirectory()) {
            return;
        }

        List<FileInfo> fileInfos = new ArrayList<>();
        scanDirectory(srcDir, srcPath, fileInfos);

        Collections.sort(fileInfos, (a, b) -> {
            if (a.size != b.size) {
                return Integer.compare(a.size, b.size);
            }
            return a.relativePath.compareTo(b.relativePath);
        });

        for (FileInfo info : fileInfos) {
            System.out.println(info.size + " " + info.relativePath);
        }
    }

    private static void scanDirectory(File dir, String basePath, List<FileInfo> fileInfos) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, basePath, fileInfos);
            } else if (file.getName().endsWith(".java")) {
                processFile(file, basePath, fileInfos);
            }
        }
    }

    private static void processFile(File file, String basePath, List<FileInfo> fileInfos) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), getCharset(file)))) {

            StringBuilder content = new StringBuilder();
            String line;
            boolean skipMode = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains("@Test") || line.contains("org.junit.Test")) {
                    return;
                }

                String trimmed = line.trim();
                if (trimmed.startsWith("package") || trimmed.startsWith("import")) {
                    skipMode = true;
                    continue;
                }
                if (skipMode && trimmed.endsWith(";")) {
                    skipMode = false;
                    continue;
                }

                if (!skipMode) {
                    content.append(line).append("\n");
                }
            }
            String processed = trimControlChars(content.toString());
            int size = processed.getBytes(StandardCharsets.UTF_8).length;
            String relativePath = file.getPath().substring(basePath.length());
            fileInfos.add(new FileInfo(size, relativePath));

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла " + file.getPath() + ": " + e.getMessage());
        }
    }

    private static Charset getCharset(File file) {
        String[] charsets = {"UTF-8", "Windows-1251", "ISO-8859-1", "CP1251"};

        for (String charsetName : charsets) {
            try (InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file), charsetName)) {
                char[] buffer = new char[1024];
                reader.read(buffer);
                return Charset.forName(charsetName);
            } catch (Exception e) {
            }
        }

        return StandardCharsets.UTF_8;
    }

    private static String trimControlChars(String str) {
        if (str == null || str.isEmpty()) return "";

        int start = 0;
        int end = str.length() - 1;
        while (start <= end && str.charAt(start) < 33) {
            start++;
        }
        while (end >= start && str.charAt(end) < 33) {
            end--;
        }

        if (start > end) return "";
        return str.substring(start, end + 1);
    }

    static class FileInfo {
        int size;
        String relativePath;

        FileInfo(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}
