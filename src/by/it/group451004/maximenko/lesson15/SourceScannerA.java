package by.it.group451004.maximenko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        try {
            // Получаем все .java файлы
            List<Path> javaFiles = findAllJavaFiles(srcPath);

            // Обрабатываем каждый файл
            List<FileData> fileDataList = new ArrayList<>();
            for (Path filePath : javaFiles) {
                String content = readFileWithMalformedInputIgnore(filePath);

                // Пропускаем файлы с тестами
                if (content.contains("@Test") || content.contains("org.junit.Test")) {
                    continue;
                }

                // Обрабатываем содержимое
                String processedContent = processContent(content);

                // Получаем размер в байтах (UTF-8)
                int size = processedContent.getBytes("UTF-8").length;

                // Получаем относительный путь
                String relativePath = srcPath.relativize(filePath).toString();

                fileDataList.add(new FileData(relativePath, size, processedContent));
            }

            // Сортируем по размеру, затем по пути
            Collections.sort(fileDataList);

            // Выводим результаты
            for (FileData fileData : fileDataList) {
                System.out.println(fileData.size + " " + fileData.relativePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Рекурсивно находим все .java файлы
    private static List<Path> findAllJavaFiles(Path dir) throws IOException {
        try (Stream<Path> walk = Files.walk(dir)) {
            return walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .collect(Collectors.toList());
        }
    }

    // Чтение файла с игнорированием MalformedInputException
    private static String readFileWithMalformedInputIgnore(Path filePath) throws IOException {
        // Создаем декодер с обработкой ошибок
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        decoder.onMalformedInput(CodingErrorAction.IGNORE);
        decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);

        // Читаем все байты и декодируем с помощью нашего декодера
        byte[] bytes = Files.readAllBytes(filePath);
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(bytes);
        return decoder.decode(buffer).toString();
    }

    // Обработка содержимого файла
    private static String processContent(String content) {
        // Разделяем на строки
        String[] lines = content.split("\\r?\\n");
        StringBuilder result = new StringBuilder();

        // Удаляем package и import строки за O(n)
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package") && !trimmedLine.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        String processed = result.toString();

        // Удаляем символы с кодом <33 в начале
        int start = 0;
        while (start < processed.length() && processed.charAt(start) < 33) {
            start++;
        }

        // Удаляем символы с кодом <33 в конце
        int end = processed.length() - 1;
        while (end >= 0 && processed.charAt(end) < 33) {
            end--;
        }

        if (start > end) {
            return "";
        }

        return processed.substring(start, end + 1);
    }

    // Вспомогательный класс для хранения данных о файле
    static class FileData implements Comparable<FileData> {
        String relativePath;
        int size;
        String content;

        FileData(String relativePath, int size, String content) {
            this.relativePath = relativePath;
            this.size = size;
            this.content = content;
        }

        @Override
        public int compareTo(FileData other) {
            if (this.size != other.size) {
                return Integer.compare(this.size, other.size);
            }
            return this.relativePath.compareTo(other.relativePath);
        }
    }
}