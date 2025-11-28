package by.it.group410902.dziatko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) {
        Path srcPath = Paths.get(System.getProperty("user.dir"), "src");


        List<Result> results = new ArrayList<>();

        try {
            Files.walk(srcPath)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String content = readFileSafe(path);

                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            String processed = processText(content);

                            int size = processed.getBytes(Charset.defaultCharset()).length;

                            String relative = srcPath.relativize(path.toAbsolutePath()).toString();

                            results.add(new Result(size, relative));

                        } catch (IOException e) {
                            System.err.println("Ошибка чтения файла: " + path + " -> " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка обхода каталога: " + e.getMessage());
        }

        results.sort(Comparator.comparingInt(Result::size)
                .thenComparing(Result::path));

        for (Result r : results) {
            System.out.println(r.size + " байт\t" + r.path);
        }
    }

    private static String readFileSafe(Path path) throws IOException {
        try {
            return Files.readString(path);
        } catch (MalformedInputException e) {
            return Files.readString(path, Charset.forName("ISO-8859-1"));
        }
    }

    private static String processText(String text) {
        StringBuilder sb = new StringBuilder();
        boolean inBlockComment = false;

        for (String line : text.split("\n")) {
            String trimmed = line.trim();

            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue;
            }

            if (inBlockComment) {
                if (trimmed.contains("*/")) {
                    inBlockComment = false;
                }
                continue;
            }
            if (trimmed.startsWith("/*")) {
                inBlockComment = true;
                continue;
            }
            if (trimmed.startsWith("//")) {
                continue;
            }

            trimmed = trimmed.replaceAll("^[\\x00-\\x20]+", "");
            trimmed = trimmed.replaceAll("[\\x00-\\x20]+$", "");

            if (trimmed.isEmpty()) continue;

            sb.append(trimmed).append("\n");
        }

        return sb.toString();
    }

    private static class Result {
        int size;
        String path;

        Result(int size, String path) {
            this.size = size;
            this.path = path;
        }

        int size() { return size; }
        String path() { return path; }
    }
}
