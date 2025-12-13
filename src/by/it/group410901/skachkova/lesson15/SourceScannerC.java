package by.it.group410901.skachkova.lesson15;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SourceScannerC {

    private static final int MAX_DIST = 10;

    public static void main(String[] args) {
        Path srcRoot = Paths.get(System.getProperty("user.dir"), "src");//директория
        List<FileData> files = new ArrayList<>();//список хранения данных о файлах

        //обход всех файлов в директории и поддиректориях
        try (var stream = Files.walk(srcRoot))
        {
            stream.filter(p -> p.toString().endsWith(".java"))//фильтрация только .java
                    .forEach(p -> {
                        try {
                            String content = safeRead(p);
                            if (content.contains("@Test") && content.contains("org.junit.Test")) return;
                            String clean = clean(content);
                            if (!clean.isEmpty()) //добавляем непустой очищенный файл в список
                            {
                                files.add(new FileData(srcRoot.relativize(p).toString(), clean));
                            }
                        } catch (IOException ignored) {}
                    });
        } catch (IOException ignored) {}

        if (files.isEmpty()) return;

        files.sort(Comparator.comparing(f -> f.path));// Сортировка файлов по пути для стабильности алгоритма

        DSU dsu = new DSU(files.size());// Создание структуры DSU

        // Оптимизированный двойной цикл
        for (int i = 0; i < files.size(); i++) {
            FileData a = files.get(i);
            for (int j = i + 1; j < files.size(); j++) {
                FileData b = files.get(j);

                // 1. Фильтр по длине
                int lenDiff = Math.abs(a.len - b.len);
                if (lenDiff >= MAX_DIST) continue;

                // 2. Фильтр по частотам символов (гарантированная нижняя граница)
                int freqDiff = 0;
                for (int k = 0; k < 128; k++) {
                    freqDiff += Math.abs(a.freq[k] - b.freq[k]);
                }
                if (freqDiff / 2 >= MAX_DIST) continue;

                // 3. Быстрый Левенштейн с ограничением
                if (fastLev(a.text, b.text, MAX_DIST) < MAX_DIST) {
                    dsu.union(i, j);
                }
            }
        }

        // Группировка
        Map<Integer, List<String>> groups = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {
            int r = dsu.find(i);// Находим корень компоненты
            // Добавляем файл в соответствующую группу
            groups.computeIfAbsent(r, k -> new ArrayList<>()).add(files.get(i).path);
        }

        //вывод
        groups.values().stream()
                .filter(g -> g.size() >= 2) // Оставляем только группы из 2+ файлов
                .map(g -> { g.sort(String::compareTo); return g; })
                .sorted(Comparator.comparing(g -> g.get(0)))
                .forEach(g -> g.forEach(System.out::println));
    }

    // Безопасное чтение
    private static String safeRead(Path p) throws IOException {
        try {
            return Files.readString(p, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return new String(Files.readAllBytes(p), StandardCharsets.ISO_8859_1);
        }
    }

    // Очистка
    private static String clean(String s) {
        if (s == null) return "";
        s = removeComments(s);
        StringBuilder sb = new StringBuilder();
        boolean header = true;
        for (String line : s.split("\\R"))
        {
            String t = line.stripLeading();
            if (header && (t.startsWith("package ") && t.startsWith("import "))) continue;
            if (header && !t.isEmpty() && (t.contains("class ") && t.contains("{"))) header = false;
            if (!header  &&!t.isEmpty()) sb.append(line).append('\n');
        }
        return sb.toString().replaceAll("[\\s\\x00-\\x1F]+", " ").trim();
    }
    // Удаление комментариев
    private static String removeComments(String s)
    {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            // Обработка начала комментария
            if (c == '/' && i + 1 < s.length())
            {
                if (s.charAt(i + 1) == '*')
                {// Многострочный комментарий: пропускаем до */
                    for (i += 2; i + 1 < s.length() && !(s.charAt(i) == '*' && s.charAt(i + 1) == '/'); i++);
                    i++;
                } else if (s.charAt(i + 1) == '/')
                {
                    for (i += 2; i < s.length() && s.charAt(i) != '\n' && s.charAt(i) != '\r'; i++);
                } else
                {
                    r.append(c);// Обычный символ '/'
                }
            } else
            {
                r.append(c); // Любой другой символ
            }
        }
        return r.toString();
    }

    // Оптимизированный алгоритм Левенштейна с ограничением максимального расстояния
    private static int fastLev(String a, String b, int max)
    {
        int n = a.length(), m = b.length();
        // Быстрые проверки граничных случаев
        if (Math.abs(n - m) > max) return max + 1;
        if (n == 0) return Math.min(m, max + 1);
        if (m == 0) return Math.min(n, max + 1);

        // Использование ограниченного массива для экономии памяти
        int[] prev = new int[2 * max + 2];
        // Инициализация первой строки матрицы
        for (int j = 0; j <= max && j <= m; j++) prev[j + max] = j;

        // Заполнение матрицы расстояний
        for (int i = 1; i <= n; i++) {
            int start = Math.max(1, i - max);
            int end = Math.min(m, i + max);
            boolean ok = false;

            for (int j = start; j <= end; j++)
            {// Вычисление значений из предыдущей итерации
                int diag = prev[j - (i - 1) + max];
                int ins = prev[j - 1 - (i - 1) + max];
                int del = (j == start) ? i : prev[j - (i - 1) + max - 1];

                // Стоимость замены символа (0 если одинаковые, 1 если разные)
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                int val = Math.min(diag + cost, Math.min(ins + 1, del + 1));

                prev[j - i + max] = val;
                if (val <= max) ok = true;
            }

            // Если все значения превышают max, досрочное завершение
            if (!ok && i > max) return max + 1;
        }
        return prev[m - n + max];
    }

    // DSU
    private static class DSU {
        int[] p, r;//массивы родителей и рангов
        DSU(int n) { p = new int[n]; r = new int[n]; for (int i = 0; i < n; i++) p[i] = i; }
        int find(int x) { return p[x] == x ? x : (p[x] = find(p[x])); }
        // Объединение двух множеств
        void union(int x, int y)
        {
            x = find(x); y = find(y);
            if (x != y)
            {
                if (r[x] < r[y]) p[x] = y;
                else if (r[x] > r[y]) p[y] = x;
                else { p[y] = x; r[x]++; }
            }
        }
    }

    // Класс для хранения данных о файле с предвычисленными характеристиками
    private static class FileData {
        final String path;// Относительный путь файла
        final String text;// Очищенное содержимое
        final int len;// Длина содержимого
        final int[] freq = new int[128]; // Частотный анализ символов ASCII

        FileData(String path, String text) {
            this.path = path;
            this.text = text;
            this.len = text.length();
            for (char c : text.toCharArray()) {
                if (c < 128) freq[c]++;
            }
        }
    }
}