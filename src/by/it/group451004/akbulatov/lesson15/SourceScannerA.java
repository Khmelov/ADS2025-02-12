package by.it.group451004.akbulatov.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerA
{
    private static class FileResult
    {
        String relativePath;
        long sizeBytes;

        public FileResult(String relativePath, long sizeBytes)
        {
            this.relativePath = relativePath;
            this.sizeBytes = sizeBytes;
        }
    }

    public static void main(String[] args)
    {

        String rootDir = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path rootPath = Paths.get(rootDir);

//        if (!Files.exists(rootPath)) {
//            System.err.println("Каталог src не найден по пути: " + rootDir);
//            return;
//        }

        List<FileResult> results = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(rootPath))
        {
            paths.filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".java")).forEach(path ->
            {
                try
                {
                    processFile(path, rootPath, results);
                }
                catch (Exception e)
                {
                    System.err.println("Ошибка при чтении файла " + path + ": " + e.getMessage());
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        results.sort(Comparator.comparingLong((FileResult r) -> r.sizeBytes).thenComparing(r -> r.relativePath));

        for (FileResult res : results)
            System.out.println(res.sizeBytes + " " + res.relativePath);
    }

    private static void processFile(Path path, Path root, List<FileResult> results)
    {
        List<String> lines;
        try
        {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            return;
        }

        if (lines.stream().anyMatch(line -> line.contains("@Test") || line.contains("org.junit.Test")))
            return;

        StringBuilder sb = new StringBuilder();

        for (String line : lines)
        {
            String trimmedLine = line.trim();
            if (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import "))
                continue;

            sb.append(line).append(System.lineSeparator());
        }

        String processedText = sb.toString().trim();

        byte[] bytes = processedText.getBytes(StandardCharsets.UTF_8);

        String relativePath = root.relativize(path).toString();

        results.add(new FileResult(relativePath, bytes.length));
    }
}