package by.it.group410902.siomchena.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator; //формируем путь к  каталогу

        List<FileData> processedFiles = new ArrayList<>(); //список для хранения данных

        try {
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<Path>() { //рекурсивно обходим каталог
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".java")) { //обрабатываем джавы
                        processJavaFile(file, processedFiles);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            Map<String, List<String>> copies = findCopies(processedFiles); //ищем копии

            List<String> sortedFiles = new ArrayList<>(copies.keySet()); //сортируем
            Collections.sort(sortedFiles);

            for (String filePath : sortedFiles) { //для каждого файла выводим список копий
                List<String> copyPaths = copies.get(filePath);
                System.out.println(filePath);
                for (String copyPath : copyPaths) {
                    System.out.println(copyPath);
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталога: " + e.getMessage());
        }
    }

    private static void processJavaFile(Path filePath, List<FileData> processedFiles) {
        String relativePath = Paths.get(System.getProperty("user.dir")).relativize(filePath).toString(); //относительный путь

        String content = readFileWithFallback(filePath); //читаем файлы с обработкой разных колировок
        if (content == null) {
            return;
        }

        if (content.contains("@Test") || content.contains("org.junit.Test")) { //пропускаем тесты
            return;
        }

        String processedContent = processContent(content); //обрабатыаем и добавляем в список
        processedFiles.add(new FileData(relativePath, processedContent));
    }

    private static String readFileWithFallback(Path filePath) { //читаем с разными кодировками пока не получится
        Charset[] charsets = {StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1,
                Charset.forName("windows-1251")}; //список кодировок

        for (Charset charset : charsets) {
            try {
                return Files.readString(filePath, charset);
            } catch (MalformedInputException e) {
                continue;
            } catch (IOException e) {
                System.err.println("Ошибка чтения файла " + filePath + ": " + e.getMessage());
                return null;
            }
        }

        System.err.println("Не удалось прочитать файл " + filePath + " ни в одной из кодировок");
        return null;
    }

    private static String processContent(String content) {
        String withoutPackagesAndImports = removePackagesAndImports(content); // удаляем package и imports// Удаляем комментарии
        String withoutComments = removeComments(withoutPackagesAndImports); //удаляем комментарии
        String normalizedWhitespace = normalizeWhitespace(withoutComments); //убраем последовательности символов с кодом <33
        return normalizedWhitespace.trim(); //удаляем пробелы в начале и конце
    }

    private static String removePackagesAndImports(String content) { //пропускаем строки блабла
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package") && !trimmedLine.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String removeComments(String content) { //находим и убираем строчные и блочные комментарии
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = content.length();
        boolean inBlockComment = false;
        boolean inString = false;
        boolean inChar = false;

        while (i < n) {
            if (inBlockComment) {
                if (i + 1 < n && content.charAt(i) == '*' && content.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i += 2;
                } else {
                    i++;
                }
            } else if (inString) {
                result.append(content.charAt(i));
                if (content.charAt(i) == '"' && (i == 0 || content.charAt(i - 1) != '\\')) {
                    inString = false;
                }
                i++;
            } else if (inChar) {
                result.append(content.charAt(i));
                if (content.charAt(i) == '\'' && (i == 0 || content.charAt(i - 1) != '\\')) {
                    inChar = false;
                }
                i++;
            } else {
                if (i + 1 < n && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                    while (i < n && content.charAt(i) != '\n') {
                        i++;
                    }
                } else if (i + 1 < n && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i += 2;
                } else {
                    if (content.charAt(i) == '"') {
                        inString = true;
                    } else if (content.charAt(i) == '\'') {
                        inChar = true;
                    }
                    result.append(content.charAt(i));
                    i++;
                }
            }
        }

        return result.toString();
    }

    private static String normalizeWhitespace(String content) { //заменяем  <33 на прообелы
        StringBuilder result = new StringBuilder();
        boolean inWhitespace = false;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c < 33) {
                if (!inWhitespace) {
                    result.append(' '); //последовательности пробелов заменяем на одиночный пробел
                    inWhitespace = true;
                }
            } else {
                result.append(c);
                inWhitespace = false;
            }
        }

        return result.toString();
    }

    private static Map<String, List<String>> findCopies(List<FileData> files) {
        Map<String, List<String>> copies = new HashMap<>();
        int n = files.size();

        Map<Integer, List<FileData>> lengthGroups = new HashMap<>(); //группирруем файлы по длине
        for (FileData file : files) {
            int length = file.content.length();
            lengthGroups.computeIfAbsent(length, k -> new ArrayList<>()).add(file);
        }

        for (List<FileData> group : lengthGroups.values()) { //сравниваем в группах с одинаковой длиной
            if (group.size() > 1) {
                for (int i = 0; i < group.size(); i++) {
                    FileData file1 = group.get(i);
                    for (int j = i + 1; j < group.size(); j++) {
                        FileData file2 = group.get(j);

                        int distance = optimizedLevenshtein(file1.content, file2.content, 10); //считаем рнасстояние левенштейна
                        if (distance < 10) { //если оно меньше 10 значит копии добавляем
                            copies.computeIfAbsent(file1.path, k -> new ArrayList<>()).add(file2.path);
                            copies.computeIfAbsent(file2.path, k -> new ArrayList<>()).add(file1.path);
                        }
                    }
                }
            }
        }

        Map<String, List<String>> result = new HashMap<>(); //удаляем дубликаты и сортируем списки копий
        for (Map.Entry<String, List<String>> entry : copies.entrySet()) {
            List<String> uniqueCopies = new ArrayList<>(new HashSet<>(entry.getValue()));
            Collections.sort(uniqueCopies);
            result.put(entry.getKey(), uniqueCopies);
        }

        return result;
    }

    private static int optimizedLevenshtein(String s1, String s2, int threshold) {
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        int n = s1.length();
        int m = s2.length();

        if (s1.equals(s2)) { //проверка на одинаковость
            return 0;
        }

        if (Math.abs(n - m) > threshold) { //если разница больше 10 все нормально
            return threshold + 1;
        }

        return levenshteinWithThreshold(s1, s2, threshold); //считаем расстояние левенштейа
    }

    private static int levenshteinWithThreshold(String s1, String s2, int threshold) {
        int n = s1.length();
        int m = s2.length();

        // Меняем местами для экономии памяти, если нужно
        if (n > m) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
            n = s1.length();
            m = s2.length();
        }

        int[] current = new int[n + 1];
        int[] previous = new int[n + 1];

        // Инициализация первой строки
        for (int i = 0; i <= n; i++) {
            previous[i] = i;
        }

        for (int j = 1; j <= m; j++) {
            current[0] = j;
            int minInRow = j;

            for (int i = 1; i <= n; i++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                current[i] = Math.min(
                        Math.min(current[i - 1] + 1, previous[i] + 1),
                        previous[i - 1] + cost
                );
                minInRow = Math.min(minInRow, current[i]);
            }

            // Отсечение: если минимальное значение в строке превышает порог
            if (minInRow > threshold) {
                return threshold + 1;
            }

            // Обмен массивов
            int[] temp = previous;
            previous = current;
            current = temp;
        }

        return previous[n] <= threshold ? previous[n] : threshold + 1;
    }

    private static class FileData {
        String path;
        String content;

        FileData(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}