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

public class SourceScannerB {
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
                            // Обрабатываем ошибки чтения
                            System.err.println("Ошибка чтения файла: " + path);
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
        // Удаляем package и imports
        content = removePackageAndImports(content);
        // Удаляем комментарии
        content = removeComments(content);
        // Удаляем символы с кодом <33 в начале и конце
        content = trimLowChars(content);
        // Удаляем пустые строки
        content = removeEmptyLines(content);

        return content;
    }

    private static String removePackageAndImports(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package") && !trimmedLine.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean inChar = false;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    result.append(c);
                }
                continue;
            }

            if (inBlockComment) {
                if (c == '*' && i + 1 < content.length() && content.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i++; // Пропускаем '/'
                }
                continue;
            }

            if (inString) {
                result.append(c);
                if (c == '\\' && i + 1 < content.length()) {
                    result.append(content.charAt(i + 1));
                    i++;
                } else if (c == '"') {
                    inString = false;
                }
                continue;
            }

            if (inChar) {
                result.append(c);
                if (c == '\\' && i + 1 < content.length()) {
                    result.append(content.charAt(i + 1));
                    i++;
                } else if (c == '\'') {
                    inChar = false;
                }
                continue;
            }

            if (c == '"') {
                inString = true;
                result.append(c);
            } else if (c == '\'') {
                inChar = true;
                result.append(c);
            } else if (c == '/' && i + 1 < content.length()) {
                if (content.charAt(i + 1) == '/') {
                    inLineComment = true;
                    i++;
                } else if (content.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i++;
                } else {
                    result.append(c);
                }
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private static String trimLowChars(String text) {
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

    private static String removeEmptyLines(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
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