package by.it.group410902.barbashova.lesson15;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {
    public static void main(String[] args) throws IOException {
        // Получаем путь к папке src
        String src = System.getProperty("user.dir") + File.separator + "src";

        // Список для хранения результатов
        List<String> results = new ArrayList<>();

        // Ищем все .java файлы
        Files.walk(Paths.get(src))
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(file -> {
                    try {

                        String content = Files.readString(file);
                        // Пропускаем тестовые файлы
                        if (content.contains("@Test") || content.contains("org.junit.Test")) {
                            return;
                        }

                        // Обрабатываем содержимое
                        String processed = processContent(content);

                        // Получаем размер и путь
                        int size = processed.getBytes().length;
                        String relativePath = Paths.get(src).relativize(file).toString();

                        results.add(size + " " + relativePath);

                    } catch (Exception e) {
                        // Игнорируем ошибки
                    }
                });

        // Сортируем и выводим
        Collections.sort(results, (a, b) -> {
            String[] partsA = a.split(" ", 2);
            String[] partsB = b.split(" ", 2);
            int sizeA = Integer.parseInt(partsA[0]);
            int sizeB = Integer.parseInt(partsB[0]);

            if (sizeA != sizeB) {
                return Integer.compare(sizeA, sizeB);
            }
            return partsA[1].compareTo(partsB[1]);
        });

        for (String result : results) {
            System.out.println(result);
        }
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");
        boolean inBlockComment = false;

        for (String line : lines) {
            String trimmed = line.trim();

            // Пропускаем package и import
            if (trimmed.startsWith("package") || trimmed.startsWith("import")) {
                continue;
            }

            // Обрабатываем комментарии
            CommentResult commentResult = removeComments(line, inBlockComment);
            inBlockComment = commentResult.inBlockComment;
            String processedLine = commentResult.text.trim();

            // Пропускаем пустые строки
            if (!processedLine.isEmpty()) {
                result.append(processedLine).append("\n");
            }
        }
        return result.toString().trim();
    }

    private static CommentResult removeComments(String line, boolean inBlockComment) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        boolean currentInBlockComment = inBlockComment;

        while (i < line.length()) {
            if (currentInBlockComment) {
                // Ищем конец блочного комментария */
                if (i + 1 < line.length() && line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                    currentInBlockComment = false;
                    i += 2;
                } else {
                    i++;
                }
            } else {
                // Проверяем начало комментариев
                if (i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                    currentInBlockComment = true;
                    i += 2;
                } else if (i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                    // Пропускаем строковый комментарий
                    break;
                } else {
                    result.append(line.charAt(i));
                    i++;
                }
            }
        }

        return new CommentResult(result.toString(), currentInBlockComment);
    }

    // Вспомогательный класс для возврата двух значений
    static class CommentResult {
        String text;
        boolean inBlockComment;

        CommentResult(String text, boolean inBlockComment) {
            this.text = text;
            this.inBlockComment = inBlockComment;
        }
    }
}