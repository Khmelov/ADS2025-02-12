package by.it.group451001.russu.lesson15;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileInfo> results = new ArrayList<>();

        try {
            Files.walk(Paths.get(src))
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String content = readFileSafe(path);

                            if (content == null) return;

                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            StringBuilder sb = new StringBuilder();
                            for (String line : content.split("\n")) {
                                String trimmed = line.trim();
                                if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                                    continue;
                                }
                                sb.append(line).append("\n");
                            }

                            String noComments = removeComments(sb.toString());

                            String trimmed = trimLowChars(noComments);

                            StringBuilder finalText = new StringBuilder();
                            for (String line : trimmed.split("\n")) {
                                if (!line.trim().isEmpty()) {
                                    finalText.append(line).append("\n");
                                }
                            }

                            String processed = finalText.toString();

                            int size = processed.getBytes(Charset.defaultCharset()).length;

                            // Относительный пут
                            String relPath = path.toString().substring(src.length());

                            results.add(new FileInfo(size, relPath));

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        results.sort(Comparator
                .comparingInt(FileInfo::size)
                .thenComparing(FileInfo::path));

        for (FileInfo fi : results) {
            System.out.println(fi.size + " " + fi.path);
        }
    }

    private static String readFileSafe(Path path) {
        try {
            return Files.readString(path, Charset.defaultCharset());
        } catch (MalformedInputException e) {
            try {
                return Files.readString(path, Charset.forName("ISO-8859-1"));
            } catch (IOException ex) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder();
        boolean inBlock = false;
        for (int i = 0; i < text.length(); i++) {
            if (!inBlock && i + 1 < text.length() && text.charAt(i) == '/' && text.charAt(i + 1) == '/') {

                while (i < text.length() && text.charAt(i) != '\n') i++;
            } else if (!inBlock && i + 1 < text.length() && text.charAt(i) == '/' && text.charAt(i + 1) == '*') {

                inBlock = true;
                i++;
            } else if (inBlock && i + 1 < text.length() && text.charAt(i) == '*' && text.charAt(i + 1) == '/') {

                inBlock = false;
                i++;
            } else if (!inBlock) {
                sb.append(text.charAt(i));
            }
        }
        return sb.toString();
    }

    private static String trimLowChars(String text) {
        int start = 0;
        int end = text.length();
        while (start < end && text.charAt(start) < 33) start++;
        while (end > start && text.charAt(end - 1) < 33) end--;
        return text.substring(start, end);
    }

    private static class FileInfo {
        int size;
        String path;

        FileInfo(int size, String path) {
            this.size = size;
            this.path = path;
        }

        int size() { return size; }
        String path() { return path; }
    }
}
