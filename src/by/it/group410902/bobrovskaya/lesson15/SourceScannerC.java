package by.it.group410902.bobrovskaya.lesson15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import java.io.File;

public class SourceScannerC {

    public static void main(String[] args) throws Exception {

        // Получаем путь к корневой папке src
        // System.getProperty("user.dir") → текущая рабочая директория
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        Path root = Paths.get(src);
        if (!Files.exists(root)) {
            return; // если src не существует — просто выходим
        }
        // Список всех файлов с очищенным текстом
        List<Item> items = new ArrayList<>();

        // Рекурсивно обходим все файлы в каталоге src
        try (Stream<Path> walk = Files.walk(root)) {
            walk
                    .filter(p -> p.toString().endsWith(".java")) // ищем только .java
                    .forEach(p -> processFile(p, root, items)); // обрабатываем
        }

        // Сортировка
        items.sort(Comparator.comparing(Item::relPath));

        // Map: файл, список его копий
        Map<String, List<String>> copies = new TreeMap<>();

        // Сравниваем каждый файл со всеми последующими
        int n = items.size();
        for (int i = 0; i < n; i++) {
            Item a = items.get(i);
            String s1 = a.text; // Получение очищенного текста

            for (int j = i + 1; j < n; j++) {
                Item b = items.get(j);
                String s2 = b.text;

                // Если тексты совпадают полностью — это копии
                if (s1.equals(s2)) {
                    copies.computeIfAbsent(a.relPath(), k -> new ArrayList<>())
                            .add(b.relPath());
                    continue;
                }

                // Если длины слишком разные
                if (Math.abs(s1.length() - s2.length()) > 9)
                    continue;

                if (!cheapFilter(s1, s2))
                    continue;

                // Считаем расстояние Левенштейна
                int dist = levenshteinLimit(s1, s2, 9);
                if (dist < 10) {
                    // Текст считается копией, если число правок <10
                    copies.computeIfAbsent(a.relPath(), k -> new ArrayList<>())
                            .add(b.relPath());
                }
            }
        }

        // Вывод всех файлов, у которых есть копии
        for (var e : copies.entrySet()) {
            System.out.println(e.getKey()); // основной файл
            for (String p : e.getValue()) {
                System.out.println(p); // его копии
            }
        }
    }

    private static void processFile(Path file, Path root, List<Item> items) {
        String text;

        try {
            // Читаем файл как UTF-8
            byte[] bytes = Files.readAllBytes(file);
            text = new String(bytes, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            return;
        } catch (IOException e) {
            return; // если ошибка чтения — пропускаем
        }

        // Пропускаем тестовые файлы
        if (text.contains("@Test") || text.contains("org.junit.Test"))
            return;

        // Убираем package и import
        String noHeaders = removePackageAndImports(text);

        // Удаляем комментарии // и /* */
        String noComments = removeComments(noHeaders);

        // Заменяем все пробельные символы(код <33) на один пробел
        String normalized = normalizeWhitespace(noComments);

        // Еще раз нормализуем и trim() — удаляем лишние пробелы по краям
        String finalText = normalizeWhitespace(normalized).trim();

        // Ограничиваем текст 2000 символами для экономии времени
        finalText = cut(finalText);

        // Записываем результат с относительным путем
        items.add(new Item(root.relativize(file).toString(), finalText));
    }

    private static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = br.readLine()) != null) {

                // сравниваем trimmed строку
                String t = line.trim();

                // пропускаем package и import
                if ((t.startsWith("package ") && t.endsWith(";")) ||
                        (t.startsWith("import ") && t.endsWith(";"))) {
                    continue;
                }

                // остальные строки добавляем
                sb.append(line).append('\n');
            }
        } catch (IOException ignored) {} // не может произойти, но оставлено для совместимости
        return sb.toString();
    }

    private static String removeComments(String s) {
        StringBuilder out = new StringBuilder();
        int n = s.length();

        boolean inLine = false;  // внутри //
        boolean inBlock = false; // внутри /* */

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);

            // старт // комментария
            if (!inBlock && c == '/' && i + 1 < n && s.charAt(i + 1) == '/') {
                inLine = true;
                i++; // пропускаем второй символ '/'
                continue;
            }

            // старт /* комментария
            if (!inLine && c == '/' && i + 1 < n && s.charAt(i + 1) == '*') {
                inBlock = true;
                i++;
                continue;
            }

            // если в // комментарии — ждём конца строки
            if (inLine) {
                if (c == '\n') {
                    inLine = false;
                    out.append('\n');
                }
                continue;
            }

            // если в /* */ комментарии — ждём */
            if (inBlock) {
                if (c == '*' && i + 1 < n && s.charAt(i + 1) == '/') {
                    inBlock = false;
                    i++;
                }
                continue;
            }

            // обычный символ
            out.append(c);
        }
        return out.toString();
    }

    private static String normalizeWhitespace(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        boolean lastSpace = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // если символ <33 → считаем его пробельным
            if (c < 33) {
                if (!lastSpace) { // не допускаем двойных пробелов
                    sb.append(' ');
                    lastSpace = true;
                }
            } else {
                sb.append(c);
                lastSpace = false;
            }
        }
        return sb.toString();
    }

    private static String cut(String s) {
        int MAX = 2000;
        return s.length() <= MAX ? s : s.substring(0, MAX);
    }

    private static boolean cheapFilter(String s1, String s2) {
        return Math.abs(s1.hashCode() - s2.hashCode()) < 50000;
    }

    private static int levenshteinLimit(String s1, String s2, int maxDist) {
        int n = s1.length();
        int m = s2.length();

        // Если разница длин больше maxDist — ничего считать не нужно
        if (Math.abs(n - m) > maxDist)
            return maxDist + 1;

        // Используются только два ряда DP для экономии памяти
        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];

        // Заполняем первую строку DP (расстояние от пустой строки)
        for (int j = 0; j <= m; j++)
            prev[j] = j;

        // Основной цикл DP
        for (int i = 1; i <= n; i++) {
            cur[0] = i; // расстояние между s1[0..i] и пустой строкой

            int minRow = cur[0]; // минимальный элемент в строке

            char ci = s1.charAt(i - 1);

            for (int j = 1; j <= m; j++) {
                int cost = (ci == s2.charAt(j - 1)) ? 0 : 1; // сравнение символов

                // Формула Левенштейна
                int r = Math.min(
                        Math.min(cur[j - 1] + 1, prev[j] + 1), // вставка/удаление
                        prev[j - 1] + cost                      // замена
                );

                cur[j] = r;
                minRow = Math.min(minRow, r);
            }

            // Если вся строка больше maxDist — можно прервать
            if (minRow > maxDist)
                return maxDist + 1;

            // Меняем строки местами
            int[] t = prev;
            prev = cur;
            cur = t;
        }

        return prev[m]; // итоговое расстояние
    }

    // record — удобная неизменяемая структура данных
    private record Item(String relPath, String text) {}
}
