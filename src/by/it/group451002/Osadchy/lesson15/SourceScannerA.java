package by.it.group451002.Osadchy.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerA {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Paths.get(src);

        if (!Files.exists(root)) return;

        List<FileEntry> entries = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(root)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            List<String> lines = Files.readAllLines(path);

                            for (String line : lines) {
                                if (line.contains("@Test") || line.contains("org.junit.Test")) return;
                            }

                            StringBuilder sb = new StringBuilder();
                            for (String line : lines) {
                                String t = line.trim();
                                if (!t.startsWith("package") && !t.startsWith("import")) {
                                    sb.append(line).append(System.lineSeparator());
                                }
                            }

                            String text = sb.toString().trim();
                            long size = text.getBytes().length;
                            entries.add(new FileEntry(size, root.relativize(path).toString()));

                        } catch (MalformedInputException e) {
                            //
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        entries.sort(Comparator.comparingLong((FileEntry f) -> f.size)
                .thenComparing(f -> f.path));

        for (FileEntry entry : entries) {
            System.out.println(entry.size + " " + entry.path);
        }
    }

    private static class FileEntry {
        long size;
        String path;

        FileEntry(long size, String path) {
            this.size = size;
            this.path = path;
        }
    }
}