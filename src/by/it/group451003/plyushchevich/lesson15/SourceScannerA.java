package by.it.group451003.plyushchevich.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    private static class FileData {
        final String relativePath;
        final byte[] content;

        FileData(String relativePath, byte[] content) {
            this.relativePath = relativePath;
            this.content = content;
        }
    }

    public static void main(String[] args) {
        Path srcPath = Paths.get(System.getProperty("user.dir"), "src");
        if (!Files.exists(srcPath) || !Files.isDirectory(srcPath)) {
            return;
        }

        List<FileData> fileList = new ArrayList<>();

        Deque<Path> stack = new ArrayDeque<>();
        stack.push(srcPath);

        while (!stack.isEmpty()) {
            Path dir = stack.pop();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path entry : stream) {
                    try {
                        if (Files.isDirectory(entry)) {
                            // не спускаться в скрытые/системные, но обычно достаточно обычной проверки
                            String name = entry.getFileName() != null ? entry.getFileName().toString() : "";
                            if (name.equals(".git") || name.equals("out") || name.equals("bin") || name.equals("target")) {
                                continue;
                            }
                            stack.push(entry);
                        } else {
                            String fileName = entry.getFileName() != null ? entry.getFileName().toString() : "";
                            if (!fileName.endsWith(".java")) continue;

                            // Читаем файл построчно, игнорируя ошибки кодировки
                            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                                    .onMalformedInput(CodingErrorAction.IGNORE)
                                    .onUnmappableCharacter(CodingErrorAction.IGNORE);

                            boolean isTestFile = false;
                            StringBuilder sb = new StringBuilder();

                            try (InputStream in = Files.newInputStream(entry);
                                 Reader r = new InputStreamReader(in, decoder);
                                 BufferedReader br = new BufferedReader(r)) {

                                String line;
                                while ((line = br.readLine()) != null) {
                                    // если встретили маркер теста — пропускаем файл
                                    if (line.contains("@Test") || line.contains("org.junit.Test")) {
                                        isTestFile = true;
                                        break;
                                    }
                                    String trimmed = line.trim();
                                    if (trimmed.startsWith("package") || trimmed.startsWith("import")) {
                                        continue; // пропускаем package/import
                                    }
                                    sb.append(line).append("\n");
                                }

                            } catch (IOException e) {
                                // если не можем прочитать файл — просто пропускаем его
                                continue;
                            }

                            if (isTestFile) continue;

                            // Удаляем символы с кодом <33 в начале и конце
                            String cleaned = sb.toString();
                            int start = 0, end = cleaned.length();
                            while (start < end && cleaned.charAt(start) < 33) start++;
                            while (end > start && cleaned.charAt(end - 1) < 33) end--;
                            if (start >= end) {
                                cleaned = "";
                            } else {
                                cleaned = cleaned.substring(start, end);
                            }

                            byte[] finalBytes = cleaned.getBytes(StandardCharsets.UTF_8);

                            String relative = srcPath.relativize(entry).toString(); // сохраняем формат, как в тесте
                            fileList.add(new FileData(relative, finalBytes));
                        }

                    } catch (Exception e) {
                        // пропустить проблемную запись, но продолжить обход
                    }
                }
            } catch (IOException ignoredDir) {
                // пропускаем эту директорию и продолжаем дальше
            }
        }

        // Сортировка: сначала по размеру (возрастание), затем по пути лексикографически
        fileList.sort(Comparator
                .comparingInt((FileData fd) -> fd.content.length)
                .thenComparing(fd -> fd.relativePath));

        // Выводим: размер и относительный путь (по одному на строку)
        for (FileData fd : fileList) {
            System.out.println(fd.content.length + " " + fd.relativePath);
        }
    }
}
