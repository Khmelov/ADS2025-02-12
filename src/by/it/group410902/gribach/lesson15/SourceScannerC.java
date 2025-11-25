package by.it.group410902.gribach.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        String srcPath = System.getProperty("user.dir") + File.separator + "src";
        Path srcDir = Path.of(srcPath);
        List<SourceFile> sourceFiles = new ArrayList<>();

        // Чтение и обработка файлов
        try {
            Files.walk(srcDir)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = readFileWithFallback(p);

                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            String processed = processContent(content);
                            String relPath = srcDir.relativize(p).toString();

                            sourceFiles.add(new SourceFile(relPath, processed));

                        } catch (IOException e) {
                            // игнор ошибок чтения файлов
                        }
                    });
        } catch (IOException e) {
            // игнор ошибок обхода директории
        }

        // Поиск копий
        Map<String, List<String>> copies = findCopies(sourceFiles);

        // Вывод результатов
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

    private static String processContent(String content) {
        if (content.isEmpty()) return content;

        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean inChar = false;
        char prevChar = 0;
        boolean afterPackageImports = false;

        // Комбинированная обработка: удаление комментариев + package/import + нормализация пробелов
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (inBlockComment) {
                if (prevChar == '*' && c == '/') {
                    inBlockComment = false;
                }
            } else if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    // Добавляем пробел вместо комментария
                    if (result.length() > 0 && result.charAt(result.length() - 1) != ' ') {
                        result.append(' ');
                    }
                }
            } else if (inString) {
                if (c == '"' && prevChar != '\\') {
                    inString = false;
                }
                result.append(c);
            } else if (inChar) {
                if (c == '\'' && prevChar != '\\') {
                    inChar = false;
                }
                result.append(c);
            } else {
                // Проверка начала комментариев
                if (prevChar == '/' && c == '*') {
                    inBlockComment = true;
                    result.deleteCharAt(result.length() - 1);
                } else if (prevChar == '/' && c == '/') {
                    inLineComment = true;
                    result.deleteCharAt(result.length() - 1);
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

                    // Замена символов с кодом <33 на пробел
                    if (c < 33) {
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

    private static boolean startsWithPackageOrImport(StringBuilder result, char nextChar) {
        // Собираем текущее слово
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

    private static Map<String, List<String>> findCopies(List<SourceFile> sourceFiles) {
        Map<String, List<String>> copies = new HashMap<>();
        int n = sourceFiles.size();

        // Предварительно вычисляем хеши для оптимизации
        int[] hashes = new int[n];
        for (int i = 0; i < n; i++) {
            hashes[i] = sourceFiles.get(i).content.hashCode();
        }

        for (int i = 0; i < n; i++) {
            SourceFile file1 = sourceFiles.get(i);
            List<String> fileCopies = new ArrayList<>();

            for (int j = i + 1; j < n; j++) {
                SourceFile file2 = sourceFiles.get(j);

                // Быстрая проверка по хешу для исключения заведомо разных файлов
                if (hashes[i] == hashes[j]) {
                    // Хеши совпали - точно копия
                    fileCopies.add(file2.path);
                } else {
                    // Проверяем расстояние Левенштейна только если файлы похожи по длине
                    int lenDiff = Math.abs(file1.content.length() - file2.content.length());
                    if (lenDiff < 100) { // Эвристика: если длины сильно различаются, не проверяем
                        int distance = optimizedLevenshtein(file1.content, file2.content);
                        if (distance < 10) {
                            fileCopies.add(file2.path);
                        }
                    }
                }
            }

            if (!fileCopies.isEmpty()) {
                copies.put(file1.path, fileCopies);
            }
        }

        return copies;
    }

    // Оптимизированная версия расстояния Левенштейна
    private static int optimizedLevenshtein(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        // Быстрая проверка тривиальных случаев
        if (len1 == 0) return len2;
        if (len2 == 0) return len1;
        if (Math.abs(len1 - len2) > 10) return Math.max(len1, len2); // Если разница больше порога

        // Используем два массива для экономии памяти
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

        // Инициализация
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

            // Обмен массивами
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[len2];
    }

    private static class SourceFile {
        private final String path;
        private final String content;

        public SourceFile(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}