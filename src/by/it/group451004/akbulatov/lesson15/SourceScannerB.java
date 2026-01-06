package by.it.group451004.akbulatov.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerB
{
    private static class FileResult
    {
        String relativePath;
        long processedSize;

        public FileResult(String relativePath, long processedSize)
        {
            this.relativePath = relativePath;
            this.processedSize = processedSize;
        }

        public long getProcessedSize() {
            return processedSize;
        }

        public String getRelativePath() {
            return relativePath;
        }
    }

    public static void main(String[] args)
    {
        String rootDir = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path startPath = Paths.get(rootDir);
        if (!Files.exists(startPath))
        {
            System.out.println("Каталог src не найден по пути: " + rootDir);
            return;
        }

        List<FileResult> results = new ArrayList<>();

        try
        {
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                {
                    if (file.toString().endsWith(".java"))
                        processFile(file, startPath, results);

                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        results.sort(Comparator.comparingLong(FileResult::getProcessedSize).thenComparing(FileResult::getRelativePath));

        for (FileResult res : results)
            System.out.println(res.processedSize + " " + res.relativePath);
    }

    private static void processFile(Path filePath, Path rootPath, List<FileResult> results)
    {
        String content;
        try
        {
            content = readFileSafe(filePath);
        }
        catch (IOException e)
        {
            System.err.println("Ошибка чтения файла: " + filePath);
            return;
        }

        if (content.contains("@Test") || content.contains("org.junit.Test"))
            return;

        content = content.replaceAll("(?m)^\\s*(package|import)\\s+.*;\\s*$", "");

        content = removeComments(content);

        content = content.trim();

        content = content.replaceAll("(?m)^\\s*[\r\n]+", "");

        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

        String relativePath = rootPath.relativize(filePath).toString();

        results.add(new FileResult(relativePath, bytes.length));
    }

    private static String readFileSafe(Path path) throws IOException
    {
        return Files.readString(path, StandardCharsets.UTF_8);
    }


    private static String removeComments(String code)
    {
        StringBuilder sb = new StringBuilder();
        boolean inString = false; // "..."
        boolean inChar = false;   // '.'
        boolean inBlockComment = false; // /* ... */
        boolean inLineComment = false;  // // ...

        char[] chars = code.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            char c = chars[i];
            char next = (i + 1 < chars.length) ? chars[i + 1] : '\0';

            if (inBlockComment) {
                if (c == '*' && next == '/')
                {
                    inBlockComment = false;
                    i++;
                }
            }
            else if (inLineComment)
            {
                if (c == '\n')
                {
                    inLineComment = false;
                    sb.append(c);
                }
            }
            else if (inString)
            {
                sb.append(c);
                if (c == '\\' && next == '"')
                {
                    sb.append(next);
                    i++;
                }
                else if (c == '"')
                {
                    inString = false;
                }
            }
            else if (inChar)
            {
                sb.append(c);
                if (c == '\\' && next == '\'')
                {
                    sb.append(next);
                    i++;
                }
                else if (c == '\'')
                {
                    inChar = false;
                }
            }
            else
            {
                if (c == '/' && next == '*')
                {
                    inBlockComment = true;
                    i++;
                }
                else if (c == '/' && next == '/')
                {
                    inLineComment = true;
                    i++;
                }
                else
                {
                    sb.append(c);
                    if (c == '"') inString = true;
                    else if (c == '\'') inChar = true;
                }
            }
        }
        return sb.toString();
    }
}