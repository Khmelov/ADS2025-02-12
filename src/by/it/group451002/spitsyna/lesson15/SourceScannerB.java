package by.it.group451002.spitsyna.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerB {
    private static class FileInfo {
        int size;
        String relativePath;

        FileInfo(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        Path srcPath = Paths.get(src);
        List<FileInfo> results = new ArrayList<>();

        try {
            // Рекурсивно обходим все файлы *.java в src
            Files.walk(srcPath)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            processFile(path, srcPath, results);
                        } catch (IOException e) {
                            return;
                        }
                    });
        } catch (IOException e) {
            return;
        }

        // Сортировка: по размеру, при равенстве - лексикографически по пути
        results.sort(Comparator.comparingInt((FileInfo f) -> f.size).thenComparing(f -> f.relativePath));

        for (FileInfo info : results) {
            System.out.println(info.size + " " + info.relativePath);
        }
    }

    private static void processFile(Path path, Path srcPath, List<FileInfo> results) throws IOException {
        String content;
        try {
            // Пробуем прочитать как UTF-8
            content = Files.readString(path, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            try {
                //Пробуем прочитать как ISO-8859-1 (читает любые байты)
                content = Files.readString(path, StandardCharsets.ISO_8859_1);
            } catch (IOException ex) {
                return;
            }
        }

        //Пропускаем файлы с тестами
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        //Удаление package и imports
        String processed = removePackageAndImports(content);

        //Удаление всех комментариев
        processed = removeAllComments(processed);

        //Удаление символов с кодом < 33 в начале и конце
        processed = trimControlChars(processed);

        //Удаление всех пустых строк
        processed = removeEmptyStrings(processed);

        //Размер в байтах
        int sizeInBytes = processed.getBytes(StandardCharsets.UTF_8).length;

        //Относительный путь от src
        String relativePath = srcPath.relativize(path).toString();

        results.add(new FileInfo(sizeInBytes, relativePath));
    }

    private static String removeEmptyStrings(String content){
        StringBuilder result = new StringBuilder();
        String[] strings = content.split("\n");
        for (String line : strings){
            if (!line.trim().isEmpty()){
                result.append(line);
                result.append("\n");
            }
        }
        return result.toString();
    }

    private static String removeAllComments(String content){
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
    }

    private static String trimControlChars(String text) {
        int start = 0;
        int end = text.length();

        // Удаляем символы с кодом < 33 в начале
        while (start < end && text.charAt(start) < 33) {
            start++;
        }

        // Удаляем символы с кодом < 33 в конце
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        return text.substring(start, end);
    }
}
