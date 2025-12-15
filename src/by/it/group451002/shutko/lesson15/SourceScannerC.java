package by.it.group451002.shutko.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

// Найти Java-файлы с копиями по расстоянию Левенштейна (менее 10 правок),
// исключая тестовые файлы и удаляя package/import/комментарии, и вывести группы копируемых файлов.
public class SourceScannerC {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // Получение пути к директории src
        String srcPath = System.getProperty("user.dir") + File.separator + "src";
        Path srcDir = Path.of(srcPath);
        List<SourceFile> sourceFiles = new ArrayList<>(); // Список обработанных файлов

        // Чтение и обработка файлов
        try {
            Files.walk(srcDir)
                    .filter(p -> p.toString().endsWith(".java")) // Фильтр по расширению .java
                    .forEach(p -> {
                        try {
                            // Чтение файла с обработкой кодировок
                            String content = readFileWithFallback(p);

                            // Пропуск тестовых файлов
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Обработка содержимого (удаление package/import/комментариев, нормализация)
                            String processed = processContent(content);
                            // Относительный путь
                            String relPath = srcDir.relativize(p).toString();

                            // Сохранение информации о файле
                            sourceFiles.add(new SourceFile(relPath, processed));

                        } catch (IOException e) {
                            // Игнорирование ошибок чтения
                        }
                    });
        } catch (IOException e) {
            // Игнорирование ошибок обхода директории
        }

        // Поиск копий среди обработанных файлов
        Map<String, List<String>> copies = findCopies(sourceFiles);

        // Сортировка и вывод результатов
        List<String> sortedFiles = new ArrayList<>(copies.keySet());
        Collections.sort(sortedFiles); // Лексикографическая сортировка путей

        for (String filePath : sortedFiles) {
            List<String> copyPaths = copies.get(filePath);
            System.out.println(filePath); // Вывод оригинального файла
            for (String copyPath : copyPaths) {
                System.out.println(copyPath); // Вывод его копий
            }
            if (!copyPaths.isEmpty()) {
                System.out.println(); // Пустая строка между группами
            }
        }

