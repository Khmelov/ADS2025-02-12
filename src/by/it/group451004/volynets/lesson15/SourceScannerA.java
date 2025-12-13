package by.it.group451004.volynets.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SourceScannerA {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<Result> results = new ArrayList<>();

        try {
            Files.walk(Paths.get(src))
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String text;
                            try {
                                text = Files.readString(path, Charset.defaultCharset());
                            } catch (MalformedInputException e) {
                                try {
                                    text = Files.readString(path, Charset.forName("ISO-8859-1"));
                                } catch (IOException ex) {
                                    return;
                                }
                            }

                            if (text.contains("@Test") || text.contains("org.junit.Test")) {
                                return;
                            }

                            StringBuilder sb = new StringBuilder();
                            for (String line : text.split("\n")) {
                                String trimmed = line.trim();
                                if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                                    continue;
                                }
                                sb.append(line).append("\n");
                            }

                            // Удаляем символы <33 в начале и конце
                            String cleaned = trimLowChars(sb.toString());

                            // Размер в байтах
                            int size = cleaned.getBytes(Charset.defaultCharset()).length;

                            // Относительный путь
                            String relPath = Paths.get(src).relativize(path).toString();

                            results.add(new Result(size, relPath));
                        } catch (IOException ignored) {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        results.sort(Comparator
                .comparingInt(Result::size)
                .thenComparing(Result::path));

        for (Result r : results) {
            System.out.println(r.size + " " + r.path);
        }
    }

    private static String trimLowChars(String text) {
        int start = 0;
        int end = text.length();

        while (start < end && text.charAt(start) < 33) {
            start++;
        }
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }
        return text.substring(start, end);
    }

    private record Result(int size, String path) {}
}
