package by.it.group451002.shandr.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {
    public static void main(String[] args) {
        // Засекаем время начала выполнения
        long startTime = System.currentTimeMillis();

        // Определяем путь к директории src текущего проекта
        String srcPath = System.getProperty("user.dir") + File.separator + "src";
        Path srcDir = Path.of(srcPath);

        // Список для хранения информации о файлах
        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            // Рекурсивно обходим все файлы в директории src
            Files.walk(srcDir)
                    // Фильтруем только Java файлы
                    .filter(p -> p.toString().endsWith(".java"))
                    // Обрабатываем каждый файл
                    .forEach(p -> {
                        try {
                            // Читаем файл с обработкой разных кодировок
                            String content = readFileWithFallback(p);

                            // Пропускаем тестовые файлы
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return; // переходим к следующему файлу
                            }

                            // Обрабатываем содержимое файла
                            String processed = processContent(content);

                            // Вычисляем размер обработанного содержимого
                            int sizeBytes = processed.getBytes().length;

                            // Получаем относительный путь
                            String relPath = srcDir.relativize(p).toString();

                            // Сохраняем информацию о файле
                            fileInfos.add(new FileInfo(sizeBytes, relPath));

                        } catch (IOException e) {
                            // Игнорируем ошибки чтения файлов
                        }
                    });
        } catch (IOException e) {
            // Игнорируем ошибки обхода директории
        }

        // Сортируем файлы по размеру (возрастание) и по пути
        fileInfos.sort(Comparator.comparingInt(FileInfo::getSize)
                .thenComparing(FileInfo::getPath));

        // Выводим результаты
        for (FileInfo fi : fileInfos) {
            System.out.println(fi.getSize() + " " + fi.getPath());
        }

        // Выводим время выполнения
        long endTime = System.currentTimeMillis();
        System.err.println("Execution time: " + (endTime - startTime) + "ms");
    }

    // Метод для чтения файла с поддержкой разных кодировок
    private static String readFileWithFallback(Path filePath) throws IOException {
        // Список кодировок для перебора (в порядке приоритета)
        List<Charset> charsets = Arrays.asList(
                StandardCharsets.UTF_8,      // Современная универсальная кодировка
                Charset.forName("Windows-1251"), // Кириллическая кодировка Windows
                Charset.forName("ISO-8859-5"),   // Кириллическая кодировка ISO
                StandardCharsets.ISO_8859_1      // Латиница ISO
        );

        // Пробуем прочитать файл каждой кодировкой
        for (Charset charset : charsets) {
            try {
                return Files.readString(filePath, charset);
            } catch (MalformedInputException e) {
                // Если кодировка не подходит - пробуем следующую
                continue;
            }
        }

        // Если ни одна кодировка не подошла - возвращаем пустую строку
        return "";
    }

    // Основной метод обработки содержимого Java файла
    private static String processContent(String content) {
        if (content.isEmpty()) return content;

        StringBuilder result = new StringBuilder();

        // Состояния парсера для обработки комментариев и строк
        boolean inBlockComment = false;  // Внутри многострочного комментария /* ... */
        boolean inLineComment = false;   // Внутри однострочного комментария // ...
        boolean inString = false;        // Внутри строки " ... "
        boolean inChar = false;          // Внутри символьного литерала ' ... '
        char prevChar = 0;               // Предыдущий символ

        // ============== ФАЗА 1: Удаление комментариев ==============
        // Проходим по всем символам файла один раз (O(n))
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (inBlockComment) {
                // Находимся внутри блочного комментария
                // Ищем конец комментария */
                if (prevChar == '*' && c == '/') {
                    inBlockComment = false; // Комментарий закончился
                }
            } else if (inLineComment) {
                // Находимся внутри однострочного комментария
                // Он заканчивается на переносе строки
                if (c == '\n') {
                    inLineComment = false; // Комментарий закончился
                    result.append(c);      // Сохраняем перенос строки
                }
            } else if (inString) {
                // Находимся внутри строки
                result.append(c); // Сохраняем содержимое строки
                // Проверяем конец строки (исключая экранированные кавычки)
                if (c == '"' && prevChar != '\\') {
                    inString = false; // Строка закончилась
                }
            } else if (inChar) {
                // Находимся внутри символьного литерала
                result.append(c); // Сохраняем содержимое
                // Проверяем конец символьного литерала
                if (c == '\'' && prevChar != '\\') {
                    inChar = false; // Символьный литерал закончился
                }
            } else {
                // Мы НЕ находимся ни в комментарии, ни в строке, ни в символьном литерале
                // Проверяем начало комментариев, строк или символьных литералов

                if (prevChar == '/' && c == '*') {
                    // Начало блочного комментария /*
                    inBlockComment = true;
                    result.deleteCharAt(result.length() - 1); // Удаляем предыдущий '/'
                } else if (prevChar == '/' && c == '/') {
                    // Начало однострочного комментария //
                    inLineComment = true;
                    result.deleteCharAt(result.length() - 1); // Удаляем предыдущий '/'
                } else if (c == '"') {
                    // Начало строки
                    inString = true;
                    result.append(c);
                } else if (c == '\'') {
                    // Начало символьного литерала
                    inChar = true;
                    result.append(c);
                } else {
                    // Обычный символ (не комментарий, не строка, не символьный литерал)
                    result.append(c);
                }
            }
            prevChar = c; // Сохраняем текущий символ как предыдущий для следующей итерации
        }

        String withoutComments = result.toString();

        // ============== ФАЗА 2: Удаление package и import ==============
        result.setLength(0); // Очищаем StringBuilder для повторного использования

        String[] lines = withoutComments.split("\r?\n"); // Разделяем на строки
        boolean afterPackageImports = false; // Флаг: прошли ли мы секцию package/import

        for (String line : lines) {
            String trimmedLine = line.trim(); // Убираем пробелы по краям

            if (!afterPackageImports) {
                // Мы еще в начале файла (может быть секция package/import)
                if (trimmedLine.startsWith("package") || trimmedLine.startsWith("import")) {
                    continue; // Пропускаем строки package и import
                }
                // Если нашли строку, которая НЕ package и НЕ import
                if (!trimmedLine.isEmpty() && !trimmedLine.startsWith("package") &&
                        !trimmedLine.startsWith("import")) {
                    afterPackageImports = true; // Секция package/import закончилась
                }
            }

            // Добавляем строку только если:
            // 1. Мы уже прошли секцию package/import
            // 2. Строка не пустая (после удаления комментариев могли появиться пустые строки)
            if (afterPackageImports && !line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        // Удаляем последний лишний перенос строки
        if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        String filteredContent = result.toString();

        // Удаляем управляющие символы с краев всего текста
        filteredContent = removeControlCharsFromEdges(filteredContent);

        // Удаляем пустые строки
        return removeEmptyLines(filteredContent);
    }

    // Метод удаления управляющих символов (ASCII < 33) с начала и конца строки
    private static String removeControlCharsFromEdges(String text) {
        if (text.isEmpty()) return text;

        int start = 0;
        int end = text.length();

        // Удаляем управляющие символы с начала
        while (start < end && text.charAt(start) < 33) {
            start++;
        }

        // Удаляем управляющие символы с конца
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        return text.substring(start, end);
    }

    // Метод удаления пустых строк
    private static String removeEmptyLines(String text) {
        if (text.isEmpty()) return text;

        StringBuilder result = new StringBuilder();
        String[] lines = text.split("\r?\n");

        for (String line : lines) {
            if (!line.trim().isEmpty()) { // Если строка не пустая после удаления пробелов
                result.append(line).append("\n");
            }
        }

        // Удаляем последний лишний перенос строки
        if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    // Внутренний класс для хранения информации о файле
    private static class FileInfo {
        private final int size;      // Размер обработанного содержимого (байты)
        private final String path;   // Относительный путь к файлу

        public FileInfo(int size, String path) {
            this.size = size;
            this.path = path;
        }

        public int getSize() {
            return size;
        }

        public String getPath() {
            return path;
        }
    }
}