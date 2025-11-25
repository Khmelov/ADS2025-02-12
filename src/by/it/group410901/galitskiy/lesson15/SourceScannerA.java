package by.it.group410901.galitskiy.lesson15;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerA {
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

                            // Разбиваем текст на строки для обработки
                            List<String> lines = Arrays.asList(text.split("\n"));
                            List<String> filtered = new ArrayList<>();

                            // Фильтруем строки: удаляем package и import
                            for (String line : lines) {
                                line = line.replace("\r", ""); // Убираем возврат каретки
                                // Пропускаем строки с объявлением пакета и импортами
                                if (line.trim().startsWith("package ")) continue;
                                if (line.trim().startsWith("import ")) continue;
                                filtered.add(line);
                            }
                            // Собираем отфильтрованные строки обратно в текст
                            String joined = String.join("\n", filtered);

                            // Удаляем управляющие символы в начале и конце текста
                            joined = trimControlChars(joined);

                            // Получаем относительный путь файла (относительно src)
                            String rel = src.endsWith(File.separator)
                                    ? path.toString().substring(src.length())
                                    : path.toString().replace(src, "");

                            // Вычисляем размер содержимого в байтах в UTF-8 кодировке
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
     * Вспомогательный метод для удаления управляющих символов (< 33)
     * в начале и конце строки
     * @param text исходный текст
     * @return текст без управляющих символов по краям
     */
    private static String trimControlChars(String text) {
        int start = 0;
        int end = text.length();
        // Находим первый непробельный символ (код >= 33)
        while (start < end && text.charAt(start) < 33) start++;
        // Находим последний непробельный символ
        while (end > start && text.charAt(end - 1) < 33) end--;
        // Возвращаем подстроку без управляющих символов по краям
        return text.substring(start, end);
    }

    /**
     * Внутренний класс для хранения информации о файле
     * - относительный путь от src
     * - размер содержимого в байтах
     */
    static class FileInfo {
        final String relPath;  // Относительный путь файла
        final int size;        // Размер содержимого в байтах

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