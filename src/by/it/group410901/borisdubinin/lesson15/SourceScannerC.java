package by.it.group410901.borisdubinin.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerC {
    // Порог для определения "похожих" файлов (максимальное количество различий)
    private static final int COPY_THRESHOLD = 10;

    public static void main(String[] args) {
        // Получаем путь к директории src проекта
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        try {
            // Находим все Java-файлы в директории src и поддиректориях
            List<Path> javaFiles = findJavaFiles(Paths.get(src));

            // Обрабатываем файлы: читаем, фильтруем тесты, нормализуем содержимое
            Map<Path, String> processedFiles = new LinkedHashMap<>();
            for (Path path : javaFiles) {
                String content = readFileSafely(path);
                if (content != null && !isTestFile(content)) {
                    String processed = processText(content);
                    if (!processed.isEmpty()) {
                        processedFiles.put(path, processed);
                    }
                }
            }

            // Находим дубликаты и похожие файлы
            Map<Path, Set<Path>> duplicates = findDuplicates(processedFiles);

            // Выводим результаты
            printDuplicates(duplicates);

        } catch (IOException e) {
            System.err.println("Ошибка при обработке файлов: " + e.getMessage());
        }
    }

    /**
     * Рекурсивно находит все Java-файлы в директории и её поддиректориях
     */
    private static List<Path> findJavaFiles(Path startDir) throws IOException {
        if (!Files.exists(startDir)) {
            return Collections.emptyList();
        }

        // Используем Files.walk для рекурсивного обхода
        try (Stream<Path> paths = Files.walk(startDir)) {
            return paths
                    .filter(Files::isRegularFile)           // только файлы (не директории)
                    .filter(p -> p.toString().endsWith(".java")) // только .java файлы
                    .collect(Collectors.toList());
        }
    }

    /**
     * Безопасно читает файл, пробуя разные кодировки для обработки проблем с кодировкой
     */
    private static String readFileSafely(Path path) {
        // Список кодировок для попытки чтения (в порядке приоритета)
        Charset[] charsets = {
                StandardCharsets.UTF_8,      // современная стандартная кодировка
                StandardCharsets.ISO_8859_1, // Latin-1
                Charset.defaultCharset()     // системная кодировка по умолчанию
        };

        // Пробуем прочитать файл в каждой кодировке
        for (Charset charset : charsets) {
            try {
                return Files.readString(path, charset);
            } catch (MalformedInputException e) {
                // Пробуем следующую кодировку при ошибке в текущей
                continue;
            } catch (IOException e) {
                return null;
            }
        }

        // Последняя попытка: читаем как байты и декодируем с заменой ошибок
        try {
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPLACE)  // заменяем некорректные последовательности
                    .onUnmappableCharacter(CodingErrorAction.REPLACE); // заменяем непереводимые символы

            byte[] bytes = Files.readAllBytes(path);
            return decoder.decode(java.nio.ByteBuffer.wrap(bytes)).toString();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Проверяет, является ли файл тестовым (содержит аннотации JUnit)
     */
    private static boolean isTestFile(String content) {
        return content.contains("@Test") || content.contains("org.junit.Test");
    }

    /**
     * Обрабатывает текст: удаляет package/imports, комментарии, нормализует пробелы
     */
    private static String processText(String text) {
        // 1. Удаляем package и импорты
        text = removePackageAndImports(text);

        // 2. Удаляем комментарии за O(n)
        text = removeComments(text);

        // 3. Заменяем управляющие символы (с кодом < 33) на пробелы
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            sb.append(c < 33 ? ' ' : c);
        }

        // 4. Обрезаем пробелы в начале и конце
        return sb.toString().trim();
    }

    /**
     * Удаляет package и import statements из кода
     */
    private static String removePackageAndImports(String text) {
        StringBuilder result = new StringBuilder();
        String[] lines = text.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();
            // Сохраняем только строки, которые не начинаются с package или import
            if (!trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                result.append(line).append('\n');
            }
        }

        return result.toString();
    }

    /**
     * Удаляет комментарии из Java кода, учитывая строковые литералы
     * Работает за O(n) - один проход по тексту
     */
    private static String removeComments(String text) {
        StringBuilder result = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;

        while (i < len) {
            // Обработка строковых литералов (в двойных кавычках)
            if (text.charAt(i) == '"') {
                result.append(text.charAt(i));
                i++;
                // Копируем всё содержимое строки, включая escape-последовательности
                while (i < len) {
                    char c = text.charAt(i);
                    result.append(c);
                    if (c == '\\' && i + 1 < len) {
                        // Обрабатываем escape-последовательности (\n, \t, \" и т.д.)
                        result.append(text.charAt(i + 1));
                        i += 2;
                    } else if (c == '"') {
                        // Конец строкового литерала
                        i++;
                        break;
                    } else {
                        i++;
                    }
                }
            }
            // Обработка символьных литералов (в одинарных кавычках)
            else if (text.charAt(i) == '\'') {
                result.append(text.charAt(i));
                i++;
                while (i < len) {
                    char c = text.charAt(i);
                    result.append(c);
                    if (c == '\\' && i + 1 < len) {
                        // Обрабатываем escape-последовательности
                        result.append(text.charAt(i + 1));
                        i += 2;
                    } else if (c == '\'') {
                        // Конец символьного литерала
                        i++;
                        break;
                    } else {
                        i++;
                    }
                }
            }
            // Обработка многострочных комментариев (/* ... */)
            else if (i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '*') {
                i += 2; // Пропускаем /*
                // Ищем конец комментария */
                while (i + 1 < len) {
                    if (text.charAt(i) == '*' && text.charAt(i + 1) == '/') {
                        i += 2;
                        break;
                    }
                    i++;
                }
            }
            // Обработка однострочных комментариев (// ...)
            else if (i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '/') {
                // Пропускаем всё до конца строки
                while (i < len && text.charAt(i) != '\n') {
                    i++;
                }
                // Сохраняем символ новой строки
                if (i < len) {
                    result.append('\n');
                    i++;
                }
            }
            // Обычный символ - копируем
            else {
                result.append(text.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    /**
     * Находит дубликаты и похожие файлы среди обработанных
     * Возвращает Map: файл -> множество его дубликатов/похожих файлов
     */
    private static Map<Path, Set<Path>> findDuplicates(Map<Path, String> files) {
        Map<Path, Set<Path>> duplicates = new TreeMap<>();
        List<Map.Entry<Path, String>> entries = new ArrayList<>(files.entrySet());

        // Этап 1: Группируем по хешу для быстрой фильтрации точных дубликатов
        Map<Integer, List<Map.Entry<Path, String>>> byHash = new HashMap<>();
        for (Map.Entry<Path, String> entry : entries) {
            int hash = entry.getValue().hashCode();
            byHash.computeIfAbsent(hash, k -> new ArrayList<>()).add(entry);
        }

        // Обрабатываем группы с одинаковым хешем (потенциальные точные дубликаты)
        for (List<Map.Entry<Path, String>> group : byHash.values()) {
            if (group.size() > 1) {
                // Проверяем точные совпадения внутри группы
                for (int i = 0; i < group.size(); i++) {
                    for (int j = i + 1; j < group.size(); j++) {
                        if (group.get(i).getValue().equals(group.get(j).getValue())) {
                            // Добавляем взаимные ссылки между дубликатами
                            duplicates.computeIfAbsent(group.get(i).getKey(), k -> new TreeSet<>())
                                    .add(group.get(j).getKey());
                            duplicates.computeIfAbsent(group.get(j).getKey(), k -> new TreeSet<>())
                                    .add(group.get(i).getKey());
                        }
                    }
                }
            }
        }

        // Этап 2: Поиск похожих файлов (не точных дубликатов)
        // Сортируем файлы по длине для оптимизации сравнения
        entries.sort(Comparator.comparingInt(e -> e.getValue().length()));

        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<Path, String> e1 = entries.get(i);
            int len1 = e1.getValue().length();

            for (int j = i + 1; j < entries.size(); j++) {
                Map.Entry<Path, String> e2 = entries.get(j);
                int len2 = e2.getValue().length();

                // Если разница в длине >= порога, дальше проверять бессмысленно
                if (len2 - len1 >= COPY_THRESHOLD) {
                    break; // переходим к следующему i, т.к. файлы отсортированы по длине
                }

                // Пропускаем уже найденные точные дубликаты
                if (e1.getValue().equals(e2.getValue())) {
                    continue;
                }

                // Быстрая проверка: подсчёт различающихся символов
                if (quickDifferenceCheck(e1.getValue(), e2.getValue())) {
                    // Точный расчет расстояния Левенштейна
                    int distance = levenshtein(e1.getValue(), e2.getValue());

                    // Если расстояние меньше порога, считаем файлы похожими
                    if (distance < COPY_THRESHOLD) {
                        duplicates.computeIfAbsent(e1.getKey(), k -> new TreeSet<>())
                                .add(e2.getKey());
                        duplicates.computeIfAbsent(e2.getKey(), k -> new TreeSet<>())
                                .add(e1.getKey());
                    }
                }
            }
        }

        return duplicates;
    }

    /**
     * Быстрая проверка различий между строками
     * Возвращает true, если количество различий меньше порога
     */
    private static boolean quickDifferenceCheck(String s1, String s2) {
        int len = Math.min(s1.length(), s2.length());
        int differences = Math.abs(s1.length() - s2.length()); // начальная разница из-за разной длины

        // Если разница в длине уже >= порога, не тратим время на дальнейшую проверку
        if (differences >= COPY_THRESHOLD) {
            return false;
        }

        // Подсчитываем различия в символах только до достижения порога
        for (int i = 0; i < len && differences < COPY_THRESHOLD; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                differences++;
            }
        }

        return differences < COPY_THRESHOLD;
    }

    /**
     * Вычисляет расстояние Левенштейна между двумя строками
     * (минимальное количество операций вставки, удаления, замены для превращения одной строки в другую)
     * Использует оптимизацию с двумя строками для экономии памяти
     */
    private static int levenshtein(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        if (len1 == 0) return len2;
        if (len2 == 0) return len1;

        // Оптимизация: используем только два массива вместо матрицы
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

        // Инициализация первой строки
        for (int j = 0; j <= len2; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            curr[0] = i;
            int minInRow = i; // отслеживаем минимум в текущей строке для раннего выхода

            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(curr[j - 1] + 1,     // вставка
                                prev[j] + 1),         // удаление
                        prev[j - 1] + cost           // замена
                );
                minInRow = Math.min(minInRow, curr[j]);
            }

            // Ранний выход: если минимум в строке уже >= порога
            if (minInRow >= COPY_THRESHOLD) {
                return COPY_THRESHOLD;
            }

            // Меняем массивы местами для следующей итерации
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[len2];
    }

    /**
     * Выводит группы дубликатов в удобочитаемом формате
     */
    private static void printDuplicates(Map<Path, Set<Path>> duplicates) {
        if (duplicates.isEmpty()) {
            return;
        }

        Set<Path> printed = new HashSet<>(); // для избежания повторного вывода

        for (Map.Entry<Path, Set<Path>> entry : duplicates.entrySet()) {
            Path file = entry.getKey();
            // Если файл еще не выводился, выводим всю группу
            if (!printed.contains(file)) {
                System.out.println(file);
                for (Path duplicate : entry.getValue()) {
                    System.out.print("copy: ");
                    System.out.println(duplicate);
                    printed.add(duplicate);
                }
                printed.add(file);
            }
        }
    }
}