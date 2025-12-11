package by.it.group410901.galitskiy.lesson15;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerB {
    public static void main(String[] args) {
        // Формируем путь к директории src проекта
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        // Список для хранения информации о файлах
        List<FileInfo> results = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(Paths.get(src))) {
            // Рекурсивно обходим все файлы в директории src
            stream
                    // Исключаем директории из обработки
                    .filter(p -> !Files.isDirectory(p))
                    // Обрабатываем только Java-файлы
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            // Читаем всё содержимое файла в строку
                            String text = Files.readString(path, StandardCharsets.UTF_8);

                            // Пропускаем файлы-тесты (содержащие аннотации @Test)
                            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                            // Удаляем строки package и import
                            List<String> lines = Arrays.asList(text.split("\n"));
                            List<String> filtered = new ArrayList<>();
                            for (String line : lines) {
                                line = line.replace("\r", ""); // Убираем возврат каретки
                                String trimmed = line.trim();
                                // Пропускаем строки с объявлением пакета и импортами
                                if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) continue;
                                filtered.add(line);
                            }
                            // Собираем отфильтрованные строки обратно в текст
                            String joined = String.join("\n", filtered);

                            // Удаляем все комментарии из кода
                            joined = removeComments(joined);

                            // Удаляем управляющие символы в начале и конце текста
                            joined = trimControlChars(joined);

                            // Удаляем пустые строки для чистоты анализа
                            lines = Arrays.asList(joined.split("\n"));
                            filtered = new ArrayList<>();
                            for (String line : lines) {
                                // Пропускаем полностью пустые строки
                                if (line.trim().isEmpty()) continue;
                                filtered.add(line);
                            }
                            joined = String.join("\n", filtered);

                            // Получаем относительный путь файла (относительно src)
                            String rel = src.endsWith(File.separator)
                                    ? path.toString().substring(src.length())
                                    : path.toString().replace(src, "");

                            // Вычисляем размер очищенного содержимого в байтах в UTF-8
                            int byteSize = joined.getBytes(StandardCharsets.UTF_8).length;

                            // Сохраняем информацию о файле
                            results.add(new FileInfo(rel, byteSize));
                        } catch (MalformedInputException e) {
                            // Игнорируем файлы с некорректной кодировкой
                        } catch (IOException e) {
                            // Игнорируем другие ошибки ввода-вывода при чтении файлов
                        }
                    });
        } catch (IOException e) {
            // Обрабатываем ошибки обхода файловой системы
            System.err.println("Ошибка обхода файлов: " + e.getMessage());
        }

        // Сортируем результаты: сначала по размеру (возрастание), затем по пути (лексикографически)
        results.stream()
                .sorted(Comparator.comparingInt(FileInfo::getSize)
                        .thenComparing(FileInfo::getRelPath))
                .forEach(info ->
                        // Выводим результат в формате: "размер путь"
                        System.out.println(info.size + " " + info.relPath)
                );
    }

    /**
     * Удаление всех комментариев из Java-кода: однострочных и многострочных
     * Алгоритм работает за O(n) - один проход по тексту
     * @param text исходный текст с комментариями
     * @return текст без комментариев
     */
    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder(); // Для накопления результата
        int len = text.length();
        boolean isBlock = false;  // Флаг многострочного комментария (/* ... */)
        boolean isLine = false;   // Флаг однострочного комментария (// ...)

        // Обрабатываем текст посимвольно
        for (int i = 0; i < len; ) {
            // Обнаружение начала многострочного комментария
            if (!isBlock && !isLine && i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '*') {
                isBlock = true;
                i += 2; // Пропускаем два символа '/*'
                continue;
            }
            // Обнаружение конца многострочного комментария
            if (isBlock && i + 1 < len && text.charAt(i) == '*' && text.charAt(i + 1) == '/') {
                isBlock = false;
                i += 2; // Пропускаем два символа '*/'
                continue;
            }
            // Обнаружение начала однострочного комментария
            if (!isBlock && !isLine && i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '/') {
                isLine = true;
                i += 2; // Пропускаем два символа '//'
                continue;
            }
            // Обнаружение конца однострочного комментария (конец строки)
            if (isLine && (text.charAt(i) == '\n' || text.charAt(i) == '\r')) {
                isLine = false;
                sb.append(text.charAt(i)); // Сохраняем символ перевода строки
                i++;
                continue;
            }
            // Пропускаем символы внутри комментариев
            if (isBlock || isLine) {
                i++;
                continue;
            }
            // Сохраняем символы вне комментариев
            sb.append(text.charAt(i));
            i++;
        }
        return sb.toString();
    }

    /**
     * Удаление управляющих символов (< 33) в начале и конце текста
     * @param text исходный текст
     * @return текст без управляющих символов по краям
     */
    private static String trimControlChars(String text) {
        int start = 0;
        int end = text.length();
        // Находим первый непробельный символ (код >= 33)
        while (start < end && text.charAt(start) < 33) ++start;
        // Находим последний непробельный символ
        while (end > start && text.charAt(end - 1) < 33) --end;
        // Возвращаем подстроку без управляющих символов по краям
        return text.substring(start, end);
    }

    /**
     * Внутренний класс для хранения информации о файле
     * - относительный путь от src
     * - размер очищенного содержимого в байтах
     */
    static class FileInfo {
        final String relPath;  // Относительный путь файла
        final int size;        // Размер очищенного содержимого в байтах

        FileInfo(String relPath, int size) {
            this.relPath = relPath;
            this.size = size;
        }

        // Геттер для размера (используется в компараторе)
        int getSize() { return size; }

        // Геттер для пути (используется в компараторе)
        String getRelPath() { return relPath; }
    }
}