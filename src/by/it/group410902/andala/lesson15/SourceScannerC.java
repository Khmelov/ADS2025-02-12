package by.it.group410902.andala.lesson15;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerC {
    public static void main(String[] args) {
        // Получаем путь к папке src проекта
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileText> fileTexts = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(Paths.get(src))) {
            stream
                    .filter(p -> !Files.isDirectory(p)) // только файлы
                    .filter(p -> p.toString().endsWith(".java")) // только .java
                    .forEach(path -> {
                        try {
                            String text = Files.readString(path, StandardCharsets.UTF_8);
                            // Пропускаем тестовые файлы
                            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                            // Удаляем package и import строки
                            List<String> lines = Arrays.asList(text.split("\n"));
                            List<String> filtered = new ArrayList<>();
                            for (String line : lines) {
                                String trimmed = line.replace("\r", "").trim();
                                if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) continue;
                                filtered.add(line);
                            }
                            String joined = String.join("\n", filtered);

                            // Удаляем комментарии
                            joined = removeComments(joined);

                            // Нормализуем пробелы и управляющие символы
                            joined = normalizeLowChars(joined);

                            // Обрезаем пробелы по краям
                            joined = joined.trim();

                            // Получаем относительный путь
                            String relPath = src.endsWith(File.separator)
                                    ? path.toString().substring(src.length())
                                    : path.toString().replace(src, "");

                            fileTexts.add(new FileText(relPath, joined));
                        } catch (MalformedInputException e) {
                            // Игнорируем файлы с некорректной кодировкой
                        } catch (IOException e) {
                            // Игнорируем ошибки чтения
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка обхода файлов: " + e.getMessage());
        }

        // Сортируем файлы по пути для последовательной обработки
        fileTexts.sort(Comparator.comparing(ft -> ft.relPath));

        // Поиск похожих файлов (плагиата)
        int n = fileTexts.size();
        boolean[] reported = new boolean[n]; // чтобы не дублировать отчеты
        for (int i = 0; i < n; ++i) {
            List<String> copies = new ArrayList<>();
            FileText a = fileTexts.get(i);

            for (int j = 0; j < n; ++j) {
                if (i == j) continue;
                FileText b = fileTexts.get(j);

                // Быстрая проверка по длине текста
                if (Math.abs(a.text.length() - b.text.length()) > 10) continue;

                // Проверка по префиксу (первые 64 символа)
                int prefixLen = Math.min(64, Math.min(a.text.length(), b.text.length()));
                if (!a.text.substring(0, prefixLen).equals(b.text.substring(0, prefixLen)))
                    continue;

                // Детальное сравнение с помощью расстояния Левенштейна
                if (levenshteinBanded(a.text, b.text, 10) < 10) {
                    copies.add(b.relPath);
                    reported[j] = true; // помечаем как уже обработанный
                }
            }
            // Выводим группу похожих файлов
            if (!copies.isEmpty() && !reported[i]) {
                Collections.sort(copies);
                System.out.println(a.relPath);
                for (String cp : copies) {
                    System.out.println(cp);
                }
            }
        }
    }

    // Удаляет все комментарии из кода
    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder();
        int len = text.length();
        boolean isBlock = false, isLine = false; // флаги комментариев
        for (int i = 0; i < len; ) {
            // Начало блочного комментария /*
            if (!isBlock && !isLine && i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '*') {
                isBlock = true; i += 2; continue;
            }
            // Конец блочного комментария */
            if (isBlock && i + 1 < len && text.charAt(i) == '*' && text.charAt(i + 1) == '/') {
                isBlock = false; i += 2; continue;
            }
            // Начало строчного комментария //
            if (!isBlock && !isLine && i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '/') {
                isLine = true; i += 2; continue;
            }
            // Конец строчного комментария (новая строка)
            if (isLine && (text.charAt(i) == '\n' || text.charAt(i) == '\r')) {
                isLine = false; sb.append(text.charAt(i)); i++; continue;
            }
            // Пропускаем символы внутри комментариев
            if (isBlock || isLine) { i++; continue; }
            // Добавляем обычные символы
            sb.append(text.charAt(i)); i++;
        }
        return sb.toString();
    }

    // Заменяет последовательности управляющих символов на один пробел
    private static String normalizeLowChars(String text) {
        StringBuilder sb = new StringBuilder();
        boolean inSeq = false; // флаг последовательности пробелов
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!inSeq) sb.append(' '); // добавляем пробел только один раз
                inSeq = true;
            } else {
                sb.append(c);
                inSeq = false;
            }
        }
        return sb.toString();
    }

    // Оптимизированное расстояние Левенштейна с ограниченной полосой
    private static int levenshteinBanded(String a, String b, int max) {
        int lenA = a.length(), lenB = b.length();
        // Быстрая проверка по разнице длин
        if (Math.abs(lenA - lenB) > max) return max;

        int[] prev = new int[lenB + 1], curr = new int[lenB + 1];
        // Инициализация первой строки
        for (int j = 0; j <= lenB; j++) prev[j] = j;

        for (int i = 1; i <= lenA; i++) {
            curr[0] = i;
            // Ограничиваем область вычислений для оптимизации
            int from = Math.max(1, i - max);
            int to = Math.min(lenB, i + max);
            int localMin = curr[0];

            for (int j = from; j <= to; j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
                localMin = Math.min(localMin, curr[j]);
            }
            // Ранний выход если минимальное значение превышает порог
            if (localMin > max) return max;
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[lenB];
    }

    // Класс для хранения текста файла и его пути
    static class FileText {
        final String relPath; // относительный путь
        final String text;    // обработанный текст
        FileText(String relPath, String text) {
            this.relPath = relPath;
            this.text = text;
        }
    }
}