package by.it.group410902.barbashova.lesson15;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {
    public static void main(String[] args) throws IOException {
        // Получаем путь к папке src
        String src = System.getProperty("user.dir") + File.separator + "src";

        // Список для хранения результатов (размер + путь)
        List<String> results = new ArrayList<>();

        // Ищем все .java файлы в папке src
        Files.walk(Paths.get(src))
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(file -> {
                    try {
                        // Читаем файл
                        String content = Files.readString(file);

                        // Пропускаем тестовые файлы
                        if (content.contains("@Test") || content.contains("org.junit.Test")) {
                            return;
                        }

                        // Удаляем package и import строки
                        String[] lines = content.split("\n");
                        StringBuilder newContent = new StringBuilder();
                        for (String line : lines) {
                            if (!line.trim().startsWith("package") &&
                                    !line.trim().startsWith("import")) {
                                newContent.append(line).append("\n");
                            }
                        }

                        // Удаляем пробелы в начале и конце
                        String finalText = newContent.toString().trim();

                        // Получаем размер в байтах
                        int size = finalText.getBytes().length;

                        // Получаем относительный путь
                        String relativePath = Paths.get(src).relativize(file).toString();

                        // Добавляем в результаты
                        results.add(size + " " + relativePath);

                    } catch (Exception e) {
                        // Игнорируем ошибки чтения файлов
                    }
                });

        // Сортируем результаты
        Collections.sort(results, (a, b) -> {
            String[] partsA = a.split(" ", 2);
            String[] partsB = b.split(" ", 2);
            int sizeA = Integer.parseInt(partsA[0]);
            int sizeB = Integer.parseInt(partsB[0]);

            if (sizeA != sizeB) {
                return Integer.compare(sizeA, sizeB);
            } else {
                return partsA[1].compareTo(partsB[1]);
            }
        });

        // Выводим результаты
        for (String result : results) {
            System.out.println(result);
        }
    }
}
