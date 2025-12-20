package by.it.group451002.sidarchuk.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.*;

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        try {
            List<FileInfo> fileInfos = processJavaFiles(src);
            printResults(fileInfos);
        } catch (IOException e) {
            System.err.println("Ошибка при обработке файлов: " + e.getMessage());
        }
    }

    private static List<FileInfo> processJavaFiles(String srcDir) throws IOException {
        List<FileInfo> fileInfos = new ArrayList<>();
        Path srcPath = Paths.get(srcDir);

        if (!Files.exists(srcPath)) {
            System.out.println("Каталог src не найден: " + srcDir);
            return fileInfos;
        }

        // Используем try-with-resources для ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<FileInfo>> futures = new ArrayList<>();

        Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".java")) {
                    // Обрабатываем каждый файл в отдельной задаче с таймаутом
                    Future<FileInfo> future = executor.submit(() ->
                            processJavaFileWithTimeout(file, srcPath)
                    );
                    futures.add(future);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.err.println("Не удалось访问 файл: " + file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                // Пропускаем системные и скрытые каталоги
                if (Files.isHidden(dir) || dir.toString().contains("node_modules") ||
                        dir.toString().contains(".git") || dir.toString().contains("target")) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }
        });

        // Собираем результаты с таймаутом
        for (Future<FileInfo> future : futures) {
            try {
                FileInfo result = future.get(1, TimeUnit.SECONDS); // Таймаут 1 секунда на файл
                if (result != null) {
                    fileInfos.add(result);
                }
            } catch (TimeoutException e) {
                System.err.println("Таймаут при обработке файла");
                future.cancel(true);
            } catch (Exception e) {
                System.err.println("Ошибка при обработке файла: " + e.getMessage());
            }
        }

        executor.shutdown();
        return fileInfos;
    }

    private static FileInfo processJavaFileWithTimeout(Path file, Path srcPath) {
        try {
            String content = readFileWithFallback(file);

            // Быстрая проверка на тесты
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return null;
            }

            String processedContent = processContent(content);
            String relativePath = srcPath.relativize(file).toString();
            int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

            return new FileInfo(relativePath, size);

        } catch (IOException e) {
            System.err.println("Ошибка при обработке файла " + file + ": " + e.getMessage());
            return null;
        }
    }

    private static String readFileWithFallback(Path file) throws IOException {
        // Читаем файл с ограничением по размеру
        long fileSize = Files.size(file);
        if (fileSize > 1024 * 1024) { // Не обрабатываем файлы больше 1MB
            throw new IOException("Файл слишком большой: " + fileSize + " bytes");
        }

        // Быстрое чтение с основной кодировкой
        try {
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            // Альтернативное чтение для проблемных файлов
            return readFileWithAlternativeMethod(file);
        }
    }

    private static String readFileWithAlternativeMethod(Path file) throws IOException {
        // Читаем как байты и конвертируем с заменой невалидных символов
        byte[] bytes = Files.readAllBytes(file);

        // Пробуем разные кодировки
        Charset[] charsets = {
                StandardCharsets.UTF_8,
                Charset.forName("Windows-1251"),
                StandardCharsets.ISO_8859_1,
                StandardCharsets.US_ASCII
        };

        for (Charset charset : charsets) {
            try {
                String result = new String(bytes, charset);
                // Проверяем, что строка не содержит много нечитаемых символов
                if (isReadable(result)) {
                    return result;
                }
            } catch (Exception e) {
                // Пробуем следующую кодировку
            }
        }

        // Последняя попытка - заменяем невалидные символы
        return new String(bytes, StandardCharsets.UTF_8)
                .replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");
    }

    private static boolean isReadable(String text) {
        // Проверяем, что текст в основном состоит из читаемых символов
        if (text == null || text.isEmpty()) return false;

        int controlChars = 0;
        int totalChars = Math.min(text.length(), 1000); // Проверяем первые 1000 символов

        for (int i = 0; i < totalChars; i++) {
            char c = text.charAt(i);
            if (c < 32 && c != '\n' && c != '\r' && c != '\t') {
                controlChars++;
            }
        }

        // Если больше 10% управляющих символов - считаем нечитаемым
        return (controlChars * 100 / totalChars) < 10;
    }

    private static String processContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("package") && !trimmed.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        String processed = result.toString();
        return removeControlCharsFromStartAndEnd(processed);
    }

    private static String removeControlCharsFromStartAndEnd(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        int start = 0;
        while (start < text.length() && text.charAt(start) < 33) {
            start++;
        }

        int end = text.length();
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        return text.substring(start, end);
    }

    private static void printResults(List<FileInfo> fileInfos) {
        fileInfos.sort((f1, f2) -> {
            int sizeCompare = Integer.compare(f1.size, f2.size);
            if (sizeCompare != 0) {
                return sizeCompare;
            }
            return f1.relativePath.compareTo(f2.relativePath);
        });

        for (FileInfo fileInfo : fileInfos) {
            System.out.println(fileInfo.size + " " + fileInfo.relativePath);
        }
    }

    private static class FileInfo {
        String relativePath;
        int size;

        FileInfo(String relativePath, int size) {
            this.relativePath = relativePath;
            this.size = size;
        }
    }
}