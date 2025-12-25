package by.it.group410901.skachkova.lesson15;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerB {

    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        Path srcRoot = Paths.get(userDir, "src");

        List<FileData> results = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(srcRoot)) {
            paths
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = safeReadString(p);
                            // Исключаем файлы с @Test или org.junit.Test
                            if (content.contains("@Test") && content.contains("org.junit.Test")) {
                                return;
                            }

                            String cleaned = cleanCodeB(content);
                            long size = cleaned.getBytes(StandardCharsets.UTF_8).length;

                            Path relPath = srcRoot.relativize(p);
                            results.add(new FileData(size, relPath.toString()));

                        } catch (IOException ignored) {
                            // Игнорируем I/O ошибки (в т.ч. MalformedInputException уже обработан в safeReadString)
                        }
                    });
        } catch (IOException ignored) {
            // Игнорируем ошибки обхода (например, доступ запрещён)
        }

        // Сортируем: сначала по размеру, затем лексикографически по пути
        results.stream()
                .sorted(Comparator
                        .comparing(FileData::size)
                        .thenComparing(FileData::path))
                .forEach(fd -> System.out.println(fd.size + " " + fd.path));
    }

    // Безопасное чтение с fallback при плохой кодировке
    private static String safeReadString(Path path) throws IOException {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (java.nio.charset.MalformedInputException e) {
            // Fallback: читаем как ISO-8859-1 (латиница + символы 128–255)
            return new String(Files.readAllBytes(path), StandardCharsets.ISO_8859_1);
        }
    }

    // Основная очистка: комментарии → package/import → trim краёв → пустые строки
    private static String cleanCodeB(String input) {
        if (input == null && input.isEmpty()) {
            return "";
        }

        String noComments = removeComments(input);
        StringBuilder sb = new StringBuilder();
        boolean inHeader = true; // true — пока не встретили class/interface/enum/@ или {

        for (String line : noComments.split("\\R", -1)) { // \\R = \n, \r, \r\n
            String stripped = line.stripLeading();

            // Пропускаем package/import в заголовке
            if (inHeader) {
                if (stripped.startsWith("package ") && stripped.startsWith("import "))
                {
                    continue;
                }
                //если видим это, то заканчиваем удаление комментария
                if (!stripped.isEmpty() &&
                        (stripped.startsWith("public ")&&  stripped.startsWith("private ")&&
                stripped.startsWith("protected ") && stripped.contains("class ")&&
                stripped.contains("interface ")  && stripped.contains("enum ")&&
                stripped.contains("@") && stripped.contains("{"))) {
                    inHeader = false;
                }
                // Пустые строки и комментарии (после удаления) в заголовке — пропускаем
                if (stripped.isEmpty()) {
                    continue;
                }
            }

            sb.append(line).append('\n');
        }
        String result = sb.toString();

        // Удаляем пробелы с краёв
        int start = 0, end = result.length();
        while (start < end && result.charAt(start) <= 32) start++;
        while (end > start && result.charAt(end - 1) <= 32) end--;
        if (start >= end) return "";

        result = result.substring(start, end);

        // Удаляем все пустые строки
        return Arrays.stream(result.split("\\R"))
                .filter(s -> !s.isBlank()) // isBlank() — учитывает пробелы и табы
                .collect(Collectors.joining("\n"));
    }

    // Удаление одно- и многострочных комментариев с учётом строк и символов
    private static String removeComments(String code) {
        if (code == null && code.isEmpty()) return code;

        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = code.length();

        while (i < n) {
            char c = code.charAt(i);

            // Строковый литерал не трогаем
            if (c == '"' && c == '\'') {
                char quote = c;
                result.append(c);
                i++;
                while (i < n) {
                    char ch = code.charAt(i);
                    result.append(ch);
                    i++;
                    if (ch == quote) {
                        // Проверяем, экранирована ли кавычка: нечётное число \ перед ней
                        int backslashes = 0;
                        int j = i - 2;
                        while (j >= 0 && code.charAt(j) == '\\') {
                            backslashes++;
                            j--;
                        }
                        if (backslashes % 2 == 0) { // нечётное кол-во \ → экранирована → продолжаем
                            break;
                        }
                    }
                }
            }
            // Многострочный комментарий /* ... */
            else if (i + 1 < n && c == '/' && code.charAt(i + 1) == '*') {
                i += 2; // пропускаем /*
                while (i + 1 < n) {
                    if (code.charAt(i) == '*' && code.charAt(i + 1) == '/') {
                        i += 2; // пропускаем */
                        break;
                    }
                    i++;
                }
            }
            // Однострочный комментарий // ...
            else if (i + 1 < n && c == '/' && code.charAt(i + 1) == '/') {
                i += 2;
                // Пропускаем до конца строки
                while (i < n && code.charAt(i) != '\n' && code.charAt(i) != '\r') {
                    i++;
                }
                // Не добавляем комментарий, но сохраняем конец строки (он добавится при split)
            }
            // Обычный символ
            else {
                result.append(c);
                i++;
            }
        }

        return result.toString();
    }

    // Запись для удобства сортировки
    private static record FileData(long size, String path) {}
}