package by.it.group410901.evtuhovskaya.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Paths.get(src);

        List<FileInfo> list = new ArrayList<>();

        try {
            Files.walk(root)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> processFile(path, root, list));
        } catch (IOException ignored) {}

        list.sort(Comparator
                .comparing(FileInfo::size)
                .thenComparing(FileInfo::relativePath));

        for (FileInfo fi : list) {
            System.out.println(fi.size() + " " + fi.relativePath());
        }
    }

    private static void processFile(Path file, Path root, List<FileInfo> list) {
        String text;

        try {
            text = Files.readString(file, Charset.defaultCharset());
        } catch (MalformedInputException e) {
            return;
        } catch (IOException e) {
            return;
        }

        if (text.contains("@Test") || text.contains("org.junit.Test"))
            return;

        String cleaned = cleanText(text);

        cleaned = trimLowChars(cleaned);
        cleaned = removeEmptyLines(cleaned);

        int size = cleaned.getBytes().length;
        String relative = root.relativize(file).toString();

        list.add(new FileInfo(relative, size));
    }

    private static String cleanText(String text) {
        StringBuilder out = new StringBuilder(text.length());

        boolean inBlockComment = false;
        boolean inLineComment = false;

        try (BufferedReader br = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (!inBlockComment && (trimmed.startsWith("package ") || trimmed.startsWith("import ")))
                    continue;

                StringBuilder sb = new StringBuilder();
                int i = 0;
                while (i < line.length()) {
                    if (!inLineComment && !inBlockComment &&
                            i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                        break;
                    }
                    if (!inLineComment && !inBlockComment &&
                            i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                        inBlockComment = true;
                        i += 2;
                        continue;
                    }
                    if (inBlockComment &&
                            i + 1 < line.length() && line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                        inBlockComment = false;
                        i += 2;
                        continue;
                    }
                    if (!inBlockComment) {
                        sb.append(line.charAt(i));
                    }
                    i++;
                }
                inLineComment = false;
                if (!inBlockComment) {
                    out.append(sb).append("\n");
                }
            }
        } catch (IOException ignored) {}

        return out.toString();
    }

    private static String trimLowChars(String s) {
        int start = 0, end = s.length() - 1;
        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;
        if (start > end) return "";
        return s.substring(start, end + 1);
    }

    private static String removeEmptyLines(String s) {
        StringBuilder out = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new StringReader(s))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    out.append(line).append("\n");
                }
            }
        } catch (IOException ignored) {}
        return out.toString();
    }

    record FileInfo(String relativePath, int size) {}
}