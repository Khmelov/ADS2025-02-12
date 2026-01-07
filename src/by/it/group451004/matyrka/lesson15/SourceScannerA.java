package by.it.group451004.matyrka.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileData> fileDataList = new ArrayList<>();

        try {
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().endsWith(".java")) {
                        processJavaFile(file, fileDataList);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ignored) {
        }

        fileDataList.sort((a, b) -> {
            int c = Integer.compare(a.size, b.size);
            return c != 0 ? c : a.relativePath.compareTo(b.relativePath);
        });

        for (FileData f : fileDataList) {
            System.out.println(f.size + " " + f.relativePath);
        }
    }

    private static void processJavaFile(Path file, List<FileData> list) {
        try {
            String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);

            if (content.contains("@Test") || content.contains("org.junit.Test")) return;

            String processed = processFileContent(content);

            String srcPath = System.getProperty("user.dir")
                    + File.separator + "src" + File.separator;

            String relativePath = file.toString().substring(srcPath.length());

            int size = processed.getBytes(StandardCharsets.UTF_8).length;

            list.add(new FileData(size, relativePath));

        } catch (MalformedInputException ignored) {
        } catch (IOException ignored) {
        }
    }

    private static String processFileContent(String content) {
        StringBuilder sb = new StringBuilder();

        for (String line : content.split("\n")) {
            String t = line.trim();
            if (t.startsWith("package") || t.startsWith("import")) continue;
            sb.append(line).append('\n');
        }

        String res = sb.toString();
        res = trimLowStart(res);
        res = trimLowEnd(res);
        return res;
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
