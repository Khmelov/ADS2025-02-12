package by.it.group451003.chveikonstantcin.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerC {

    public static void main(String[] args) {
        String srcRoot = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(srcRoot);

        List<FileNode> processedFiles = new ArrayList<>();
        if (Files.exists(srcPath)) {
            try (Stream<Path> walk = Files.walk(srcPath)) {
                walk.filter(p -> !Files.isDirectory(p) && p.toString().endsWith(".java"))
                        .forEach(path -> {
                            try {
                                String content = Files.readString(path, StandardCharsets.UTF_8);
                                if (!content.contains("@Test") && !content.contains("org.junit.Test")) {
                                    processAndAdd(path, content, srcPath, processedFiles);
                                }
                            } catch (MalformedInputException e) {
                                try {
                                    String content = new String(Files.readAllBytes(path), StandardCharsets.ISO_8859_1);
                                    if (!content.contains("@Test") && !content.contains("org.junit.Test")) {
                                        processAndAdd(path, content, srcPath, processedFiles);
                                    }
                                } catch (IOException ex) { }
                            } catch (IOException e) { }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(processedFiles, Comparator.comparing(o -> o.path));

        Map<String, List<String>> results = new TreeMap<>();
        int size = processedFiles.size();

        for (int i = 0; i < size; i++) {
            FileNode f1 = processedFiles.get(i);
            for (int j = i + 1; j < size; j++) {
                FileNode f2 = processedFiles.get(j);

                if (Math.abs(f1.content.length - f2.content.length) >= 10) continue;

                int diff = 0;
                for (int k = 0; k < 128; k++) {
                    diff += Math.abs(f1.charCounts[k] - f2.charCounts[k]);
                    if (diff > 18) break;
                }
                if (diff > 18) continue;

                if (checkDistance(f1.content, f2.content)) {
                    results.computeIfAbsent(f1.path, k -> new ArrayList<>()).add(f2.path);
                    results.computeIfAbsent(f2.path, k -> new ArrayList<>()).add(f1.path);
                }
            }
        }

        for (Map.Entry<String, List<String>> entry : results.entrySet()) {
            System.out.println(entry.getKey());
            List<String> copies = entry.getValue();
            Collections.sort(copies);
            for (String copy : copies) {
                System.out.println(copy);
            }
        }
    }

    private static void processAndAdd(Path path, String content, Path srcPath, List<FileNode> list) {
        content = content.replaceAll("(?m)^\\s*(package|import)\\s+.*;\\s*$", "");
        content = removeComments(content);
        char[] clean = cleanContent(content);
        if (clean.length > 0) {
            list.add(new FileNode(srcPath.relativize(path).toString(), clean));
        }
    }

    private static char[] cleanContent(String content) {
        StringBuilder sb = new StringBuilder(content.length());
        boolean space = false;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c < 33) {
                if (!space) {
                    sb.append(' ');
                    space = true;
                }
            } else {
                sb.append(c);
                space = false;
            }
        }
        return sb.toString().trim().toCharArray();
    }

    private static String removeComments(String code) {
        StringBuilder sb = new StringBuilder(code.length());
        boolean inStr = false, inCh = false, inBlk = false, inLn = false;
        int len = code.length();
        for (int i = 0; i < len; i++) {
            char c = code.charAt(i);
            char n = (i + 1 < len) ? code.charAt(i + 1) : 0;
            if (inBlk) {
                if (c == '*' && n == '/') { inBlk = false; i++; }
            } else if (inLn) {
                if (c == '\n') { inLn = false; sb.append(c); }
            } else if (inStr) {
                sb.append(c);
                if (c == '\\') { sb.append(n); i++; } else if (c == '"') inStr = false;
            } else if (inCh) {
                sb.append(c);
                if (c == '\\') { sb.append(n); i++; } else if (c == '\'') inCh = false;
            } else {
                if (c == '/' && n == '*') { inBlk = true; i++; }
                else if (c == '/' && n == '/') { inLn = true; i++; }
                else {
                    sb.append(c);
                    if (c == '"') inStr = true;
                    else if (c == '\'') inCh = true;
                }
            }
        }
        return sb.toString();
    }

    private static boolean checkDistance(char[] s1, char[] s2) {
        int n = s1.length, m = s2.length;
        int limit = 9;
        int[] p = new int[m + 1];
        int[] c = new int[m + 1];
        for (int j = 0; j <= m; j++) p[j] = j;

        for (int i = 1; i <= n; i++) {
            int start = (i > limit) ? i - limit : 1;
            int end = (i + limit < m) ? i + limit : m;

            Arrays.fill(c, 100);
            c[0] = i;

            int minRow = 100;
            for (int j = start; j <= end; j++) {
                int cost = (s1[i - 1] == s2[j - 1]) ? 0 : 1;
                int d = Math.min(Math.min(c[j - 1] + 1, p[j] + 1), p[j - 1] + cost);
                c[j] = d;
                if (d < minRow) minRow = d;
            }

            if (minRow > limit) return false;

            int[] t = p; p = c; c = t;
        }
        return p[m] <= limit;
    }

    static class FileNode {
        String path;
        char[] content;
        int[] charCounts;
        FileNode(String p, char[] c) {
            path = p; content = c;
            charCounts = new int[128];
            for (char ch : c) if (ch < 128) charCounts[ch]++;
        }
    }
}