package by.it.group410901.kliaus.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerA {

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
        int writePos = 0;
        int i = 0;

        // Удаляем package и imports за один проход
        while (i < len) {
            // Пропускаем пробелы и символы < 33
            while (i < len && chars[i] <= 32) {
                i++;
            }

            if (i >= len) break;

            // Проверяем начало строки на package или import
            if (i + 7 <= len && chars[i] == 'p' && chars[i+1] == 'a' && chars[i+2] == 'c'
                    && chars[i+3] == 'k' && chars[i+4] == 'a' && chars[i+5] == 'g' && chars[i+6] == 'e'
                    && (i + 7 >= len || chars[i+7] <= 32)) {
                // Пропускаем всю строку package
                while (i < len && chars[i] != '\n') {
                    i++;
                }
                if (i < len) i++; // пропускаем \n
                continue;
            }

            if (i + 6 <= len && chars[i] == 'i' && chars[i+1] == 'm' && chars[i+2] == 'p'
                    && chars[i+3] == 'o' && chars[i+4] == 'r' && chars[i+5] == 't'
                    && (i + 6 >= len || chars[i+6] <= 32)) {
                // Пропускаем всю строку import
                while (i < len && chars[i] != '\n') {
                    i++;
                }
                if (i < len) i++; // пропускаем \n
                continue;
            }

            // Копируем символ
            chars[writePos++] = chars[i++];
        }

        // Удаляем символы < 33 в начале
        int start = 0;
        while (start < writePos && chars[start] < 33) {
            start++;
        }

        // Удаляем символы < 33 в конце
        int end = writePos;
        while (end > start && chars[end - 1] < 33) {
            end--;
        }

        return new String(chars, start, end - start);
    }
}