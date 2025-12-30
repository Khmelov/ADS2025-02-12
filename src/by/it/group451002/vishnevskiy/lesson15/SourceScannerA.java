package by.it.group451002.vishnevskiy.lesson15;

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

public class SourceScannerA {

    public static void main(String[] args) {
        // Получаем путь к директории src в текущем проекте
        // user.dir - текущая рабочая директория (корень проекта)
        // File.separator - разделитель пути для текущей ОС (например, "/" в Linux/Mac, "\" в Windows)
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

        // Используем Stream API для обхода всех файлов и поддиректорий
        try (Stream<Path> paths = Files.walk(srcPath)) {
            // Фильтруем только Java-файлы
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

        // Сортировка файлов:
        // 1. Сначала по размеру файла (возрастание)
        // 2. При равных размерах - по относительному пути (алфавитный порядок)
        fileInfos.sort(Comparator.comparingInt((FileInfo fi) -> fi.size)
                .thenComparing(fi -> fi.relativePath));

        // Вывод результатов в формате: размер пробел путь
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

        // Обрабатываем содержимое: удаляем package, imports, лишние пробелы
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

    // Метод для обработки содержимого Java-файла
    private static String processContent(String content) {
        // Используем StringBuilder для эффективного построения строки
        StringBuilder result = new StringBuilder(content.length());

        // Разделяем содержимое на строки
        String[] lines = content.split("\n", -1); // -1 сохраняет пустые строки в конце

        for (String line : lines) {
            String trimmed = line.trim(); // Удаляем пробелы в начале и конце строки

            // Пропускаем строки с объявлением пакета и импортами
            if (trimmed.startsWith("package ") ||
                    trimmed.startsWith("import ")) {
                continue; // Переходим к следующей строке
            }
            // Добавляем исходную строку (с сохранением форматирования)
            result.append(line).append('\n');
        }

        // Удаляем последний символ переноса строки, если он есть
        if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        // Получаем обработанный текст
        String text = result.toString();
        int start = 0;
        int end = text.length();

        // Удаляем непечатаемые символы (код <33) в начале строки
        // Коды 0-32: управляющие символы, пробелы, табуляции
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