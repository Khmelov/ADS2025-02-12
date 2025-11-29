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
import java.util.regex.Matcher;
import java.util.regex.Pattern; // Использование Pattern для удаления пустых строк

public class SourceScannerB {
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
            if (this.size != other.size) {
                return Long.compare(this.size, other.size);
            }
            return this.relativePath.compareTo(other.relativePath);
        }
    }

    public static void main(String[] args) {
        String srcPath = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        File srcDirectory = new File(srcPath);

        List<FileData> processedFiles = new ArrayList<>();

        processDirectory(srcDirectory, srcDirectory.getAbsolutePath(), processedFiles);

        Collections.sort(processedFiles);

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
                processDirectory(file, srcBasePath, processedFiles);
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                try {
                    String fileContent = readFileContent(file);

                    // Проверяем на @Test или org.junit.Test
                    if (fileContent.contains("@Test") || fileContent.contains("org.junit.Test")) {
                        continue; // Пропускаем тестовый файл
                    }

                    // Обрабатываем текст файла
                    String cleanedContent = cleanFileContent(fileContent);

                    // Вычисляем размер в байтах (используя UTF-8)
                    long sizeInBytes = cleanedContent.getBytes(Charset.forName("UTF-8")).length;

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

        // Используем CharsetDecoder для корректной обработки MalformedInputException
        // Заменяем некорректные байтовые последовательности символом-заменителем ('?')
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        decoder.onMalformedInput(CodingErrorAction.REPLACE); // Заменить некорректные
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE); // Заменить некартируемые

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try {
            return decoder.decode(buffer).toString();
        } catch (CharacterCodingException e) {
            // Это маловероятно при CodingErrorAction.REPLACE, но на всякий случай
            System.err.println("Проблема с декодированием файла " + file.getAbsolutePath() + ". Некоторые символы могли быть заменены.");
            // В случае исключения, попробуем еще раз с теми же настройками (может быть, это была редкая гонка)
            return new String(bytes, Arrays.toString(Charset.forName("UTF-8").newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).decode(ByteBuffer.wrap(bytes)).array()));
        }
    }


    private static String cleanFileContent(String content) {
        // 1. Удаление комментариев (самое сложное)
        String contentWithoutComments = removeComments(content);

        // 2. Удаление строк package и import, и удаление пустых строк
        StringBuilder sb = new StringBuilder();
        String[] lines = contentWithoutComments.split("\r?\n"); // Разбиваем на строки

        Pattern emptyLinePattern = Pattern.compile("^\\s*$"); // Шаблон для пустой или состоящей из пробелов строки

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")) {
                continue; // Пропускаем package и import
            }

            // Проверяем, является ли строка пустой после всех удалений
            if (!emptyLinePattern.matcher(line).matches()) { // Если строка не пустая (содержит что-то, кроме пробелов)
                sb.append(line).append('\n');
            }
        }

        // Удаляем лишний перевод строки в конце, если он есть
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }

        String result = sb.toString();

        // 3. Удаляем символы с кодом <33 в начале и конце всего текста
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


    private static String removeComments(String content) {
        StringBuilder sb = new StringBuilder();
        boolean inBlockComment = false; // Состояние: находимся ли мы внутри /* ... */ комментария
        boolean inString = false;      // Состояние: находимся ли мы внутри "..." строкового литерала

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            char nextC = (i + 1 < content.length()) ? content.charAt(i + 1) : '\0'; // Следующий символ

            if (inBlockComment) {
                // Если находимся в многострочном комментарии
                if (c == '*' && nextC == '/') {
                    inBlockComment = false; // Выходим из комментария
                    i++; // Пропускаем '/'
                }
                // Иначе просто пропускаем символ
            } else if (inString) {
                // Если находимся в строковом литерале
                sb.append(c); // Добавляем символ в результат
                if (c == '\\' && nextC != '\0') {
                    // Обработка экранированных символов (например, \" или \\)
                    sb.append(nextC);
                    i++;
                } else if (c == '"') {
                    inString = false; // Выходим из строкового литерала
                }
            } else {
                // Обычный код (не в комментарии и не в строке)
                if (c == '/' && nextC == '/') {
                    // Однострочный комментарий //
                    i++; // Пропускаем вторую '/'
                    // Пропускаем все символы до конца строки
                    while (i < content.length() && content.charAt(i) != '\n' && content.charAt(i) != '\r') {
                        i++;
                    }
                    if (i < content.length()) { // Если нашли перевод строки
                        sb.append(content.charAt(i)); // Добавляем перевод строки, чтобы сохранить структуру
                    }
                } else if (c == '/' && nextC == '*') {
                    // Многострочный комментарий /* ... */
                    inBlockComment = true;
                    i++; // Пропускаем '*'
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
