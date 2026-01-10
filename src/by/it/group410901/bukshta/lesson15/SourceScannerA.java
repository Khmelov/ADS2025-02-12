package by.it.group410901.bukshta.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Paths.get(src);
        if (!Files.exists(root)) {
            System.err.println("Каталог src не найден: " + root);
            return;
        }

        List<Result> results = new ArrayList<>();

        try (var stream = Files.walk(root)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> processFile(p, root, results));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортировка: сначала по размеру, потом по пути
        results.sort(Comparator.comparingLong((Result r) -> r.size)
                .thenComparing(r -> r.relPath));

        // Вывод результатов
        for (Result r : results) {
            System.out.println(r.size + " " + r.relPath);
        }
    }

    private static void processFile(Path file, Path root, List<Result> results) {
        String text = readFileSafe(file, Charset.forName("UTF-8"));
        if (text == null) return;

        // Удаление package и import
        String processed = removePackageAndImports(text);

        // Удаление символов <33 по краям
        processed = trimLowAscii(processed);

        long size = processed.getBytes(Charset.forName("UTF-8")).length;

        // Относительный путь без замены слешей
        String rel = root.relativize(file).toString();

        results.add(new Result(rel, size));
    }

    private static String readFileSafe(Path file, Charset cs) {
        try {
            return Files.readString(file, cs);
        } catch (MalformedInputException e) {
            try {
                return Files.readString(file, Charset.forName("ISO-8859-1"));
            } catch (IOException ex) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    private static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        try (BufferedReader br = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = br.readLine()) != null) {
                String t = line.stripLeading();
                if (t.startsWith("package ") || t.startsWith("import ")) continue;
                sb.append(line).append('\n');
            }
        } catch (IOException ignored) {}
        return sb.toString();
    }

    private static String trimLowAscii(String s) {
        int start = 0, end = s.length() - 1;
        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;
        return start > end ? "" : s.substring(start, end + 1);
    }

    private record Result(String relPath, long size) {}
}
