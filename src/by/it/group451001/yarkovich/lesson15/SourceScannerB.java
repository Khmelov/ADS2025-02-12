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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceScannerB {

    // Вспомогательный класс для хранения данных о файле
    static class FileData implements Comparable<FileData> {
        long size;           // размер файла в байтах после очистки
        String relativePath; // относительный путь файла

        FileData(long size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }

        @Override
        public int compareTo(FileData other) {
            // Сортировка по размеру (возрастание), при равных - по пути
            if (this.size != other.size) {
                return Long.compare(this.size, other.size);
            }
            return this.relativePath.compareTo(other.relativePath);
        }
    }

    public static void main(String[] args) {
        // Определяем путь к каталогу src проекта
        String srcPath = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        File srcDirectory = new File(srcPath);

        // Список для хранения обработанных файлов
        List<FileData> processedFiles = new ArrayList<>();

        // Рекурсивная обработка каталога src
        processDirectory(srcDirectory, srcDirectory.getAbsolutePath(), processedFiles);

        // Сортировка файлов по размеру и пути
        Collections.sort(processedFiles);

        // Вывод результатов
        for (FileData fileData : processedFiles) {
            System.out.println(fileData.size + " " + fileData.relativePath);
        }
    }

    // Рекурсивный метод обхода каталогов
    private static void processDirectory(File directory, String srcBasePath, List<FileData> processedFiles) {
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Каталог не найден: " + directory.getAbsolutePath());
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // Рекурсивный вызов для подкаталогов
                processDirectory(file, srcBasePath, processedFiles);
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                // Обработка Java-файлов
                try {
                    String fileContent = readFileContent(file);

                    // Пропускаем тестовые файлы
                    if (fileContent.contains("@Test") || fileContent.contains("org.junit.Test")) {
                        continue;
                    }

                    // Очистка содержимого файла
                    String cleanedContent = cleanFileContent(fileContent);

                    // Вычисление размера очищенного содержимого
                    long sizeInBytes = cleanedContent.getBytes(Charset.forName("UTF-8")).length;

                    // Получение относительного пути
                    String relativePath = file.getAbsolutePath().substring(srcBasePath.length());

                    processedFiles.add(new FileData(sizeInBytes, relativePath));

                } catch (IOException e) {
                    System.err.println("Ошибка чтения файла: " + file.getAbsolutePath());
                }
            }
        }
    }

    // Метод чтения файла с обработкой ошибок кодировки
    private static String readFileContent(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));

        // Декодер UTF-8 с заменой некорректных символов
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        decoder.onMalformedInput(CodingErrorAction.REPLACE);    // Замена некорректных байтов
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE); // Замена неотображаемых символов

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try {
            return decoder.decode(buffer).toString();
        } catch (CharacterCodingException e) {
            // Резервный вариант при ошибках декодирования
            System.err.println("Проблема с декодированием файла: " + file.getAbsolutePath());
            return new String(bytes, Charset.forName("UTF-8"));
        }
    }

    // Основной метод очистки содержимого файла
    private static String cleanFileContent(String content) {
        // 1. Удаление всех комментариев (однострочных и многострочных)
        String contentWithoutComments = removeComments(content);

        // 2. Удаление package/import и пустых строк
        StringBuilder sb = new StringBuilder();
        String[] lines = contentWithoutComments.split("\r?\n");

        // Паттерн для определения пустых строк (только пробельные символы)
        Pattern emptyLinePattern = Pattern.compile("^\\s*$");

        for (String line : lines) {
            String trimmedLine = line.trim();

            // Пропускаем строки package и import
            if (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")) {
                continue;
            }

            // Добавляем только непустые строки
            if (!emptyLinePattern.matcher(line).matches()) {
                sb.append(line).append('\n');
            }
        }

        // Удаление последнего переноса строки
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }

        String result = sb.toString();

        // 3. Удаление управляющих символов в начале и конце текста
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

    // Метод для удаления комментариев из Java-кода
    private static String removeComments(String content) {
        StringBuilder sb = new StringBuilder();
        boolean inBlockComment = false; // флаг многострочного комментария
        boolean inString = false;       // флаг строкового литерала
        boolean escaped = false;        // флаг экранирования в строке

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            char nextC = (i + 1 < content.length()) ? content.charAt(i + 1) : '\0';

            if (inBlockComment) {
                // Обработка внутри многострочного комментария
                if (c == '*' && nextC == '/') {
                    inBlockComment = false;
                    i++; // пропускаем '/'
                }
                // Игнорируем все символы внутри комментария
            } else if (inString) {
                // Обработка внутри строкового литерала
                sb.append(c);
                if (escaped) {
                    escaped = false; // сброс флага экранирования
                } else if (c == '\\') {
                    escaped = true; // начало экранированной последовательности
                } else if (c == '"') {
                    inString = false; // конец строкового литерала
                }
            } else {
                // Обработка обычного кода
                if (c == '/' && nextC == '/') {
                    // Однострочный комментарий - пропускаем до конца строки
                    i++;
                    while (i < content.length() && content.charAt(i) != '\n' && content.charAt(i) != '\r') {
                        i++;
                    }
                    // Сохраняем перевод строки для сохранения структуры
                    if (i < content.length()) {
                        sb.append(content.charAt(i));
                    }
                } else if (c == '/' && nextC == '*') {
                    // Начало многострочного комментария
                    inBlockComment = true;
                    i++; // пропускаем '*'
                } else if (c == '"') {
                    // Начало строкового литерала
                    inString = true;
                    sb.append(c);
                } else {
                    // Обычный символ кода
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}