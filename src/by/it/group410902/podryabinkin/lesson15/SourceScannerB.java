package by.it.group410902.podryabinkin.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.nio.ByteBuffer;

//ЗАЧЕМ MAIN STATIC.... Почему нельзя просто унаследовать.... бесит....
public class SourceScannerB {
     public static class Ffiles {
        public int size;
        public String path;

        public Ffiles(int size, String path) {
            this.size = size;
            this.path = path;
        }
    }
    public static void main(String[] args) throws IOException {

        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<Ffiles> results = new ArrayList<>();

        Files.walk(srcPath).filter(p -> p.toString().endsWith(".java")).forEach(path -> {
                    String text = safeReadFile(path);

                    if (text == null) return;
                    if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                    String processed = processText(text);
                    processed = removeComments(processed);
                    Path relative = srcPath.relativize(path);
                    int size = processed.getBytes(StandardCharsets.UTF_8).length;

                    results.add(new Ffiles(size, relative.toString()));
                });


        results.sort((a, b) -> {
            if (a.size != b.size) return Integer.compare(a.size, b.size);
            return a.path.compareTo(b.path);
        });

        // вывод
        for (Ffiles a : results) {
            System.out.println(a.size + " " + a.path);
        }
        System.out.println(results.size());
    }

    // MalformedInputException не волнует, у нас всё в битах
    private static String safeReadFile(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);

            // Декодируем, игнорируем уже другое
            CharsetDecoder decoder = StandardCharsets.UTF_8
                    .newDecoder()
                    .onMalformedInput(CodingErrorAction.IGNORE)
                    .onUnmappableCharacter(CodingErrorAction.IGNORE);

            return decoder.decode(ByteBuffer.wrap(bytes)).toString();

        } catch (IOException e) {
            //без try не работает
            return null;
        }
    }

    private static String processText(String text) {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = br.readLine()) != null) {

                String trimmed = line.trim();

                if (trimmed.startsWith("package ")) continue;
                if (trimmed.startsWith("import ")) continue;


                if (trimmed.isEmpty()) continue;

                sb.append(line).append("\n");
            }
        } catch (IOException ignored) {}

        // trim по символам < 33
        return DelAllNonChars(sb.toString());
    }


    private static String DelAllNonChars(String s) { // <33
        int start = 0;
        int end = s.length() - 1;

        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;

        if (start > end) return "";
        return s.substring(start, end + 1);
    }
    private static String removeComments(String s) {
        StringBuilder out = new StringBuilder();
        int n = s.length();
        int i = 0;

        boolean inString = false;
        boolean inChar = false;
        boolean inBlockComment = false;
        boolean inLineComment = false;

        while (i < n) {
            char c = s.charAt(i);
            //ОСОБОЕ
            if (inString) {
                out.append(c);
                if (c == '"' && s.charAt(i - 1) != '\\') {
                    inString = false;
                }
                i++;
                continue;
            }

            if (inChar) {
                out.append(c);
                if (c == '\'' && s.charAt(i - 1) != '\\') {
                    inChar = false;
                }
                i++;
                continue;
            }

            if (inLineComment) {
                if (c == '\n') {
                    out.append('\n');
                    inLineComment = false;
                }
                i++;
                continue;
            }
            //НАЧАЛА
            if (inBlockComment) {
                if (c == '*' && i + 1 < n && s.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i += 2;
                } else {
                    i++;
                }
                continue;
            }

            if (c == '"') {
                inString = true;
                out.append(c);
                i++;
                continue;
            }
            if (c == '\'') {
                inChar = true;
                out.append(c);
                i++;
                continue;
            }

            if (c == '/' && i + 1 < n && s.charAt(i + 1) == '/') {
                inLineComment = true;
                i += 2;
                continue;
            }
            if (c == '/' && i + 1 < n && s.charAt(i + 1) == '*') {
                inBlockComment = true;
                i += 2;
                continue;
            }

            out.append(c);
            i++;
        }

        return out.toString();
    }



}
