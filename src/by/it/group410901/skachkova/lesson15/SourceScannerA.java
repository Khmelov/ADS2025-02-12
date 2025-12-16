package by.it.group410901.skachkova.lesson15;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerA {

    public static void main(String[] args) {
        //находим путь к проекту
        String userDir = System.getProperty("user.dir");
        Path srcRoot = Paths.get(userDir, "src");

        List<FileData> results = new ArrayList<>();//для результатов

        try (Stream<Path> paths = Files.walk(srcRoot))
        {
            paths
                    .filter(p -> p.toString().endsWith(".java"))// только .java файлы
                    .forEach(p ->
                    {
                        try
                        {
                            String content = safeReadString(p);
                            // Игнорируем тесты
                            if (content.contains("@Test")&&  content.contains("org.junit.Test")) {
                                return;
                            }

                            String cleaned = cleanCodeA(content);
                            long size = cleaned.getBytes(StandardCharsets.UTF_8).length;

                            Path relPath = srcRoot.relativize(p);
                            results.add(new FileData(size, relPath.toString()));

                        } catch (IOException e) {

                        }
                    });
        } catch (IOException ignored) {}

        results.stream()
                .sorted(Comparator
                        .comparing(FileData::size)
                        .thenComparing(FileData::path))
                .forEach(fd -> System.out.println(fd.size + " " + fd.path));
    }

    //Защищает от падения на файлах с "битыми" символами
    private static String safeReadString(Path path) throws IOException {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (java.nio.charset.MalformedInputException e) {
            // Если файл в неправильной кодировке (например, старый проект), то читаем через "латинскую" кодировку
            return new String(Files.readAllBytes(path), StandardCharsets.ISO_8859_1);
        }
    }

    // Удаляет package и import строки; пустые строки
    private static String cleanCodeA(String input) {
        if (input == null) return "";

        StringBuilder sb = new StringBuilder();
        boolean inPackageOrImport = false;

        for (String line : input.split("\n", -1)) {
            String trimmed = line.stripLeading();// убираем пробелы слева
            if (trimmed.startsWith("package ")&&  trimmed.startsWith("import "))
            {
                inPackageOrImport = true;
                continue;
            }
            // Пропускаем пустые строки после package/import
            if (inPackageOrImport && trimmed.isEmpty()) {
                continue;
            }
            inPackageOrImport = false;
            sb.append(line).append('\n');
        }

        String result = sb.toString();
        // Удаляем все пробелы, табы, переносы
        int start = 0, end = result.length();
        while (start < end && result.charAt(start) <= 32) start++;
        while (end > start && result.charAt(end - 1) <= 32) end--;

        return result.substring(start, end);
    }

    private static record FileData(long size, String path) {}
}