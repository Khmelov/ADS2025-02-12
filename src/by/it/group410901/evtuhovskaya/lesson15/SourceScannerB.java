package by.it.group410901.evtuhovskaya.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {

    //-комментарии

    public static void main(String[] args) {
        // Определяем корень проекта: текущая директория + "src"
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path root = Paths.get(src);

        // Список для хранения информации о файлах
        List<FileInfo> list = new ArrayList<>();

        try {
            // Обход всех файлов и подпапок внутри root
            Files.walk(root)
                    .filter(path -> path.toString().endsWith(".java")) // выбираем только .java файлы
                    .forEach(path -> processFile(path, root, list));  // обрабатываем каждый файл
        } catch (IOException ignored) {} // Игнорируем ошибки при обходе

        // Сортировка: сначала по размеру файла, потом по имени файла
        list.sort(Comparator
                .comparing(FileInfo::size)
                .thenComparing(FileInfo::relativePath));

        // Вывод информации о файлах: размер + относительный путь
        for (FileInfo fi : list) {
            System.out.println(fi.size() + " " + fi.relativePath());
        }
    }

    // Обработка одного файла
    private static void processFile(Path file, Path root, List<FileInfo> list) {
        String text;

        try {
            // Читаем содержимое файла в строку
            text = Files.readString(file, Charset.defaultCharset());
        } catch (MalformedInputException e) {
            return; // Пропускаем файлы с неправильной кодировкой
        } catch (IOException e) {
            return; // Пропускаем файлы при других ошибках
        }

        // Пропускаем тестовые файлы
        if (text.contains("@Test") || text.contains("org.junit.Test"))
            return;

        // Очищаем текст: убираем package, import и комментарии
        String cleaned = cleanText(text);

        // Убираем невидимые символы с начала и конца
        cleaned = trimLowChars(cleaned);
        // Убираем пустые строки
        cleaned = removeEmptyLines(cleaned);

        // Размер файла после очистки
        int size = cleaned.getBytes().length;
        // Относительный путь к файлу относительно root
        String relative = root.relativize(file).toString();

        // Добавляем файл в список
        list.add(new FileInfo(relative, size));
    }

    // Убираем package, import и комментарии
    private static String cleanText(String text) {
        StringBuilder out = new StringBuilder(text.length());

        boolean inBlockComment = false; // флаг для /* ... */ комментариев
        boolean inLineComment = false;  // флаг для // комментариев

        try (BufferedReader br = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                // Пропускаем package и import, если не внутри блока комментариев
                if (!inBlockComment && (trimmed.startsWith("package ") || trimmed.startsWith("import ")))
                    continue;

                StringBuilder sb = new StringBuilder();
                int i = 0;
                while (i < line.length()) {
                    // Начало однострочного комментария //
                    if (!inLineComment && !inBlockComment &&
                            i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                        break; // игнорируем остаток строки
                    }
                    // Начало блочного комментария /* */
                    if (!inLineComment && !inBlockComment &&
                            i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                        inBlockComment = true;
                        i += 2;
                        continue;
                    }
                    // Конец блочного комментария */
                    if (inBlockComment &&
                            i + 1 < line.length() && line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                        inBlockComment = false;
                        i += 2;
                        continue;
                    }
                    // Добавляем символ, если не внутри блока комментария
                    if (!inBlockComment) {
                        sb.append(line.charAt(i));
                    }
                    i++;
                }
                inLineComment = false; // сбрасываем флаг однострочного комментария
                if (!inBlockComment) {
                    out.append(sb).append("\n"); // добавляем очищенную строку
                }
            }
        } catch (IOException ignored) {}

        return out.toString();
    }

    // Убирает невидимые символы и пробелы в начале и конце
    private static String trimLowChars(String s) {
        int start = 0, end = s.length() - 1;
        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;
        if (start > end) return "";
        return s.substring(start, end + 1);
    }

    // Убирает пустые строки
    private static String removeEmptyLines(String s) {
        StringBuilder out = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new StringReader(s))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    out.append(line).append("\n");
                }
            }
        } catch (IOException ignored) {}
        return out.toString();
    }

    // Класс для хранения информации о файле
    record FileInfo(String relativePath, int size) {}
}
