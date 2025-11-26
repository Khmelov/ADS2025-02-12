package by.it.group410901.getmanchuk.lesson15;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SourceScannerC {

    // основной метод: собираем файлы, очищаем текст, ищем копии
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Path.of(src);

        List<FileData> filesData = new ArrayList<>();

        // обход всех файлов .java
        try {
            Files.walk(srcPath)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        if (isTestFile(p)) return;

                        String text = processFile(p);
                        if (text != null) {
                            filesData.add(new FileData(p, text));
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // сортировка по пути для консистентного вывода
        filesData.sort(Comparator.comparing(fd -> fd.path.toString()));

        // поиск копий
        Map<String, List<String>> copiesMap = new LinkedHashMap<>();
        for (int i = 0; i < filesData.size(); i++) {
            FileData file1 = filesData.get(i);
            List<String> copies = new ArrayList<>();
            for (int j = i + 1; j < filesData.size(); j++) {
                FileData file2 = filesData.get(j);
                int diff = Math.abs(file1.text.length() - file2.text.length());
                if (diff >= 10) {
                    continue;
                }
                int dist = levenshteinBounded(file1.text, file2.text, 9);
                if (dist < 10) {
                    copies.add(srcPath.relativize(file2.path).toString());
                }
            }
            if (!copies.isEmpty()) {
                copies.sort(Comparator.naturalOrder());
                copiesMap.put(srcPath.relativize(file1.path).toString(), copies);
            }
        }

        // вывод результата
        for (Map.Entry<String, List<String>> entry : copiesMap.entrySet()) {
            System.out.println(entry.getKey());
            for (String copy : entry.getValue()) {
                System.out.println(copy);
            }
        }
    }

    // проверка тестовых файлов
    private static boolean isTestFile(Path p) {
        try (BufferedReader reader = new BufferedReader(new FileReader(p.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("@Test") || line.contains("org.junit.Test")) return true;
            }
        } catch (IOException e) {
            return true; // игнорируем проблемные файлы
        }
        return false;
    }

    // обработка файла: удаление package/import, комментариев, замена последовательностей <33 на один пробел, trim
    private static String processFile(Path p) {
        try (BufferedReader reader = new BufferedReader(new FileReader(p.toFile()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            boolean blockComment = false;
            boolean lastSpace = false;

            while ((line = reader.readLine()) != null) {
                String t = line;

                // многострочные комментарии
                if (blockComment) {
                    if (t.contains("*/")) {
                        blockComment = false;
                        t = t.substring(t.indexOf("*/") + 2);
                    } else {
                        continue;
                    }
                }

                while (t.contains("/*")) {
                    int start = t.indexOf("/*");
                    int end = t.indexOf("*/", start + 2);
                    if (end != -1) {
                        t = t.substring(0, start) + t.substring(end + 2);
                    } else {
                        t = t.substring(0, start);
                        blockComment = true;
                        break;
                    }
                }

                // однострочные комментарии
                if (t.contains("//")) {
                    t = t.substring(0, t.indexOf("//"));
                }

                t = t.trim();

                if (t.startsWith("package ") || t.startsWith("import ")) continue;

                for (int k = 0; k < t.length(); k++) {
                    char c = t.charAt(k);
                    if (c < 33) {
                        if (!lastSpace) {
                            sb.append(' ');
                            lastSpace = true;
                        }
                    } else {
                        sb.append(c);
                        lastSpace = false;
                    }
                }
                if (!lastSpace) {
                    sb.append(' ');
                    lastSpace = true;
                }
            }

            return sb.toString().trim();

        } catch (MalformedInputException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    private static int levenshteinBounded(String a, String b, int limit) {
        int n = a.length();
        int m = b.length();
        if (Math.abs(n - m) > limit) return limit + 1;

        char[] ac = a.toCharArray();
        char[] bc = b.toCharArray();

        int INF = 1_000_000;
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) prev[j] = j <= limit ? j : INF;

        for (int i = 1; i <= n; i++) {
            Arrays.fill(curr, INF);
            curr[0] = i <= limit ? i : INF;
            int start = Math.max(1, i - limit);
            int end = Math.min(m, i + limit);
            int rowMin = limit + 1;
            for (int j = start; j <= end; j++) {
                int cost = ac[i - 1] == bc[j - 1] ? 0 : 1;
                int v = prev[j - 1] + cost;
                int ins = curr[j - 1] + 1;
                int del = prev[j] + 1;
                if (ins < v) v = ins;
                if (del < v) v = del;
                curr[j] = v;
                if (v < rowMin) rowMin = v;
            }
            if (rowMin > limit) return limit + 1;
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[m];
    }

    // класс для хранения пути и текста файла
    private static class FileData {
        final Path path;
        final String text;

        FileData(Path path, String text) {
            this.path = path;
            this.text = text;
        }
    }
}