package by.it.group410902.habrukovich.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class SourceScannerB {

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<FileData> filesData = new ArrayList<>();

        if (!Files.exists(srcPath)) {
            System.err.println("Каталог src не найден!");
            return;
        }

        Files.walk(srcPath)
                .filter(Files::isRegularFile)
                .filter(f -> f.toString().endsWith(".java"))
                .forEach(f -> {
                    String text;
                    try {
                        byte[] bytes = Files.readAllBytes(f);
                        text = new String(bytes, StandardCharsets.UTF_8);
                    } catch (MalformedInputException e) {
                        // игнорируем файл
                        return;
                    } catch (IOException e) {
                        return;
                    }

                    // игнорируем тестовые файлы
                    if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                    // удаляем package и import строки
                    StringBuilder sb = new StringBuilder();
                    String[] lines = text.split("\\R"); // любая новая строка
                    for (String line : lines) {
                        String trimmed = line.trim();
                        if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) continue;
                        sb.append(line).append("\n");
                    }
                    String cleaned = sb.toString();

                    // удаляем комментарии
                    cleaned = removeComments(cleaned);

                    // удалить символы с кодом <33 в начале и конце
                    int start = 0, end = cleaned.length() - 1;
                    while (start <= end && cleaned.charAt(start) < 33) start++;
                    while (end >= start && cleaned.charAt(end) < 33) end--;
                    if (start > end) cleaned = "";
                    else cleaned = cleaned.substring(start, end + 1);

                    // удалить пустые строки
                    StringBuilder sb2 = new StringBuilder();
                    String[] lines2 = cleaned.split("\\R");
                    for (String l : lines2) {
                        if (l.trim().isEmpty()) continue;
                        sb2.append(l).append("\n");
                    }
                    cleaned = sb2.toString();

                    int size = cleaned.getBytes(StandardCharsets.UTF_8).length;
                    String relPath = srcPath.relativize(f).toString().replace(File.separatorChar, '/');

                    filesData.add(new FileData(size, relPath));
                });

        // сортировка
        filesData.sort((a, b) -> {
            if (a.size != b.size) return Integer.compare(a.size, b.size);
            return a.path.compareTo(b.path);
        });

        // вывод
        for (FileData fd : filesData) {
            System.out.println(fd.size + " " + fd.path);
        }
    }

    // удаление всех комментариев
    private static String removeComments(String code) {
        StringBuilder sb = new StringBuilder();
        int len = code.length();
        boolean inBlock = false;
        boolean inLine = false;
        for (int i = 0; i < len; i++) {
            if (inBlock) {
                if (i + 1 < len && code.charAt(i) == '*' && code.charAt(i + 1) == '/') {
                    inBlock = false;
                    i++;
                }
            } else if (inLine) {
                if (code.charAt(i) == '\n' || code.charAt(i) == '\r') {
                    inLine = false;
                    sb.append(code.charAt(i));
                }
            } else {
                if (i + 1 < len) {
                    if (code.charAt(i) == '/' && code.charAt(i + 1) == '/') {
                        inLine = true;
                        i++;
                        continue;
                    } else if (code.charAt(i) == '/' && code.charAt(i + 1) == '*') {
                        inBlock = true;
                        i++;
                        continue;
                    }
                }
                sb.append(code.charAt(i));
            }
        }
        return sb.toString();
    }

    static class FileData {
        int size;
        String path;

        FileData(int size, String path) {
            this.size = size;
            this.path = path;
        }
    }
}

