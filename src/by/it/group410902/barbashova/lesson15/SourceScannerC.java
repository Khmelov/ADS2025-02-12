package by.it.group410902.barbashova.lesson15;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {
    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src";
        List<FileData> files = new ArrayList<>();

        // Читаем и обрабатываем все файлы
        Files.walk(Paths.get(src))
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(file -> {
                    try {

                        String content = Files.readString(file);

                        if (content.contains("@Test") || content.contains("org.junit.Test")) {
                            return;
                        }

                        String processed = processContent(content);
                        String relativePath = Paths.get(src).relativize(file).toString();
                        files.add(new FileData(relativePath, processed));

                    } catch (Exception e) {}
                });

        // Группируем файлы по размеру (быстрая предварительная фильтрация)
        Map<Integer, List<FileData>> filesBySize = new HashMap<>();
        for (FileData file : files) {
            int size = file.content.length();
            filesBySize.computeIfAbsent(size, k -> new ArrayList<>()).add(file);
        }

        // Ищем копии среди файлов схожего размера
        Map<String, List<String>> copies = new TreeMap<>();

        for (List<FileData> sameSizeFiles : filesBySize.values()) {
            if (sameSizeFiles.size() < 2) continue;

            for (int i = 0; i < sameSizeFiles.size(); i++) {
                for (int j = i + 1; j < sameSizeFiles.size(); j++) {
                    FileData file1 = sameSizeFiles.get(i);
                    FileData file2 = sameSizeFiles.get(j);

                    // Быстрая проверка
                    if (!quickCheck(file1.content, file2.content)) continue;

                    // Если быстро прошли, проверяем полное расстояние
                    int distance = fastSimilarityCheck(file1.content, file2.content);

                    if (distance < 10) {
                        copies.computeIfAbsent(file1.path, k -> new ArrayList<>())
                                .add(file2.path);
                        copies.computeIfAbsent(file2.path, k -> new ArrayList<>())
                                .add(file1.path);
                    }
                }
            }
        }

        // Выводим результаты
        for (String filePath : copies.keySet()) {
            System.out.println(filePath);
            List<String> copyPaths = copies.get(filePath);
            Collections.sort(copyPaths);
            for (String copyPath : copyPaths) {
                System.out.println(copyPath);
            }
        }
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            // Обработка комментариев
            if (!inBlockComment && !inLineComment && i + 1 < content.length()) {
                if (c == '/' && content.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i++;
                    continue;
                } else if (c == '/' && content.charAt(i + 1) == '/') {
                    inLineComment = true;
                    i++;
                    continue;
                }
            }

            if (inBlockComment && i + 1 < content.length() &&
                    c == '*' && content.charAt(i + 1) == '/') {
                inBlockComment = false;
                i++;
                continue;
            }

            if (inLineComment && c == '\n') {
                inLineComment = false;
            }

            if (!inBlockComment && !inLineComment) {
                // Пропускаем package и import только в начале строки
                if (i == 0 || content.charAt(i - 1) == '\n') {
                    if (content.startsWith("package", i) || content.startsWith("import", i)) {
                        // Пропускаем всю строку
                        while (i < content.length() && content.charAt(i) != '\n') {
                            i++;
                        }
                        continue;
                    }
                }

                // Заменяем символы <33 на пробелы
                result.append(c < 33 ? ' ' : c);
            }
        }

        return result.toString().trim();
    }

    // Быстрая проверка по первым символам
    private static boolean quickCheck(String s1, String s2) {
        if (s1.length() != s2.length()) return false;

        // Проверяем первые 50 символов
        int checkLength = Math.min(50, s1.length());
        for (int i = 0; i < checkLength; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    // Быстрая проверка схожести б
    private static int fastSimilarityCheck(String s1, String s2) {
        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0) return 0;

        int differences = 0;
        int minLength = Math.min(s1.length(), s2.length());

        // Считаем различия в пределах минимальной длины
        for (int i = 0; i < minLength; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                differences++;
                if (differences >= 10) return differences;
            }
        }

        // Добавляем разницу в длинах
        differences += Math.abs(s1.length() - s2.length());

        return differences;
    }

    static class FileData {
        String path;
        String content;

        FileData(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}