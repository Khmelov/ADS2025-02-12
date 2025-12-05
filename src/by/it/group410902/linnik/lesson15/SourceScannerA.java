package by.it.group410902.linnik.lesson15;

import java.io.*;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator; //путь к каталогу

        try {
            List<FileData> processedFiles = processJavaFiles(src); //вызываем обработку. передаем путь
            printResults(processedFiles);
        } catch (IOException e) {
            System.err.println("Ошибка при обработке файлов: " + e.getMessage());
        }
    }

    private static List<FileData> processJavaFiles(String srcDir) throws IOException {
        List<FileData> result = new ArrayList<>();
        Path startDir = Paths.get(srcDir); //путь до файла, был стринг стал пас, для работы с файловой системой

        if (!Files.exists(startDir)) {
            System.err.println("Каталог src не найден: " + srcDir);
            return result;
        }

        Files.walkFileTree(startDir, new SimpleFileVisitor<Path>() { //рекурсивно обходит папки
            @Override //в зависимости от того какой результат поиска файла вызываем метод
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException { //путь+инфа о файле
                if (file.toString().endsWith(".java")) { //если файл джава то обрабатываем
                    processFile(file, startDir, result);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                if (exc instanceof AccessDeniedException) { //если ошибка из-за отсутствия доступа
                    System.err.println("Нет доступа к файлу: " + file);
                    return FileVisitResult.CONTINUE;
                }
                return super.visitFileFailed(file, exc); //для других ошибок стандартная обработка
            }
        });

        return result;
    }

    private static void processFile(Path file, Path startDir, List<FileData> result) {
        try {
            String content = readFileContent(file); //читаем, ниже

            if (content.contains("@Test") || content.contains("org.junit.Test")) { //если тест пропускаем
                return;
            }

            String processedContent = processContent(content); //обрабатываем, ниже
            byte[] bytes = processedContent.getBytes(StandardCharsets.UTF_8); //преобразуем стринг в байты
            int size = bytes.length;

            String relativePath = startDir.relativize(file).toString(); //путь от src

            result.add(new FileData(relativePath, size));

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла " + file + ": " + e.getMessage());
        }
    }

    private static String readFileContent(Path file) throws IOException { //читаем содержимое файла по этому пути если не получается выбрасываем ошибку
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE) //игнорируеи битые байты
                .onUnmappableCharacter(CodingErrorAction.IGNORE); //игнорируем непереносимые символы

        try (InputStream is = Files.newInputStream(file);
             Reader reader = new InputStreamReader(is, decoder); //преобразуем байты в символы
             BufferedReader br = new BufferedReader(reader)) { //буферизация - читаем большими блоками

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) { //построчно читаем файл
                content.append(line).append("\n"); //сохраняем строки
            }
            return content.toString();
        }
    }

    private static String processContent(String content) { //обрабатываем содержимое: удаляем package import строки
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n"); //разбиваем на строки

        for (String line : lines) {
            String trimmedLine = line.trim(); //убираем пробелы

            // пропускаем если  package и import
            if (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")) {
                continue; // и идем дальше
            }

            result.append(line).append("\n"); // или добавляем строку
        }

        // удаляем символы с кодом <33 в начале и конце
        String processed = result.toString();
        processed = removeLowCharsFromStart(processed);
        processed = removeLowCharsFromEnd(processed);

        return processed;
    }

    private static String removeLowCharsFromStart(String str) {
        int start = 0;
        while (start < str.length() && str.charAt(start) < 33) {
            start++;
        }
        return str.substring(start);
    }

    private static String removeLowCharsFromEnd(String str) {
        int end = str.length() - 1;
        while (end >= 0 && str.charAt(end) < 33) {
            end--;
        }
        return str.substring(0, end + 1);
    }

    private static void printResults(List<FileData> files) {
        files.sort((f1, f2) -> { //сортируем по размеру и пути
            int sizeCompare = Integer.compare(f1.size, f2.size);
            if (sizeCompare != 0) { //нсли размеры разные то берем результат их сравнения
                return sizeCompare;
            }
            return f1.path.compareTo(f2.path); // есом ожинаковые сравниваем пути
        });

        for (FileData file : files) {
            System.out.println(file.size + " " + file.path);
        }
    }

    private static class FileData {
        String path;
        int size;

        FileData(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }
}