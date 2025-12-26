package by.it.group410902.yarmashuk.lesson15;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SourceScannerA {
    // Вспомогательный класс для хранения данных о файле (путь и размер)
    static class FileData implements Comparable<FileData> {
        long size;
        String relativePath;

        FileData(long size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }

        @Override
        public int compareTo(FileData other) {
            // Сортировка по размеру (возрастание)
            if (this.size != other.size) {
                return Long.compare(this.size, other.size);
            }
            // Если размер одинаковый, сортировка по пути (лексикографически)
            return this.relativePath.compareTo(other.relativePath);
        }
    }

    public static void main(String[] args) {
        String srcPath = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        File srcDirectory = new File(srcPath);

        // Список для хранения обработанных данных файлов
        List<FileData> processedFiles = new ArrayList<>();

        // Запускаем рекурсивный обход каталога
        processDirectory(srcDirectory, srcDirectory.getAbsolutePath(), processedFiles);

        // Сортируем файлы согласно условиям
        Collections.sort(processedFiles);

        // Выводим результаты
        for (FileData fileData : processedFiles) {
            System.out.println(fileData.size + " " + fileData.relativePath);
        }
    }

    private static void processDirectory(File directory, String srcBasePath, List<FileData> processedFiles) {
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Каталог не найден или не является каталогом: " + directory.getAbsolutePath());
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // Если это подкаталог, рекурсивно вызываем обработку
                processDirectory(file, srcBasePath, processedFiles);
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                // Если это .java файл, обрабатываем его
                try {
                    String fileContent = readFileContent(file);

                    // Проверяем на @Test или org.junit.Test
                    if (fileContent.contains("@Test") || fileContent.contains("org.junit.Test")) {
                        continue; // Пропускаем тестовый файл
                    }

                    // Обрабатываем текст файла
                    String cleanedContent = cleanFileContent(fileContent);

                    // Вычисляем размер в байтах (используя UTF-8, как наиболее распространенную кодировку)

                    long sizeInBytes = cleanedContent.getBytes(Charset.forName("UTF-8")).length;

                    // Вычисляем относительный путь
                    String relativePath = file.getAbsolutePath().substring(srcBasePath.length());

                    processedFiles.add(new FileData(sizeInBytes, relativePath));

                } catch (IOException e) {
                    System.err.println("Ошибка чтения файла " + file.getAbsolutePath() + ": " + e.getMessage());
                }
            }
        }
    }


    private static String readFileContent(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));

        // Используем CharsetDecoder для обработки MalformedInputException
        // Попытаемся использовать UTF-8, если не сработает, то платформенную или другую.
        // Для демонстрации MalformedInputException, используем UTF-8
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        decoder.onMalformedInput(CodingErrorAction.IGNORE); // Игнорировать некорректные последовательности байтов
        decoder.onUnmappableCharacter(CodingErrorAction.IGNORE); // Игнорировать некартируемые символы

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try {
            return decoder.decode(buffer).toString();
        } catch (CharacterCodingException e) {
            System.err.println("Проблема с декодированием файла (MalformedInputException) " + file.getAbsolutePath() + ". Часть символов могла быть потеряна.");
            return new String(bytes, Arrays.toString(Charset.forName("UTF-8").newDecoder().onMalformedInput(CodingErrorAction.IGNORE).onUnmappableCharacter(CodingErrorAction.IGNORE).decode(ByteBuffer.wrap(bytes)).array()));
        }
    }


     // удаляет package/import, удаляет символы <33 в начале/конце
    private static String cleanFileContent(String content) {
        StringBuilder sb = new StringBuilder();
        String[] lines = content.split("\r?\n"); // Разбиваем на строки, учитывая разные окончания строк

        for (String line : lines) {
            String trimmedLine = line.trim(); // Удаляем пробельные символы в начале и конце строки

            // Пропускаем строки package и import
            if (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")) {
                continue;
            }
            sb.append(line).append('\n'); // Добавляем исходную строку (не trimmedLine), но с новым переводом строки
        }

        // Удаляем лишний перевод строки в конце, если он есть
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }

        String result = sb.toString();

        // Удаляем символы с кодом <33 в начале и конце всего текста
        int start = 0;
        while (start < result.length() && result.charAt(start) < 33) {
            start++;
        }

        int end = result.length();
        while (end > start && result.charAt(end - 1) < 33) {
            end--;
        }

        return result.substring(start, end);
    }
}
