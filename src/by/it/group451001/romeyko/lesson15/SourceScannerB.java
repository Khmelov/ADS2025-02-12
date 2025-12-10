package by.it.group451001.romeyko.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class SourceScannerB {
    static class FileInfo {
        final int size;
        final String path;

        FileInfo(int size, String path) {
            this.size = size;
            this.path = path;
        }

        public int size() { return size; }
        public String path() { return path; }
    }

    public static void main(String[] args) throws IOException {
        String srcRoot = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Paths.get(srcRoot);

        if (!Files.exists(root)) {
            System.err.println("Каталог src не найден: " + root);
            return;
        }

        List<Path> javaFiles;
        try (var s = Files.walk(root)) {
            javaFiles = s.filter(p -> p.toString().endsWith(".java")).collect(Collectors.toList());
        }

        List<FileInfo> results = new ArrayList<>();
        for (Path file : javaFiles) {
            String text;
            try {
                text = Files.readString(file, StandardCharsets.UTF_8);
            } catch (MalformedInputException e) {
                continue;
            } catch (IOException e) {
                continue;
            }

            // пропускаем тестовые файлы
            if (text.contains("@Test") || text.contains("org.junit.Test")) {
                continue;
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new StringReader(text))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String trim = line.trim();
                    if (trim.startsWith("package ")) continue;
                    if (trim.startsWith("import ")) continue;
                    sb.append(line).append('\n');
                }
            }
            String withoutPackageImports = sb.toString();

            String noComments = stripComments(withoutPackageImports);
            String cleaned = trimLowAscii(noComments);
            cleaned = removeEmptyLines(cleaned);

            int sizeBytes = cleaned.getBytes(StandardCharsets.UTF_8).length;
            String relPath = root.relativize(file).toString();
            results.add(new FileInfo(sizeBytes, relPath));
        }

        results.sort(Comparator.comparing(FileInfo::size).thenComparing(FileInfo::path));
        for (FileInfo fi : results) {
            System.out.println(fi.size + "  " + fi.path);
        }
    }


    public static String stripComments(String text) {
        StringBuilder out = new StringBuilder();
        int n = text.length();

        enum State { NORMAL, SLASH, LINE_COMMENT, BLOCK_COMMENT, BLOCK_COMMENT_STAR }
        State st = State.NORMAL;
        for (int i = 0; i < n; i++) {
            char c = text.charAt(i);
            switch (st) {
                case NORMAL:
                    if (c == '/') {
                        st = State.SLASH;
                    } else {
                        out.append(c);
                    }
                    break;

                case SLASH:
                    if (c == '/') {
                        st = State.LINE_COMMENT;
                    } else if (c == '*') {
                        st = State.BLOCK_COMMENT;
                    } else {
                        out.append('/');
                        out.append(c);
                        st = State.NORMAL;
                    }
                    break;

                case LINE_COMMENT:
                    if (c == '\n') {
                        out.append('\n');
                        st = State.NORMAL;
                    }
                    break;

                case BLOCK_COMMENT:
                    if (c == '*') {
                        st = State.BLOCK_COMMENT_STAR;
                    }
                    break;

                case BLOCK_COMMENT_STAR:
                    if (c == '/') {
                        st = State.NORMAL;
                    } else if (c == '*') {
                    } else {
                        st = State.BLOCK_COMMENT;
                    }
                    break;
            }
        }

        if (st == State.SLASH) {
            out.append('/');
        }

        return out.toString();
    }

    private static String trimLowAscii(String s) {
        int start = 0;
        int end = s.length();
        while (start < end && s.charAt(start) < 33) start++;
        while (end > start && s.charAt(end - 1) < 33) end--;
        return s.substring(start, end);
    }

    private static String removeEmptyLines(String s) throws IOException {
        StringBuilder out = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new StringReader(s))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    out.append(line).append('\n');
                }
            }
        }
        return out.toString();
    }
}

