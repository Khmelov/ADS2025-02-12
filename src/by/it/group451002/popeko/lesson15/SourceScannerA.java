package by.it.group451002.popeko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerA {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            Files.walk(srcPath)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String content = Files.readString(path, StandardCharsets.UTF_8);

                            // Пропускаем тестовые файлы
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Обрабатываем файл
                            String processedContent = processContent(content);
                            byte[] bytes = processedContent.getBytes(StandardCharsets.UTF_8);
                            int size = bytes.length;
                            String relativePath = srcPath.relativize(path).toString();

                            fileInfos.add(new FileInfo(relativePath, size));

                        } catch (IOException e) {
                            // Игнорируем файлы с ошибками чтения
                        }
                    });

            // Сортировка и вывод
            fileInfos.stream()
                    .sorted(Comparator.comparingInt(FileInfo::getSize)
                            .thenComparing(FileInfo::getPath))
                    .forEach(info -> System.out.println(info.getSize() + " " + info.getPath()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processContent(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmedLine = line.trim();
            // Пропускаем package и import строки
            if (!trimmedLine.startsWith("package") && !trimmedLine.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        // Удаляем символы с кодом <33 в начале и конце
        String text = result.toString();
        int start = 0;
        int end = text.length();

        while (start < end && text.charAt(start) < 33) {
            start++;
        }
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        return text.substring(start, end);
    }

    private static class FileInfo {
        private final String path;
        private final int size;

        public FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
        }

        public String getPath() { return path; }
        public int getSize() { return size; }
    }
}