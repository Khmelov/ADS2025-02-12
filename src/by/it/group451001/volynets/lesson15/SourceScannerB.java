package by.it.group451001.volynets.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Paths.get(src);

        List<Result> results = new ArrayList<>();

        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (!attrs.isRegularFile()) return FileVisitResult.CONTINUE;
                if (!file.getFileName().toString().endsWith(".java")) return FileVisitResult.CONTINUE;

                String text = readTextSafely(file);
                if (text == null) return FileVisitResult.CONTINUE;

                if (containsTestMarkers(text)) return FileVisitResult.CONTINUE;

                // 1) удалить package/import (O(n))
                String noPkgImports = removePackageAndImports(text);

                // 2) удалить комментарии (O(n))
                String noComments = stripComments(noPkgImports);

                // 3) trim управляющих символов по краям
                String trimmed = trimControlEdges(noComments);

                // 4) удалить пустые строки
                String finalText = removeEmptyLines(trimmed);

                int sizeBytes = finalText.getBytes(Charset.forName("UTF-8")).length;
                String rel = root.relativize(file).toString(); // системный разделитель

                results.add(new Result(rel, sizeBytes));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });

        results.sort((a, b) -> {
            int cmp = Integer.compare(a.size, b.size);
            return (cmp != 0) ? cmp : a.path.compareTo(b.path);
        });

        for (Result r : results) {
            System.out.println(r.size + " " + r.path);
        }
    }

    static class Result {
        final String path;
        final int size;
        Result(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }

    // Корректная обработка MalformedInputException: пробуем несколько кодировок
    static String readTextSafely(Path file) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(file);
        } catch (IOException e) {
            return null;
        }
        List<Charset> candidates = List.of(
                Charset.forName("UTF-8"),
                Charset.forName("windows-1251"),
                Charset.forName("ISO-8859-1")
        );
        for (Charset cs : candidates) {
            try {
                return new String(bytes, cs);
            } catch (Exception e) {
                if (!(e instanceof MalformedInputException)) {
                    return null;
                }
            }
        }
        return null;
    }

    static boolean containsTestMarkers(String text) {
        return text.contains("@Test") || text.contains("org.junit.Test");
    }

    // Удаление package/import строк (линейный проход)
    static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;
        while (i < len) {
            int lineStart = i;
            int lineEnd = i;
            while (lineEnd < len) {
                char c = text.charAt(lineEnd);
                if (c == '\n') break;
                lineEnd++;
            }
            String line = text.substring(lineStart, lineEnd);
            String trimmed = fastTrim(line);

            boolean skip = false;
            if (!trimmed.isEmpty()) {
                int p = firstNonControl(trimmed);
                if (p < trimmed.length()) {
                    if (startsWithWord(trimmed, p, "package")) skip = true;
                    else if (startsWithWord(trimmed, p, "import")) skip = true;
                }
            }
            if (!skip) sb.append(line);
            if (lineEnd < len) {
                sb.append('\n');
                i = lineEnd + 1;
            } else {
                i = lineEnd;
            }
        }
        return sb.toString();
    }

    // Удаление комментариев (// и /* */), с уважением к строкам/символам и экранированию
    static String stripComments(String s) {
        StringBuilder out = new StringBuilder(s.length());
        int n = s.length();
        boolean inSLComment = false; // // ... до конца строки
        boolean inMLComment = false; // /* ... */
        boolean inString = false;    // "..."
        boolean inChar = false;      // 'a'
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            char next = (i + 1 < n) ? s.charAt(i + 1) : '\0';

            if (inSLComment) {
                // до конца строки пропускаем
                if (c == '\n') {
                    inSLComment = false;
                    out.append(c); // сохранить перевод строки
                }
                continue;
            }

            if (inMLComment) {
                // ищем */
                if (c == '*' && next == '/') {
                    inMLComment = false;
                    i++; // пропустить '/'
                }
                continue;
            }

            if (!inString && !inChar) {
                // возможное начало комментария
                if (c == '/' && next == '/') {
                    inSLComment = true;
                    i++; // пропустить вторую '/'
                    continue;
                }
                if (c == '/' && next == '*') {
                    inMLComment = true;
                    i++; // пропустить '*'
                    continue;
                }
            }

            // обработка строк/символьных литералов с экранированием
            out.append(c);
            if (inString) {
                if (c == '\\') {
                    // экранированный символ — добавить следующий как есть
                    if (i + 1 < n) {
                        out.append(s.charAt(i + 1));
                        i++;
                    }
                } else if (c == '"') {
                    inString = false;
                }
            } else if (inChar) {
                if (c == '\\') {
                    if (i + 1 < n) {
                        out.append(s.charAt(i + 1));
                        i++;
                    }
                } else if (c == '\'') {
                    inChar = false;
                }
            } else {
                if (c == '"') inString = true;
                else if (c == '\'') inChar = true;
            }
        }
        return out.toString();
    }

    static String trimControlEdges(String text) {
        int l = 0, r = text.length() - 1;
        while (l <= r && text.charAt(l) < 33) l++;
        while (r >= l && text.charAt(r) < 33) r--;
        return (l <= r) ? text.substring(l, r + 1) : "";
    }

    static String removeEmptyLines(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;
        while (i < len) {
            int ls = i, le = i;
            while (le < len) {
                char c = text.charAt(le);
                if (c == '\n') break;
                le++;
            }
            // строка [ls, le)
            String line = text.substring(ls, le);
            String trimmed = fastTrim(line);
            if (!trimmed.isEmpty()) {
                sb.append(line);
                if (le < len) sb.append('\n');
            } else {
                // пропускаем пустую; если дальше есть непустая, разделители между непустыми сохранятся
                if (le < len) {
                    // не добавляем перевод строки
                }
            }
            i = (le < len) ? (le + 1) : le;
        }
        return sb.toString();
    }

    // Вспомогательные функции тримминга и префикса
    static String fastTrim(String s) {
        int l = 0, r = s.length() - 1;
        while (l <= r && s.charAt(l) < 33) l++;
        while (r >= l && s.charAt(r) < 33) r--;
        return (l <= r) ? s.substring(l, r + 1) : "";
    }

    static int firstNonControl(String s) {
        int i = 0;
        while (i < s.length() && s.charAt(i) < 33) i++;
        return i;
    }

    static boolean startsWithWord(String s, int pos, String word) {
        int wLen = word.length();
        if (pos + wLen > s.length()) return false;
        for (int i = 0; i < wLen; i++) {
            if (s.charAt(pos + i) != word.charAt(i)) return false;
        }
        int next = pos + wLen;
        if (next >= s.length()) return true;
        char c = s.charAt(next);
        return c <= 32 || c == '\t';
    }
}
