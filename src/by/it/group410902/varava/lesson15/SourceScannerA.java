package by.it.group410902.varava.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Path.of(src);// Создаём объект Path для папки src
        List<Result> results = new ArrayList<>();// Список, куда будем складывать результаты: размер + путь

        try (var walk = Files.walk(root)) {// Перебираем все файлы внутри каталога src
            walk.forEach(path -> {
                if (path.toString().endsWith(".java")) {  // Проверяем — это файл .java

                    String text;
                    try {
                        text = Files.readString(path);
                    } catch (MalformedInputException e) { // Если файл в "плохой" кодировке — просто пропускаем его

                        return;
                    } catch (IOException e) {
                        return;
                    }
                    if (text.contains("@Test") || text.contains("org.junit.Test")) { // Пропускаем тесты
                        return;
                    }

                    // Удаляем package и import
                    StringBuilder sb = new StringBuilder();
                    String[] lines = text.split("\n");
                    for (String line : lines) {// Перебираем все строки файла
                        String trimmed = line.trim();
                        if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) { // Если строка начинается с package и с import — пропускаем
                            continue;
                        }
                        sb.append(line).append("\n");
                    }

                    String cleaned = trimLowAscii(sb.toString());  // Очищаем ненужные символы в начале и в конце

                    int size = cleaned.getBytes().length;// Считаем размер в байтах

                    String rel = root.relativize(path).toString();// Получаем путь файла относительно src/

                    results.add(new Result(size, rel));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // сортировка: сначала size, потом путь
        results.sort(
                Comparator.comparingInt((Result r) -> r.size)
                        .thenComparing(r -> r.path)
        );

        for (Result r : results) {
            System.out.println(r.size + " " + r.path);
        }
    }

    private static String trimLowAscii(String s) {// Функция удаляет все символы < 33 (пробелы, табуляции, переносы)
        int start = 0;
        int end = s.length() - 1;

        while (start <= end && s.charAt(start) < 33) start++;// Двигаем указатель start вправо пока символы < 33
        while (end >= start && s.charAt(end) < 33) end--;

        if (start > end) return "";
        return s.substring(start, end + 1);
    }

    private record Result(int size, String path) {}// Простая запись результата: размер + путь
}
