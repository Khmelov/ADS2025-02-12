package by.it.group451001.volynets.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
        import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerC {

    static class Item {
        String path;
        String text;
        int len;
        Item(String path, String text) {
            this.path = path;
            this.text = text;
            this.len = text.length();
        }
    }

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Paths.get(src);

        List<Item> items = new ArrayList<>();

        // Сбор текстов
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (!attrs.isRegularFile()) return FileVisitResult.CONTINUE;
                if (!file.getFileName().toString().endsWith(".java")) return FileVisitResult.CONTINUE;

                String raw = readTextSafely(file);
                if (raw == null) return FileVisitResult.CONTINUE;

                if (isTestFile(raw)) return FileVisitResult.CONTINUE;

                String cleaned = removePackageAndImports(raw);
                cleaned = stripComments(cleaned);
                cleaned = normalizeSpaces(cleaned).trim();
                if (cleaned.isEmpty()) return FileVisitResult.CONTINUE;

                String rel = root.relativize(file).toString(); // системный разделитель
                items.add(new Item(rel, cleaned));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });

        // Сортируем по длине, затем по пути — упрощает группировку по длине
        items.sort((a, b) -> {
            int cmp = Integer.compare(a.len, b.len);
            return (cmp != 0) ? cmp : a.path.compareTo(b.path);
        });

        // Поиск пар-копий: ограниченный Левенштейн (<10), с отсечками по длине
        Map<String, List<String>> copies = new TreeMap<>();
        for (int i = 0; i < items.size(); i++) {
            Item ai = items.get(i);
            // кандидатам по длине — только те, чья длина в диапазоне [ai.len - 9, ai.len + 9]
            for (int j = i + 1; j < items.size(); j++) {
                Item bj = items.get(j);
                int diffLen = bj.len - ai.len;
                if (diffLen >= 10) break; // дальше только длиннее на >=10 — не копии
                if (diffLen <= -10) continue; // защитный кейс (хотя j>i гарантирует bj.len >= ai.len)

                // ограниченный Левенштейн с порогом 10
                int dist = levenshteinBounded(ai.text, bj.text, 10);
                if (dist >= 0 && dist < 10) {
                    // фиксируем обоих: ai имеет копию bj
                    copies.computeIfAbsent(ai.path, k -> new ArrayList<>()).add(bj.path);
                    // симметрия: bj тоже имеет копию ai
                    copies.computeIfAbsent(bj.path, k -> new ArrayList<>()).add(ai.path);
                }
            }
        }

        // Вывод: только файлы, у которых есть копии. Map уже отсортирован по пути.
        for (Map.Entry<String, List<String>> e : copies.entrySet()) {
            String base = e.getKey();
            List<String> peers = e.getValue();
            Collections.sort(peers);
            System.out.println(base);
            for (String p : peers) {
                System.out.println(p);
            }
        }
    }

    // Безопасное чтение: UTF-8, затем Windows-1251, затем ISO-8859-1
    static String readTextSafely(Path file) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(file);
        } catch (IOException e) {
            return null;
        }
        List<Charset> csList = List.of(
                Charset.forName("UTF-8"),
                Charset.forName("windows-1251"),
                Charset.forName("ISO-8859-1")
        );
        for (Charset cs : csList) {
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

    static boolean isTestFile(String text) {
        return text.contains("@Test") || text.contains("org.junit.Test");
    }

    // Удаление строк package/import — один проход по строкам
    static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;
        while (i < len) {
            int ls = i, le = i;
            while (le < len && text.charAt(le) != '\n') le++;
            String line = text.substring(ls, le);
            String trimmed = fastTrim(line);
            boolean skip = false;
            if (!trimmed.isEmpty()) {
                int p = firstNonControl(trimmed);
                if (p < trimmed.length()) {
                    if (startsWithWord(trimmed, p, "package")) skip = true;
                    else if (startsWithWord(trimmed, p, "import")) skip = true;
                }
            }
            if (!skip) {
                sb.append(line);
                if (le < len) sb.append('\n');
            } else {
                if (le < len) sb.append('\n'); // сохраняем структуру строк, чтобы корректно работать со stripComments
            }
            i = (le < len) ? le + 1 : le;
        }
        return sb.toString();
    }

    // Удаление комментариев: поддержка // и /* */, защита строк/символьных литералов
    static String stripComments(String s) {
        StringBuilder out = new StringBuilder(s.length());
        int n = s.length();
        boolean sl = false; // //
        boolean ml = false; // /* */
        boolean inStr = false; // "
        boolean inChr = false; // '
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            char next = (i + 1 < n) ? s.charAt(i + 1) : '\0';

            if (sl) {
                if (c == '\n') {
                    sl = false;
                    out.append(c);
                }
                continue;
            }
            if (ml) {
                if (c == '*' && next == '/') {
                    ml = false;
                    i++;
                }
                continue;
            }

            if (!inStr && !inChr) {
                if (c == '/' && next == '/') {
                    sl = true;
                    i++;
                    continue;
                }
                if (c == '/' && next == '*') {
                    ml = true;
                    i++;
                    continue;
                }
            }

            out.append(c);

            if (inStr) {
                if (c == '\\') {
                    if (i + 1 < n) {
                        out.append(s.charAt(i + 1));
                        i++;
                    }
                } else if (c == '"') {
                    inStr = false;
                }
            } else if (inChr) {
                if (c == '\\') {
                    if (i + 1 < n) {
                        out.append(s.charAt(i + 1));
                        i++;
                    }
                } else if (c == '\'') {
                    inChr = false;
                }
            } else {
                if (c == '"') inStr = true;
                else if (c == '\'') inChr = true;
            }
        }
        return out.toString();
    }

    // Замена любой последовательности символов с кодом <33 на один пробел, затем trim
    static String normalizeSpaces(String s) {
        StringBuilder out = new StringBuilder(s.length());
        int n = s.length();
        boolean inSpace = false;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c < 33) {
                if (!inSpace) {
                    out.append(' ');
                    inSpace = true;
                }
            } else {
                out.append(c);
                inSpace = false;
            }
        }
        return out.toString();
    }

    // Ограниченный Левенштейн с порогом limit: если расстояние >= limit, возвращаем >= limit (или -1 при раннем стопе)
    static int levenshteinBounded(String a, String b, int limit) {
        int n = a.length();
        int m = b.length();

        // Быстрые отсечки по длине
        int diff = Math.abs(n - m);
        if (diff >= limit) return diff; // точно >= limit

        // Выровненный band: считаем только диагональ шириной 2*limit+1
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) prev[j] = j;

        for (int i = 1; i <= n; i++) {
            curr[0] = i;

            // ограничение по столбцам: b от max(1, i - limit) до min(m, i + limit)
            int from = Math.max(1, i - limit);
            int to = Math.min(m, i + limit);

            // Заполним левую часть вне band как большие значения
            for (int j = 1; j < from; j++) curr[j] = limit;

            int rowMin = curr[from - 1];

            char ca = a.charAt(i - 1);
            for (int j = from; j <= to; j++) {
                int cost = (ca == b.charAt(j - 1)) ? 0 : 1;
                int v = Math.min(
                        Math.min(prev[j] + 1, curr[j - 1] + 1),
                        prev[j - 1] + cost
                );
                curr[j] = v;
                if (v < rowMin) rowMin = v;
            }

            // Заполним правую часть вне band
            for (int j = to + 1; j <= m; j++) curr[j] = limit;

            if (rowMin >= limit) return rowMin; // ранний стоп
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        int ans = prev[m];
        return ans;
    }

    // Вспомогательные функции
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