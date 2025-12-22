package by.it.group410901.borisdubinin.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerB {

    // Класс для хранения результатов сканирования файлов с реализацией Comparable для сортировки
    static class FileResult implements Comparable<FileResult> {
        String relativePath; // относительный путь файла
        int size;            // размер обработанного содержимого файла в байтах

        FileResult(String relativePath, int size) {
            this.relativePath = relativePath;
            this.size = size;
        }

        // Реализация сравнения для сортировки: сначала по размеру, затем по пути
        @Override
        public int compareTo(FileResult other) {
            if (this.size != other.size) {
                return Integer.compare(this.size, other.size); // сортировка по размеру (возрастание)
            }
            return this.relativePath.compareTo(other.relativePath); // лексикографическая сортировка путей
        }
    }

    public static void main(String[] args) {
        // Получаем абсолютный путь к директории src проекта
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileResult> results = new ArrayList<>();

        try {
            // Обрабатываем всю директорию src рекурсивно
            processDirectory(Paths.get(src), src, results);
        } catch (IOException e) {
            System.err.println("Error reading directory: " + e.getMessage());
            return;
        }

        // Сортируем результаты (используется compareTo из FileResult)
        results.sort(null);

        // Выводим результаты: размер и относительный путь
        for (FileResult result : results) {
            System.out.println(result.size + " " + result.relativePath);
        }
    }

    /**
     * Рекурсивно обрабатывает директорию и все поддиректории
     */
    private static void processDirectory(Path dir, String srcRoot, List<FileResult> results)
            throws IOException {
        // Используем Files.walk для рекурсивного обхода всех файлов
        try (Stream<Path> paths = Files.walk(dir)) {
            paths.filter(Files::isRegularFile)           // только файлы (не директории)
                    .filter(p -> p.toString().endsWith(".java")) // только Java файлы
                    .forEach(p -> processFile(p, srcRoot, results)); // обработка каждого файла
        }
    }

    /**
     * Обрабатывает один Java файл
     */
    private static void processFile(Path file, String srcRoot, List<FileResult> results) {
        // Читаем файл с попыткой использовать разные кодировки
        String content = readFileWithFallback(file);
        if (content == null) {
            return; // пропускаем файлы, которые не удалось прочитать
        }

        // Пропускаем тестовые файлы
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        // Обрабатываем содержимое: удаляем package, imports, комментарии и т.д.
        String processed = processContent(content);

        // Вычисляем размер обработанного содержимого в байтах
        int size = processed.getBytes(StandardCharsets.UTF_8).length;

        // Получаем относительный путь (убираем часть пути до src)
        String relativePath = file.toString().substring(srcRoot.length());

        results.add(new FileResult(relativePath, size));
    }

    /**
     * Читает файл с использованием нескольких кодировок (fallback механизм)
     */
    private static String readFileWithFallback(Path file) {
        // Список кодировок для попытки чтения (в порядке приоритета)
        Charset[] charsets = {
                StandardCharsets.UTF_8,      // современная стандартная кодировка
                StandardCharsets.ISO_8859_1, // Latin-1, распространенная в старых системах
                Charset.defaultCharset()     // кодировка по умолчанию системы
        };

        // Пробуем прочитать файл в каждой кодировке до первой успешной попытки
        for (Charset charset : charsets) {
            try {
                return Files.readString(file, charset);
            } catch (IOException e) {
                continue; // пробуем следующую кодировку
            }
        }

        System.err.println("Could not read file: " + file);
        return null;
    }

    /**
     * Основной метод обработки содержимого файла
     */
    private static String processContent(String content) {
        content = removePackageAndImports(content); // удаляем package и import
        content = removeComments(content);          // удаляем комментарии
        content = removeEmptyLines(content);        // удаляем пустые строки
        content = trimNonPrintable(content);        // обрезаем непечатаемые символы
        return content;
    }

    /**
     * Удаляет package и import statements из кода
     */
    private static String removePackageAndImports(String content) {
        StringBuilder sb = new StringBuilder(content.length());
        int i = 0;
        int len = content.length();

        while (i < len) {
            // Пропускаем начальные пробельные символы
            while (i < len && Character.isWhitespace(content.charAt(i))) {
                i++;
            }

            if (i >= len) break;

            // Проверяем, начинается ли строка с "package " или "import "
            if (content.startsWith("package ", i) || content.startsWith("import ", i)) {
                // Пропускаем всю строку до символа новой строки
                while (i < len && content.charAt(i) != '\n') {
                    i++;
                }
                if (i < len) i++; // пропускаем сам символ новой строки
            } else {
                // Копируем строку, которая не является package или import
                int lineStart = i;
                while (i < len && content.charAt(i) != '\n') {
                    i++;
                }
                sb.append(content, lineStart, i);
                if (i < len) {
                    sb.append('\n');
                    i++;
                }
            }
        }

        return sb.toString();
    }

    /**
     * Удаляет комментарии из Java кода (однострочные и многострочные)
     * С учетом того, что комментарии внутри строк не должны удаляться
     */
    private static String removeComments(String content) {
        StringBuilder sb = new StringBuilder(content.length());
        int i = 0;
        int len = content.length();
        boolean inString = false;    // флаг нахождения внутри строкового литерала
        char stringChar = 0;         // символ, начавший строковый литерал (' или ")

        while (i < len) {
            char c = content.charAt(i);

            if (inString) {
                // Мы внутри строкового литерала - копируем все символы
                sb.append(c);
                if (c == '\\' && i + 1 < len) {
                    // Обрабатываем escape-последовательности
                    i++;
                    sb.append(content.charAt(i));
                } else if (c == stringChar) {
                    // Конец строкового литерала
                    inString = false;
                }
                i++;
            } else if (c == '"' || c == '\'') {
                // Начало строкового литерала
                inString = true;
                stringChar = c;
                sb.append(c);
                i++;
            } else if (i + 1 < len && c == '/' && content.charAt(i + 1) == '/') {
                // Однострочный комментарий - пропускаем до конца строки
                while (i < len && content.charAt(i) != '\n') {
                    i++;
                }
            } else if (i + 1 < len && c == '/' && content.charAt(i + 1) == '*') {
                // Многострочный комментарий - пропускаем до */
                i += 2; // пропускаем /*
                while (i + 1 < len) {
                    if (content.charAt(i) == '*' && content.charAt(i + 1) == '/') {
                        i += 2; // пропускаем */
                        break;
                    }
                    i++;
                }
            } else {
                // Обычный символ - копируем
                sb.append(c);
                i++;
            }
        }

        return sb.toString();
    }

    /**
     * Удаляет пустые строки из содержимого
     */
    private static String removeEmptyLines(String content) {
        StringBuilder sb = new StringBuilder(content.length());
        String[] lines = content.split("\n", -1); // -1 сохраняет trailing empty strings

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                sb.append(line).append('\n');
            }
        }

        // Удаляем последний символ новой строки, если он есть
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    /**
     * Удаляет непечатаемые символы (с ASCII кодом < 33) с начала и конца содержимого
     */
    private static String trimNonPrintable(String content) {
        int start = 0;
        int end = content.length();

        // Убираем непечатаемые символы в начале
        while (start < end && content.charAt(start) < 33) {
            start++;
        }

        // Убираем непечатаемые символы в конце
        while (end > start && content.charAt(end - 1) < 33) {
            end--;
        }

        return content.substring(start, end);
    }
}