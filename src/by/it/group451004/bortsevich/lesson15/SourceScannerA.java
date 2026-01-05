package by.it.group451004.bortsevich.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
/*
Создайте класс SourceScannerA с методом main,
который читает все файлы *.java из каталога src и его подкаталогов.

Каталог можно получить так:
String src = System.getProperty("user.dir")
        + File.separator + "src" + File.separator;

Файлы, содержащие в тексте @Test или org.junit.Test (тесты)
не участвуют в обработке.

В каждом тексте файла необходимо:
        1. Удалить строку package и все импорты за O(n).
        2. Удалить все символы с кодом <33 в начале и конце текстов.

В полученном наборе текстов:
        1. Рассчитать размер в байтах для полученных текстов
и вывести в консоль
размер и относительный от src путь к каждому из файлов (по одному в строке)
2. При выводе сортировать файлы по размеру,
а если размер одинаковый,
то лексикографически сортировать пути

Найдите способ игнорировать ошибки MalformedInputException

Все операции не должны ничего менять на дисках (разрешено только чтение)
Работа не имеет цели найти плагиат, поэтому не нужно менять коды своих программ.
*/

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        List<FileData> fileDataList = new ArrayList<>();

        try {
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().endsWith(".java")) {
                        processJavaFile(file, fileDataList);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
        }

        fileDataList.sort((f1, f2) -> {
            int sizeCompare = Integer.compare(f1.size, f2.size);
            if (sizeCompare != 0) return sizeCompare;
            return f1.relativePath.compareTo(f2.relativePath);
        });

        for (FileData data : fileDataList) {
            System.out.println(data.size + " " + data.relativePath);
        }
    }

    private static void processJavaFile(Path file, List<FileData> fileDataList) {
        try {
            String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);

            if (content.contains("@Test") || content.contains("org.junit.Test")) return;

            String processedContent = processFileContent(content);

            String srcPath = System.getProperty("user.dir") + File.separator + "src" + File.separator;
            String relativePath = file.toString().substring(srcPath.length());

            int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

            fileDataList.add(new FileData(size, relativePath));

        } catch (MalformedInputException e) {
        } catch (IOException e) {
        }
    }

    private static String processFileContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            if (line.trim().startsWith("package") || line.trim().startsWith("import")) continue;
            result.append(line).append("\n");
        }

        String processed = result.toString();
        processed = removeLowCharsFromStart(processed);
        processed = removeLowCharsFromEnd(processed);
        return processed;
    }

    private static String removeLowCharsFromStart(String str) {
        int start = 0;
        while (start < str.length() && str.charAt(start) < 33) start++;
        return str.substring(start);
    }

    private static String removeLowCharsFromEnd(String str) {
        int end = str.length();
        while (end > 0 && str.charAt(end - 1) < 33) end--;
        return str.substring(0, end);
    }

    private static class FileData {
        int size;
        String relativePath;

        FileData(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}