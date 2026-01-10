package by.it.group451004.akbulatov.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerC
{
    private static class FileData
    {
        Path path;
        String content;
        int length;

        public FileData(Path path, String content)
        {
            this.path = path;
            this.content = content;
            this.length = content.length();
        }
    }

    public static void main(String[] args)
    {
        String rootDir = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path start = Paths.get(rootDir);
        if (!Files.exists(start)) return;

        try {
            List<FileData> allFiles;
            try (Stream<Path> pathStream = Files.walk(start))
            {
                allFiles = pathStream.filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".java")).parallel()
                        .map(SourceScannerC::processFile).filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }

            allFiles.sort(Comparator.comparingInt(f -> f.length));

           Map<Path, List<Path>> results = new HashMap<>();

            for (int i = 0; i < allFiles.size(); i++)
            {
                FileData f1 = allFiles.get(i);

                for (int j = i + 1; j < allFiles.size(); j++)
                {
                    FileData f2 = allFiles.get(j);

                    if (f2.length - f1.length >= 10)
                        break;

                    boolean isCopy = false;
                    if (f1.length == f2.length && f1.content.equals(f2.content))
                        isCopy = true;
                    else
                    {
                        if (isLevenshteinLow(f1.content, f2.content, 10))
                            isCopy = true;
                    }

                    if (isCopy)
                    {
                        results.computeIfAbsent(f1.path, k -> new ArrayList<>()).add(f2.path);
                        results.computeIfAbsent(f2.path, k -> new ArrayList<>()).add(f1.path);
                    }
                }
            }

            List<Path> sortedKeys = new ArrayList<>(results.keySet());
            sortedKeys.sort(Comparator.comparing(Path::toString));

            for (Path originalPath : sortedKeys)
            {
                System.out.println(originalPath);
                List<Path> copies = results.get(originalPath);
                copies.sort(Comparator.comparing(Path::toString));
                for (Path copyPath : copies)
                    System.out.println(copyPath);
            }

        } catch (IOException e) {}
    }

    private static FileData processFile(Path path)
    {
        try
        {
            String content;
            try
            {
                content = Files.readString(path, StandardCharsets.UTF_8);
            }
            catch (MalformedInputException e)
            {
                return null;
            }

            if (content.contains("@Test") || content.contains("org.junit.Test"))
                return null;

            String cleaned = fastClean(content);

            return new FileData(path, cleaned);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private static String fastClean(String source)
    {
        StringBuilder sb = new StringBuilder(source.length());
        char[] chars = source.toCharArray();
        int len = chars.length;

        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean lastWasSpace = true;

        int startIdx = 0;

        int i = 0;

        while (i < len)
        {
            int lineStart = i;
            while (i < len && chars[i] <= ' ') i++;

            if (startsWith(chars, i, "package") || startsWith(chars, i, "import"))
            {
                while (i < len && chars[i] != ';' && chars[i] != '\n') i++;
                if (i < len) i++;
            }
            else
            {
                i = lineStart;
                break;
            }
        }
        startIdx = i;

        for (i = startIdx; i < len; i++)
        {
            char c = chars[i];

            if (inString)
            {
                if (c == '\\' && i + 1 < len)
                    sb.append(c).append(chars[++i]);
                else
                {
                    sb.append(c);
                    if (c == '"') inString = false;
                }
                continue;
            }

            if (inBlockComment)
            {
                if (c == '*' && i + 1 < len && chars[i + 1] == '/')
                {
                    inBlockComment = false;
                    i++;
                }
                continue;
            }

            if (inLineComment)
            {
               if (c == '\n')
               {
                    inLineComment = false;
                    if (!lastWasSpace)
                    {
                        sb.append(' ');
                        lastWasSpace = true;
                    }
                }
                continue;
            }

            if (c == '/' && i + 1 < len)
            {
                if (chars[i + 1] == '*')
                {
                    inBlockComment = true;
                    i++;
                    continue;
                }
                else if (chars[i + 1] == '/')
                {
                    inLineComment = true;
                    i++;
                    continue;
                }
            }

            if (c == '"')
            {
                inString = true;
                sb.append(c);
                lastWasSpace = false;
                continue;
            }

            if (c < 33)
            {
                if (!lastWasSpace)
                {
                    sb.append(' ');
                    lastWasSpace = true;
                }
            }
            else
            {
                sb.append(c);
                lastWasSpace = false;
            }
        }

        if (!sb.isEmpty() && sb.charAt(sb.length() - 1) == ' ')
            sb.setLength(sb.length() - 1);

        return sb.toString();
    }

    private static boolean startsWith(char[] chars, int idx, String prefix)
    {
        if (idx + prefix.length() > chars.length) return false;

        for (int i = 0; i < prefix.length(); i++)
            if (chars[idx + i] != prefix.charAt(i)) return false;

        return true;
    }

    private static boolean isLevenshteinLow(CharSequence s1, CharSequence s2, int limit)
    {
        int n = s1.length();
        int m = s2.length();

        if (Math.abs(n - m) >= limit) return false;

        int[] p = new int[n + 1];
        int[] d = new int[n + 1];
        int[] swap;

        for (int i = 0; i <= n; i++) p[i] = i;

        for (int j = 1; j <= m; j++)
        {
            char t_j = s2.charAt(j - 1);
            d[0] = j;

            int minRow = j;

            for (int i = 1; i <= n; i++)
            {
                int cost = (s1.charAt(i - 1) == t_j) ? 0 : 1;
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
                minRow = Math.min(minRow, d[i]);
            }

            if (minRow >= limit) return false;

            swap = p; p = d; d = swap;
        }

        return p[n] < limit;
    }
}