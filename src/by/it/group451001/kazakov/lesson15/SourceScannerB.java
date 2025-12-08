package by.it.group451001.kazakov.lesson15;

import java.io.File;
import java.io.IOException;
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
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        // Список для хранения информации о файлах
        List<FileInfo> fileInfoList = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(srcPath)) {
            // Фильтруем только .java файлы и обрабатываем каждый
            walk.filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> processFile(p, srcPath, fileInfoList));
        } catch (IOException e) {
            System.err.println("Error walking directory: " + e.getMessage());
        }

        // Сортируем файлы по размеру, а затем по пути
        fileInfoList.sort(Comparator.comparingLong(FileInfo::getSize)
                .thenComparing(FileInfo::getRelativePath));

        // Выводим результаты
        for (FileInfo info : fileInfoList) {
            System.out.println(info.getSize() + " " + info.getRelativePath());
        }
    }

    // Обрабатывает отдельный файл
    private static void processFile(Path filePath, Path srcPath, List<FileInfo> fileInfoList) {
        // Читаем содержимое файла с обработкой ошибок кодировки
        String content = readFileWithErrorHandling(filePath);

        if (content == null) return;

        // Пропускаем тестовые файлы
        if (content.contains("@Test") || content.contains("org.junit.Test")) return;

        // Удаляем package и import
        String processed = removePackageAndImports(content);
        // Удаляем комментарии
        processed = removeComments(processed);
        // Удаляем управляющие символы и лишние пробелы
        processed = removeControlCharacters(processed);
        // Удаляем пустые строки
        processed = removeEmptyLines(processed);

        // Вычисляем размер в байтах в кодировке UTF-8
        long size = processed.getBytes(StandardCharsets.UTF_8).length;

        // Получаем относительный путь от src
        String relativePath = srcPath.relativize(filePath).toString();

        // Добавляем информацию о файле в список
        fileInfoList.add(new FileInfo(size, relativePath));
    }

    // Читает файл с обработкой различных кодировок
    private static String readFileWithErrorHandling(Path filePath) {
        try {
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (java.nio.charset.MalformedInputException e) {
            try {
                // Если UTF-8 не работает, пробуем ISO-8859-1
                return Files.readString(filePath, StandardCharsets.ISO_8859_1);
            } catch (IOException ex) {
                try {
                    // Если ISO-8859-1 не работает, пробуем US-ASCII
                    return Files.readString(filePath, StandardCharsets.US_ASCII);
                } catch (IOException exc) {
                    System.err.println("Cannot read file " + filePath + " with any encoding");
                    return null;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
            return null;
        }
    }

    // Удаляет строки с package и import
    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\\n", -1);

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    // Удаляет однострочные и многострочные комментарии
    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int len = content.length();

        while (i < len) {
            // Обработка однострочных комментариев (//)
            if (i < len - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                while (i < len && content.charAt(i) != '\n') {
                    i++;
                }
                if (i < len && content.charAt(i) == '\n') {
                    result.append('\n');
                    i++;
                }
            }
            // Обработка многострочных комментариев (/* ... */)
            else if (i < len - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                i += 2;
                while (i < len - 1) {
                    if (content.charAt(i) == '*' && content.charAt(i + 1) == '/') {
                        i += 2;
                        break;
                    }
                    if (content.charAt(i) == '\n') {
                        result.append('\n');
                    }
                    i++;
                }
            }
            // Обработка строковых литералов (сохраняем как есть)
            else if (content.charAt(i) == '"') {
                result.append(content.charAt(i));
                i++;
                while (i < len) {
                    char c = content.charAt(i);
                    result.append(c);
                    if (c == '"' && (i == 0 || content.charAt(i - 1) != '\\')) {
                        i++;
                        break;
                    }
                    i++;
                }
            }
            // Обработка символьных литералов (сохраняем как есть)
            else if (content.charAt(i) == '\'') {
                result.append(content.charAt(i));
                i++;
                while (i < len) {
                    char c = content.charAt(i);
                    result.append(c);
                    if (c == '\'' && (i == 0 || content.charAt(i - 1) != '\\')) {
                        i++;
                        break;
                    }
                    i++;
                }
            }
            // Обычные символы
            else {
                result.append(content.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    // Удаляет управляющие символы и лишние пробелы в начале и конце строк
    private static String removeControlCharacters(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n", -1);

        for (String line : lines) {
            int start = 0;
            // Пропускаем пробельные символы в начале строки
            while (start < line.length() && line.charAt(start) < 33) {
                start++;
            }

            int end = line.length() - 1;
            // Пропускаем пробельные символы в конце строки
            while (end >= start && line.charAt(end) < 33) {
                end--;
            }

            if (start <= end) {
                result.append(line.substring(start, end + 1)).append("\n");
            } else {
                result.append("\n");
            }
        }

        return result.toString();
    }

    // Удаляет полностью пустые строки
    private static String removeEmptyLines(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n", -1);

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        String res = result.toString();
        if (res.endsWith("\n")) {
            res = res.substring(0, res.length() - 1);
        }

        return res;
    }

    // Вложенный класс для хранения информации о файле
    private static class FileInfo {
        private final long size;
        private final String relativePath;

        public FileInfo(long size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }

        public long getSize() {
            return size;
        }

        public String getRelativePath() {
            return relativePath;
        }
    }
}