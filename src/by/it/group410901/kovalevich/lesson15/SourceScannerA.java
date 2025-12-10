package by.it.group410901.kovalevich.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Paths.get(src);

        List<FileInfo> list = new ArrayList<>();

        try {
            Files.walk(root)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> processFile(path, root, list));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        StringBuilder cleaned = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.startsWith("package ") || trimmed.startsWith("import "))
                    continue;
                cleaned.append(line).append("\n");
            }
        } catch (IOException ignored) {}

        String result = trimLowChars(cleaned.toString());

        int size = result.getBytes().length;
        String relative = root.relativize(file).toString();

        list.add(new FileInfo(relative, size));
    }

    private static String trimLowChars(String s) {
        int start = 0;
        int end = s.length() - 1;

        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end)   < 33) end--;

        if (start > end) return "";
        return s.substring(start, end + 1);
    }

    record FileInfo(String relativePath, int size) {}
}
