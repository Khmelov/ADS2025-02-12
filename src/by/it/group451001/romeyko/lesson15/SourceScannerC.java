package by.it.group451001.romeyko.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class SourceScannerC {

    public static void main(String[] args) throws IOException {

        String srcRoot = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path root = Paths.get(srcRoot);

        if (!Files.exists(root)) {
            System.err.println("Каталог src не найден: " + root);
            return;
        }

        List<Path> javaFiles;
        try (var s = Files.walk(root)) {javaFiles = s.filter(p -> p.toString().endsWith(".java")).collect(Collectors.toList());}

        Map<String, String> texts = new HashMap<>();
        for (Path file : javaFiles) {
            String text;
            try {
                text = Files.readString(file, StandardCharsets.UTF_8);
            } catch (MalformedInputException e) {
                continue;
            } catch (IOException e) {
                continue;
            }

            if (text.contains("@Test") || text.contains("org.junit.Test")) {
                continue;
            }
            String cleaned = prepareText(text);
            if (!cleaned.isEmpty()) {
                String relPath = root.relativize(file).toString();
                texts.put(relPath, cleaned);
            }
        }

        List<String> paths = new ArrayList<>(texts.keySet());
        Collections.sort(paths);
        Set<String> processed = new HashSet<>();
        for (int i = 0; i < paths.size(); i++) {
            String p1 = paths.get(i);
            if (processed.contains(p1)) continue;

            List<String> copies = new ArrayList<>();
            String t1 = texts.get(p1);
            for (int j = i + 1; j < paths.size(); j++) {
                String p2 = paths.get(j);
                if (processed.contains(p2)) continue;
                String t2 = texts.get(p2);
                if (Math.abs(t1.length() - t2.length()) >= 10) continue;
                int dist = levenshteinDistance(t1, t2, 10);
                if (dist < 10) {
                    copies.add(p2);
                    processed.add(p2);
                }
            }

            if (!copies.isEmpty()) {
                System.out.println(p1);
                for (String copy : copies) {
                    System.out.println(copy);
                }
                processed.add(p1);
            }
        }
    }

    private static String prepareText(String text) {
        String withoutPkgImport = removePackageAndImports(text);
        String noComments = SourceScannerB.stripComments(withoutPkgImport);
        noComments = noComments.replaceAll("[\\x00-\\x20]+", " ").trim();
        return noComments;
    }

    private static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trim = line.trim();
                if (trim.startsWith("package ")) continue;
                if (trim.startsWith("import ")) continue;
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
        }
        return sb.toString();
    }

    private static int levenshteinDistance(String s1, String s2, int threshold) {
        int len1 = s1.length();
        int len2 = s2.length();

        if (Math.abs(len1 - len2) >= threshold) return threshold;
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];
        for (int j = 0; j <= len2; j++) prev[j] = j;
        for (int i = 1; i <= len1; i++) {
            curr[0] = i;
            int minInRow = curr[0];

            for (int j = 1; j <= len2; j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(curr[j - 1] + 1, prev[j] + 1),
                        prev[j - 1] + cost
                );
                minInRow = Math.min(minInRow, curr[j]);
            }
            if (minInRow >= threshold) return threshold;
            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }
        return prev[len2];
    }
}

