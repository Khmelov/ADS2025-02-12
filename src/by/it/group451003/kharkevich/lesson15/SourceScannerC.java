package by.it.group451003.kharkevich.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        // Формируем путь к папке src текущего проекта
        String src = System.getProperty("user.dir") + File.separator + "src";
        List<FileData> fileDataList = new ArrayList<>();
        Path srcPath = Paths.get(src);

        try {
            // Рекурсивный обход всех Java-файлов в проекте
            Files.walk(srcPath)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(file -> processJavaFile(file, srcPath, fileDataList));
        } catch (IOException e) {}

        // Находим дубликаты файлов с использованием хеширования
        Map<String, List<String>> copies = findCopiesByHashing(fileDataList);

        // Сортируем имена файлов для красивого вывода
        List<String> sortedFiles = new ArrayList<>(copies.keySet());
        Collections.sort(sortedFiles);

        // Выводим результаты: для каждого файла показываем его дубликаты
        for (String filePath : sortedFiles) {
            System.out.println(filePath);  // оригинальный файл
            List<String> copyPaths = copies.get(filePath);
            Collections.sort(copyPaths);
            for (String copyPath : copyPaths) {
                System.out.println(copyPath);  // его дубликаты
            }
        }
    }

    // Обрабатывает Java-файл и извлекает его содержимое
    private static void processJavaFile(Path file, Path srcPath, List<FileData> fileDataList) {
        try {
            String content = Files.readString(file, StandardCharsets.UTF_8);
            // Пропускаем тестовые файлы
            if (content.contains("@Test") || content.contains("org.junit.Test")) return;

            // Нормализуем содержимое файла
            String processed = processFileContent(content);
            // Получаем относительный путь
            String relativePath = srcPath.relativize(file).toString();

            // Сохраняем путь и обработанное содержимое
            fileDataList.add(new FileData(relativePath, processed));

        } catch (MalformedInputException e) {
        } catch (IOException e) {}
    }

    // Нормализует содержимое файла для сравнения
    private static String processFileContent(String content) {
        // Удаляем комментарии
        String withoutComments = removeComments(content);
        // Разбиваем на строки
        String[] lines = withoutComments.split("\\R");
        StringBuilder result = new StringBuilder();

        // Обрабатываем каждую строку
        for (String line : lines) {
            String processedLine = processLine(line);
            if (!processedLine.isEmpty()) {
                // Собираем все в одну строку с пробелами
                result.append(processedLine).append(" ");
            }
        }

        String processed = result.toString();
        // Заменяем множественные пробелы на одинарные и обрезаем
        processed = processed.replaceAll("\\s+", " ").trim();
        return processed;
    }

    // Удаляет комментарии из кода
    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int length = content.length();
        int i = 0;

        while (i < length) {
            // Однострочные комментарии
            if (i + 1 < length && content.charAt(i) == '/' && content.charAt(i + 1) == '/') {
                while (i < length && content.charAt(i) != '\n' && content.charAt(i) != '\r') i++;
            }
            // Многострочные комментарии
            else if (i + 1 < length && content.charAt(i) == '/' && content.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < length && !(content.charAt(i) == '*' && content.charAt(i + 1) == '/')) i++;
                i += 2;
            }
            // Обычный текст
            else {
                result.append(content.charAt(i));
                i++;
            }
        }
        return result.toString();
    }

    // Обрабатывает отдельную строку
    private static String processLine(String line) {
        String trimmed = line.trim();
        // Удаляем package и import
        if (trimmed.startsWith("package") || trimmed.startsWith("import")) return "";
        return trimmed;
    }

    // Основной алгоритм поиска дубликатов с использованием хеширования
    private static Map<String, List<String>> findCopiesByHashing(List<FileData> fileDataList) {
        Map<String, List<String>> copies = new HashMap<>();
        // Группируем файлы по их "сигнатуре" (длина + хеш)
        Map<FileSignature, List<FileData>> groups = new HashMap<>();

        // Группировка файлов по сигнатуре для быстрого поиска кандидатов
        for (FileData file : fileDataList) {
            FileSignature signature = new FileSignature(file.content);
            groups.computeIfAbsent(signature, k -> new ArrayList<>()).add(file);
        }

        // В каждой группе ищем точные дубликаты
        for (List<FileData> group : groups.values()) {
            if (group.size() > 1) {  // Если в группе больше одного файла
                for (int i = 0; i < group.size(); i++) {
                    FileData file1 = group.get(i);
                    List<String> fileCopies = new ArrayList<>();

                    // Сравниваем с остальными файлами в группе
                    for (int j = i + 1; j < group.size(); j++) {
                        FileData file2 = group.get(j);
                        if (isCopy(file1.content, file2.content)) {
                            fileCopies.add(file2.path);
                        }
                    }

                    // Если нашли дубликаты, сохраняем результат
                    if (!fileCopies.isEmpty()) {
                        copies.put(file1.path, fileCopies);
                    }
                }
            }
        }

        return copies;
    }

    // Проверяет, являются ли два файла дубликатами
    private static boolean isCopy(String s1, String s2) {
        // Быстрая проверка на точное совпадение
        if (s1.equals(s2)) return true;
        // Если длины сильно отличаются - не дубликаты
        if (Math.abs(s1.length() - s2.length()) > 20) return false;
        // Проверяем схожесть с помощью n-грамм
        return calculateSimilarity(s1, s2) > 0.95;  // 95% схожести
    }

    // Вычисляет коэффициент Жаккара (Jaccard similarity) на основе n-грамм
    private static double calculateSimilarity(String s1, String s2) {
        // Разбиваем строки на 3-граммы (подстроки длиной 3 символа)
        Set<String> ngrams1 = getNgrams(s1, 3);
        Set<String> ngrams2 = getNgrams(s2, 3);

        // Вычисляем пересечение множеств n-грамм
        int intersection = 0;
        for (String ngram : ngrams1) {
            if (ngrams2.contains(ngram)) {
                intersection++;
            }
        }

        // Вычисляем объединение множеств
        int union = ngrams1.size() + ngrams2.size() - intersection;
        // Коэффициент Жаккара = |intersection| / |union|
        return union == 0 ? 0 : (double) intersection / union;
    }

    // Генерирует n-граммы для строки
    private static Set<String> getNgrams(String str, int n) {
        Set<String> ngrams = new HashSet<>();
        for (int i = 0; i <= str.length() - n; i++) {
            ngrams.add(str.substring(i, i + n));
        }
        return ngrams;
    }

    // Класс для хранения информации о файле
    private static class FileData {
        final String path;     // относительный путь файла
        final String content;  // нормализованное содержимое
        FileData(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }

    // "Сигнатура" файла для быстрой группировки потенциальных дубликатов
    private static class FileSignature {
        final int length;  // длина содержимого
        final long hash;   // выборочный хеш содержимого

        FileSignature(String content) {
            this.length = content.length();
            this.hash = calculateContentHash(content);
        }

        // Вычисляет "грубый" хеш, беря каждый n-ый символ
        private long calculateContentHash(String content) {
            if (content.length() == 0) return 0;
            long hash = 0;
            // Берем каждый 10-й символ (или чаще для коротких строк)
            int step = Math.max(1, content.length() / 10);
            for (int i = 0; i < content.length(); i += step) {
                hash = hash * 31 + content.charAt(i);
            }
            return hash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FileSignature that = (FileSignature) o;
            // Считаем файлы похожими если длины близки и хеши отличаются не сильно
            return length == that.length && Math.abs(hash - that.hash) < 1000;
        }

        @Override
        public int hashCode() {
            // Группируем файлы с близкими хешами вместе
            return Objects.hash(length, hash / 1000);
        }
    }
}