package by.it.group451002.karbanovich.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerC {

    public static void main(String[] args) {
        // Получаем путь к директории src в текущем проекте
        // user.dir - текущая рабочая директория (корень проекта)
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        // Преобразуем строку в объект Path для работы с файловой системой
        Path srcPath = Paths.get(src);

        // Проверяем, существует ли директория src
        if (!Files.exists(srcPath)) {
            System.err.println("Каталог src не найден: " + src);
            return; // Завершаем программу, если директория не найдена
        }

        // Список для хранения содержимого файлов и их путей
        List<FileContent> fileContents = new ArrayList<>();

        // Используем Stream API для рекурсивного обхода всех файлов и поддиректорий
        try (Stream<Path> paths = Files.walk(srcPath)) {
            // Фильтруем только Java-файлы (с расширением .java)
            paths.filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            // Обрабатываем каждый Java-файл
                            processFile(path, srcPath, fileContents);
                        } catch (IOException e) {
                            // Игнорируем ошибки чтения файлов
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталогов: " + e.getMessage());
            return;
        }

        // Находим группы файлов с похожим содержимым (расстояние Левенштейна < 10)
        Map<String, Set<String>> copies = findCopies(fileContents);

        // Сортируем пути файлов в алфавитном порядке для вывода
        List<String> sortedPaths = new ArrayList<>(copies.keySet());
        Collections.sort(sortedPaths);

        // Выводим результаты: для каждого файла выводим его путь и пути похожих файлов
        for (String path : sortedPaths) {
            System.out.println(path); // Выводим путь текущего файла
            List<String> copyPaths = new ArrayList<>(copies.get(path));
            Collections.sort(copyPaths); // Сортируем похожие файлы по алфавиту
            for (String copyPath : copyPaths) {
                System.out.println(copyPath); // Выводим путь похожего файла
            }
        }
    }

    // Метод для обработки одного файла
    private static void processFile(Path filePath, Path srcPath, List<FileContent> fileContents)
            throws IOException {
        // Читаем содержимое файла, игнорируя ошибки кодирования
        String content = readFileIgnoringErrors(filePath);

        // Пропускаем файлы с тестами
        // Если файл содержит аннотации @Test, значит это тестовый файл
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return; // Не добавляем тестовые файлы в результат
        }

        // Обрабатываем содержимое: удаляем комментарии, package, imports, нормализуем пробелы
        String processed = processContent(content);

        // Получаем относительный путь файла (от директории src)
        String relativePath = srcPath.relativize(filePath).toString();

        // Сохраняем содержимое файла и его путь
        fileContents.add(new FileContent(relativePath, processed));
    }

    // Метод для чтения файла с обработкой ошибок кодирования
    private static String readFileIgnoringErrors(Path filePath) throws IOException {
        // Читаем все байты из файла
        byte[] bytes = Files.readAllBytes(filePath);

        // Создаем декодер UTF-8 с указанием действий при ошибках:
        // IGNORE - игнорировать некорректные символы вместо выброса исключения
        return StandardCharsets.UTF_8
                .newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE)
                .decode(java.nio.ByteBuffer.wrap(bytes))
                .toString();
    }

    // Метод для обработки содержимого Java-файла
    private static String processContent(String content) {
        // Шаг 1: Удаление комментариев из кода
        String withoutComments = removeComments(content);

        // Шаг 2: Удаление package и imports
        StringBuilder result = new StringBuilder();
        String[] lines = withoutComments.split("\n", -1); // -1 сохраняет пустые строки

        for (String line : lines) {
            String trimmed = line.trim(); // Удаляем пробелы в начале и конце
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue; // Пропускаем строки package и import
            }
            result.append(line).append('\n'); // Добавляем строку с переносом
        }

        // Шаг 3: Нормализация пробелов - заменяем все последовательности непечатаемых символов одним пробелом
        String text = result.toString();
        StringBuilder normalized = new StringBuilder(text.length());
        boolean inWhitespace = false; // Флаг: находимся ли внутри последовательности пробелов

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) { // Символы с кодом < 33 - непечатаемые (пробелы, табуляции, переносы)
                if (!inWhitespace) {
                    normalized.append(' '); // Добавляем один пробел вместо последовательности
                    inWhitespace = true;
                }
            } else {
                normalized.append(c); // Добавляем обычный символ
                inWhitespace = false;
            }
        }

        // Удаляем пробелы в начале и конце результата
        return normalized.toString().trim();
    }

    // Метод для удаления комментариев из Java-кода
    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder(content.length());

        int i = 0;
        int len = content.length();
        boolean inLineComment = false;   // Флаг однострочного комментария (//)
        boolean inBlockComment = false;  // Флаг многострочного комментария (/* */)
        boolean inString = false;        // Флаг строкового литерала (")
        boolean inChar = false;          // Флаг символьного литерала (')
        char prevChar = 0;               // Предыдущий символ (для обработки экранирования)

        while (i < len) {
            char c = content.charAt(i);

            // Обработка строковых и символьных литералов (чтобы не удалять комментарии внутри них)
            if (!inLineComment && !inBlockComment) {
                if (c == '"' && prevChar != '\\') { // Начало или конец строки, если не экранировано
                    inString = !inString;
                    result.append(c);
                    prevChar = c;
                    i++;
                    continue;
                }

                if (c == '\'' && prevChar != '\\') { // Начало или конец символьного литерала
                    inChar = !inChar;
                    result.append(c);
                    prevChar = c;
                    i++;
                    continue;
                }
            }

            // Если внутри строки или символа - добавляем символ как есть
            if (inString || inChar) {
                result.append(c);
                prevChar = c;
                i++;
                continue;
            }

            // Начало многострочного комментария (/*)
            if (!inLineComment && !inBlockComment && i + 1 < len
                    && c == '/' && content.charAt(i + 1) == '*') {
                inBlockComment = true;
                i += 2; // Пропускаем "/*"
                prevChar = 0;
                continue;
            }

            // Нахождение внутри многострочного комментария
            if (inBlockComment) {
                // Конец многострочного комментария (*/)
                if (i + 1 < len && c == '*' && content.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i += 2; // Пропускаем "*/"
                    prevChar = 0;
                } else {
                    i++; // Пропускаем символ внутри комментария
                }
                continue;
            }

            // Начало однострочного комментария (//)
            if (!inLineComment && i + 1 < len
                    && c == '/' && content.charAt(i + 1) == '/') {
                inLineComment = true;
                i += 2; // Пропускаем "//"
                prevChar = 0;
                continue;
            }

            // Нахождение внутри однострочного комментария
            if (inLineComment) {
                // Конец строки - конец комментария
                if (c == '\n') {
                    inLineComment = false;
                    result.append(c); // Сохраняем перенос строки
                }
                prevChar = c;
                i++;
                continue;
            }

            // Обычный символ (не комментарий, не внутри строки/символа)
            result.append(c);
            prevChar = c;
            i++;
        }

        return result.toString();
    }

    // Метод для поиска похожих файлов (с расстоянием Левенштейна < 10)
    private static Map<String, Set<String>> findCopies(List<FileContent> fileContents) {
        // Карта для хранения групп похожих файлов
        // Ключ: путь файла, Значение: множество путей похожих файлов
        Map<String, Set<String>> copies = new TreeMap<>();
        int n = fileContents.size();

        // Вычисляем хеши для каждого файла для быстрого предварительного сравнения
        for (FileContent fc : fileContents) {
            fc.computeHash();
        }

        // Группируем файлы по длине для оптимизации сравнения
        // Сравниваем только файлы с близкой длиной
        Map<Integer, List<FileContent>> lengthBuckets = new HashMap<>();
        for (FileContent fc : fileContents) {
            // Делим длину на 10, чтобы создать группы файлов с примерно одинаковой длиной
            int bucket = fc.content.length() / 10;
            lengthBuckets.computeIfAbsent(bucket, k -> new ArrayList<>()).add(fc);
        }

        // Сравниваем файлы для поиска похожих
        for (int i = 0; i < n; i++) {
            FileContent file1 = fileContents.get(i);
            int bucket1 = file1.content.length() / 10;

            // Проверяем только файлы из текущей и соседних групп
            for (int b = bucket1 - 1; b <= bucket1 + 1; b++) {
                List<FileContent> candidates = lengthBuckets.get(b);
                if (candidates == null) continue;

                for (FileContent file2 : candidates) {
                    // Чтобы избежать дублирования сравнений (A-B и B-A)
                    if (file1.path.compareTo(file2.path) >= 0) {
                        continue;
                    }

                    // Быстрая проверка по разнице длин
                    int lenDiff = Math.abs(file1.content.length() - file2.content.length());
                    if (lenDiff >= 10) {
                        continue; // Если разница >= 10 символов, точно не похожи
                    }

                    // Быстрая проверка по хешу (аппроксимация)
                    if (Math.abs(file1.hash - file2.hash) > 10 * 31) {
                        continue; // Разница хешей слишком велика
                    }

                    // Точное вычисление расстояния Левенштейна
                    int distance = levenshteinDistanceFast(file1.content, file2.content);

                    // Если расстояние меньше 10, файлы считаются похожими
                    if (distance < 10) {
                        // Добавляем взаимные ссылки
                        copies.computeIfAbsent(file1.path, k -> new TreeSet<>()).add(file2.path);
                        copies.computeIfAbsent(file2.path, k -> new TreeSet<>()).add(file1.path);
                    }
                }
            }
        }

        return copies;
    }

    // Оптимизированное вычисление расстояния Левенштейна с отсечением
    private static int levenshteinDistanceFast(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        // Быстрые проверки для отсечения
        if (s1.equals(s2)) return 0; // Строки идентичны

        int lengthDiff = Math.abs(len1 - len2);
        if (lengthDiff >= 10) return 10; // Разница длин >= 10, точно не похожи

        if (len1 == 0) return Math.min(len2, 10); // Первая строка пустая
        if (len2 == 0) return Math.min(len1, 10); // Вторая строка пустая

        // Используем оптимизацию с двумя строками (вместо полной матрицы)
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

        // Инициализация первой строки (пустая строка s1)
        for (int j = 0; j <= len2; j++) {
            prev[j] = Math.min(j, 10); // Ограничиваем максимальное значение 10
        }

        // Динамическое программирование с узкой полосой
        for (int i = 1; i <= len1; i++) {
            curr[0] = Math.min(i, 10);

            // Рассматриваем только узкую полосу вокруг диагонали (±10)
            // Это оптимизация, так как мы ищем только похожие строки
            int minJ = Math.max(1, i - 10);
            int maxJ = Math.min(len2, i + 10);

            int minInRow = curr[0]; // Минимальное значение в текущей строке
            char c1 = s1.charAt(i - 1);

            for (int j = minJ; j <= maxJ; j++) {
                int cost = (c1 == s2.charAt(j - 1)) ? 0 : 1; // Символы совпадают?

                // Формула расстояния Левенштейна:
                // min(удаление, вставка, замена/совпадение)
                int val = Math.min(
                        Math.min(prev[j] + 1,      // Удаление
                                curr[j - 1] + 1), // Вставка
                        prev[j - 1] + cost        // Замена или совпадение
                );

                curr[j] = Math.min(val, 10); // Ограничиваем максимальное значение 10
                minInRow = Math.min(minInRow, curr[j]);
            }

            // Ранний выход: если минимальное значение в строке уже 10
            if (minInRow >= 10) {
                return 10;
            }

            // Меняем массивы местами для следующей итерации
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[len2]; // Результат в последнем элементе
    }

    // Вспомогательный класс для хранения содержимого файла и его метаданных
    static class FileContent {
        String path;      // Относительный путь файла
        String content;   // Обработанное содержимое файла
        int hash;         // Хеш для быстрого сравнения

        FileContent(String path, String content) {
            this.path = path;
            this.content = content;
        }

        // Вычисление простого хеша для быстрого предварительного сравнения файлов
        void computeHash() {
            int h = 0;
            // Используем только первые 100 символов для скорости
            // Умножаем на 31 - стандартный множитель в Java хеш-функциях
            int len = Math.min(content.length(), 100);
            for (int i = 0; i < len; i++) {
                h = h * 31 + content.charAt(i);
            }
            this.hash = h;
        }
    }
}
