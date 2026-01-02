package by.it.group410902.habrukovich.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<FileData> filesData = new ArrayList<>();

        if (!Files.exists(srcPath)) {
            System.err.println("Каталог src не найден!");
            return;
        }

        Files.walk(srcPath) // рекурсивно проходит по всем файлам и папкам.
                .filter(Files::isRegularFile) //только Java файлы оставляем
                .filter(f -> f.toString().endsWith(".java"))
                .forEach(f -> {
                    String text = null;
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

                    // удалить символы с кодом <33 в начале и конце
                    int start = 0, end = cleaned.length() - 1;
                    while (start <= end && cleaned.charAt(start) < 33) start++;
                    while (end >= start && cleaned.charAt(end) < 33) end--;
                    if (start > end) cleaned = "";
                    else cleaned = cleaned.substring(start, end + 1);

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

    static class FileData {
        int size;
        String path;

        FileData(int size, String path) {
            this.size = size;
            this.path = path;
        }
    }
}

