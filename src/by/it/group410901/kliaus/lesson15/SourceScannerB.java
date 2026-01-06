package by.it.group410901.kliaus.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerB {

    static class FileInfo {
        String path;
        int size;

        FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);

        List<FileInfo> files = new ArrayList<>();

        try {
            Files.walk(root)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = Files.readString(p);

                            // Пропускаем тестовые файлы
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Обрабатываем содержимое
                            String processed = processContent(content);

                            // Получаем относительный путь
                            String relativePath = root.relativize(p).toString();

                            // Добавляем в список
                            files.add(new FileInfo(relativePath, processed.getBytes().length));

                        } catch (MalformedInputException e) {
                            // Игнорируем MalformedInputException
                        } catch (IOException e) {
                            // Игнорируем другие ошибки чтения
                        }
                    });

            // Сортируем: сначала по размеру, потом лексикографически
            files.sort(Comparator.comparingInt((FileInfo f) -> f.size)
                    .thenComparing(f -> f.path));

            // Выводим результат
            for (FileInfo file : files) {
                System.out.println(file.size + " " + file.path);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Обработка содержимого файла за O(n)
    private static String processContent(String content) {
        char[] chars = content.toCharArray();
        int len = chars.length;
        char[] result = new char[len];
        int writePos = 0;
        int i = 0;

        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean inChar = false;

        // Единый проход для удаления package, import и комментариев
        while (i < len) {
            // Обработка строковых литералов
            if (!inBlockComment && !inLineComment) {
                if (chars[i] == '"' && (i == 0 || chars[i-1] != '\\')) {
                    inString = !inString;
                    result[writePos++] = chars[i++];
                    continue;
                }
                if (chars[i] == '\'' && (i == 0 || chars[i-1] != '\\')) {
                    inChar = !inChar;
                    result[writePos++] = chars[i++];
                    continue;
                }
            }

            if (inString || inChar) {
                result[writePos++] = chars[i++];
                continue;
            }

            // Начало блочного комментария
            if (!inLineComment && i + 1 < len && chars[i] == '/' && chars[i+1] == '*') {
                inBlockComment = true;
                i += 2;
                continue;
            }

            // Конец блочного комментария
            if (inBlockComment && i + 1 < len && chars[i] == '*' && chars[i+1] == '/') {
                inBlockComment = false;
                i += 2;
                continue;
            }

            // Начало строчного комментария
            if (!inBlockComment && i + 1 < len && chars[i] == '/' && chars[i+1] == '/') {
                inLineComment = true;
                i += 2;
                continue;
            }

            // Конец строчного комментария
            if (inLineComment && chars[i] == '\n') {
                inLineComment = false;
                result[writePos++] = chars[i++];
                continue;
            }

            // Пропускаем символы внутри комментариев
            if (inBlockComment || inLineComment) {
                i++;
                continue;
            }

            // Проверяем package/import в начале строки
            // Пропускаем пробелы
            int lineStart = writePos;
            while (i < len && chars[i] <= 32 && chars[i] != '\n') {
                i++;
            }

            // Проверяем package
            if (i + 7 <= len && chars[i] == 'p' && chars[i+1] == 'a' && chars[i+2] == 'c'
                    && chars[i+3] == 'k' && chars[i+4] == 'a' && chars[i+5] == 'g' && chars[i+6] == 'e'
                    && (i + 7 >= len || chars[i+7] <= 32)) {
                while (i < len && chars[i] != '\n') {
                    i++;
                }
                if (i < len) i++;
                continue;
            }

            // Проверяем import
            if (i + 6 <= len && chars[i] == 'i' && chars[i+1] == 'm' && chars[i+2] == 'p'
                    && chars[i+3] == 'o' && chars[i+4] == 'r' && chars[i+5] == 't'
                    && (i + 6 >= len || chars[i+6] <= 32)) {
                while (i < len && chars[i] != '\n') {
                    i++;
                }
                if (i < len) i++;
                continue;
            }

            // Копируем символ
            result[writePos++] = chars[i++];
        }

        // Удаляем пустые строки
        StringBuilder sb = new StringBuilder(writePos);
        boolean lineEmpty = true;
        for (i = 0; i < writePos; i++) {
            char c = result[i];
            if (c == '\n') {
                if (!lineEmpty) {
                    sb.append(c);
                }
                lineEmpty = true;
            } else if (c > 32) {
                sb.append(c);
                lineEmpty = false;
            } else if (!lineEmpty) {
                sb.append(c);
            }
        }

        String processed = sb.toString();

        // Удаляем символы < 33 в начале
        int start = 0;
        while (start < processed.length() && processed.charAt(start) < 33) {
            start++;
        }

        // Удаляем символы < 33 в конце
        int end = processed.length();
        while (end > start && processed.charAt(end - 1) < 33) {
            end--;
        }

        return processed.substring(start, end);
    }
}