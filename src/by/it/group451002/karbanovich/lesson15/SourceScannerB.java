package by.it.group451002.karbanovich.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerB {

    public static void main(String[] args) {
        // Получаем путь к директории src в текущем проекте
        // user.dir - текущая рабочая директория (корень проекта)
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        // Преобразуем строку в объект Path для работы с файловой системой
        Path srcPath = Paths.get(src);

        // Проверяем, существует ли директория src
        if (!Files.exists(srcPath)) {
            System.err.println("Каталог src не найден: " + src);
            return; // Завершаем программу, если директория не найдена
        }

        // Список для хранения информации о файлах
        List<FileInfo> fileInfos = new ArrayList<>();

        // Используем Stream API для рекурсивного обхода всех файлов и поддиректорий
        try (Stream<Path> paths = Files.walk(srcPath)) {
            // Фильтруем только Java-файлы (с расширением .java)
            paths.filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            // Обрабатываем каждый Java-файл
                            processFile(path, srcPath, fileInfos);
                        } catch (IOException e) {
                            // Игнорируем ошибки чтения файлов (например, нет прав доступа)
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталогов: " + e.getMessage());
            return;
        }

        // Сортировка файлов по двум критериям:
        // 1. Сначала по размеру файла (возрастание)
        // 2. При равных размерах - по относительному пути (лексикографический порядок)
        fileInfos.sort(Comparator.comparingInt((FileInfo fi) -> fi.size)
                .thenComparing(fi -> fi.relativePath));

        // Вывод результатов в формате: размер_файла пробел относительный_путь
        for (FileInfo info : fileInfos) {
            System.out.println(info.size + " " + info.relativePath);
        }
    }

    // Метод для обработки одного файла
    private static void processFile(Path filePath, Path srcPath, List<FileInfo> fileInfos)
            throws IOException {
        // Читаем содержимое файла, игнорируя ошибки кодирования
        String content = readFileIgnoringErrors(filePath);

        // Пропускаем файлы с тестами
        // Если файл содержит аннотации @Test, значит это тестовый файл
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return; // Не добавляем тестовые файлы в результат
        }

        // Обрабатываем содержимое: удаляем package, imports, комментарии и лишние пробелы
        String processed = processContent(content);

        // Вычисляем размер обработанного содержимого в байтах (UTF-8 кодировка)
        int size = processed.getBytes(StandardCharsets.UTF_8).length;

        // Получаем относительный путь файла (от директории src)
        String relativePath = srcPath.relativize(filePath).toString();

        // Сохраняем информацию о файле
        fileInfos.add(new FileInfo(size, relativePath));
    }

    // Метод для чтения файла с обработкой ошибок кодирования
    private static String readFileIgnoringErrors(Path filePath) throws IOException {
        // Читаем все байты из файла
        byte[] bytes = Files.readAllBytes(filePath);

        // Создаем декодер UTF-8 с указанием действий при ошибках:
        // IGNORE - игнорировать некорректные символы вместо выброса исключения
        return StandardCharsets.UTF_8
                .newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE)
                .decode(java.nio.ByteBuffer.wrap(bytes))
                .toString();
    }

    // Метод для обработки содержимого Java-файла с удалением комментариев
    private static String processContent(String content) {
        // Используем StringBuilder для эффективного построения строки
        StringBuilder result = new StringBuilder(content.length());

        int i = 0;                      // Текущая позиция в строке
        int len = content.length();     // Длина содержимого файла
        boolean inLineComment = false;  // Флаг нахождения в однострочном комментарии
        boolean inBlockComment = false; // Флаг нахождения в многострочном комментарии
        StringBuilder currentLine = new StringBuilder(); // Буфер для текущей строки

        // Проходим по всем символам содержимого файла
        while (i < len) {
            char c = content.charAt(i);

            // Обработка начала многострочного комментария (/*)
            if (!inLineComment && !inBlockComment && i + 1 < len
                    && c == '/' && content.charAt(i + 1) == '*') {
                inBlockComment = true;
                i += 2; // Пропускаем символы "/*"
                continue; // Переходим к следующей итерации
            }

            // Если находимся в многострочном комментарии
            if (inBlockComment) {
                // Проверяем конец многострочного комментария (*/)
                if (i + 1 < len && c == '*' && content.charAt(i + 1) == '/') {
                    inBlockComment = false; // Выходим из комментария
                    i += 2; // Пропускаем символы "*/"
                } else {
                    i++; // Пропускаем символ внутри комментария
                }
                continue; // Переходим к следующей итерации
            }

            // Обработка начала однострочного комментария (//)
            if (!inLineComment && i + 1 < len
                    && c == '/' && content.charAt(i + 1) == '/') {
                inLineComment = true;
                i += 2; // Пропускаем символы "//"
                continue; // Переходим к следующей итерации
            }

            // Если находимся в однострочном комментарии
            if (inLineComment) {
                // Конец строки означает конец однострочного комментария
                if (c == '\n') {
                    inLineComment = false;
                    // Обрабатываем накопленную строку (до комментария)
                    processLine(currentLine.toString(), result);
                    currentLine.setLength(0); // Очищаем буфер строки
                }
                i++;
                continue; // Переходим к следующей итерации
            }

            // Обработка обычных символов (не комментарии)
            if (c == '\n') {
                // Конец строки - обрабатываем накопленную строку
                processLine(currentLine.toString(), result);
                currentLine.setLength(0); // Очищаем буфер строки
            } else {
                // Добавляем символ в текущую строку
                currentLine.append(c);
            }

            i++; // Переходим к следующему символу
        }

        // Обрабатываем последнюю строку (если файл не заканчивается символом \n)
        if (currentLine.length() > 0) {
            processLine(currentLine.toString(), result);
        }

        // Удаляем последний символ переноса строки, если он есть
        if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        // Удаляем непечатаемые символы (код <33) в начале и конце результата
        // Коды 0-32: управляющие символы, пробелы, табуляции, переносы строк
        String text = result.toString();
        int start = 0;
        int end = text.length();

        // Удаляем непечатаемые символы в начале строки
        while (start < end && text.charAt(start) < 33) {
            start++;
        }

        // Удаляем непечатаемые символы в конце строки
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        // Возвращаем подстроку без лишних символов в начале и конце
        return text.substring(start, end);
    }

    // Метод для обработки одной строки кода
    private static void processLine(String line, StringBuilder result) {
        // Удаляем пробелы в начале и конце строки
        String trimmed = line.trim();

        // Пропускаем строки с объявлением пакета и импортами
        if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
            return; // Не добавляем эти строки в результат
        }

        // Пропускаем пустые строки (после удаления комментариев)
        if (trimmed.isEmpty()) {
            return; // Не добавляем пустые строки в результат
        }

        // Добавляем непустую строку с сохранением исходного форматирования
        result.append(line).append('\n');
    }

    // Вспомогательный класс для хранения информации о файле
    static class FileInfo {
        int size;              // Размер обработанного содержимого в байтах
        String relativePath;   // Относительный путь от директории src

        FileInfo(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}
