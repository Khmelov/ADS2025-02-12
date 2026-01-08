package by.it.group451002.spizharnaya.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/*
В каждом тексте файла необходимо:
1. Удалить строку package и все импорты.
2. Удалить все комментарии за O(n) от длины текста.
3. Заменить все последовательности символов с кодом <33 на 32 (один пробел), т.е привести текст к строке.
4. Выполнить trim() для полученной строки.

В полученном наборе текстов:
1. Найти наиболее похожие тексты по метрике "расстояние Левенштейна",
   и определить копия ли это, считая копиями тексты с числом правок <10.
2. Если текст имеет копию(и), то вывести путь файла этого текста
   и в следующих строках путь(и) к копии(ям).
3. Повторить для всех файлов с копиями,
   при выводе сортировать файлы лексикографически по их пути.

Найдите способ корректно обрабатывать ошибки MalformedInputException
 */
public class SourceScannerC {
    private static class FileData {
        String path;
        String content;
        FileData(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }

    private static final int K = 10;

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        Path srcPath = Paths.get(src);
        List<FileData> files = new ArrayList<>();

        try {
            Files.walk(srcPath)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            processFile(path, srcPath, files);
                        } catch (IOException e) {
                            return;
                        }
                    });
        } catch (IOException e) {
            return;
        }

        findAndPrintCopies(files);
    }

    private static void processFile(Path path, Path srcPath, List<FileData> files) throws IOException {
        String content;
        try {
            content = Files.readString(path, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            try {
                content = Files.readString(path, StandardCharsets.ISO_8859_1);
            } catch (IOException ex) {
                return;
            }
        }

        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        String processed = removePackageAndImports(content);

        processed = removeAllComments(processed);

        processed = replaceControlCharsWithSpace(processed);

        processed = processed.trim();

        String relativePath = srcPath.relativize(path).toString();
        files.add(new FileData(relativePath, processed));
    }

    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = content.length();

        //Проходимся по всем строкам файла
        while (i < n) {
            int lineStart = i;

            // Проверяем, начинается ли строка с package или import
            boolean skipLine = false;

            if (i < n && startsWithKeyword(content, i, "package ")) {
                skipLine = true;
            } else if (i < n && startsWithKeyword(content, i, "import ")) {
                skipLine = true;
            }

            // Находим конец строки
            int lineEnd = i;
            while (lineEnd < n && content.charAt(lineEnd) != '\n') {
                lineEnd++;
            }

            if (skipLine) {
                // Пропускаем строку (включая \n)
                i = lineEnd;
                if (i < n && content.charAt(i) == '\n') {
                    i++;
                }
            } else {
                // Добавляем строку в результат
                if (lineEnd < n) {
                    result.append(content, lineStart, lineEnd + 1); // включая \n
                    i = lineEnd + 1;
                } else {
                    result.append(content, lineStart, lineEnd);
                    i = lineEnd;
                }
            }
        }

        return result.toString();
    }

    private static boolean startsWithKeyword(String content, int index, String keyword) {
        if (index + keyword.length() > content.length()) {
            return false;
        }
        for (int i = 0; i < keyword.length(); i++) {
            if (content.charAt(index + i) != keyword.charAt(i)) {
                return false;
            }
        }
        return true;
    }private static String removeAllComments(String content){
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = content.length();

        while (i < n){
            //Если текущий символ двойная кавычка, то копируем все до закрывающей двойной кавычки
            if (content.charAt(i) == '"'){
                result.append(content.charAt(i));
                i++;
                while ((i < n) && content.charAt(i) != '"'){
                    result.append(content.charAt(i));
                    i++;
                }
                result.append(content.charAt(i));
                i++;
            }
            //Если начинается однострочный комментарий
            else if ((i+1 < n) && (content.charAt(i) == '/') && (content.charAt(i+1) == '/')){
                while (i < n && content.charAt(i) != '\n')
                    i++;
            }
            //Если начинается многострочный комментарий
            else if ((i+1 < n) && (content.charAt(i) == '/') && (content.charAt(i+1) == '*')){
                i += 2;
                while ((i+1 < n) && (content.charAt(i) != '*') && (content.charAt(i+1) != '/')){
                    i++;
                }
                i += 2;
            }
            else {
                result.append(content.charAt(i));
                i++;
            }
        }
        return result.toString();
    }

    private static String replaceControlCharsWithSpace(String content) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = content.length();

        while (i < n) {
            if (content.charAt(i) < 33) {
                while (i < n && content.charAt(i) < 33)
                    i++;
                result.append(' ');
            } else {
                result.append(content.charAt(i++));
            }
        }
        return result.toString();
    }

    private static void findAndPrintCopies(List<FileData> files) {
        int n = files.size();
        Map<String, Set<String>> copies = new TreeMap<>();

        // Конвертируем в char[] для быстрого доступа
        char[][] chars = new char[n][];
        for (int i = 0; i < n; i++) {
            chars[i] = files.get(i).content.toCharArray();
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Быстрая проверка длины
                if (Math.abs(chars[i].length - chars[j].length) >= K) continue;

                int dist = levenshtein(chars[i], chars[j]);
                if (dist < K) {
                    String p1 = files.get(i).path;
                    String p2 = files.get(j).path;
                    copies.computeIfAbsent(p1, k -> new TreeSet<>()).add(p2);
                    copies.computeIfAbsent(p2, k -> new TreeSet<>()).add(p1);
                }
            }
        }

        for (Map.Entry<String, Set<String>> e : copies.entrySet()) {
            System.out.println(e.getKey());
            for (String p : e.getValue()) {
                System.out.println(p);
            }
        }
    }


    private static int levenshtein(char[] s1, char[] s2) {
        int len1 = s1.length, len2 = s2.length;

        if (Math.abs(len1 - len2) >= K) return K;

        // s1 должен быть короче
        if (len1 > len2) {
            char[] t = s1; s1 = s2; s2 = t;
            int tl = len1; len1 = len2; len2 = tl;
        }

        // Используем один массив с хитрым обновлением
        int[] d = new int[len1 + 1];
        for (int i = 0; i <= len1; i++) d[i] = i;

        for (int j = 1; j <= len2; j++) {
            int prev = d[0];
            d[0] = j;

            int from = Math.max(1, j - K);
            int to = Math.min(len1, j + K);

            // Сбрасываем значения за пределами полосы
            if (from > 1) d[from - 1] = K;

            int minInRow = K;

            for (int i = from; i <= to; i++) {
                int temp = d[i];
                int cost = (s1[i - 1] == s2[j - 1]) ? 0 : 1;
                d[i] = Math.min(Math.min(d[i] + 1, d[i - 1] + 1), prev + cost);
                minInRow = Math.min(minInRow, d[i]);
                prev = temp;
            }

            if (minInRow >= K) return K;
        }

        return Math.min(d[len1], K);
    }

}