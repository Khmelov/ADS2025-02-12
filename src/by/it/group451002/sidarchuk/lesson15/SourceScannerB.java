package by.it.group451002.sidarchuk.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        try {
            List<FileInfo> processedFiles = processJavaFiles(src);
            printResults(processedFiles);
        } catch (Exception e) {
            System.err.println("Ошибка при обработке файлов: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<FileInfo> processJavaFiles(String srcDir) throws IOException {
        List<FileInfo> result = new ArrayList<>();
        Path startDir = Paths.get(srcDir);

        if (!Files.exists(startDir)) {
            System.err.println("Каталог src не найден: " + srcDir);
            return result;
        }

        Files.walkFileTree(startDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".java")) {
                    processFile(file, startDir, result);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.err.println("Не удалось обработать файл: " + file + " - " + exc.getMessage());
                return FileVisitResult.CONTINUE;
            }
        });

        return result;
    }

    private static void processFile(Path file, Path baseDir, List<FileInfo> result) {
        try {
            String content = readFileContent(file);

            // Проверяем, не является ли файл тестом
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            String processedContent = processContent(content);

            if (!processedContent.isEmpty()) {
                String relativePath = baseDir.relativize(file).toString();
                int size = processedContent.getBytes(StandardCharsets.UTF_8).length;
                result.add(new FileInfo(relativePath, size));
            }

        } catch (MalformedInputException e) {
            System.err.println("Некорректные данные в файле (возможно, неверная кодировка): " + file);
        } catch (Exception e) {
            System.err.println("Ошибка при обработке файла " + file + ": " + e.getMessage());
        }
    }

    private static String readFileContent(Path file) throws IOException {
        // Пробуем разные кодировки для обработки MalformedInputException
        List<String> encodings = List.of(
                StandardCharsets.UTF_8.name(),
                "Windows-1251",
                "ISO-8859-1",
                "US-ASCII"
        );

        for (String encoding : encodings) {
            try {
                return Files.readString(file, java.nio.charset.Charset.forName(encoding));
            } catch (MalformedInputException e) {
                // Пробуем следующую кодировку
                continue;
            }
        }

        // Если все кодировки не подошли, используем UTF-8 с игнорированием ошибок
        return new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
    }

    private static String processContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        // 1. Удаляем package и импорты
        content = removePackageAndImports(content);

        // 2. Удаляем комментарии
        content = removeComments(content);

        // 3. Удаляем символы с кодом <33 в начале и конце
        content = trimLowChars(content);

        // 4. Удаляем пустые строки
        content = removeEmptyLines(content);

        return content;
    }

    private static String removePackageAndImports(String content) {
        // Регулярное выражение для удаления package и import statements
        String regex = "^(package|import)\\s+.*?;$";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        return pattern.matcher(content).replaceAll("");
    }

    private static String removeComments(String content) {
        // Удаляем блочные комментарии /* ... */
        content = content.replaceAll("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/", "");

        // Удаляем однострочные комментарии // ...
        content = content.replaceAll("//.*", "");

        return content;
    }

    private static String trimLowChars(String content) {
        // Удаляем символы с кодом <33 в начале
        int start = 0;
        while (start < content.length() && content.charAt(start) < 33) {
            start++;
        }

        // Удаляем символы с кодом <33 в конце
        int end = content.length();
        while (end > start && content.charAt(end - 1) < 33) {
            end--;
        }

        return content.substring(start, end);
    }

    private static String removeEmptyLines(String content) {
        String[] lines = content.split("\\r?\\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        // Удаляем последний \n если он есть
        if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    private static void printResults(List<FileInfo> files) {
        // Сортируем файлы по размеру (по возрастанию), а при равном размере - по пути
        Collections.sort(files);

        System.out.println("Результаты обработки файлов:");
        System.out.println("Размер (байты) | Путь к файлу");
        System.out.println("----------------|-------------");

        for (FileInfo fileInfo : files) {
            System.out.printf("%-14d | %s\n", fileInfo.size, fileInfo.relativePath);
        }

        // Выводим общую статистику
        int totalFiles = files.size();
        long totalSize = files.stream().mapToLong(f -> f.size).sum();
        System.out.println("\nОбщая статистика:");
        System.out.println("Обработано файлов: " + totalFiles);
        System.out.println("Общий размер: " + totalSize + " байт");
    }

    // Вспомогательный класс для хранения информации о файле
    private static class FileInfo implements Comparable<FileInfo> {
        String relativePath;
        int size;

        FileInfo(String relativePath, int size) {
            this.relativePath = relativePath;
            this.size = size;
        }

        @Override
        public int compareTo(FileInfo other) {
            if (this.size != other.size) {
                return Integer.compare(this.size, other.size);
            }
            return this.relativePath.compareTo(other.relativePath);
        }
    }
}
