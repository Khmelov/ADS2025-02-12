package by.it.group451001.sobol.lesson15;

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

public class SourceScannerB {
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
                            List<String> rawLines = Files.readAllLines(path);
                            String fullText = String.join("\n", rawLines);

                            if (fullText.contains("@Test") || fullText.contains("org.junit.Test")) return;

                            // Удаление комментариев (// и /* */)
                            String noComments = fullText.replaceAll("//.*|/\\*[\\s\\S]*?\\*/", "");

                            StringBuilder sb = new StringBuilder();
                            for (String line : noComments.split("\n")) {
                                String trimmed = line.trim();
                                if (trimmed.isEmpty()) continue;
                                if (trimmed.startsWith("package") || trimmed.startsWith("import")) continue;

                                sb.append(line).append(System.lineSeparator());
                            }

                            String resultText = sb.toString().trim();

                            entries.add(new FileEntry(resultText.getBytes().length, root.relativize(path).toString()));

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