package by.it.group410902.linnik.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator; // путь к каталогу

        List<FileData> processedFiles = new ArrayList<>(); //список для хранения данных

        try {
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<Path>() { //рекурсивно обходим каталог
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".java")) { //если джава обрабатываем
                        processJavaFile(file, processedFiles);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            processedFiles.sort((f1, f2) -> { //сортируем по размеру и пути
                int sizeCompare = Integer.compare(f1.size, f2.size);
                if (sizeCompare != 0) {
                    return sizeCompare;
                }
                return f1.relativePath.compareTo(f2.relativePath);
            });

            for (FileData fileData : processedFiles) {
                System.out.println(fileData.size + " " + fileData.relativePath);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталога: " + e.getMessage());
        }
    }

    private static void processJavaFile(Path filePath, List<FileData> processedFiles) {
        String relativePath = Paths.get(System.getProperty("user.dir")).relativize(filePath).toString(); //получаем относительный путь

        String content = readFileWithFallback(filePath); //читаем если есть ошибки обрабатываем
        if (content == null) {
            return;
        }

        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return; //пропускаем тесты
        }

        String processedContent = processContent(content);
        processedContent = processedContent.trim(); // удаляем символы с кодом <33 в начале и конце
        processedContent = removeEmptyLines(processedContent); //удаляем пустые строки
        int size = processedContent.getBytes(StandardCharsets.UTF_8).length; //считаем размер в байтах

        processedFiles.add(new FileData(size, relativePath)); //добавлям данные в список
    }

    private static String readFileWithFallback(Path filePath) { //читаем файл, при ошибке пробуем другую кодировку
        Charset[] charsets = {StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1,
                Charset.forName("windows-1251")}; //список кодировок, с помощью которых удем читать

        for (Charset charset : charsets) { //до первой успешной
            try {
                return Files.readString(filePath, charset);
            } catch (MalformedInputException e) {
                continue;
            } catch (IOException e) {
                System.err.println("Ошибка чтения файла " + filePath + ": " + e.getMessage());
                return null;
            }
        }

        System.err.println("Не удалось прочитать файл " + filePath + " ни в одной из кодировок");
        return null;
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n"); //разбиваем на строки
        boolean inBlockComment = false; //указатель находимся ли в комментарии

        for (String line : lines) {
            String processedLine = processLine(line, inBlockComment); //обрабатываем строку

            if (processedLine.contains("/*")) { //проверяем начало конец комментария
                inBlockComment = true;
            }
            if (processedLine.contains("*/")) {
                inBlockComment = false;
            }

            if (!processedLine.trim().startsWith("package") && //пропускаем строки packege inport
                    !processedLine.trim().startsWith("import")) {
                result.append(processedLine).append("\n");
            }
        }

        return result.toString();
    }

    private static String processLine(String line, boolean inBlockComment) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = line.length();

        while (i < n) { //посимвольно
            if (inBlockComment) { //если мы в комментарии ищем конец
                int endComment = line.indexOf("*/", i);
                if (endComment != -1) { //когда нашли пропускаем все до конца
                    i = endComment + 2;
                    inBlockComment = false;
                } else {
                    i = n; // если не нашли значит вся строка коммнатприй
                }
            } else {
                if (i + 1 < n && line.charAt(i) == '/' && line.charAt(i + 1) == '/') { //проверяем на начало
                    break; //пропускаем строку
                } else if (i + 1 < n && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i += 2;
                } else {
                    result.append(line.charAt(i)); //обычный символ добавляем
                    i++;
                }
            }
        }

        return result.toString();
    }

    private static String removeEmptyLines(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            if (!line.trim().isEmpty()) { //удаляем пробелы и добавляем непустые строки
                result.append(line).append("\n");
            }
        }

        return result.toString();
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