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

public class SourceScannerA {

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


    private static String readFileWithFallback(Path path) throws IOException {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            try {
                return Files.readString(path, Charset.forName("Windows-1251"));
            } catch (IOException e2) {
                try {
                    return Files.readString(path, StandardCharsets.ISO_8859_1);
                } catch (IOException e3) {
                    return "";
                }
            }
        }
    }

    /**
     * Обработка содержимого файла:
     * 1. Удаление package и импортов
     * 2. Удаление символов с кодом <33 в начале и конце
     */
    private static String processFileContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        // Разбиваем содержимое на строки для обработки
        String[] lines = content.split("\\r?\\n");
        StringBuilder result = new StringBuilder();

        boolean inComment = false;

        for (String line : lines) {
            String trimmedLine = line.trim();

            // Пропускаем package и import statements
            if (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")) {
                continue;
            }

            // Обработка многострочных комментариев
            if (inComment) {
                if (trimmedLine.contains("*/")) {
                    inComment = false;
                    // Добавляем часть после комментария
                    String afterComment = trimmedLine.substring(trimmedLine.indexOf("*/") + 2).trim();
                    if (!afterComment.isEmpty()) {
                        result.append(afterComment).append("\n");
                    }
                }
                continue;
            }

            // Проверка начала многострочного комментария
            if (trimmedLine.contains("/*")) {
                inComment = true;
                // Добавляем часть до комментария
                String beforeComment = trimmedLine.substring(0, trimmedLine.indexOf("/*")).trim();
                if (!beforeComment.isEmpty()) {
                    result.append(beforeComment).append("\n");
                }
                // Проверяем, не заканчивается ли комментарий в этой же строке
                if (trimmedLine.contains("*/")) {
                    inComment = false;
                    String afterComment = trimmedLine.substring(trimmedLine.indexOf("*/") + 2).trim();
                    if (!afterComment.isEmpty()) {
                        result.append(afterComment).append("\n");
                    }
                }
                continue;
            }

            // Пропускаем пустые строки и однострочные комментарии
            if (trimmedLine.isEmpty() || trimmedLine.startsWith("//")) {
                continue;
            }

            // Добавляем обработанную строку
            result.append(line).append("\n");
        }

        // Удаляем символы с кодом <33 в начале и конце
        String processed = result.toString();
        processed = removeControlCharsFromStartAndEnd(processed);

        return processed;
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