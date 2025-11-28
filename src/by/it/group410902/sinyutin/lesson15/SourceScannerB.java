package by.it.group410902.sinyutin.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class SourceScannerB {

    // Класс для хранения результата по каждому файлу
    static class ScanResult {
        String relativePath;
        long sizeBytes;

        public ScanResult(String relativePath, long sizeBytes) {
            this.relativePath = relativePath;
            this.sizeBytes = sizeBytes;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Paths.get(src);

        if (!Files.exists(root)) {
            System.err.println("Каталог src не найден: " + src);
            return;
        }

        List<ScanResult> results = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(root)) {
            paths.filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        ScanResult res = processFile(path, root);
                        if (res != null) {
                            results.add(res);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортировка:
        // 1. По размеру (по возрастанию)
        // 2. Если размер одинаковый - лексикографически по пути
        results.sort(Comparator
                .comparingLong((ScanResult r) -> r.sizeBytes)
                .thenComparing(r -> r.relativePath)
        );

        // Вывод
        for (ScanResult res : results) {
            System.out.println(res.sizeBytes + " " + res.relativePath);
        }
    }

    /**
     * Обрабатывает один файл. Возвращает null, если файл пустой,
     * содержит тесты или произошла ошибка чтения.
     */
    private static ScanResult processFile(Path path, Path root) {
        // 1. Безопасное чтение с игнорированием ошибок кодировки
        String content = readFileSafe(path);
        if (content == null || content.isEmpty()) return null;

        // Фильтр тестов (пропускаем файл целиком)
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return null;
        }

        // ПОРЯДОК ОБРАБОТКИ:
        // Чтобы корректно удалить imports и package, сначала лучше удалить комментарии.
        // Иначе комментарий вида /* package com.example; */ может сбить логику regex.

        // 2. Удалить все комментарии за O(N)
        String noComments = removeComments(content);

        // 3. Удалить строку package и все импорты.
        // (?m) - multiline режим
        // ^\s* - начало строки с возможными пробелами
        // (package|import) - ключевые слова
        // .*? - любые символы (лениво)
        // ; - до точки с запятой
        String noImports = noComments.replaceAll("(?m)^\\s*(package|import)\\s+.*?;", "");

        // 4. Удалить символы < 33 в начале и конце (trim)
        String trimmed = noImports.trim();

        // 5. Удалить пустые строки
        // Заменяем последовательности из нескольких переносов строк на один,
        // либо удаляем пустые строки, состоящие только из пробелов.
        // (?m)^\s*(\r?\n) -> ищем строки, где только пробелы и перенос, удаляем их
        String finalContent = trimmed.replaceAll("(?m)^\\s*\\r?\\n", "");

        // Рассчитываем размер в байтах (UTF-8)
        byte[] bytes = finalContent.getBytes(StandardCharsets.UTF_8);

        String relativePath = root.relativize(path).toString();

        return new ScanResult(relativePath, bytes.length);
    }

    /**
     * Читает файл, игнорируя MalformedInputException.
     */
    private static String readFileSafe(Path path) {
        try {
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.IGNORE)
                    .onUnmappableCharacter(CodingErrorAction.IGNORE);

            byte[] allBytes = Files.readAllBytes(path);
            CharBuffer cb = decoder.decode(ByteBuffer.wrap(allBytes));
            return cb.toString();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Удаляет комментарии (блочные и строчные) за один проход O(N).
     * Учитывает, что комментарии внутри строк ("...") или чаров ('...') не считаются.
     */
    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();

        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false; // "..."
        boolean inChar = false;   // '.'

        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);
            char next = (i + 1 < len) ? text.charAt(i + 1) : '\0';

            if (inBlockComment) {
                if (c == '*' && next == '/') {
                    inBlockComment = false;
                    i++; // Пропускаем '/'
                }
            } else if (inLineComment) {
                if (c == '\n' || c == '\r') {
                    inLineComment = false;
                    sb.append(c); // Сохраняем перенос строки (важно для структуры)
                }
            } else {
                // Мы в коде или строке
                if (inString) {
                    sb.append(c);
                    if (c == '\\') { // Экранирование внутри строки
                        sb.append(next);
                        i++;
                    } else if (c == '"') {
                        inString = false;
                    }
                } else if (inChar) {
                    sb.append(c);
                    if (c == '\\') { // Экранирование внутри чара
                        sb.append(next);
                        i++;
                    } else if (c == '\'') {
                        inChar = false;
                    }
                } else {
                    // Обычный код (не в комментарии и не в литерале)
                    if (c == '/' && next == '*') {
                        inBlockComment = true;
                        i++;
                    } else if (c == '/' && next == '/') {
                        inLineComment = true;
                        i++;
                    } else if (c == '"') {
                        inString = true;
                        sb.append(c);
                    } else if (c == '\'') {
                        inChar = true;
                        sb.append(c);
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }
}