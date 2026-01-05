package by.it.group451004.matyrka.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileData> list = new ArrayList<>();
        Path root = Paths.get(src);

        try {
            Files.walk(root)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> process(p, root, list));
        } catch (IOException ignored) {
        }

        list.sort((a, b) -> {
            int c = Integer.compare(a.size, b.size);
            return c != 0 ? c : a.relativePath.compareTo(b.relativePath);
        });

        for (FileData f : list) {
            System.out.println(f.size + " " + f.relativePath);
        }
    }

    private static void process(Path file, Path root, List<FileData> list) {
        try {
            String content = Files.readString(file, StandardCharsets.UTF_8);
            if (content.contains("@Test") || content.contains("org.junit.Test")) return;

            String processed = processText(content);

            String relativePath = root.relativize(file).toString();
            int size = processed.getBytes(StandardCharsets.UTF_8).length;

            list.add(new FileData(size, relativePath));

        } catch (MalformedInputException ignored) {
        } catch (IOException ignored) {
        }
    }

    private static String processText(String text) {
        text = removeComments(text);

        StringBuilder sb = new StringBuilder();
        for (String line : text.split("\\R")) {
            String t = line.trim();
            if (t.startsWith("package") || t.startsWith("import")) continue;

            line = trimLowStart(line);
            line = trimLowEnd(line);
            if (!line.isEmpty()) sb.append(line).append('\n');
        }

        String res = sb.toString();
        res = trimLowStart(res);
        res = trimLowEnd(res);
        return res;
    }

    private static String removeComments(String s) {
        StringBuilder r = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (i + 1 < s.length() && s.charAt(i) == '/' && s.charAt(i + 1) == '/') {
                while (i < s.length() && s.charAt(i) != '\n') i++;
            } else if (i + 1 < s.length() && s.charAt(i) == '/' && s.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < s.length() && !(s.charAt(i) == '*' && s.charAt(i + 1) == '/')) i++;
                i += 2;
            } else {
                r.append(s.charAt(i++));
            }
        }
        return r.toString();
    }

    private static String trimLowStart(String s) {
        int i = 0;
        while (i < s.length() && s.charAt(i) < 33) i++;
        return s.substring(i);
    }

    private static String trimLowEnd(String s) {
        int i = s.length();
        while (i > 0 && s.charAt(i - 1) < 33) i--;
        return s.substring(0, i);
    }

    private static class FileData {
        int size;
        String relativePath;

        FileData(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}
