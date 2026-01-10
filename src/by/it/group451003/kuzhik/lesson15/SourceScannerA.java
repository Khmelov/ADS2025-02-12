package by.it.group451003.kuzhik.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SourceScannerA {

    // Метод для чтения файла с обработкой ошибок кодировки
    private static String readFileSafely(Path filePath) throws IOException {
        // Пробуем разные кодировки
        Charset[] charsets = {
                StandardCharsets.UTF_8,
                StandardCharsets.ISO_8859_1,
                Charset.forName("Windows-1251"),
                StandardCharsets.US_ASCII
        };

        for (Charset charset : charsets) {
            try {
                return Files.readString(filePath, charset);
            } catch (MalformedInputException e) {
                continue;
            }
        }

        byte[] bytes = Files.readAllBytes(filePath);

        int printableChars = 0;
        for (byte b : bytes) {
            if (b >= 32 || b == 9 || b == 10 || b == 13) {
                printableChars++;
            }
        }

        // Если больше 70% печатных символов, считаем текстовым
        if (printableChars > bytes.length * 0.7) {
            return new String(bytes, StandardCharsets.UTF_8);
        }

        // Не текстовый файл, возвращаем пустую строку
        return "";
    }

    // Удаление package и imports
    private static String removePackageAndImports(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("package") && !trimmed.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    // Удаление символов с кодом <33 в начале и конце
    private static String trimLowChars(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }

        char[] chars = str.toCharArray();
        int start = 0;
        int end = chars.length - 1;

        // Находим первый символ с кодом >= 33
        while (start <= end && chars[start] < 33) {
            start++;
        }

        // Находим последний символ с кодом >= 33
        while (end >= start && chars[end] < 33) {
            end--;
        }

        if (start > end) {
            return "";
        }

        return new String(chars, start, end - start + 1);
    }

    // Класс для хранения информации о файле
    private static class FileInfo implements Comparable<FileInfo> {
        final String path;
        final int size;

        FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
        }

        @Override
        public int compareTo(FileInfo other) {
            if (this.size != other.size) {
                return Integer.compare(this.size, other.size);
            }
            return this.path.compareTo(other.path);
        }

        @Override
        public String toString() {
            return size + " " + path;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);

        List<FileInfo> fileInfos = new ArrayList<>();

        try (var walk = Files.walk(root)) {
            walk.filter(p -> {
                        String pathStr = p.toString();
                        boolean isJava = pathStr.endsWith(".java");
                        boolean isFile = Files.isRegularFile(p);
                        return isJava && isFile;
                    })
                    .forEach(p -> {
                        try {
                            // Читаем файл с обработкой ошибок кодировки
                            String content = readFileSafely(p);

                            // Пропускаем тестовые файлы
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Удаляем package и imports
                            content = removePackageAndImports(content);

                            // Удаляем символы с кодом <33 в начале и конце
                            content = trimLowChars(content);

                            // Получаем относительный путь
                            String relativePath = root.relativize(p).toString();

                            // Размер в байтах (UTF-8)
                            int size = content.getBytes(StandardCharsets.UTF_8).length;

                            fileInfos.add(new FileInfo(relativePath, size));

                        } catch (IOException e) {
                            System.err.println("Ошибка обработки файла " + p + ": " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка обхода каталога: " + e.getMessage());
            return;
        }

        // Сортировка
        fileInfos.sort(FileInfo::compareTo);

        // Вывод
        for (FileInfo info : fileInfos) {
            System.out.println(info);
        }

        System.err.println("Обработано файлов: " + fileInfos.size());
    }
}