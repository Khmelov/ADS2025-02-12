package by.it.group451002.jasko.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerC {
    public static void main(String[] args) {
        // Получаем путь к каталогу src относительно текущей рабочей директории
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        // Список для хранения обработанных файлов
        List<ProcessedFile> files = new ArrayList<>();

        try {
            // Рекурсивный обход всех файлов в каталоге src и его подкаталогах
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // Обрабатываем только Java файлы
                    if (file.toString().endsWith(".java")) {
                        processFile(file, src, files);
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

        // Оптимизация производительности: сначала группируем по хешам для быстрого отсева
        // Файлы с разными хешами гарантированно разные, их не нужно сравнивать
        Map<Integer, List<ProcessedFile>> hashGroups = new HashMap<>();
        for (ProcessedFile file : files) {
            int hash = file.content.hashCode();
            hashGroups.computeIfAbsent(hash, k -> new ArrayList<>()).add(file);
        }

        // Находим копии только среди файлов с одинаковыми хешами
        // Ключ - путь оригинального файла, значение - список путей к его копиям
        Map<String, List<String>> copies = new HashMap<>();

        // Обрабатываем только группы с более чем одним файлом (потенциальные копии)
        for (List<ProcessedFile> group : hashGroups.values()) {
            if (group.size() > 1) {
                findCopiesInGroup(group, copies);
            }
        }

        // Сортируем пути оригинальных файлов лексикографически для упорядоченного вывода
        List<String> sortedPaths = new ArrayList<>(copies.keySet());
        Collections.sort(sortedPaths);

        // Выводим результаты: для каждого файла с копиями выводим его путь и пути копий
        for (String path : sortedPaths) {
            System.out.println(path);
            List<String> copyPaths = copies.get(path);
            Collections.sort(copyPaths);
            for (String copyPath : copyPaths) {
                System.out.println(copyPath);
            }
        }
    }

    /**
     * Обрабатывает отдельный Java файл для поиска копий:
     * нормализует код и подготавливает для сравнения
     */
    private static void processFile(Path file, String srcRoot, List<ProcessedFile> files) {
        try {
            // Чтение файла с обработкой ошибок кодировки
            String content = readFileWithFallback(file);

            // Пропускаем тестовые файлы (содержащие аннотации JUnit)
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            // Удаляем package и импорты для нормализации кода
            content = removePackageAndImports(content);

            // Удаляем комментарии для сравнения только логики кода
            content = removeComments(content);

            // Заменяем последовательности символов с кодом <33 на пробелы (нормализация пробелов)
            content = normalizeWhitespace(content);

            // Удаляем пробелы в начале и конце
            content = content.trim();

            // Оптимизация производительности: если файл слишком большой,
            // берем только первые 1000 символов для сравнения
            // Это значительно ускоряет алгоритм Левенштейна без большой потери точности
            if (content.length() > 1000) {
                content = content.substring(0, 1000);
            }

            // Получаем относительный путь и сохраняем обработанный файл
            String relativePath = Paths.get(srcRoot).relativize(file).toString();
            files.add(new ProcessedFile(relativePath, content));

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
     */
    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = content.length();

        while (i < n) {
            // Обработка однострочных комментариев
            if (i < n - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                while (i < n && content.charAt(i) != '\n') {
                    i++;
                }
            }
            // Обработка многострочных комментариев
            else if (i < n - 1 && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                i += 2;
                while (i < n - 1 && !(content.charAt(i) == '*' && content.charAt(i + 1) == '/')) {
                    i++;
                }
                i += 2;
            } else {
                // Обычный текст
                result.append(content.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    /**
     * Нормализует пробельные символы: заменяет последовательности символов
     * с кодом <33 на один пробел (код 32)
     */
    private static String normalizeWhitespace(String text) {
        StringBuilder result = new StringBuilder();
        boolean inWhitespace = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                // Если встретили пробельный символ и до этого не были в последовательности пробелов
                if (!inWhitespace) {
                    result.append(' ');  // Добавляем один пробел
                    inWhitespace = true; // Отмечаем, что находимся в последовательности пробелов
                }
                // Последующие пробельные символы в последовательности игнорируем
            } else {
                // Не пробельный символ - добавляем и сбрасываем флаг
                result.append(c);
                inWhitespace = false;
            }
        }

        return result.toString();
    }

    /**
     * Находит копии файлов внутри группы файлов с одинаковыми хешами
     * Использует комбинацию быстрого сравнения и алгоритма Левенштейна
     */
    private static void findCopiesInGroup(List<ProcessedFile> group, Map<String, List<String>> copies) {
        int n = group.size();

        // Сравниваем каждый файл с каждым последующим в группе
        for (int i = 0; i < n; i++) {
            ProcessedFile file1 = group.get(i);
            List<String> fileCopies = new ArrayList<>();

            for (int j = i + 1; j < n; j++) {
                ProcessedFile file2 = group.get(j);

                // Быстрая проверка: если содержимое идентично, считаем файлы копиями
                if (file1.content.equals(file2.content)) {
                    fileCopies.add(file2.path);
                } else {
                    // Если содержимое разное, используем расстояние Левенштейна
                    // для определения степени похожести
                    int distance = optimizedLevenshteinDistance(file1.content, file2.content);
                    // Считаем копиями файлы с числом правок меньше 10
                    if (distance < 10) {
                        fileCopies.add(file2.path);
                    }
                }
            }

            // Если нашли копии для текущего файла, сохраняем в результирующую мапу
            if (!fileCopies.isEmpty()) {
                copies.put(file1.path, fileCopies);
            }
        }
    }

    /**
     * Оптимизированная версия алгоритма Левенштейна для вычисления
     * редакционного расстояния между строками
     * Использует несколько оптимизаций для ускорения работы:
     * - Быстрый отсев по разнице длин
     * - Использование только двух массивов вместо матрицы
     * - Ранний выход при больших расстояниях
     */
    private static int optimizedLevenshteinDistance(String s1, String s2) {

        // Оптимизация: если строки сильно различаются по длине,
        // минимальное расстояние уже равно разнице длин
        int lenDiff = Math.abs(s1.length() - s2.length());
        if (lenDiff >= 10) {
            return lenDiff; // Минимальное расстояние уже больше порога в 10
        }

        int m = s1.length();
        int n = s2.length();

        // Используем только два массива для экономии памяти (O(n) вместо O(m*n))
        // prev хранит предыдущую строку матрицы, curr - текущую
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        // Инициализация первой строки матрицы
        for (int j = 0; j <= n; j++) {
            prev[j] = j;
        }

        // Заполнение матрицы построчно
        for (int i = 1; i <= m; i++) {
            curr[0] = i; // Первый элемент текущей строки

            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    // Символы совпадают - расстояние не увеличивается
                    curr[j] = prev[j - 1];
                } else {
                    // Символы разные - берем минимальное из трех вариантов:
                    // удаление, вставка или замена
                    curr[j] = 1 + Math.min(
                            Math.min(prev[j], curr[j - 1]),
                            prev[j - 1]
                    );
                }

                // Дополнительная оптимизация: если расстояние уже слишком большое
                // и мы далеко от диагонали, можно пропустить некоторые вычисления
                // (диагональная полоса шириной 20 символов)
                if (curr[j] >= 10 && j >= i - 10 && j <= i + 10) {
                    continue;
                }
            }

            // Меняем массивы местами для следующей итерации
            // Текущая строка становится предыдущей для следующего шага
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        // Результат находится в последней ячейке предыдущего массива
        return prev[n];
    }

    /**
     * Вспомогательный класс для хранения обработанного файла:
     * относительный путь и нормализованное содержимое
     */
    private static class ProcessedFile {
        String path;
        String content;

        ProcessedFile(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}