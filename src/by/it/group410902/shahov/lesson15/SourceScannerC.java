package by.it.group410902.shahov.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // Получаем путь к директории src текущего проекта
        String srcPath = System.getProperty("user.dir") + File.separator + "src";
        Path srcDir = Path.of(srcPath);
        List<SourceFile> sourceFiles = new ArrayList<>();

        // Чтение и обработка файлов
        try {
            Files.walk(srcDir)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            // Читаем файл с учетом разных кодировок
                            String content = readFileWithFallback(p);

                            // Пропускаем тестовые файлы
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Обрабатываем содержимое файла
                            String processed = processContent(content);
                            String relPath = srcDir.relativize(p).toString();

                            // Сохраняем информацию о файле
                            sourceFiles.add(new SourceFile(relPath, processed));

                        } catch (IOException e) {
                            // Игнорируем ошибки чтения отдельных файлов
                        }
                    });
        } catch (IOException e) {
            // Игнорируем ошибки обхода директории
        }

        // Поиск похожих файлов (копий)
        Map<String, List<String>> copies = findCopies(sourceFiles);

        // Вывод результатов (сортировка по алфавиту)
        List<String> sortedFiles = new ArrayList<>(copies.keySet());
        Collections.sort(sortedFiles);

        for (String filePath : sortedFiles) {
            List<String> copyPaths = copies.get(filePath);
            System.out.println(filePath);
            for (String copyPath : copyPaths) {
                System.out.println(copyPath);
            }
            if (!copyPaths.isEmpty()) {
                System.out.println(); // пустая строка между группами
            }
        }

        long endTime = System.currentTimeMillis();
        System.err.println("Execution time: " + (endTime - startTime) + "ms");
    }

    /**
     * Читает файл с попыткой использовать разные кодировки
     */
    private static String readFileWithFallback(Path filePath) throws IOException {
        List<Charset> charsets = Arrays.asList(
                StandardCharsets.UTF_8,
                Charset.forName("Windows-1251"),
                Charset.forName("ISO-8859-5"),
                StandardCharsets.ISO_8859_1
        );

        for (Charset charset : charsets) {
            try {
                return Files.readString(filePath, charset);
            } catch (MalformedInputException e) {
                continue;
            }
        }

        return "";
    }

    /**
     * Обрабатывает содержимое Java файла:
     * 1. Удаляет комментарии
     * 2. Удаляет package и import
     * 3. Нормализует пробельные символы
     * 4. Удаляет лишние пробелы
     */
    private static String processContent(String content) {
        if (content.isEmpty()) return content;

        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean inChar = false;
        char prevChar = 0;
        boolean afterPackageImports = false;

        // Комбинированная обработка за один проход
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (inBlockComment) {
                // Находим конец блочного комментария
                if (prevChar == '*' && c == '/') {
                    inBlockComment = false;
                }
            } else if (inLineComment) {
                // Строчный комментарий заканчивается на новой строке
                if (c == '\n') {
                    inLineComment = false;
                    // Добавляем пробел вместо комментария для разделения
                    if (result.length() > 0 && result.charAt(result.length() - 1) != ' ') {
                        result.append(' ');
                    }
                }
            } else if (inString) {
                // Внутри строкового литерала
                if (c == '"' && prevChar != '\\') {
                    inString = false;
                }
                result.append(c);
            } else if (inChar) {
                // Внутри символьного литерала
                if (c == '\'' && prevChar != '\\') {
                    inChar = false;
                }
                result.append(c);
            } else {
                // Проверка начала комментариев
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
                    // Обработка package/import
                    if (!afterPackageImports) {
                        // Проверяем, не начинается ли текущая позиция с package/import
                        if (startsWithPackageOrImport(result, c)) {
                            // Пропускаем символы до конца строки
                            while (i < content.length() && content.charAt(i) != '\n') {
                                i++;
                            }
                            prevChar = '\n';
                            continue;
                        }

                        // Если мы дошли сюда и result не пуст, значит package/import закончились
                        if (result.length() > 0 && (c == '\n' || i == content.length() - 1)) {
                            afterPackageImports = true;
                        }
                    }

                    // Нормализация пробельных символов: заменяем все управляющие символы на пробелы
                    if (c < 33) {
                        // Добавляем только один пробел (избегаем множественных пробелов)
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

        String processed = result.toString().trim();
        return processed;
    }

    /**
     * Проверяет, начинается ли текущее слово с "package" или "import"
     */
    private static boolean startsWithPackageOrImport(StringBuilder result, char nextChar) {
        // Собираем текущее слово из конца result + следующий символ
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

    /**
     * Находит похожие файлы (копии) среди всех обработанных файлов
     * Использует комбинацию хешей и расстояния Левенштейна для оптимизации
     */
    private static Map<String, List<String>> findCopies(List<SourceFile> sourceFiles) {
        Map<String, List<String>> copies = new HashMap<>();
        int n = sourceFiles.size();

        // Предварительно вычисляем хеши для быстрой проверки
        int[] hashes = new int[n];
        for (int i = 0; i < n; i++) {
            hashes[i] = sourceFiles.get(i).content.hashCode();
        }

        // Попарное сравнение всех файлов
        for (int i = 0; i < n; i++) {
            SourceFile file1 = sourceFiles.get(i);
            List<String> fileCopies = new ArrayList<>();

            for (int j = i + 1; j < n; j++) {
                SourceFile file2 = sourceFiles.get(j);

                // Быстрая проверка по хешу
                if (hashes[i] == hashes[j]) {
                    // Хеши совпали - файлы идентичны
                    fileCopies.add(file2.path);
                } else {
                    // Проверяем только если файлы похожи по длине
                    int lenDiff = Math.abs(file1.content.length() - file2.content.length());
                    if (lenDiff < 100) { // Эвристика: если длины сильно различаются, не проверяем
                        int distance = optimizedLevenshtein(file1.content, file2.content);
                        // Считаем файлы копиями если расстояние Левенштейна < 10
                        if (distance < 10) {
                            fileCopies.add(file2.path);
                        }
                    }
                }
            }

            // Сохраняем только файлы у которых есть копии
            if (!fileCopies.isEmpty()) {
                copies.put(file1.path, fileCopies);
            }
        }

        return copies;
    }

    /**
     * Оптимизированная версия расстояния Левенштейна
     * Использует два массива для экономии памяти и ранний выход при превышении порога
     */
    private static int optimizedLevenshtein(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        // Быстрая проверка тривиальных случаев
        if (len1 == 0) return len2;
        if (len2 == 0) return len1;
        if (Math.abs(len1 - len2) > 10) return Math.max(len1, len2); // Если разница больше порога

        // Используем два массива вместо матрицы для экономии памяти
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

        // Инициализация первого ряда
        for (int j = 0; j <= len2; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            curr[0] = i;

            for (int j = 1; j <= len2; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(Math.min(prev[j], curr[j - 1]), prev[j - 1]);
                }

                // Ранний выход если расстояние уже превысило порог
                if (curr[j] > 10) {
                    return 11; // Заведомо больше порога
                }
            }

            // Обмен массивами для следующей итерации
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[len2];
    }

    /**
     * Вспомогательный класс для хранения информации о файле
     */
    private static class SourceFile {
        private final String path;    // Относительный путь к файлу
        private final String content; // Обработанное содержимое файла

        public SourceFile(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}
