package by.it.group451002.shutko.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

// Просканировать Java-файлы в директории src, удалить тестовые файлы,
// package/import строки, все комментарии и пустые строки, вычислить размер
// обработанных текстов и вывести отсортированный список (размер + путь).
public class SourceScannerB {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // Получаем абсолютный путь к директории src
        String srcPath = System.getProperty("user.dir") + File.separator + "src";
        Path srcDir = Path.of(srcPath);
        List<FileInfo> fileInfos = new ArrayList<>(); // Список для хранения информации о файлах

        try {
            // Рекурсивный обход всех файлов и поддиректорий
            Files.walk(srcDir)
                    .filter(p -> p.toString().endsWith(".java")) // Только .java файлы
                    .forEach(p -> {
                        try {
                            // Чтение файла с обработкой разных кодировок
                            String content = readFileWithFallback(p);

                            // Пропуск файлов, содержащих тесты
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return; // пропустить этот файл
                            }

                            // Обработка содержимого файла
                            String processed = processContent(content);
                            // Размер в байтах после обработки
                            int sizeBytes = processed.getBytes().length;
                            // Относительный путь (от src)
                            String relPath = srcDir.relativize(p).toString();

                            // Сохранение информации о файле
                            fileInfos.add(new FileInfo(sizeBytes, relPath));

                        } catch (IOException e) {
                            // Игнорирование ошибок чтения файлов
                        }
                    });
        } catch (IOException e) {
            // Игнорирование ошибок обхода директории
        }

        // Сортировка: сначала по размеру (возрастание), затем по пути
        fileInfos.sort(Comparator.comparingInt(FileInfo::getSize)
                .thenComparing(FileInfo::getPath));

        // Вывод результатов
        for (FileInfo fi : fileInfos) {
            System.out.println(fi.getSize() + " " + fi.getPath());
        }

        // Вывод времени выполнения в stderr
        long endTime = System.currentTimeMillis();
        System.err.println("Execution time: " + (endTime - startTime) + "ms");
    }

    // Метод для чтения файла с перебором разных кодировок
    // (обработка MalformedInputException)
    private static String readFileWithFallback(Path filePath) throws IOException {
        // Список кодировок для перебора (от наиболее вероятных к менее)
        List<Charset> charsets = Arrays.asList(
                StandardCharsets.UTF_8,       // современные файлы
                Charset.forName("Windows-1251"), // кириллица Windows
                Charset.forName("ISO-8859-5"),   // кириллица Unix
                StandardCharsets.ISO_8859_1      // западноевропейская
        );

        // Пробуем каждую кодировку
        for (Charset charset : charsets) {
            try {
                return Files.readString(filePath, charset);
            } catch (MalformedInputException e) {
                continue; // пробуем следующую кодировку
            }
        }

        return ""; // если ни одна кодировка не подошла, возвращаем пустую строку
    }

    // Основной метод обработки содержимого файла (O(n) сложность)
    private static String processContent(String content) {
        if (content.isEmpty()) return content;

        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false; // внутри /* ... */
        boolean inLineComment = false;  // внутри //
        boolean inString = false;       // внутри "..."
        boolean inChar = false;         // внутри '.'
        char prevChar = 0;              // предыдущий символ

        // УДАЛЕНИЕ КОММЕНТАРИЕВ (O(n))
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (inBlockComment) {
                // Выход из блочного комментария */
                if (prevChar == '*' && c == '/') {
                    inBlockComment = false;
                }
            } else if (inLineComment) {
                // Выход из строчного комментария при новой строке
                if (c == '\n') {
                    inLineComment = false;
                    result.append(c); // сохраняем перенос строки
                }
            } else if (inString) {
                // Выход из строки, если кавычка не экранирована
                if (c == '"' && prevChar != '\\') {
                    inString = false;
                }
                result.append(c); // сохраняем содержимое строки
            } else if (inChar) {
                // Выход из символьного литерала
                if (c == '\'' && prevChar != '\\') {
                    inChar = false;
                }
                result.append(c); // сохраняем символьный литерал
            } else {
                // Обычный режим (не в комментарии/строке/символе)
                if (prevChar == '/' && c == '*') {
                    // Начало блочного комментария /*
                    inBlockComment = true;
                    result.deleteCharAt(result.length() - 1); // удаляем '/'
                } else if (prevChar == '/' && c == '/') {
                    // Начало строчного комментария //
                    inLineComment = true;
                    result.deleteCharAt(result.length() - 1); // удаляем '/'
                } else if (c == '"') {
                    // Начало строки
                    inString = true;
                    result.append(c);
                } else if (c == '\'') {
                    // Начало символьного литерала
                    inChar = true;
                    result.append(c);
                } else {
                    // Обычный символ
                    result.append(c);
                }
            }
            prevChar = c; // запоминаем текущий символ как предыдущий
        }

        String withoutComments = result.toString();

        // УДАЛЕНИЕ PACKAGE И IMPORTS (O(n))
        result.setLength(0); // очищаем StringBuilder для повторного использования
        String[] lines = withoutComments.split("\r?\n"); // разбиваем на строки
        boolean afterPackageImports = false; // флаг: прошли ли мы package/imports

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (!afterPackageImports) {
                // Пропускаем строки package и import
                if (trimmedLine.startsWith("package") || trimmedLine.startsWith("import")) {
                    continue;
                }
                // Если нашли строку, которая не package и не import,
                // значит package/imports закончились
                if (!trimmedLine.isEmpty() && !trimmedLine.startsWith("package") &&
                        !trimmedLine.startsWith("import")) {
                    afterPackageImports = true;
                }
            }

            // Добавляем строку только если она не пустая и мы прошли package/imports
            if (afterPackageImports && !line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        // Удаляем последний лишний перенос строки
        if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        String filteredContent = result.toString();

        // УДАЛЕНИЕ УПРАВЛЯЮЩИХ СИМВОЛОВ (код < 33) ПО КРАЯМ
        filteredContent = removeControlCharsFromEdges(filteredContent);

        // УДАЛЕНИЕ ПУСТЫХ СТРОК
        return removeEmptyLines(filteredContent);
    }

    // Удаление символов с кодом < 33 только в начале и конце текста
    private static String removeControlCharsFromEdges(String text) {
        if (text.isEmpty()) return text;

        int start = 0;
        int end = text.length();

        // Находим первый не-управляющий символ
        while (start < end && text.charAt(start) < 33) {
            start++;
        }

        // Находим последний не-управляющий символ
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        return text.substring(start, end);
    }

    // Удаление пустых строк
    private static String removeEmptyLines(String text) {
        if (text.isEmpty()) return text;

        StringBuilder result = new StringBuilder();
        String[] lines = text.split("\r?\n");

        for (String line : lines) {
            if (!line.trim().isEmpty()) { // если строка не пустая после trim()
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
        private final int size;    // Размер в байтах
        private final String path; // Относительный путь

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