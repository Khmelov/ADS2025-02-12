package by.it.group451003.khmilevskiy.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerA {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<FileInfo> fileInfos = new ArrayList<>();

        try (var walk = Files.walk(root)) {
            walk.filter(p -> {

                        boolean isJavaFile = p.toString().endsWith(".java");
                        boolean isReadable = Files.isReadable(p) && Files.isRegularFile(p);
                        return isJavaFile && isReadable;
                    })
                    .forEach(p -> {
                        try {
                            String content = Files.readString(p, StandardCharsets.UTF_8);
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }
                            String processed = processContentA(content);
                            String relativePath = root.relativize(p).toString();

                            fileInfos.add(new FileInfo(relativePath, processed.getBytes(StandardCharsets.UTF_8).length));

                        } catch (IOException e) {

                            System.err.println("Ошибка чтения (пропускаем файл): " + p);
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка обхода каталога: " + e.getMessage());
            return;
        }

        fileInfos.sort(Comparator
                .comparingInt(FileInfo::getSize)
                .thenComparing(FileInfo::getPath));

        for (FileInfo info : fileInfos) {
            System.out.println(info.getSize() + " " + info.getPath());
        }

        System.err.println("Обработано файлов: " + fileInfos.size() + " (ТОЛЬКО ЧТЕНИЕ)");
    }

    private static String processContentA(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.startsWith("package") || trimmedLine.startsWith("import")) {
                continue;
            }

            result.append(line).append("\n");
        }

        return removeLowCharsFromStartAndEnd(result.toString());
    }

    private static String removeLowCharsFromStartAndEnd(String str) {
        int start = 0;
        int end = str.length();

        while (start < end && str.charAt(start) < 33) {
            start++;
        }

        while (end > start && str.charAt(end - 1) < 33) {
            end--;
        }

        return str.substring(start, end);
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