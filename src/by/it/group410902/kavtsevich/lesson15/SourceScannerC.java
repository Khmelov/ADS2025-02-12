package by.it.group410902.kavtsevich.lesson15;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        File root = new File(src);

        List<FileData> files = new ArrayList<>();
        scanJavaFiles(root, root, files);

        // Группировка по "быстрому ключу" = длина + хеш текста
        Map<Long, List<FileData>> hashBuckets = new HashMap<>();
        for (FileData f : files) {
            long key = f.text.length();
            key = key * 31 + f.text.hashCode();
            hashBuckets.computeIfAbsent(key, k -> new ArrayList<>()).add(f);
        }

        // Словарь для вывода копий: путь -> список копий
        Map<String, List<String>> copiesMap = new TreeMap<>();

        for (List<FileData> bucket : hashBuckets.values()) {
            int n = bucket.size();
            for (int i = 0; i < n; i++) {
                FileData f1 = bucket.get(i);
                List<String> copies = new ArrayList<>();
                for (int j = i + 1; j < n; j++) {
                    FileData f2 = bucket.get(j);
                    if (f1.text.equals(f2.text)) {
                        copies.add(f2.path);
                    }
                }
                if (!copies.isEmpty()) copiesMap.put(f1.path, copies);
            }
        }

        // Вывод результатов
        for (Map.Entry<String, List<String>> entry : copiesMap.entrySet()) {
            System.out.println(entry.getKey());
            for (String c : entry.getValue()) System.out.println(c);
        }
    }

    private static void scanJavaFiles(File dir, File baseDir, List<FileData> results) {
        if (!dir.exists() || !dir.isDirectory()) return;
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isDirectory()) scanJavaFiles(f, baseDir, results);
            else if (f.getName().endsWith(".java")) processFile(f, baseDir, results);
        }
    }

    private static void processFile(File file, File baseDir, List<FileData> results) {
        try {
            String content = readFileSafe(file);
            if (content == null) return;
            if (content.contains("@Test") || content.contains("org.junit.Test")) return;

            String text = removePackageAndImports(content);
            text = removeComments(text);
            text = replaceLowCharsWithSpace(text).trim();

            String relativePath = baseDir.toPath().relativize(file.toPath()).toString();
            results.add(new FileData(relativePath, text));
        } catch (Exception ignored) {}
    }

    private static String readFileSafe(File file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[4096];
            int n;
            while ((n = reader.read(buf)) != -1) sb.append(buf, 0, n);
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private static String removePackageAndImports(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (String line : s.split("\n")) {
            String t = line.trim();
            if (!t.startsWith("package") && !t.startsWith("import")) sb.append(line).append("\n");
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
            } else {
                if (c == '*' && i + 1 < n && s.charAt(i + 1) == '/') { inBlock = false; i += 2; }
                else i++;
            }
        }
        return out.toString();
    }

    private static String replaceLowCharsWithSpace(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (char c : s.toCharArray()) sb.append(c < 33 ? ' ' : c);
        return sb.toString();
    }

    private static class FileData {
        String path;
        String text;
        FileData(String path, String text)
        {
            this.path = path;
            this.text = text;
        }
    }
}
