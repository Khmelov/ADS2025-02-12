package by.it.group410901.bukshta.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) throws IOException {

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
        }

        // Сортировка: по размеру, затем по пути
        results.sort(Comparator
                .comparingLong((Result r) -> r.size)
                .thenComparing(r -> r.relPath));

        // Вывод
        for (Result r : results) {
            System.out.println(r.size + "  " + r.relPath);
        }
    }

    private static void processFile(Path file, Path root, List<Result> results) {
        String text;

        // Чтение с обработкой MalformedInputException
        text = readFileSafe(file);
        if (text == null) return;

        // Пропуск тестов
        if (text.contains("@Test") || text.contains("org.junit.Test")) return;

        // 1–2. Удаление package, import, комментариев (оба — O(n))
        text = removePackageImportsAndComments(text);

        // 3. Удалить символы <33 по краям
        text = trimLowAscii(text);

        // 4. Удалить пустые строки
        text = removeEmptyLines(text);

        // Итоговый размер
        long size = text.getBytes(Charset.forName("UTF-8")).length;

        String rel = root.relativize(file).toString();

        results.add(new Result(rel, size));
    }

    //ЧТЕНИЕ ФАЙЛА

    private static String readFileSafe(Path file) {
        try {
            return Files.readString(file, Charset.forName("UTF-8"));
        } catch (MalformedInputException e1) {
            // Пробуем ISO-8859-1
            try {
                return Files.readString(file, Charset.forName("ISO-8859-1"));
            } catch (Exception e2) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    //УДАЛЕНИЕ package/import +

    private static String removePackageImportsAndComments(String text) {
        StringBuilder out = new StringBuilder(text.length());

        boolean inBlockComment = false;

        try (BufferedReader br = new BufferedReader(new StringReader(text))) {
            String line;

            while ((line = br.readLine()) != null) {
                String trimmed = line.stripLeading();

                // Удаляем package/import
                if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                    continue;
                }

                // Удаление комментариев
                String cleaned = removeCommentsFromLine(line, new boolean[]{inBlockComment});
                inBlockComment = cleaned.endsWith("\u0000"); // спец-метка "продолжается /* */"
                if (inBlockComment) {
                    cleaned = cleaned.substring(0, cleaned.length() - 1);
                }

                out.append(cleaned).append('\n');
            }

        } catch (IOException ignored) {}

        return out.toString();
    }

    // Удаляет // и /* */ (оба вида)
    private static String removeCommentsFromLine(String line, boolean[] blockState) {
        boolean inBlock = blockState[0];
        StringBuilder sb = new StringBuilder(line.length());

        for (int i = 0; i < line.length(); i++) {
            if (inBlock) {
                if (i + 1 < line.length() && line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                    inBlock = false;
                    i++;
                }
                continue;
            }

            if (i + 1 < line.length()) {
                char c = line.charAt(i);
                char n = line.charAt(i + 1);

                if (c == '/' && n == '/') {
                    break; // остальная строка — комментарий
                }
                if (c == '/' && n == '*') {
                    inBlock = true;
                    i++;
                    continue;
                }
            }

            sb.append(line.charAt(i));
        }

        blockState[0] = inBlock;
        if (inBlock) {
            sb.append('\u0000'); // спец-метка, что блок не закрыт
        }

        return sb.toString();
    }

    // ТРИМ И УДАЛЕНИЕ ПУСТЫХ СТРОК

    private static String trimLowAscii(String s) {
        int start = 0, end = s.length() - 1;

        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;

        if (start > end) return "";
        return s.substring(start, end + 1);
    }

    private static String removeEmptyLines(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        try (BufferedReader br = new BufferedReader(new StringReader(s))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.strip().isEmpty()) {
                    sb.append(line).append('\n');
                }
            }
        } catch (IOException ignored) {}
        return sb.toString();
    }

    //РЕЗУЛЬТАТ

    private record Result(String relPath, long size) {}
}
