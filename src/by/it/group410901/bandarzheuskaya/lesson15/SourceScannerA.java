package by.it.group410901.bandarzheuskaya.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        List<FileInfo> fileInfos = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(src))) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = readFileIgnoringMalformed(p);

                            // Пропуск тестовых файлов
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            content = removePackageAndImports(content);
                            content = trimLowChars(content);

                            long size = content.getBytes(StandardCharsets.UTF_8).length;
                            String relativePath = srcPathRelative(src, p.toFile());
                            fileInfos.add(new FileInfo(size, relativePath));

                        } catch (IOException e) {
                            // Логируем ошибку
                            System.err.println("Ошибка при чтении файла: " + p);
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталога src: " + e.getMessage());
        }

        // Сортировка
        fileInfos.sort(Comparator.comparingLong(FileInfo::size)
                .thenComparing(FileInfo::path));

        // Вывод
        fileInfos.forEach(info -> System.out.println(info.size() + " " + info.path()));
    }

    private static String readFileIgnoringMalformed(Path path) throws IOException {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);  //читаем файл как строку
        } catch (MalformedInputException e) {
            return "";
        }
    }

    private static String removePackageAndImports(String content) {
        StringBuilder sb = new StringBuilder();
        String[] lines = content.split("\n");   //разделяем строки
        for (String line : lines) {
            String trimmed = line.trim();   //удаляем пробелы по краям
            if (trimmed.startsWith("package") || trimmed.startsWith("import")) continue;
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    private static String trimLowChars(String content) {    //удаляем все символы <33 с начала и конца строки
        int start = 0;
        int end = content.length() - 1;
        while (start <= end && content.charAt(start) < 33) start++;
        while (end >= start && content.charAt(end) < 33) end--;
        return start <= end ? content.substring(start, end + 1) : "";
    }

    private static String srcPathRelative(String src, File file) {
        Path srcPath = Paths.get(src).toAbsolutePath(); //приводим к абсолютному пути
        Path filePath = file.toPath().toAbsolutePath();
        return srcPath.relativize(filePath).toString(); //вычисляет относительный путь между двумя абсолютными путями
    }

    // record вместо обычного класса
    private record FileInfo(long size, String path) {}
}

