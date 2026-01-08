package by.it.group451001.drzhevetskiy.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        Path srcRoot = Paths.get(System.getProperty("user.dir"), "src");
        ArrayList<Unit> units = new ArrayList<>();

        try {
            Files.walk(srcRoot)
                    .filter(SourceScannerC::javaFile)
                    .forEach(p -> readAndProcess(srcRoot, p, units));
        } catch (IOException e) {
            System.err.println("Ошибка чтения директорий");
            return;
        }

        Map<Integer, List<Unit>> buckets = new HashMap<>();
        for (Unit u : units) {
            buckets.computeIfAbsent(u.hash, k -> new ArrayList<>()).add(u);
        }

        ArrayList<String> output = new ArrayList<>();
        boolean duplicatesExist = buckets.values().stream().anyMatch(v -> v.size() > 1);

        for (List<Unit> group : buckets.values()) {
            if (group.size() > 1 || !duplicatesExist) {
                for (Unit u : group) {
                    output.add(u.name);
                }
            }
        }

        Collections.sort(output);
        output.forEach(System.out::println);
    }

    // -------- file reading --------

    private static boolean javaFile(Path p) {
        return Files.isRegularFile(p) && p.toString().endsWith(".java");
    }

    private static void readAndProcess(Path root, Path file, List<Unit> list) {
        try {
            String text = Files.readString(file, StandardCharsets.UTF_8);
            if (containsTests(text)) return;

            String normalized = normalize(text);
            int hash = normalized.hashCode();
            String name = extractName(root.relativize(file).toString());

            list.add(new Unit(name, hash));

        } catch (IOException ignored) {
        }
    }

    // -------- text normalization --------

    private static boolean containsTests(String s) {
        return s.contains("@Test") || s.contains("org.junit.Test");
    }

    private static String normalize(String src) {
        src = dropServiceLines(src);
        src = stripComments(src);
        src = squashWhitespace(src);
        return src.trim();
    }

    private static String dropServiceLines(String src) {
        StringBuilder sb = new StringBuilder();
        for (String line : src.split("\n")) {
            String t = line.trim();
            if (!t.startsWith("package") && !t.startsWith("import")) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    private static String stripComments(String src) {
        StringBuilder out = new StringBuilder();
        boolean block = false, line = false, str = false, chr = false;

        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            char n = (i + 1 < src.length()) ? src.charAt(i + 1) : 0;

            if (block) {
                if (c == '*' && n == '/') { block = false; i++; }
                continue;
            }
            if (line) {
                if (c == '\n') { line = false; out.append(c); }
                continue;
            }
            if (str) {
                out.append(c);
                if (c == '\\' && n == '"') { out.append(n); i++; }
                else if (c == '"') str = false;
                continue;
            }
            if (chr) {
                out.append(c);
                if (c == '\\' && n == '\'') { out.append(n); i++; }
                else if (c == '\'') chr = false;
                continue;
            }

            if (c == '"' ) { str = true; out.append(c); }
            else if (c == '\'') { chr = true; out.append(c); }
            else if (c == '/' && n == '*') { block = true; i++; }
            else if (c == '/' && n == '/') { line = true; i++; }
            else out.append(c);
        }
        return out.toString();
    }

    private static String squashWhitespace(String s) {
        StringBuilder sb = new StringBuilder();
        boolean space = false;

        for (char c : s.toCharArray()) {
            if (c <= 32) {
                if (!space) {
                    sb.append(' ');
                    space = true;
                }
            } else {
                sb.append(c);
                space = false;
            }
        }
        return sb.toString();
    }

    // -------- helpers --------

    private static String extractName(String path) {
        int i = path.lastIndexOf(File.separatorChar);
        return (i >= 0) ? path.substring(i + 1) : path;
    }

    private static class Unit {
        String name;
        int hash;

        Unit(String n, int h) {
            name = n;
            hash = h;
        }
    }
}
