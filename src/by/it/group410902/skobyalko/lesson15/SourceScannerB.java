package by.it.group410902.skobyalko.lesson15;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerB {

    // основной метод: собирает файлы, очищает тексты от package/import, комментариев, пустых строк и выводит размер + путь
    public static void main(String[] args) {
        // путь к каталогу src
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Path.of(src);

        // список для хранения информации о файлах
        List<FileData> filesData = new ArrayList<>();

        // обход всех файлов .java
        try {
            Files.walk(srcPath)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        // пропуск тестовых файлов
                        if (isTestFile(p)) return;

                        // обработка файла: удаление package/import, комментариев, пустых строк и символов <33
                        int size = processFile(p);
                        if (size >= 0) {
                            filesData.add(new FileData(p, size));
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // сортировка по размеру, затем по пути
        filesData.sort(Comparator.comparingInt(FileData::size)
                .thenComparing(fd -> fd.path.toString()));

        // вывод результата с относительным путем относительно src
        for (FileData fd : filesData) {
            Path relativePath = srcPath.relativize(fd.path);
            System.out.println(fd.size + " " + relativePath.toString());
        }
    }

    // проверка, содержит ли файл тестовые аннотации
    private static boolean isTestFile(Path p) {
        try (BufferedReader reader = new BufferedReader(new FileReader(p.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("@Test") || line.contains("org.junit.Test")) return true;
            }
        } catch (IOException e) {
            return true; // игнорируем проблемные файлы
        }
        return false;
    }

    // чтение и обработка файла: удаление package/import, комментариев, пустых строк и символов <33
    private static int processFile(Path p) {
        try (BufferedReader reader = new BufferedReader(new FileReader(p.toFile()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            boolean blockComment = false;

            while ((line = reader.readLine()) != null) {
                String t = line.trim();

                // удаляем многострочные комментарии /* ... */
                if (blockComment) {
                    if (t.contains("*/")) {
                        blockComment = false;
                        t = t.substring(t.indexOf("*/") + 2).trim();
                    } else {
                        continue;
                    }
                }
                while (t.contains("/*")) {
                    int start = t.indexOf("/*");
                    int end = t.indexOf("*/", start + 2);
                    if (end != -1) {
                        t = t.substring(0, start) + t.substring(end + 2);
                    } else {
                        t = t.substring(0, start);
                        blockComment = true;
                        break;
                    }
                    t = t.trim();
                }

                // удаляем однострочные комментарии //
                if (t.contains("//")) {
                    t = t.substring(0, t.indexOf("//")).trim();
                }

                // пропускаем package и import строки
                if (t.startsWith("package ") || t.startsWith("import ")) continue;

                // пропускаем пустые строки
                if (t.isEmpty()) continue;

                sb.append(t).append("\n");
            }

            String cleaned = trimLowChars(sb.toString());
            return cleaned.getBytes().length;

        } catch (MalformedInputException e) {
            return -1; // игнорируем файлы с неверной кодировкой
        } catch (IOException e) {
            return -1; // игнорируем проблемные файлы
        }
    }

    // удаление символов с кодом <33 в начале и конце текста
    private static String trimLowChars(String s) {
        int start = 0;
        int end = s.length() - 1;
        while (start <= end && s.charAt(start) < 33) start++;
        while (end >= start && s.charAt(end) < 33) end--;
        return start > end ? "" : s.substring(start, end + 1);
    }

    // класс для хранения информации о файле: путь и размер
    private static class FileData {
        final Path path;
        final int size;

        FileData(Path path, int size) {
            this.path = path;
            this.size = size;
        }

        int size() {
            return size;
        }
    }
}