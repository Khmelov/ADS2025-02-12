package by.it.group451002.jasko.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerB {
    public static void main(String[] args) {
        // Получаем путь к каталогу src относительно текущей рабочей директории
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        // Список для хранения данных о файлах (путь и размер)
        List<FileData> fileDataList = new ArrayList<>();

        try {
            // Рекурсивный обход всех файлов в каталоге src и его подкаталогах
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // Обрабатываем только Java файлы
                    if (file.toString().endsWith(".java")) {
                        processFile(file, src, fileDataList);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // Игнорируем ошибки некорректного ввода (битые файлы)
                    if (exc instanceof MalformedInputException) {
                        return FileVisitResult.CONTINUE;
                    }
                    return super.visitFileFailed(file, exc);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортировка по размеру, затем по пути
        // Сначала сравниваем размеры, если равны - сравниваем пути лексикографически
        fileDataList.sort((f1, f2) -> {
            int sizeCompare = Integer.compare(f1.size, f2.size);
            return sizeCompare != 0 ? sizeCompare : f1.relativePath.compareTo(f2.relativePath);
        });

        // Вывод результатов: размер и относительный путь для каждого файла
        for (FileData data : fileDataList) {
            System.out.println(data.size + " " + data.relativePath);
        }
    }

    /**
     * Обрабатывает отдельный Java файл с расширенной очисткой:
     * удаление package/imports, комментариев, непечатаемых символов и пустых строк
     */
    private static void processFile(Path file, String srcRoot, List<FileData> resultList) {
        try {
            // Чтение файла с обработкой ошибок кодировки
            String content = readFileWithFallback(file);

            // Пропускаем тестовые файлы (содержащие аннотации JUnit)
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            // Удаляем package и импорты для нормализации кода
            content = removePackageAndImports(content);

            // Удаляем комментарии (однострочные и многострочные)
            content = removeComments(content);

            // Удаляем непечатаемые символы (код <33) в начале и конце текста
            content = trimLowChars(content);

            // Удаляем пустые строки для компактности
            content = removeEmptyLines(content);

            // Рассчитываем размер очищенного содержимого в байтах (UTF-8 кодировка)
            int size = content.getBytes(StandardCharsets.UTF_8).length;

            // Получаем относительный путь от корня src
            String relativePath = Paths.get(srcRoot).relativize(file).toString();

            // Добавляем обработанные данные в список
            resultList.add(new FileData(relativePath, size));

        } catch (Exception e) {
            // Игнорируем ошибки чтения и обработки отдельных файлов
        }
    }

    /**
     * Чтение файла с fallback механизмом для обработки разных кодировок
     */
    private static String readFileWithFallback(Path file) throws IOException {
        try {
            // Пытаемся прочитать как UTF-8 (стандартная кодировка для Java)
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            // Если UTF-8 не работает, пробуем Latin-1 (более простая кодировка)
            return Files.readString(file, StandardCharsets.ISO_8859_1);
        }
    }

    /**
     * Удаляет package и import строки из исходного кода
     * Работает за O(n) где n - количество строк
     */
    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();
            // Сохраняем только строки, которые не являются package или import
            if (!trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    /**
     * Удаляет комментарии из исходного кода за O(n) где n - длина текста
     * Обрабатывает как однострочные (//), так и многострочные (/* ... * /) комментарии
     */
    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = content.length();

        // Проходим по всем символам текста
        while (i < n) {
            // Проверяем начало однострочного комментария
            if (i < n - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                // Однострочный комментарий - пропускаем все до конца строки
                while (i < n && content.charAt(i) != '\n') {
                    i++;
                }
            }
            // Проверяем начало многострочного комментария
            else if (i < n - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                // Многострочный комментарий - пропускаем все до закрывающего */
                i += 2; // Пропускаем открывающий /*
                while (i < n - 1 && !(content.charAt(i) == '*' && content.charAt(i + 1) == '/')) {
                    i++;
                }
                i += 2; // Пропускаем закрывающий */
            } else {
                // Обычный текст - добавляем в результат
                result.append(content.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    /**
     * Удаляет символы с кодом меньше 33 (непечатаемые символы)
     * из начала и конца строки
     */
    private static String trimLowChars(String text) {
        // Удаляем символы с кодом <33 в начале
        int start = 0;
        while (start < text.length() && text.charAt(start) < 33) {
            start++;
        }

        // Удаляем символы с кодом <33 в конце
        int end = text.length();
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        return text.substring(start, end);
    }

    /**
     * Удаляет пустые строки из текста
     * Строка считается пустой, если после trim() она становится пустой
     */
    private static String removeEmptyLines(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            // Сохраняем только непустые строки
            if (!line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    /**
     * Вспомогательный класс для хранения данных о файле:
     * относительный путь и размер очищенного содержимого
     */
    private static class FileData {
        String relativePath;
        int size;

        FileData(String relativePath, int size) {
            this.relativePath = relativePath;
            this.size = size;
        }
    }
}