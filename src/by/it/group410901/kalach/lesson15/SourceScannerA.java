package by.it.group410901.kalach.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerA {

    // Вспомогательный класс для хранения информации о файле
    static class FileInfo {
        String relativePath; // относительный путь файла
        int size;            // размер файла после обработки (в байтах)

        FileInfo(String relativePath, int size) {
            this.relativePath = relativePath;
            this.size = size;
        }
    }

    public static void main(String[] args) {
        // Получаем путь к директории src текущего проекта
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path srcPath = Paths.get(src);
        List<FileInfo> results = new ArrayList<>();

        // Используем Files.walk для рекурсивного обхода всех файлов в директории src
        try (Stream<Path> paths = Files.walk(srcPath)) {
            paths.filter(Files::isRegularFile)           // только обычные файлы (не директории)
                    .filter(p -> p.toString().endsWith(".java")) // только Java файлы
                    .forEach(path -> processFile(path, srcPath, results)); // обработка каждого файла
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортировка результатов: сначала по размеру (по возрастанию),
        // затем лексикографически по пути
        results.sort((a, b) -> {
            int sizeCompare = Integer.compare(a.size, b.size);
            if (sizeCompare != 0) {
                return sizeCompare; // если размеры разные, сортируем по размеру
            }
            return a.relativePath.compareTo(b.relativePath); // иначе по пути
        });

        // Вывод результатов в формате: размер относительный_путь
        for (FileInfo info : results) {
            System.out.println(info.size + " " + info.relativePath);
        }
    }

    /**
     * Обрабатывает один Java файл: читает, фильтрует, вычисляет размер
     */
    private static void processFile(Path filePath, Path srcPath, List<FileInfo> results) {
        try {
            // Чтение файла с обработкой ошибок кодировки
            // IGNORE означает, что некорректные символы будут пропущены
            String content = Files.readString(filePath,
                    StandardCharsets.UTF_8.newDecoder()
                            .onMalformedInput(CodingErrorAction.IGNORE)
                            .onUnmappableCharacter(CodingErrorAction.IGNORE)
                            .charset());

            // Пропускаем файлы, содержащие тесты
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            // Удаляем package и import statements для чистоты измерения
            content = removePackageAndImports(content);

            // Удаляем служебные символы (с кодом < 33) в начале и конце
            content = trimLowChars(content);

            // Вычисляем размер в байтах в UTF-8 кодировке
            int size = content.getBytes(StandardCharsets.UTF_8).length;

            // Получаем относительный путь (относительно src директории)
            String relativePath = srcPath.relativize(filePath).toString();

            // Добавляем информацию о файле в результаты
            results.add(new FileInfo(relativePath, size));

        } catch (IOException e) {
            // Игнорируем ошибки чтения файлов
        }
    }

    /**
     * Удаляет package и import statements из содержимого файла за O(n)
     * Алгоритм делает один проход по строке
     */
    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder(content.length());
        int i = 0;
        int len = content.length();

        while (i < len) {
            // Пропускаем все пробельные символы в начале
            while (i < len && Character.isWhitespace(content.charAt(i))) {
                i++;
            }

            if (i >= len) break;

            // Проверяем, не начинается ли строка с "package"
            if (i + 7 <= len && content.substring(i, i + 7).equals("package")) {
                // Пропускаем всю строку package до точки с запятой
                while (i < len && content.charAt(i) != ';') {
                    i++;
                }
                if (i < len) i++; // Пропускаем саму точку с запятой
                continue;
            }

            // Проверяем, не начинается ли строка с "import"
            if (i + 6 <= len && content.substring(i, i + 6).equals("import")) {
                // Пропускаем всю строку import до точки с запятой
                while (i < len && content.charAt(i) != ';') {
                    i++;
                }
                if (i < len) i++; // Пропускаем саму точку с запятой
                continue;
            }

            // Если это не package и не import, копируем оставшуюся часть файла
            result.append(content.substring(i));
            break;
        }

        return result.toString();
    }

    /**
     * Удаляет управляющие символы (с ASCII кодом < 33) из начала и конца строки
     * Символы с кодом < 33 включают: пробелы, табуляции, переносы строк и т.д.
     */
    private static String trimLowChars(String s) {
        int start = 0;
        int end = s.length();

        // Убираем управляющие символы в начале строки
        while (start < end && s.charAt(start) < 33) {
            start++;
        }

        // Убираем управляющие символы в конце строки
        while (start < end && s.charAt(end - 1) < 33) {
            end--;
        }

        return s.substring(start, end);
    }
}