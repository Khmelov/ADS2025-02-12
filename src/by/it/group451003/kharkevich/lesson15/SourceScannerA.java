package by.it.group451003.kharkevich.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) {
        // Формируем путь к папке src текущего проекта
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        // Список для хранения информации о файлах
        List<FileData> fileDataList = new ArrayList<>();

        try {
            // Рекурсивный обход файлового дерева начиная с папки src
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    // Обрабатываем только Java-файлы
                    if (file.toString().endsWith(".java")) {
                        processJavaFile(file, fileDataList);
                    }
                    return FileVisitResult.CONTINUE; // Продолжаем обход
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    // Игнорируем ошибки доступа к файлам и продолжаем обход
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            // Игнорируем ошибки ввода-вывода
        }

        // Сортируем список файлов по двум критериям:
        // 1. По размеру файла (возрастание)
        // 2. При равных размерах - по относительному пути (алфавитный порядок)
        fileDataList.sort((f1, f2) -> {
            int sizeCompare = Integer.compare(f1.size, f2.size);
            if (sizeCompare != 0) return sizeCompare;
            return f1.relativePath.compareTo(f2.relativePath);
        });

        // Выводим отсортированный список
        for (FileData data : fileDataList) {
            System.out.println(data.size + " " + data.relativePath);
        }
    }

    // Обрабатывает отдельный Java-файл
    private static void processJavaFile(Path file, List<FileData> fileDataList) {
        try {
            // Читаем содержимое файла в кодировке UTF-8
            String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);

            // Пропускаем тестовые файлы (содержащие аннотации @Test)
            if (content.contains("@Test") || content.contains("org.junit.Test")) return;

            // Обрабатываем содержимое файла (удаляем package и import)
            String processedContent = processFileContent(content);

            // Формируем относительный путь файла (без полного пути к src)
            String srcPath = System.getProperty("user.dir") + File.separator + "src" + File.separator;
            String relativePath = file.toString().substring(srcPath.length());

            // Вычисляем размер обработанного содержимого в байтах
            int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

            // Добавляем информацию о файле в список
            fileDataList.add(new FileData(size, relativePath));

        } catch (MalformedInputException e) {
            // Игнорируем файлы с некорректной кодировкой
        } catch (IOException e) {
            // Игнорируем ошибки чтения файлов
        }
    }

    // Обрабатывает содержимое файла: удаляет package и import statements
    private static String processFileContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n"); // Разбиваем на строки

        for (String line : lines) {
            // Пропускаем строки с package и import
            if (line.trim().startsWith("package") || line.trim().startsWith("import")) continue;
            result.append(line).append("\n");
        }

        String processed = result.toString();
        // Удаляем начальные и конечные пробельные символы
        processed = removeLowCharsFromStart(processed);
        processed = removeLowCharsFromEnd(processed);
        return processed;
    }

    // Удаляет пробельные символы из начала строки
    private static String removeLowCharsFromStart(String str) {
        int start = 0;
        // Ищем первый непробельный символ (коды < 33 - пробелы, табуляции и т.д.)
        while (start < str.length() && str.charAt(start) < 33) start++;
        return str.substring(start);
    }

    // Удаляет пробельные символы из конца строки
    private static String removeLowCharsFromEnd(String str) {
        int end = str.length();
        // Ищем последний непробельный символ
        while (end > 0 && str.charAt(end - 1) < 33) end--;
        return str.substring(0, end);
    }

    // Внутренний класс для хранения информации о файле
    private static class FileData {
        int size;              // Размер обработанного файла в байтах
        String relativePath;   // Относительный путь файла

        FileData(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}