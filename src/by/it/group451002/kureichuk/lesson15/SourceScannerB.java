package by.it.group451002.kureichuk.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path rootPath = Paths.get(src);

        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            // Рекурсивный обход всех файлов в каталоге src
            Files.walk(rootPath)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            // Чтение файла с обработкой ошибок кодировки
                            String content = readFileWithFallback(path);

                            // Пропускаем файлы с тестами
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Обработка содержимого файла
                            String processedContent = processFileContent(content);

                            // Получаем относительный путь
                            String relativePath = rootPath.relativize(path).toString();

                            // Вычисляем размер обработанного содержимого в байтах
                            int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

                            fileInfos.add(new FileInfo(relativePath, size));

                        } catch (IOException e) {
                            // Игнорируем ошибки чтения файлов
                        }
                    });

            // Сортировка: по размеру (возрастание), при равенстве - лексикографически по пути
            Collections.sort(fileInfos);

            // Вывод результатов
            for (FileInfo fileInfo : fileInfos) {
                System.out.println(fileInfo.size + " " + fileInfo.path);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталога: " + e.getMessage());
        }
    }

    /**
     * Чтение файла с обработкой различных кодировок
     */
    private static String readFileWithFallback(Path path) throws IOException {
        Charset[] charsets = {
                StandardCharsets.UTF_8,
                Charset.forName("Windows-1251"),
                StandardCharsets.ISO_8859_1,
                StandardCharsets.US_ASCII
        };

        for (Charset charset : charsets) {
            try {
                return Files.readString(path, charset);
            } catch (IOException e) {
                // Пробуем следующую кодировку
            }
        }
        return "";
    }

    /**
     * Обработка содержимого файла за O(n):
     * 1. Удаление package и импортов
     * 2. Удаление комментариев
     * 3. Удаление символов с кодом <33 в начале и конце
     * 4. Удаление пустых строк
     */
    private static String processFileContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        int length = content.length();
        int i = 0;
        boolean inSingleLineComment = false;
        boolean inMultiLineComment = false;
        boolean inString = false;
        boolean inChar = false;
        char prevChar = 0;

        while (i < length) {
            char currentChar = content.charAt(i);

            if (inSingleLineComment) {
                if (currentChar == '\n') {
                    inSingleLineComment = false;
                    result.append('\n');
                }
                i++;
                continue;
            }

            if (inMultiLineComment) {
                if (prevChar == '*' && currentChar == '/') {
                    inMultiLineComment = false;
                }
                prevChar = currentChar;
                i++;
                continue;
            }

            if (inString) {
                if (currentChar == '"' && prevChar != '\\') {
                    inString = false;
                }
                result.append(currentChar);
                prevChar = currentChar;
                i++;
                continue;
            }

            if (inChar) {
                if (currentChar == '\'' && prevChar != '\\') {
                    inChar = false;
                }
                result.append(currentChar);
                prevChar = currentChar;
                i++;
                continue;
            }

            // Проверка начала однострочного комментария
            if (currentChar == '/' && i + 1 < length && content.charAt(i + 1) == '/') {
                inSingleLineComment = true;
                i += 2;
                continue;
            }

            // Проверка начала многострочного комментария
            if (currentChar == '/' && i + 1 < length && content.charAt(i + 1) == '*') {
                inMultiLineComment = true;
                i += 2;
                continue;
            }

            // Проверка начала строки
            if (currentChar == '"') {
                inString = true;
                result.append(currentChar);
                prevChar = currentChar;
                i++;
                continue;
            }

            // Проверка начала символьного литерала
            if (currentChar == '\'') {
                inChar = true;
                result.append(currentChar);
                prevChar = currentChar;
                i++;
                continue;
            }

            result.append(currentChar);
            prevChar = currentChar;
            i++;
        }

        // Теперь обрабатываем результат: удаляем package, imports, пустые строки и обрезаем
        String withoutComments = result.toString();
        return removePackageImportsAndEmptyLines(withoutComments);
    }

    /**
     * Удаляет package, imports и пустые строки
     */
    private static String removePackageImportsAndEmptyLines(String content) {
        String[] lines = content.split("\\r?\\n");
        StringBuilder result = new StringBuilder();
        boolean firstNonEmpty = false;

        for (String line : lines) {
            String trimmed = line.trim();

            // Пропускаем package и import statements
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue;
            }

            // Пропускаем пустые строки
            if (trimmed.isEmpty()) {
                continue;
            }

            // Добавляем строку
            if (firstNonEmpty) {
                result.append("\n");
            }
            result.append(line);
            firstNonEmpty = true;
        }

        // Удаляем символы с кодом <33 из начала и конца
        return removeControlCharsFromStartAndEnd(result.toString());
    }

    /**
     * Удаляет символы с кодом <33 из начала и конца строки
     */
    private static String removeControlCharsFromStartAndEnd(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        // Удаляем из начала
        int start = 0;
        while (start < str.length() && str.charAt(start) < 33) {
            start++;
        }

        // Удаляем из конца
        int end = str.length();
        while (end > start && str.charAt(end - 1) < 33) {
            end--;
        }

        return str.substring(start, end);
    }

    /**
     * Вспомогательный класс для хранения информации о файле
     */
    static class FileInfo implements Comparable<FileInfo> {
        String path;
        int size;

        FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
        }

        @Override
        public int compareTo(FileInfo other) {
            // Сначала сравниваем по размеру
            if (this.size != other.size) {
                return Integer.compare(this.size, other.size);
            }
            // При равном размере - лексикографически по пути
            return this.path.compareTo(other.path);
        }
    }
}