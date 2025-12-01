package by.it.group410902.jalilova.lesson15;
import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis(); // Засекаем время начала выполнения программы

        // Путь к папке src текущего проекта
        Path root = Paths.get(System.getProperty("user.dir") + File.separator + "src" + File.separator);

        List<Result> results = new ArrayList<>(); // Список для хранения результатов (размер файла + относительный путь)

        try {
            // Рекурсивный обход всех файлов и папок внутри src
            Files.walk(root)
                    .filter(p -> p.toString().endsWith(".java")) // Оставляем только файлы с расширением .java
                    .forEach(p -> { // Для каждого найденного java-файла
                        try {
                            String text;
                            try {
                                text = Files.readString(p); // Чтение содержимого файла как строки
                            } catch (MalformedInputException e) {
                                return; // Игнорируем файлы с некорректной кодировкой
                            }

                            // Пропускаем тестовые файлы
                            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                            // Удаляем строки package и import
                            StringBuilder sb = new StringBuilder();
                            for (String line : text.split("\n")) { // Разделяем текст на строки
                                String trimmed = line.trim(); // Убираем пробелы в начале и конце строки
                                if (trimmed.startsWith("package ")) continue; // Пропускаем строку package
                                if (trimmed.startsWith("import ")) continue; // Пропускаем строку import
                                sb.append(line).append("\n"); // Добавляем оставшиеся строки обратно
                            }

                            // Убираем все символы с кодом <33 в начале и конце текста
                            String cleaned = trimNonPrintable(sb.toString());

                            // Размер текста в байтах
                            int size = cleaned.getBytes().length;

                            // Относительный путь файла относительно src
                            String relPath = root.relativize(p).toString();

                            // Добавляем результат в список
                            results.add(new Result(size, relPath));

                        } catch (IOException ignored) {
                            // Игнорируем ошибки чтения файлов (например, нет прав)
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace(); // Если ошибка при обходе файлов, выводим стек
        }

        // Сортировка результатов: сначала по размеру, если размер одинаковый — по пути
        results.sort(Comparator
                .comparingInt((Result r) -> r.size)
                .thenComparing(r -> r.path));

        // Вывод результатов в консоль
        for (Result r : results) {
            System.out.println(r.size + " " + r.path);
        }

        // Время выполнения программы
        long endTime = System.currentTimeMillis();
        System.out.println("TIME = " + (endTime - startTime) + " ms");
    }

    // Метод убирает все непринтируемые символы с кодом <33 в начале и конце текста
    private static String trimNonPrintable(String s) {
        int start = 0;
        int end = s.length() - 1;

        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;

        return (start > end) ? "" : s.substring(start, end + 1); // Возвращаем обрезанную строку
    }

    // Класс для хранения результатов: размер файла и относительный путь
    private record Result(int size, String path) {}
}
