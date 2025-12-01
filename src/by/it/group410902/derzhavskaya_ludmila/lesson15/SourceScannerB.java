package by.it.group410902.derzhavskaya_ludmila.lesson15;
import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
//Просканировать файлы, исключить тесты, удалить package/import/комментарии/пустые строки,
// и вывести отсортированные по размеру и пути результаты.

public class SourceScannerB {

    // Вспомогательный класс для хранения информации о файле
    static class FileEntry {
        String path;
        int size;

        public FileEntry(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }
    public static void main(String[] args) {
        // Формируем путь к каталогу src в текущей рабочей директории
        String srcDir = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        // Список для хранения информации о файлах (относительный путь и размер)
        List<FileEntry> results = new ArrayList<>();

        try {
            // Рекурсивно обходим все файлы и папки
            Files.walk(Path.of(srcDir))
                    // Фильтруем только Java-файлы по расширению .java
                    .filter(path -> path.toString().endsWith(".java"))
                    // выполняем обработку
                    .forEach(path -> {
                        try {
                            // Читаем содержимое файла
                            String content = Files.readString(path);

                            if (!content.contains("@Test") && !content.contains("org.junit.Test")) {
                                // Обрабатываем содержимое
                                String processed = processContent(content);

                                int sizeInBytes = processed.getBytes().length;
                                String relativePath = Path.of(srcDir).relativize(path).toString();

                                // Добавляем информацию о файле в список результатов
                                results.add(new FileEntry(relativePath, sizeInBytes));
                            }
                        } catch (MalformedInputException e) {
                            // Обрабатываем ошибки кодировки - выводим сообщение и продолжаем работу
                            System.err.println("Malformed input: " + path);
                        } catch (IOException e) {
                            // Обрабатываем другие ошибки ввода-вывода
                            e.printStackTrace();
                        }
                    });

            // СОРТИРОВКА результатов
            results.sort(Comparator.comparingInt((FileEntry f) -> f.size)
                    .thenComparing(f -> f.path));

            // ВЫВОД результатов
            for (FileEntry entry : results) {
                System.out.println(entry.size + " " + entry.path);
            }
        } catch (IOException e) {
            // Обрабатываем ошибки при обходе файловой системы
            e.printStackTrace();
        }
    }

    /**
     * Обрабатывает содержимое файла, удаляя:
     * - строки package и import
     * - пустые строки
     * - комментарии
     * - непечатаемые символы по краям
     */
    private static String processContent(String content) {
        StringBuilder sb = new StringBuilder();

        // Разбиваем содержимое на отдельные строки для построчной обработки
        String[] lines = content.split("\n");

        for (String line : lines) {
            // Удаляем пробелы в начале и конце строки
            line = line.strip();

            // Пропускаем строки, которые начинаются с package или import
            if (line.startsWith("package") || line.startsWith("import")) {
                continue; // переходим к следующей строке
            }

            // Пропускаем пустые строки (после strip они становятся длиной 0)
            if (line.isEmpty()) {
                continue;
            }

            // Пропускаем однострочные комментарии
            if (line.startsWith("//")) {
                continue;
            }

            // Добавляем обработанную строку в результат
            sb.append(line).append("\n");
        }

        String processed = sb.toString();
        // Удаляем непечатаемые символы в начале и конце всего текста
        processed = removeNonPrintable(processed);
        return processed.stripTrailing();
    }

    // Удаляет символы с кодом < 33 (непечатаемые символы
    private static String removeNonPrintable(String text) {
        int start = 0, end = text.length();

        // Находим первый печатаемый символ (пропускаем символы с кодом < 33)
        while (start < end && text.charAt(start) < 33) {
            start++;
        }

        // Находим последний печатаемый символ (идем с конца)
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        // Возвращаем подстроку без непечатаемых символов по краям
        return text.substring(start, end);
    }


}