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

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            Files.walk(Paths.get(src))
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> processFile(path, fileInfos, src));
        } catch (IOException e) {
            // Игнорируем ошибки обхода
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
            // Читаем файл с обработкой ошибок кодировки
            String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

            // Пропускаем файлы с тестами
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            // Обработка содержимого
            String processedContent = processContent(content);

            int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

            String relativePath = Paths.get(src).relativize(path).toString();

            // Добавляет информацию о файле в список
            fileInfos.add(new FileInfo(size, relativePath));

        } catch (IOException e) {
            // Игнорируем ошибки чтения
        }
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean inChar = false;

        int i = 0;
        int n = content.length();

        while (i < n) {
            char c = content.charAt(i);

            if (inBlockComment) {
                if (c == '*' && i + 1 < n && content.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i += 2;
                    continue;
                }
                i++;
                continue;
            }

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    result.append(c);
                }
                i++;
                continue;
            }

            if (inString) {
                if (c == '"' && content.charAt(i - 1) != '\\') {
                    inString = false;
                }
                result.append(c);
                i++;
                continue;
            }

            if (inChar) {
                if (c == '\'' && content.charAt(i - 1) != '\\') {
                    inChar = false;
                }
                result.append(c);
                i++;
                continue;
            }

            // Проверка начала блочного комментария
            if (c == '/' && i + 1 < n && content.charAt(i + 1) == '*') {
                inBlockComment = true;
                i += 2;
                continue;
            }

            // Проверка начала строчного комментария
            if (c == '/' && i + 1 < n && content.charAt(i + 1) == '/') {
                inLineComment = true;
                i += 2;
                continue;
            }

            // Проверка начала строки
            if (c == '"') {
                inString = true;
                result.append(c);
                i++;
                continue;
            }

            // Проверка начала символа
            if (c == '\'') {
                inChar = true;
                result.append(c);
                i++;
                continue;
            }

            result.append(c);
            i++;
        }

        // Разделяем на строки для дальнейшей обработки
        String[] lines = result.toString().split("\n");
        StringBuilder finalResult = new StringBuilder();

        for (String line : lines) {
            String trimmedLine = line.trim();

            // Пропускаем строки package и import
            if (trimmedLine.startsWith("package") || trimmedLine.startsWith("import")) {
                continue;
            }

            // Пропускаем пустые строки
            if (!trimmedLine.isEmpty()) {
                finalResult.append(line).append("\n");
            }
        }

        // Удаляем символы с кодом <33 в начале и конце
        String text = finalResult.toString();
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