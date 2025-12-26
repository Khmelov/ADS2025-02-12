package by.it.group451003.sorokin.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.CRC32;

public class SourceScannerC {
    public static void main(String[] args) throws Exception {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Paths.get(src);

        List<Path> files = new ArrayList<>();
        Files.walkFileTree(root, new SimpleFileVisitor<>() {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().endsWith(".java"))
                    files.add(file);
                return FileVisitResult.CONTINUE;
            }
        });

        Map<Long, List<Path>> hashGroups = new HashMap<>();
        Map<Path, Path> fileToRootRel = new HashMap<>();
        Map<Path, String> processedTexts = new HashMap<>();

        for (Path p : files) {
            String text = readFileSkippingTests(p);
            if (text == null) continue;

            text = stripPackageAndImports(text);
            text = stripComments(text);
            text = normalizeText(text);

            processedTexts.put(p, text);
            long h = crc32(text);
            hashGroups.computeIfAbsent(h, k -> new ArrayList<>()).add(p);
            fileToRootRel.put(p, root.relativize(p));
        }

        // выводим все файлы + копии
        List<Path> allFiles = new ArrayList<>(processedTexts.keySet());
        allFiles.sort(Comparator.naturalOrder());

        for (Path f : allFiles) {
            System.out.println(fileToRootRel.get(f));
            long h = crc32(processedTexts.get(f));
            List<Path> group = hashGroups.get(h);
            if (group != null) {
                for (Path g : group) {
                    if (!g.equals(f))
                        System.out.println("  " + fileToRootRel.get(g));
                }
            }
        }
    }

    private static String readFileSkippingTests(Path p) {
        String txt = safeRead(p, StandardCharsets.UTF_8);
        if (txt == null) txt = safeRead(p, StandardCharsets.ISO_8859_1);
        if (txt == null) txt = safeRead(p, Charset.forName("Windows-1251"));
        if (txt == null) return null;
        if (txt.contains("@Test") || txt.contains("org.junit.Test")) return null;
        return txt;
    }

    private static String safeRead(Path p, Charset cs) {
        try { return Files.readString(p, cs); }
        catch (Exception e) { return null; }
    }

    private static String stripPackageAndImports(String s) {
        StringBuilder sb = new StringBuilder();
        for (String line : s.split("\n")) {
            String t = line.trim();
            if (t.startsWith("package ") || t.startsWith("import ")) continue;
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

    private static String stripComments(String s) {
        StringBuilder out = new StringBuilder(s.length());
        int n = s.length();
        boolean sl = false, ml = false, str = false, chr = false;

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            char nx = (i + 1 < n ? s.charAt(i + 1) : '\0');

            if (sl) { if (c == '\n') { sl = false; out.append(c); } continue; }
            if (ml) { if (c == '*' && nx == '/') { ml = false; i++; } continue; }
            if (str) { out.append(c); if (c == '\\' && nx == '"') { out.append(nx); i++; } else if (c == '"') str=false; continue; }
            if (chr) { out.append(c); if (c == '\\' && nx == '\'') { out.append(nx); i++; } else if (c == '\'') chr=false; continue; }

            if (c == '/' && nx == '/') { sl = true; i++; continue; }
            if (c == '/' && nx == '*') { ml = true; i++; continue; }
            if (c == '"') { str = true; out.append(c); continue; }
            if (c == '\'') { chr = true; out.append(c); continue; }

            out.append(c);
        }
        return out.toString();
    }

    private static String normalizeText(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) sb.append(s.charAt(i) < 33 ? ' ' : s.charAt(i));
        return sb.toString().trim();
    }

    private static long crc32(String s) {
        CRC32 crc = new CRC32();
        crc.update(s.getBytes(StandardCharsets.UTF_8));
        return crc.getValue();
    }
}