        long endTime = System.currentTimeMillis();
        System.err.println("Execution time: " + (endTime - startTime) + "ms");
    }

    // Метод чтения файла с перебором кодировок для обработки MalformedInputException
    private static String readFileWithFallback(Path filePath) throws IOException {
        // Список возможных кодировок (от наиболее вероятных)
        List<Charset> charsets = Arrays.asList(
                StandardCharsets.UTF_8,
                Charset.forName("Windows-1251"),
                Charset.forName("ISO-8859-5"),
                StandardCharsets.ISO_8859_1
        );

        // Перебор кодировок до успешного чтения
        for (Charset charset : charsets) {
            try {
                return Files.readString(filePath, charset);
            } catch (MalformedInputException e) {
                continue; // Пробуем следующую кодировку
            }
        }

        return ""; // Если ни одна кодировка не подошла
    }

    // Основной метод обработки содержимого файла
    private static String processContent(String content) {
        if (content.isEmpty()) return content;

        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;  // Внутри /* ... */
        boolean inLineComment = false;   // Внутри //
        boolean inString = false;        // Внутри "..."
        boolean inChar = false;          // Внутри '.'
        char prevChar = 0;               // Предыдущий символ
        boolean afterPackageImports = false; // Флаг: прошли ли package/import

        // Один проход по тексту (O(n)) с комбинированной обработкой
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (inBlockComment) {
                // Выход из блочного комментария при */
                if (prevChar == '*' && c == '/') {
                    inBlockComment = false;
                }
            } else if (inLineComment) {
                // Выход из строчного комментария при новой строке
                if (c == '\n') {
                    inLineComment = false;
                    // Добавляем пробел вместо удаленного комментария
                    if (result.length() > 0 && result.charAt(result.length() - 1) != ' ') {
                        result.append(' ');
                    }
                }
            } else if (inString) {
                // Обработка строковых литералов
                if (c == '"' && prevChar != '\\') {
                    inString = false;
                }
                result.append(c);
            } else if (inChar) {
                // Обработка символьных литералов
                if (c == '\'' && prevChar != '\\') {
                    inChar = false;
                }
                result.append(c);
            } else {
                // Обычный режим - проверка на начало комментариев
                if (prevChar == '/' && c == '*') {
                    inBlockComment = true;
                    result.deleteCharAt(result.length() - 1); // Удаляем '/'
                } else if (prevChar == '/' && c == '/') {
                    inLineComment = true;
                    result.deleteCharAt(result.length() - 1); // Удаляем '/'
                } else if (c == '"') {
                    inString = true;
                    result.append(c);
                } else if (c == '\'') {
                    inChar = true;
                    result.append(c);
                } else {
                    // Обработка package и import
                    if (!afterPackageImports) {
                        // Проверяем, не начинается ли текущая позиция с package/import
                        if (startsWithPackageOrImport(result, c)) {
                            // Пропускаем всю строку до конца
                            while (i < content.length() && content.charAt(i) != '\n') {
                                i++;
                            }
                            prevChar = '\n';
                            continue;
                        }

                        // Если result не пуст и достигнут конец строки или файла,
                        // значит package/import закончились
                        if (result.length() > 0 && (c == '\n' || i == content.length() - 1)) {
                            afterPackageImports = true;
                        }
                    }

                    // Замена управляющих символов (код < 33) на пробелы
                    if (c < 33) {
                        // Добавляем пробел только если предыдущий символ не пробел
                        if (result.length() > 0 && result.charAt(result.length() - 1) != ' ') {
                            result.append(' ');
                        }
                    } else {
                        result.append(c);
                    }
                }
            }
            prevChar = c;
        }

        // Удаление начальных и конечных пробелов (trim)
        String processed = result.toString().trim();
        return processed;
    }

    // Вспомогательный метод для определения, начинается ли текущее слово с package/import
    private static boolean startsWithPackageOrImport(StringBuilder result, char nextChar) {
        // Собираем текущее слово (символы до пробела/перевода строки)
        StringBuilder currentWord = new StringBuilder();
        for (int i = result.length() - 1; i >= 0; i--) {
            char c = result.charAt(i);
            if (c == ' ' || c == '\n') break;
            currentWord.insert(0, c);
        }
        currentWord.append(nextChar);

        String word = currentWord.toString();
        return word.startsWith("package") || word.startsWith("import");
    }

    // Поиск копий среди обработанных файлов
    private static Map<String, List<String>> findCopies(List<SourceFile> sourceFiles) {
        Map<String, List<String>> copies = new HashMap<>();
        int n = sourceFiles.size();

        // Оптимизация: предварительное вычисление хешей
        int[] hashes = new int[n];
        for (int i = 0; i < n; i++) {
            hashes[i] = sourceFiles.get(i).content.hashCode();
        }

        // Сравнение всех пар файлов
        for (int i = 0; i < n; i++) {
            SourceFile file1 = sourceFiles.get(i);
            List<String> fileCopies = new ArrayList<>();

            for (int j = i + 1; j < n; j++) {
                SourceFile file2 = sourceFiles.get(j);

                // Быстрая проверка по хешу (идеальные совпадения)
                if (hashes[i] == hashes[j]) {
                    // Хеши совпали - файлы идентичны
                    fileCopies.add(file2.path);
                } else {
                    // Эвристика: если длины сильно различаются, файлы вряд ли копии
                    int lenDiff = Math.abs(file1.content.length() - file2.content.length());
                    if (lenDiff < 100) { // Порог разницы длин
                        // Вычисление расстояния Левенштейна
                        int distance = optimizedLevenshtein(file1.content, file2.content);
                        if (distance < 10) { // Порог для копий
                            fileCopies.add(file2.path);
                        }
                    }
                }
            }

            // Если нашли копии для файла, добавляем в результат
            if (!fileCopies.isEmpty()) {
                copies.put(file1.path, fileCopies);
            }
        }

        return copies;
    }

    // Оптимизированная реализация расстояния Левенштейна
    private static int optimizedLevenshtein(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        // Быстрые проверки тривиальных случаев
        if (len1 == 0) return len2;
        if (len2 == 0) return len1;
        // Если разница длин больше порога, заведомо не копии
        if (Math.abs(len1 - len2) > 10) return Math.max(len1, len2);

        // Использование двух массивов для экономии памяти O(min(len1, len2))
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

        // Инициализация первой строки
        for (int j = 0; j <= len2; j++) {
            prev[j] = j;
        }

        // Заполнение матрицы
        for (int i = 1; i <= len1; i++) {
            curr[0] = i;

            for (int j = 1; j <= len2; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    // Символы совпадают
                    curr[j] = prev[j - 1];
                } else {
                    // Минимум из трех операций: удаление, вставка, замена
                    curr[j] = 1 + Math.min(Math.min(prev[j], curr[j - 1]), prev[j - 1]);
                }

                // Ранний выход: если расстояние уже превысило порог
                if (curr[j] > 10) {
                    return 11; // Заведомо больше 10
                }
            }

            // Обмен массивами для следующей итерации
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[len2];
    }

    // Внутренний класс для хранения информации о файле
    private static class SourceFile {
        private final String path;    // Относительный путь
        private final String content; // Обработанное содержимое

        public SourceFile(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}