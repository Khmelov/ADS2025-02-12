package by.it.group451001.yarkovich.lesson15;
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

    // Вспомогательный класс для хранения данных о файле
    static class FileData implements Comparable<FileData> {
        long size;           // размер файла в байтах
        String relativePath; // относительный путь файла

        FileData(long size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }

        @Override
        public int compareTo(FileData other) {
            // Сначала сортируем по размеру (по возрастанию)
            if (this.size != other.size) {
                return Long.compare(this.size, other.size);
            }
            // При равных размерах - лексикографически по пути
            return this.relativePath.compareTo(other.relativePath);
        }
    }

    public static void main(String[] args) {
        // Получаем путь к каталогу src текущего проекта
        String srcPath = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        File srcDirectory = new File(srcPath);

        // Список для хранения обработанных файлов
        List<FileData> processedFiles = new ArrayList<>();

        // Рекурсивно обрабатываем каталог src
        processDirectory(srcDirectory, srcDirectory.getAbsolutePath(), processedFiles);

        // Сортируем файлы согласно условиям задачи
        Collections.sort(processedFiles);

        // Выводим результаты: размер и относительный путь для каждого файла
        for (FileData fileData : processedFiles) {
            System.out.println(fileData.size + " " + fileData.relativePath);
        }
    }

    // Рекурсивный метод для обработки каталога и его подкаталогов
    private static void processDirectory(File directory, String srcBasePath, List<FileData> processedFiles) {
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Каталог не найден или не является каталогом: " + directory.getAbsolutePath());
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        // Обрабатываем все файлы и подкаталоги
        for (File file : files) {
            if (file.isDirectory()) {
                // Рекурсивный вызов для подкаталога
                processDirectory(file, srcBasePath, processedFiles);
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                // Обрабатываем только .java файлы
                try {
                    String fileContent = readFileContent(file);

                    // Пропускаем тестовые файлы (содержащие @Test)
                    if (fileContent.contains("@Test") || fileContent.contains("org.junit.Test")) {
                        continue;
                    }

                    // Очищаем содержимое файла (удаляем package, import и т.д.)
                    String cleanedContent = cleanFileContent(fileContent);

                    // Вычисляем размер очищенного содержимого в байтах (UTF-8)
                    long sizeInBytes = cleanedContent.getBytes(Charset.forName("UTF-8")).length;

                    // Вычисляем относительный путь относительно каталога src
                    String relativePath = file.getAbsolutePath().substring(srcBasePath.length());

                    // Добавляем файл в список для вывода
                    processedFiles.add(new FileData(sizeInBytes, relativePath));

                } catch (IOException e) {
                    System.err.println("Ошибка чтения файла " + file.getAbsolutePath() + ": " + e.getMessage());
                }
            }
        }
    }

    // Метод для чтения содержимого файла с обработкой ошибок кодировки
    private static String readFileContent(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));

        // Создаем декодер UTF-8 с обработкой ошибок
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        decoder.onMalformedInput(CodingErrorAction.IGNORE);     // Игнорировать некорректные байты
        decoder.onUnmappableCharacter(CodingErrorAction.IGNORE); // Игнорировать неотображаемые символы

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try {
            return decoder.decode(buffer).toString();
        } catch (CharacterCodingException e) {
            // Резервный вариант при ошибках декодирования
            System.err.println("Проблема с декодированием файла " + file.getAbsolutePath());
            return new String(bytes, Charset.forName("UTF-8"));
        }
    }

    // Метод для очистки содержимого файла согласно требованиям задачи
    private static String cleanFileContent(String content) {
        StringBuilder sb = new StringBuilder();
        String[] lines = content.split("\r?\n"); // Обрабатываем разные форматы переносов строк

        for (String line : lines) {
            String trimmedLine = line.trim();

            // Пропускаем строки package и import
            if (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")) {
                continue;
            }

            // Добавляем исходную строку (сохраняем форматирование)
            sb.append(line).append('\n');
        }

        // Удаляем последний перенос строки если есть
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }

        String result = sb.toString();

        // Удаляем управляющие символы (код < 33) в начале и конце текста
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