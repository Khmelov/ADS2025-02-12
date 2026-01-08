package by.it.group451001.drzhevetskiy.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) {
        Path srcRoot = Paths.get(System.getProperty("user.dir"), "src");
        List<Entry> list = new ArrayList<>();

        try {
            Files.walk(srcRoot)
                    .filter(SourceScannerB::javaSource)
                    .forEach(p -> scanFile(srcRoot, p, list));
        } catch (IOException e) {
            System.err.println("Ошибка обхода директорий");
            return;
        }

        list.sort(Comparator
                .comparingInt((Entry e) -> e.len)
                .thenComparing(e -> e.file));

        for (Entry e : list) {
            System.out.println(e.len + " " + e.file);
        }
    }

    // ---------- file logic ----------

    private static boolean javaSource(Path p) {
        return Files.isRegularFile(p) && p.toString().endsWith(".java");
    }

    private static void scanFile(Path root, Path file, List<Entry> out) {
        try {
            String src = Files.readString(file, StandardCharsets.UTF_8);
            if (hasTests(src)) return;

            String cleared = normalize(src);
            int size = cleared.getBytes(StandardCharsets.UTF_8).length;

            out.add(new Entry(root.relativize(file).toString(), size));
        } catch (IOException ignored) {
        }
    }

    // ---------- text processing ----------

    private static boolean hasTests(String s) {
        return s.indexOf("@Test") >= 0 || s.indexOf("org.junit.Test") >= 0;
    }

    private static String normalize(String src) {
        String noMeta = dropHeaders(src);
        String noComments = eraseComments(noMeta);
        String compact = trimNoise(noComments);
        return deleteEmpty(compact);
    }

    private static String dropHeaders(String src) {
        StringBuilder sb = new StringBuilder();
        for (String line : src.split("\n")) {
            String t = line.trim();
            if (!t.startsWith("package") && !t.startsWith("import")) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    private static String eraseComments(String src) {
        StringBuilder out = new StringBuilder();
        boolean block = false, line = false, str = false, chr = false;

        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            char n = (i + 1 < src.length()) ? src.charAt(i + 1) : 0;

            if (block) {
                if (c == '*' && n == '/') {
                    block = false;
                    i++;
                }
                continue;
            }

            if (line) {
                if (c == '\n') {
                    line = false;
                    out.append(c);
                }
                continue;
            }

            if (str) {
                out.append(c);
                if (c == '\\' && n == '"') {
                    out.append(n);
                    i++;
                } else if (c == '"') {
                    str = false;
                }
                continue;
            }

            if (chr) {
                out.append(c);
                if (c == '\\' && n == '\'') {
                    out.append(n);
                    i++;
                } else if (c == '\'') {
                    chr = false;
                }
                continue;
            }

            if (c == '"' ) {
                str = true;
                out.append(c);
            } else if (c == '\'') {
                chr = true;
                out.append(c);
            } else if (c == '/' && n == '*') {
                block = true;
                i++;
            } else if (c == '/' && n == '/') {
                line = true;
                i++;
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }

    private static String trimNoise(String s) {
        int l = 0, r = s.length();
        while (l < r && s.charAt(l) <= 32) l++;
        while (r > l && s.charAt(r - 1) <= 32) r--;
        return s.substring(l, r);
    }

    private static String deleteEmpty(String s) {
        StringBuilder sb = new StringBuilder();
        for (String line : s.split("\n")) {
            if (!line.trim().isEmpty()) sb.append(line).append('\n');
        }
        return sb.toString();
    }

    // ---------- data ----------

    private static class Entry {
        String file;
        int len;

        Entry(String f, int l) {
            file = f;
            len = l;
        }
    }
}
