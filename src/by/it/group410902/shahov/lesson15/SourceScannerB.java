package by.it.group410902.shahov.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // Получаем путь к директории src текущего проекта
        String srcPath = System.getProperty("user.dir") + File.separator + "src";
        Path srcDir = Path.of(srcPath);
        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            // Рекурсивно обходим все файлы в директории src
            Files.walk(srcDir)
                    // Фильтруем только Java файлы
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            // Читаем файл с учетом разных кодировок
                            String content = readFileWithFallback(p);

                            // Пропускаем тестовые файлы (содержащие аннотации @Test)
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Обрабатываем содержимое файла (удаляем комментарии, package, import и т.д.)
                            String processed = processContent(content);
                            // Вычисляем размер обработанного содержимого в байтах
                            int sizeBytes = processed.getBytes().length;
                            // Получаем относительный путь файла
                            String relPath = srcDir.relativize(p).toString();

                            // Сохраняем информацию о файле
                            fileInfos.add(new FileInfo(sizeBytes, relPath));

                        } catch (IOException e) {
                            // Игнорируем ошибки чтения отдельных файлов
                        }
                    });
        } catch (IOException e) {
            // Игнорируем ошибки обхода директории
        }

        // Сортируем файлы по размеру (по возрастанию), а при равных размерах - по пути
        fileInfos.sort(Comparator.comparingInt(FileInfo::getSize)
                .thenComparing(FileInfo::getPath));

        // Выводим отсортированный список файлов
        for (FileInfo fi : fileInfos) {
            System.out.println(fi.getSize() + " " + fi.getPath());
        }

        // Выводим время выполнения в stderr
        long endTime = System.currentTimeMillis();
        System.err.println("Execution time: " + (endTime - startTime) + "ms");
    }

    /**
     * Читает файл с попыткой использовать разные кодировки
     * Пробует UTF-8, Windows-1251, ISO-8859-5, ISO-8859-1
     */
    private static String readFileWithFallback(Path filePath) throws IOException {
        List<Charset> charsets = Arrays.asList(
                StandardCharsets.UTF_8,
                Charset.forName("Windows-1251"), // Кириллица Windows
                Charset.forName("ISO-8859-5"),   // Кириллица ISO
                StandardCharsets.ISO_8859_1      // Латинский алфавит
        );

        // Пробуем прочитать файл в разных кодировках
        for (Charset charset : charsets) {
            try {
                return Files.readString(filePath, charset);
            } catch (MalformedInputException e) {
                // Если возникла ошибка декодирования, пробуем следующую кодировку
                continue;
            }
        }

        // Если ни одна кодировка не подошла, возвращаем пустую строку
        return "";
    }

    /**
     * Обрабатывает содержимое Java файла:
     * 1. Удаляет комментарии (блочные и строчные)
     * 2. Удаляет строки package и import
     * 3. Удаляет управляющие символы с краев
     * 4. Удаляет пустые строки
     */
    private static String processContent(String content) {
        if (content.isEmpty()) return content;

        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;  // Флаг нахождения внутри /* ... */ комментария
        boolean inLineComment = false;   // Флаг нахождения внутри // комментария
        boolean inString = false;        // Флаг нахождения внутри строки ""
        boolean inChar = false;          // Флаг нахождения внутри символьного литерала ''
        char prevChar = 0;               // Предыдущий символ для определения //, /* и т.д.

        // Удаление комментариев за один проход O(n)
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (inBlockComment) {
                // Находим конец блочного комментария */
                if (prevChar == '*' && c == '/') {
                    inBlockComment = false;
                }
            } else if (inLineComment) {
                // Строчный комментарий заканчивается на новой строке
                if (c == '\n') {
                    inLineComment = false;
                    result.append(c); // Сохраняем перенос строки
                }
            } else if (inString) {
                // Внутри строки - проверяем экранирование
                if (c == '"' && prevChar != '\\') {
                    inString = false;
                }
                result.append(c); // Сохраняем содержимое строки
            } else if (inChar) {
                // Внутри символьного литерала - проверяем экранирование
                if (c == '\'' && prevChar != '\\') {
                    inChar = false;
                }
                result.append(c); // Сохраняем символьный литерал
            } else {
                // Обычный текст - проверяем начало комментариев/строк
                if (prevChar == '/' && c == '*') {
                    inBlockComment = true;
                    result.deleteCharAt(result.length() - 1); // Удаляем предыдущий '/'
                } else if (prevChar == '/' && c == '/') {
                    inLineComment = true;
                    result.deleteCharAt(result.length() - 1); // Удаляем предыдущий '/'
                } else if (c == '"') {
                    inString = true;
                    result.append(c);
                } else if (c == '\'') {
                    inChar = true;
                    result.append(c);
                } else {
                    result.append(c);
                }
            }
            prevChar = c;
        }

        String withoutComments = result.toString();

        // Удаление package и импортов
        result.setLength(0); // Очищаем StringBuilder для повторного использования
        String[] lines = withoutComments.split("\r?\n");
        boolean afterPackageImports = false; // Флаг что прошли все package/import строки

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (!afterPackageImports) {
                // Пропускаем строки package и import в начале файла
                if (trimmedLine.startsWith("package") || trimmedLine.startsWith("import")) {
                    continue;
                }
                // Если нашли не-package и не-import строку, значит package/imports закончились
                if (!trimmedLine.isEmpty() && !trimmedLine.startsWith("package") &&
                        !trimmedLine.startsWith("import")) {
                    afterPackageImports = true;
                }
            }

            // Добавляем строку только если не пустая и мы уже прошли package/imports
            if (afterPackageImports && !line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        // Удаляем последний лишний перенос строки если есть
        if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        String filteredContent = result.toString();

        // Удаление управляющих символов (с кодом < 33) с начала и конца
        filteredContent = removeControlCharsFromEdges(filteredContent);

        // Удаление пустых строк
        return removeEmptyLines(filteredContent);
    }

    /**
     * Удаляет управляющие символы (пробелы, табуляции, переносы строк и т.д.)
     * с начала и конца строки
     */
    private static String removeControlCharsFromEdges(String text) {
        if (text.isEmpty()) return text;

        int start = 0;
        int end = text.length();

        // Находим первый непробельный символ
        while (start < end && text.charAt(start) < 33) {
            start++;
        }

        // Находим последний непробельный символ
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        return text.substring(start, end);
    }

    /**
     * Удаляет пустые строки из текста
     */
    private static String removeEmptyLines(String text) {
        if (text.isEmpty()) return text;

        StringBuilder result = new StringBuilder();
        String[] lines = text.split("\r?\n");

        for (String line : lines) {
            // Сохраняем только непустые строки
            if (!line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        // Удаляем последний лишний перенос строки если есть
        if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    /**
     * Вспомогательный класс для хранения информации о файле
     */
    private static class FileInfo {
        private final int size;    // Размер файла в байтах после обработки
        private final String path; // Относительный путь к файлу

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
