package by.it.group410902.harkavy.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SourceScannerC {

    private static class Doc {
        final String path;
        final String text;
        final List<Integer> copies = new ArrayList<>();

        Doc(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Paths.get(src);
        List<Doc> docs = new ArrayList<>();

        try {
            Files.walk(root)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> processFile(p, root, docs));
        } catch (IOException e) {
            // игнорируем
        }

        // попарно считаем расстояние Левенштейна и собираем копии
        final int COPY_LIMIT = 10; // "<10" правок = копия
        int n = docs.size();
        for (int i = 0; i < n; i++) {
            String ti = docs.get(i).text;
            for (int j = i + 1; j < n; j++) {
                String tj = docs.get(j).text;
                int dist = levenshteinBounded(ti, tj, COPY_LIMIT);
                if (dist < COPY_LIMIT) {
                    docs.get(i).copies.add(j);
                    docs.get(j).copies.add(i);
                }
            }
        }

        // сортируем файлы по пути
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) indices[i] = i;
        Arrays.sort(indices, Comparator.comparing(i -> docs.get(i).path));

        // выводим только те, у кого есть копии
        for (int idx : indices) {
            Doc d = docs.get(idx);
            if (d.copies.isEmpty()) continue;

            System.out.println(d.path);
            // пути копий тоже сортируем лексикографически
            List<String> copyPaths = new ArrayList<>();
            for (int j : d.copies) {
                copyPaths.add(docs.get(j).path);
            }
            copyPaths.sort(String::compareTo);
            for (String cp : copyPaths) {
                System.out.println(cp);
            }
        }
    }

    private static void processFile(Path file, Path root, List<Doc> docs) {
        String content;
        try {
            content = Files.readString(file, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            // корректная обработка: перечитываем "сырые" байты и декодируем в безопасной однобайтовой кодировке
            try {
                byte[] bytes = Files.readAllBytes(file);
                content = new String(bytes, StandardCharsets.ISO_8859_1);
            } catch (IOException ex) {
                return;
            }
        } catch (IOException e) {
            return;
        }

        // фильтруем тесты
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        // 1. удаляем package и import
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

        // 2. удаляем комментарии за O(n)
        String noComments = removeComments(noPkgImports.toString());

        // 3. заменяем все последовательности символов с кодом <33 на один пробел (32)
        String normalized = squashLowAsciiToSpace(noComments);

        // 4. trim()
        String finalText = normalized.trim();

        String relativePath = root.relativize(file).toString();
        docs.add(new Doc(relativePath, finalText));
    }

    // Удаление // и /*...*/ с учётом строк и символьных литералов
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
                    out.append(c);
                }
                continue;
            }

            if (inBlockComment) {
                if (c == '*' && next == '/') {
                    inBlockComment = false;
                    i++; // пропускаем '/'
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

            if (c == '/' && next == '/') {
                inLineComment = true;
                i++;
                continue;
            }
            if (c == '/' && next == '*') {
                inBlockComment = true;
                i++;
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

    // Последовательности символов с кодом <33 превращаем в один пробел (32)
    private static String squashLowAsciiToSpace(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        boolean inLow = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 33) {
                if (!inLow) {
                    sb.append(' ');
                    inLow = true;
                }
            } else {
                sb.append(c);
                inLow = false;
            }
        }

        return sb.toString();
    }

    // Ограниченный Левенштейн: если дистанция >= limit, выходим раньше
    private static int levenshteinBounded(String a, String b, int limit) {
        if (a == b) return 0;
        if (a == null || b == null) return limit;

        int la = a.length();
        int lb = b.length();

        // минимально возможная дистанция — разница длин
        if (Math.abs(la - lb) >= limit) {
            return limit;
        }

        // делаем a короче
        if (la > lb) {
            String tmp = a; a = b; b = tmp;
            int t = la; la = lb; lb = t;
        }

        int[] prev = new int[la + 1];
        int[] curr = new int[la + 1];

        for (int i = 0; i <= la; i++) {
            prev[i] = i;
        }

        for (int j = 1; j <= lb; j++) {
            char bj = b.charAt(j - 1);
            curr[0] = j;
            int rowMin = curr[0];

            for (int i = 1; i <= la; i++) {
                int cost = (a.charAt(i - 1) == bj) ? 0 : 1;
                int replace = prev[i - 1] + cost;
                int insert = curr[i - 1] + 1;
                int delete = prev[i] + 1;

                int val = replace;
                if (insert < val) val = insert;
                if (delete < val) val = delete;

                curr[i] = val;
                if (val < rowMin) rowMin = val;
            }

            if (rowMin >= limit) {
                return limit;
            }

            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }

        return prev[la];
    }
}
