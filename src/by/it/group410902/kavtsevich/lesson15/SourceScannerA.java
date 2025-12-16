package by.it.group410902.kavtsevich.lesson15;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;



public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        List<FileData> files = new ArrayList<>();

        scanJavaFiles(new File(src), new File(src), files);

        files.sort((f1, f2) -> {
            int cmp = Integer.compare(f1.size, f2.size);
            return cmp != 0 ? cmp : f1.path.compareTo(f2.path);
        });

        for (FileData f : files) {
            System.out.println(f.size + " " + f.path);
        }
    }

    //Рекурсивный обход каталога
    private static void scanJavaFiles(File dir, File baseDir, List<FileData> result) {
        if (!dir.exists() || !dir.isDirectory()) return;
        File[] entries = dir.listFiles();
        if (entries == null) return;

        for (File f : entries) {
            if (f.isDirectory()) {
                scanJavaFiles(f, baseDir, result);
            } else if (f.getName().endsWith(".java")) {
                processFile(f, baseDir, result);
            }
        }
    }
    //Обработка отдельных файлов
    private static void processFile(File file, File baseDir, List<FileData> result) {
        try {
            String content = readFileSafe(file); //безопасное чтение файла с проверками
            if (content == null) return;

            if (content.contains("@Test") || content.contains("org.junit.Test")) return;

            String processed = removePackageImports(content);

            // Удаление символов <33 в начале и конце
            processed = trimLowChars(processed);

            int size = processed.getBytes(StandardCharsets.UTF_8).length;

            // Относительный путь
            String relativePath = baseDir.toPath().relativize(file.toPath()).toString();

            result.add(new FileData(relativePath, size));
        } catch (Exception ignored) {
        }
    }

    private static String readFileSafe(File file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            char[] buffer = new char[4096];
            StringBuilder sb = new StringBuilder();
            int n;
            while ((n = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, n);
            }
            return sb.toString();
        } catch (Exception e) {
            return null; // игнорируем ошибку
        }
    }

    private static String removePackageImports(String content) {
        StringBuilder sb = new StringBuilder();
        for (String line : content.split("\n")) { //разбиваем на строки
            String t = line.trim();
            if (!t.startsWith("package") && !t.startsWith("import")) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    private static String trimLowChars(String s) {
        int start = 0, end = s.length() - 1;
        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;
        return (start <= end) ? s.substring(start, end + 1) : "";
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
