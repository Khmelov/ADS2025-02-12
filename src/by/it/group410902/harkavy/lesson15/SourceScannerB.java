package by.it.group410902.harkavy.lesson15;
import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerB {

    private static class Result {
        final String path;
        final int size;

        Result(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Paths.get(src);
        List<Result> results = new ArrayList<>();

        try {
            Files.walk(root)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> processFile(p, root, results));
        } catch (IOException e) {
            // Игнорируем ошибки обхода
        }

        // сортировка: по размеру, затем лексикографически по пути
        results.sort(
                Comparator.comparingInt((Result r) -> r.size)
                        .thenComparing(r -> r.path)
        );

        for (Result r : results) {
            System.out.println(r.size + " " + r.path);
        }
    }

    private static void processFile(Path file, Path root, List<Result> results) {
        String content;
        try {
            // Пытаемся прочитать как UTF-8
            content = Files.readString(file, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            // "Корректная" обработка: fallback-чтение без исключения
            try {
                byte[] bytes = Files.readAllBytes(file);
                // Любая однобайтовая кодировка гарантированно не кидает MalformedInputException
                content = new String(bytes, StandardCharsets.ISO_8859_1);
            } catch (IOException ex) {
                return; // не смогли прочитать — просто пропускаем
            }
        } catch (IOException e) {
            return;
        }

        // Игнорируем тесты
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        // 1. Удаляем package и import за O(n) по строкам
        String[] lines = content.split("\\R", -1);
        StringBuilder noPkgImports = new StringBuilder(content.length());
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("package ")
                    || trimmed.startsWith("import ")) {
                continue;
            }
            noPkgImports.append(line).append('\n');
        }

        // 2. Удаляем все комментарии за O(n) по символам
        String noComments = removeComments(noPkgImports.toString());

        // 3. Удаляем все символы с кодом < 33 в начале и конце текста
        String trimmed = trimLowAsciiEdges(noComments);

        // 4. Удаляем пустые строки (и строки только из "мусора" <33)
        String finalText = removeEmptyLines(trimmed);

        // Размер в байтах (UTF-8)
        int size = finalText.getBytes(StandardCharsets.UTF_8).length;

        String relativePath = root.relativize(file).toString();
        results.add(new Result(relativePath, size));
    }

    // Удаление // и /* ... */ с учётом строк/символьных литералов
    private static String removeComments(String src) {
        StringBuilder out = new StringBuilder(src.length());
        boolean inLineComment = false;
        boolean inBlockComment = false;
        boolean inString = false;
        boolean inChar = false;
        boolean escape = false;

        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            char next = (i + 1 < src.length()) ? src.charAt(i + 1) : '\0';

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    out.append(c); // сохраняем перевод строки
                }
                continue;
            }

            if (inBlockComment) {
                if (c == '*' && next == '/') {
                    inBlockComment = false;
                    i++; // съели '/'
                }
                continue;
            }

            if (inString) {
                out.append(c);
                if (escape) {
                    escape = false;
                } else if (c == '\\') {
                    escape = true;
                } else if (c == '"') {
                    inString = false;
                }
                continue;
            }

            if (inChar) {
                out.append(c);
                if (escape) {
                    escape = false;
                } else if (c == '\\') {
                    escape = true;
                } else if (c == '\'') {
                    inChar = false;
                }
                continue;
            }

            // Вне строк/символов/комментов — проверяем начало комментариев
            if (c == '/' && next == '/') {
                inLineComment = true;
                i++; // пропускаем второй '/'
                continue;
            }
            if (c == '/' && next == '*') {
                inBlockComment = true;
                i++; // пропускаем '*'
                continue;
            }

            if (c == '"') {
                inString = true;
                out.append(c);
                continue;
            }
            if (c == '\'') {
                inChar = true;
                out.append(c);
                continue;
            }

            out.append(c);
        }

        return out.toString();
    }

    // Удалить символы с кодом <33 в начале и конце
    private static String trimLowAsciiEdges(String s) {
        int start = 0;
        int end = s.length() - 1;

        while (start <= end && s.charAt(start) < 33) {
            start++;
        }
        while (end >= start && s.charAt(end) < 33) {
            end--;
        }

        if (start > end) {
            return "";
        }
        return s.substring(start, end + 1);
    }

    // Удалить пустые строки (и строки только из символов <33)
    private static String removeEmptyLines(String s) {
        String[] lines = s.split("\\R", -1);
        StringBuilder sb = new StringBuilder(s.length());

        for (String line : lines) {
            boolean hasVisible = false;
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) >= 33) {
                    hasVisible = true;
                    break;
                }
            }
            if (hasVisible) {
                sb.append(line).append('\n');
            }
        }

        // убираем последний \n, если он есть
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }
}
