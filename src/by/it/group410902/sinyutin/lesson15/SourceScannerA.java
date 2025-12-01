package by.it.group410902.sinyutin.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class SourceScannerA {

    // Вспомогательный класс для хранения результата обработки файла
    static class FileResult {
        String relativePath;
        long sizeBytes;

        public FileResult(String relativePath, long sizeBytes) {
            this.relativePath = relativePath;
            this.sizeBytes = sizeBytes;
        }
    }

    public static void main(String[] args) {
        // Формирование пути к каталогу src
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path rootPath = Paths.get(src);

        if (!Files.exists(rootPath)) {
            System.err.println("Каталог src не найден: " + src);
            return;
        }

        List<FileResult> results = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(rootPath)) {
            paths.filter(p -> p.toString().endsWith(".java")) // Только .java
                    .forEach(path -> {
                        FileResult result = processFile(path, rootPath);
                        if (result != null) {
                            results.add(result);
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортировка результата
        // 1. По размеру (возрастание)
        // 2. Если размер равен, лексикографически по пути
        results.sort(Comparator
                .comparingLong((FileResult r) -> r.sizeBytes)
                .thenComparing(r -> r.relativePath)
        );

        // Вывод в консоль
        for (FileResult res : results) {
            System.out.println(res.sizeBytes + " " + res.relativePath);
        }
    }

    /**
     * Метод обработки одного файла.
     * Возвращает null, если файл нужно пропустить (тесты) или произошла ошибка чтения.
     */
    private static FileResult processFile(Path path, Path root) {
        String content = readFileSafe(path);

        // Если не удалось прочитать (null) или файл пустой
        if (content == null || content.isEmpty()) {
            return null;
        }

        // Проверка на тесты (не участвуют в обработке)
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return null;
        }

        // 1. Удалить строку package и все импорты.
        // Регулярное выражение с флагом (?m) - multiline.
        // ^ - начало строки, \s* - возможные пробелы, (package|import) - ключевые слова
        // .*? - любые символы до точки с запятой, ; - конец инструкции.
        String noImports = content.replaceAll("(?m)^\\s*(package|import)\\s+.*?;\\s*", "");

        // 2. Удалить все символы с кодом <33 в начале и конце текстов.
        // Метод trim() в Java удаляет все символы с кодом <= 32 (' ') с обоих концов строки.
        // Это полностью соответствует условию "< 33".
        String processedText = noImports.trim();

        // Рассчитать размер в байтах.
        // Используем UTF-8 для детерминированности размера.
        byte[] bytes = processedText.getBytes(StandardCharsets.UTF_8);
        long size = bytes.length;

        // Получаем относительный путь
        // root.relativize(path) вернет путь относительно src
        String relativePath = root.relativize(path).toString();

        return new FileResult(relativePath, size);
    }

    /**
     * Безопасное чтение файла с игнорированием ошибок кодировки (MalformedInputException).
     */
    private static String readFileSafe(Path path) {
        try {
            // Создаем декодер, который ЗАМЕНЯЕТ или ИГНОРИРУЕТ ошибки, вместо выбрасывания исключения
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.IGNORE)
                    .onUnmappableCharacter(CodingErrorAction.IGNORE);

            // Читаем байты с диска
            byte[] bytes = Files.readAllBytes(path);

            // Декодируем байты в символы
            CharBuffer charBuffer = decoder.decode(ByteBuffer.wrap(bytes));
            return charBuffer.toString();

        } catch (IOException e) {
            // Ошибки ввода-вывода (нет прав, файл занят и т.д.) просто логируем или игнорируем
            // System.err.println("Ошибка чтения файла: " + path);
            return null;
        }
    }
}