package by.it.group451004.matyrka.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileData> files = new ArrayList<>();
        Path root = Paths.get(src);

        try {
            Files.walk(root)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> process(p, root, files));
        } catch (IOException ignored) {
        }

        Map<String, List<String>> copies = findCopies(files);

        List<String> keys = new ArrayList<>(copies.keySet());
        Collections.sort(keys);

        for (String k : keys) {
            System.out.println(k);
            List<String> v = copies.get(k);
            Collections.sort(v);
            for (String s : v) {
                System.out.println(s);
            }
        }
    }

    private static void process(Path file, Path root, List<FileData> list) {
        try {
            String content = Files.readString(file, StandardCharsets.UTF_8);
            if (content.contains("@Test") || content.contains("org.junit.Test")) return;

            String processed = normalize(content);
            String relative = root.relativize(file).toString();
            list.add(new FileData(relative, processed));

        } catch (MalformedInputException ignored) {
        } catch (IOException ignored) {
        }
    }

    private static String normalize(String s) {
        s = removeComments(s);

        StringBuilder sb = new StringBuilder();
        for (String line : s.split("\\R")) {
            String t = line.trim();
            if (t.startsWith("package") || t.startsWith("import")) continue;
            if (!t.isEmpty()) sb.append(t).append(' ');
        }

        return sb.toString().replaceAll("\\s+", " ").trim();
    }

    private static String removeComments(String s) {
        StringBuilder r = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (i + 1 < s.length() && s.charAt(i) == '/' && s.charAt(i + 1) == '/') {
                while (i < s.length() && s.charAt(i) != '\n') i++;
            } else if (i + 1 < s.length() && s.charAt(i) == '/' && s.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < s.length() && !(s.charAt(i) == '*' && s.charAt(i + 1) == '/')) i++;
                i += 2;
            } else {
                r.append(s.charAt(i++));
            }
        }
        return r.toString();
    }

    private static Map<String, List<String>> findCopies(List<FileData> files) {
        Map<FileSignature, List<FileData>> groups = new HashMap<>();
        for (FileData f : files) {
            groups.computeIfAbsent(new FileSignature(f.content), k -> new ArrayList<>()).add(f);
        }

        Map<String, List<String>> result = new HashMap<>();
        for (List<FileData> g : groups.values()) {
            if (g.size() > 1) {
                for (int i = 0; i < g.size(); i++) {
                    List<String> c = new ArrayList<>();
                    for (int j = i + 1; j < g.size(); j++) {
                        if (similar(g.get(i).content, g.get(j).content)) {
                            c.add(g.get(j).path);
                        }
                    }
                    if (!c.isEmpty()) result.put(g.get(i).path, c);
                }
            }
        }
        return result;
    }

    private static boolean similar(String a, String b) {
        if (a.equals(b)) return true;
        if (Math.abs(a.length() - b.length()) > 20) return false;
        return similarity(a, b) > 0.95;
    }

    private static double similarity(String a, String b) {
        Set<String> s1 = ngrams(a);
        Set<String> s2 = ngrams(b);
        int i = 0;
        for (String s : s1) if (s2.contains(s)) i++;
        int u = s1.size() + s2.size() - i;
        return u == 0 ? 0 : (double) i / u;
    }

    private static Set<String> ngrams(String s) {
        Set<String> r = new HashSet<>();
        for (int i = 0; i + 2 < s.length(); i++) {
            r.add(s.substring(i, i + 3));
        }
        return r;
    }

    private static class FileData {
        String path;
        String content;

        FileData(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }

    private static class FileSignature {
        int len;
        long hash;

        FileSignature(String s) {
            len = s.length();
            long h = 0;
            int step = Math.max(1, len / 10);
            for (int i = 0; i < len; i += step) h = h * 31 + s.charAt(i);
            hash = h;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof FileSignature f)) return false;
            return len == f.len && Math.abs(hash - f.hash) < 1000;
        }

        @Override
        public int hashCode() {
            return Objects.hash(len, hash / 1000);
        }
    }
}
