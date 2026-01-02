package by.it.group410901.garkusha.lesson15;

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
        // Получаем путь к папке src
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            Files.walk(Paths.get(src))
                    .filter(path -> path.toString().endsWith(".java")) // Фильтруем только Java-файлы
                    .forEach(path -> processFile(path, fileInfos, src));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортировка по размеру, затем по пути
        fileInfos.sort(Comparator
                .comparingInt(FileInfo::getSize)
                .thenComparing(FileInfo::getRelativePath));

        // Вывод результатов
        for (FileInfo info : fileInfos) {
            System.out.println(info.getSize() + " " + info.getRelativePath());
        }
    }

    private static void processFile(Path path, List<FileInfo> fileInfos, String src) {
        try {
            String content = Files.readString(path, StandardCharsets.UTF_8);

            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            // Обработка содержимого
            String processedContent = processContent(content);

            int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

            String relativePath = Paths.get(src).relativize(path).toString();

            // Сохраняем информацию о файле
            fileInfos.add(new FileInfo(size, relativePath));

        } catch (IOException e) {
            // Игнорируем ошибки чтения
        }
    }

    private static String processContent(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            // Пропускаем строки package и import
            if (!line.trim().startsWith("package") && !line.trim().startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        // Удаляем символы с кодом <33 в начале и конце
        String text = result.toString();
        text = text.replaceAll("^[\\s\\x00-\\x20]+", "");
        text = text.replaceAll("[\\s\\x00-\\x20]+$", "");

        return text;
    }

    static class FileInfo {
        private final int size;
        private final String relativePath;

        public FileInfo(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }

        public int getSize() {
            return size;
        }

        public String getRelativePath() {
            return relativePath;
        }
    }
}