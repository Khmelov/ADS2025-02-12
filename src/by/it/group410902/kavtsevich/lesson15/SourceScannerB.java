package by.it.group410902.kavtsevich.lesson15;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        File rootDir = new File(src);

        List<FileData> results = new ArrayList<>();
        scanJavaFiles(rootDir, rootDir, results);

        // сортировка по размеру, потом по пути
        results.sort((a, b) -> {
            int cmp = Integer.compare(a.size, b.size);
            return cmp != 0 ? cmp : a.path.compareTo(b.path);
        });

        // вывод
        for (FileData f : results) {
            System.out.println(f.size + " " + f.path);
        }
    }

    //Рекурсивный обход каталога
    private static void scanJavaFiles(File dir, File baseDir, List<FileData> results) {
        if (!dir.exists() || !dir.isDirectory()) return;

        File[] files = dir.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isDirectory()) {
                scanJavaFiles(f, baseDir, results);
            } else if (f.getName().endsWith(".java")) {
                processFile(f, baseDir, results);
            }
        }
    }

    //Обработка отдельных файлов
    private static void processFile(File file, File baseDir, List<FileData> results) {
        try {
            String content = readFileSafe(file);
            if (content == null) return;

            if (content.contains("@Test") || content.contains("org.junit.Test")) return;

            String withoutPkgImp = removePackageAndImports(content);
            String noComments = removeComments(withoutPkgImp);

            // trim символов <33 и пустые строки
            String trimmed = trimLowAndRemoveEmptyLines(noComments);

            int size = trimmed.getBytes(StandardCharsets.UTF_8).length;
            String relativePath = baseDir.toPath().relativize(file.toPath()).toString();

            results.add(new FileData(relativePath, size));
        } catch (Exception ignored) {
        }
    }

    private static String readFileSafe(File file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[4096];
            int n;
            while ((n = reader.read(buffer)) != -1) sb.append(buffer, 0, n);
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private static String removePackageAndImports(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        String[] lines = s.split("\n");
        for (String line : lines) {
            String t = line.trim();
            if (!t.startsWith("package") && !t.startsWith("import")) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    private static String removeComments(String s) {
        StringBuilder out = new StringBuilder(s.length());
        int n = s.length(), i = 0;
        boolean inLine = false, inBlock = false;
        while (i < n) {
            char c = s.charAt(i);
            if (!inLine && !inBlock) {
                if (c == '/' && i + 1 < n && s.charAt(i + 1) == '/') { inLine = true; i += 2; continue; }
                if (c == '/' && i + 1 < n && s.charAt(i + 1) == '*') { inBlock = true; i += 2; continue; }
                out.append(c); i++;
            } else if (inLine) {
                if (c == '\n') { inLine = false; out.append('\n'); }
                i++;
            } else { // inBlock
                if (c == '*' && i + 1 < n && s.charAt(i + 1) == '/') { inBlock = false; i += 2; }
                else i++;
            }
        }
        return out.toString();
    }

    private static String trimLowAndRemoveEmptyLines(String s) {
        StringBuilder sb = new StringBuilder();
        String[] lines = s.split("\n");
        for (String line : lines) {
            int start = 0, end = line.length() - 1;
            while (start <= end && line.charAt(start) < 33) start++;
            while (end >= start && line.charAt(end) < 33) end--;
            if (start <= end) {
                String trimmedLine = line.substring(start, end + 1);
                if (!trimmedLine.isEmpty()) sb.append(trimmedLine).append("\n");
            }
        }
        return sb.toString();
    }

    private static class FileData {
        String path;
        int size;
        FileData(String path, int size)
        {
            this.path = path;
            this.size = size;
        }
    }
}

