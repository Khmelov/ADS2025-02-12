package by.it.group410901.shaidarov.lesson15;

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
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path srcPath = Paths.get(src);

        if (!Files.exists(srcPath)) {
            System.err.println("Каталог src не найден: " + src);
            return;
        }

        List<FileInfo> fileInfos = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(srcPath)) {
            paths.filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            processFile(path, srcPath, fileInfos);
                        } catch (IOException e) {
                            // Игнорируем ошибки чтения
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталогов: " + e.getMessage());
            return;
        }

        // Сортировка: по размеру, затем лексикографически по пути
        fileInfos.sort(Comparator.comparingInt((FileInfo fi) -> fi.size)
                .thenComparing(fi -> fi.relativePath));

        // Вывод результатов
        for (FileInfo info : fileInfos) {
            System.out.println(info.size + " " + info.relativePath);
        }
    }

    private static void processFile(Path filePath, Path srcPath, List<FileInfo> fileInfos)
            throws IOException {
        // Читаем файл с игнорированием MalformedInputException
        String content = readFileIgnoringErrors(filePath);

        // Проверка на тесты
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        // Обработка текста за O(n)
        String processed = processContent(content);

        // Вычисляем размер в байтах
        int size = processed.getBytes(StandardCharsets.UTF_8).length;

        // Получаем относительный путь
        String relativePath = srcPath.relativize(filePath).toString();

        fileInfos.add(new FileInfo(size, relativePath));
    }

    private static String readFileIgnoringErrors(Path filePath) throws IOException {
        // Читаем файл с заменой некорректных символов
        byte[] bytes = Files.readAllBytes(filePath);
        return StandardCharsets.UTF_8
                .newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE)
                .decode(java.nio.ByteBuffer.wrap(bytes))
                .toString();
    }

    private static String processContent(String content) {
        // Удаление package, imports и комментариев за O(n)
        StringBuilder result = new StringBuilder(content.length());

        int i = 0;
        int len = content.length();
        boolean inLineComment = false;
        boolean inBlockComment = false;
        StringBuilder currentLine = new StringBuilder();

        while (i < len) {
            char c = content.charAt(i);

            // Обработка многострочных комментариев
            if (!inLineComment && !inBlockComment && i + 1 < len
                    && c == '/' && content.charAt(i + 1) == '*') {
                inBlockComment = true;
                i += 2;
                continue;
            }

            if (inBlockComment) {
                if (i + 1 < len && c == '*' && content.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i += 2;
                } else {
                    i++;
                }
                continue;
            }

            // Обработка однострочных комментариев
            if (!inLineComment && i + 1 < len
                    && c == '/' && content.charAt(i + 1) == '/') {
                inLineComment = true;
                i += 2;
                continue;
            }

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    // Обрабатываем накопленную строку
                    processLine(currentLine.toString(), result);
                    currentLine.setLength(0);
                }
                i++;
                continue;
            }

            // Обычные символы
            if (c == '\n') {
                // Обрабатываем накопленную строку
                processLine(currentLine.toString(), result);
                currentLine.setLength(0);
            } else {
                currentLine.append(c);
            }

            i++;
        }

        // Обрабатываем последнюю строку
        if (currentLine.length() > 0) {
            processLine(currentLine.toString(), result);
        }

        // Удаляем последний перенос строки, если он был добавлен
        if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        // Удаляем символы с кодом <33 в начале и конце
        String text = result.toString();
        int start = 0;
        int end = text.length();

        // Удаляем в начале
        while (start < end && text.charAt(start) < 33) {
            start++;
        }

        // Удаляем в конце
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        return text.substring(start, end);
    }

    private static void processLine(String line, StringBuilder result) {
        String trimmed = line.trim();

        // Пропускаем строки package и import
        if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
            return;
        }

        // Пропускаем пустые строки
        if (trimmed.isEmpty()) {
            return;
        }

        // Добавляем непустую строку
        result.append(line).append('\n');
    }

    static class FileInfo {
        int size;
        String relativePath;

        FileInfo(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}