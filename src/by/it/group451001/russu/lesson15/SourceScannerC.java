package by.it.group451001.russu.lesson15;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileInfo> files = new ArrayList<>();

        try {
            Files.walk(Paths.get(src))
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
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

                        String normalized = normalizeLowChars(noComments);

                        String processed = normalized.trim();

                        files.add(new FileInfo(processed, path.toString().substring(src.length())));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, List<String>> copies = new TreeMap<>();
        for (int i = 0; i < files.size(); i++) {
            for (int j = i + 1; j < files.size(); j++) {
                int dist = levenshtein(files.get(i).text, files.get(j).text, 10);
                if (dist >= 0 && dist < 10) {
                    copies.computeIfAbsent(files.get(i).path, k -> new ArrayList<>()).add(files.get(j).path);
                    copies.computeIfAbsent(files.get(j).path, k -> new ArrayList<>()).add(files.get(i).path);
                }
            }
        }

        for (Map.Entry<String, List<String>> entry : copies.entrySet()) {
            System.out.println(entry.getKey());
            entry.getValue().stream().sorted().forEach(System.out::println);
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

    private static String normalizeLowChars(String text) {
        StringBuilder sb = new StringBuilder();
        boolean inSeq = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!inSeq) {
                    sb.append(' ');
                    inSeq = true;
                }
            } else {
                sb.append(c);
                inSeq = false;
            }
        }
        return sb.toString();
    }

    private static int levenshtein(String a, String b, int limit) {
        int n = a.length(), m = b.length();
        if (Math.abs(n - m) >= limit) return -1;

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) prev[j] = j;

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            int minInRow = curr[0];
            for (int j = 1; j <= m; j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(Math.min(prev[j] + 1, curr[j - 1] + 1), prev[j - 1] + cost);
                minInRow = Math.min(minInRow, curr[j]);
            }
            if (minInRow >= limit) return -1;
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[m];
    }

    private static class FileInfo {
        String text;
        String path;
        FileInfo(String text, String path) {
            this.text = text;
            this.path = path;
        }
    }
}
