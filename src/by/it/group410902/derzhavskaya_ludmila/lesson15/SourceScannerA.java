package by.it.group410902.derzhavskaya_ludmila.lesson15;
import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
// Просканировать все файлы в каталоге src, исключить тесты, удалить package/import
// и символы <33, затем вывести размеры и пути отсортированных файлов.

// Основной класс для сканирования файлов
public class SourceScannerA {

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

        // Список для хранения информации о файлах (путь и размер)
        List<FileEntry> fileEntries = new ArrayList<>();

        try {
            // рекурсивно обходит все файлы
            Files.walk(Path.of(srcDir))
                    .filter(path -> path.toString().endsWith(".java"))
                    // Для каждого найденного Java-файла выполняем обработку
                    .forEach(path -> {
                        try {
                            // Читаем содержимое файла
                            String content = Files.readString(path);

                            // Проверяем, что файл НЕ содержит тестов
                            if (!content.contains("@Test") && !content.contains("org.junit.Test")) {
                                // Обрабатываем содержимое файла
                                String processed = processContent(content);

                                // Проверяем, что после обработки остался непустой текст
                                if (!processed.isEmpty()) {
                                    int sizeInBytes = processed.getBytes().length;
                                    String relativePath = Path.of(srcDir).relativize(path).toString();

                                    // Добавляем информацию о файле в список
                                    fileEntries.add(new FileEntry(relativePath, sizeInBytes));
                                }
                            }
                        } catch (MalformedInputException e) {
                            // Обрабатываем ошибки кодировки файла (игнорируем проблемный файл)
                            System.err.println("Malformed input: " + path);
                        } catch (IOException e) {
                            // Обрабатываем другие ошибки ввода-вывода
                            e.printStackTrace();
                        }
                    });

            // сортировка
            fileEntries.sort(Comparator.comparingInt((FileEntry f) -> f.size)
                    .thenComparing(f -> f.path));

            // выводим результаты: размер и относительный путь
            for (FileEntry entry : fileEntries) {
                System.out.println(entry.size + " " + entry.path);
            }
        } catch (IOException e) {
            // Обрабатываем ошибки при обходе файловой системы
            e.printStackTrace();
        }
    }


     // 1. Удаляет строки package и import
     // 2. Удаляет непечатаемые символы в начале и конце
    private static String processContent(String content) {
        StringBuilder sb = new StringBuilder();

        // Разбиваем содержимое на отдельные строки
        String[] lines = content.split("\n");

        for (String line : lines) {
            // Удаляем пробелы в начале и конце строки
            line = line.strip();

            // Пропускаем строки, которые начинаются с package или import
            if (line.startsWith("package") || line.startsWith("import")) {
                continue;
            }

            // Добавляем обработанную строку в результат
            sb.append(line).append("\n");
        }

        String processed = sb.toString();
        // Удаляем непечатаемые символы в начале и конце всего текста
        return removeNonPrintable(processed);
    }

     // Удаляет символы с кодом < 33 (непечатаемые символы)
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