package by.it.group410902.podryabinkin.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.nio.ByteBuffer;


public class SourceScannerA {
    //Рекурсивно спускаемся по папкам
    //Берём .java
    //делаем их битами, чтобы избежать ошибок
    //насильно расшифровываем в UTF-8
    //Чистим что надо по заданию
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


}

