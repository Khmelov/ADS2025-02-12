package by.it.group451001.tsurko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();
            // Пропускаем package и import строки
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue;
            }
            result.append(line).append("\n");
        }

        return result.toString();
    }

    // Удаляем комментарии из содержимого файла
    static String removeComments(String content) {
        StringBuilder result = new StringBuilder(); // Строка для результата
        boolean inSingleLineComment = false; // Флаг: находимся ли мы в однострочном комментарии //
        boolean inMultiLineComment = false; // Флаг: находимся ли мы в многострочном комментарии /* */
        boolean inString = false; // Флаг: находимся ли мы внутри строки
        char stringChar = 0; // Символ, которым открыта строка (" или ')

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i); // Текущий символ

            if (inString) {
                // Если мы внутри строки, просто добавляем символ
                result.append(c);
                // Проверяем, не закрылась ли строка (учитываем экранирование)
                if (c == stringChar && (i == 0 || content.charAt(i - 1) != '\\')) {
                    inString = false; // Строка закрылась
                }
            } else if (inMultiLineComment) {
                // Если мы в многострочном комментарии, ищем его конец */
                if (c == '*' && i + 1 < content.length() && content.charAt(i + 1) == '/') {
                    inMultiLineComment = false; // Комментарий закончился
                    i++; // Пропускаем '/'
                }
            } else if (inSingleLineComment) {
                // Если мы в однострочном комментарии, ждём конца строки
                if (c == '\n') {
                    inSingleLineComment = false; // Комментарий закончился
                    result.append(c); // Добавляем символ новой строки
                }
            } else {
                // Обычный режим: ищем начало комментариев или строк
                if (c == '"' || c == '\'') {
                    // Начало строки
                    inString = true; // Входим в режим строки
                    stringChar = c; // Запоминаем тип кавычек
                    result.append(c); // Добавляем кавычку
                } else if (c == '/' && i + 1 < content.length()) {
                    // Возможно начало комментария
                    char next = content.charAt(i + 1);
                    if (next == '/') {
                        // Однострочный комментарий //
                        inSingleLineComment = true;
                        i++; // Пропускаем второй '/'
                    } else if (next == '*') {
                        // Многострочный комментарий /*
                        inMultiLineComment = true;
                        i++; // Пропускаем '*'
                    } else {
                        result.append(c); // Обычный символ '/'
                    }
                } else {
                    result.append(c); // Обычный символ
                }
            }
        }

        return result.toString(); // Возвращаем содержимое без комментариев
    }

    static String removeControlChars(String text) {
        // Удаляем символы с кодом < 33 в начале и конце
        int start = 0;
        while (start < text.length() && text.charAt(start) < 33) {
            start++;
        }
        int end = text.length();
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }
        return text.substring(start, end);
    }

    // Удаляем пустые строки из текста
    static String removeEmptyLines(String text) {
        StringBuilder result = new StringBuilder(); // Строка для результата
        String[] lines = text.split("\n"); // Разбиваем текст на строки
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                // Если строка не пустая (после удаления пробелов), добавляем её
                result.append(line).append("\n");
            }
        }
        return result.toString(); // Возвращаем текст без пустых строк
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            Files.walk(srcPath)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        // Пробуем разные кодировки для обработки MalformedInputException
                        String content = null;
                        Charset[] charsets = {StandardCharsets.UTF_8, Charset.forName("Windows-1251"), StandardCharsets.ISO_8859_1};

                        for (Charset charset : charsets) {
                            try {
                                content = Files.readString(p, charset);
                                break;
                            } catch (IOException e) {
                                // Пробуем следующую кодировку
                            }
                        }

                        if (content == null) {
                            return; // Не удалось прочитать файл
                        }

                        // Пропускаем тесты
                        if (content.contains("@Test") || content.contains("org.junit.Test")) {
                            return;
                        }

                        // Обрабатываем файл
                        content = removePackageAndImports(content); // Удаляем package и import
                        content = removeComments(content); // Удаляем комментарии
                        content = removeControlChars(content); // Удаляем управляющие символы
                        content = removeEmptyLines(content); // Удаляем пустые строки

                        // Вычисляем размер в байтах
                        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
                        int size = bytes.length;

                        // Получаем относительный путь (используем тот же формат, что в тесте)
                        String relativePath = srcPath.relativize(p).toString();
                        // НЕ заменяем разделители - оставляем как есть (Windows использует \)

                        fileInfos.add(new FileInfo(relativePath, size));

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортируем: сначала по размеру (по возрастанию), затем лексикографически по пути
        fileInfos.sort(new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo a, FileInfo b) {
                int sizeCompare = Integer.compare(a.size, b.size);
                if (sizeCompare != 0) {
                    return sizeCompare;
                }
                return a.path.compareTo(b.path);
            }
        });

        // Выводим результаты
        for (FileInfo info : fileInfos) {
            System.out.println(info.size + " " + info.path);
        }
    }
}
