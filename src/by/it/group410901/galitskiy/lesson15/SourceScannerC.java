package by.it.group410901.galitskiy.lesson15;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerC {
    public static void main(String[] args) {
        // Формируем путь к директории src проекта
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        // Список для хранения информации о файлах (путь + очищенный текст)
        List<FileText> fileTexts = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(Paths.get(src))) {
            // Рекурсивно обходим все файлы в директории src
            stream
                    // Исключаем директории из обработки
                    .filter(p -> !Files.isDirectory(p))
                    // Обрабатываем только Java-файлы
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            // Читаем всё содержимое файла в строку
                            String text = Files.readString(path, StandardCharsets.UTF_8);

                            // Пропускаем файлы-тесты (содержащие аннотации @Test)
                            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                            // Удаляем строки package и import
                            List<String> lines = Arrays.asList(text.split("\n"));
                            List<String> filtered = new ArrayList<>();
                            for (String line : lines) {
                                String trimmed = line.replace("\r", "").trim(); // Убираем возврат каретки и лишние пробелы
                                // Пропускаем строки с объявлением пакета и импортами
                                if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) continue;
                                filtered.add(line);
                            }
                            // Собираем отфильтрованные строки обратно в текст
                            String joined = String.join("\n", filtered);

                            // Удаляем все комментарии из кода
                            joined = removeComments(joined);

                            // Нормализуем пробелы: заменяем последовательности управляющих символов на один пробел
                            joined = normalizeLowChars(joined);

                            // Убираем пробелы в начале и конце текста
                            joined = joined.trim();

                            // Получаем относительный путь файла (относительно src)
                            String relPath = src.endsWith(File.separator)
                                    ? path.toString().substring(src.length())
                                    : path.toString().replace(src, "");

                            // Сохраняем информацию о файле
                            fileTexts.add(new FileText(relPath, joined));
                        } catch (MalformedInputException e) {
                            // Игнорируем файлы с некорректной кодировкой
                        } catch (IOException e) {
                            // Игнорируем другие ошибки ввода-вывода при чтении файлов
                        }
                    });
        } catch (IOException e) {
            // Обрабатываем ошибки обхода файловой системы
            System.err.println("Ошибка обхода файлов: " + e.getMessage());
        }

        // Сортируем файлы по пути для детерминированного вывода
        fileTexts.sort(Comparator.comparing(ft -> ft.relPath));

        // Поиск копий по оптимизированному алгоритму
        int n = fileTexts.size();
        boolean[] reported = new boolean[n]; // Массив для отслеживания уже обработанных файлов

        // Сравниваем каждую пару файлов
        for (int i = 0; i < n; ++i) {
            List<String> copies = new ArrayList<>(); // Список найденных копий для текущего файла
            FileText a = fileTexts.get(i);

            for (int j = 0; j < n; ++j) {
                if (i == j) continue; // Не сравниваем файл с самим собой
                FileText b = fileTexts.get(j);

                // Быстрая фильтрация по длине текста - если разница больше 10 символов, пропускаем
                if (Math.abs(a.text.length() - b.text.length()) > 10) continue;

                // Быстрая фильтрация по начальным символам - сравниваем префиксы до 64 символов
                int prefixLen = Math.min(64, Math.min(a.text.length(), b.text.length()));
                if (!a.text.substring(0, prefixLen).equals(b.text.substring(0, prefixLen)))
                    continue;

                // Точное сравнение с помощью оптимизированного алгоритма Левенштейна
                if (levenshteinBanded(a.text, b.text, 10) < 10) {
                    copies.add(b.relPath);
                    reported[j] = true; // Помечаем файл как уже обработанный
                }
            }

            // Выводим результат, если нашли копии и текущий файл еще не был обработан
            if (!copies.isEmpty() && !reported[i]) {
                Collections.sort(copies); // Сортируем пути копий для красивого вывода
                System.out.println(a.relPath); // Выводим оригинальный файл
                for (String cp : copies) {
                    System.out.println(cp); // Выводим все найденные копии
                }
            }
        }
    }

    /**
     * Удаление всех комментариев из Java-кода: однострочных и многострочных
     * Алгоритм работает за O(n) - один проход по тексту
     * @param text исходный текст с комментариями
     * @return текст без комментариев
     */
    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder(); // Для накопления результата
        int len = text.length();
        boolean isBlock = false;  // Флаг многострочного комментария (/* ... */)
        boolean isLine = false;   // Флаг однострочного комментария (// ...)

        // Обрабатываем текст посимвольно
        for (int i = 0; i < len; ) {
            // Обнаружение начала многострочного комментария
            if (!isBlock && !isLine && i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '*') {
                isBlock = true;
                i += 2; // Пропускаем два символа '/*'
                continue;
            }
            // Обнаружение конца многострочного комментария
            if (isBlock && i + 1 < len && text.charAt(i) == '*' && text.charAt(i + 1) == '/') {
                isBlock = false;
                i += 2; // Пропускаем два символа '*/'
                continue;
            }
            // Обнаружение начала однострочного комментария
            if (!isBlock && !isLine && i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '/') {
                isLine = true;
                i += 2; // Пропускаем два символа '//'
                continue;
            }
            // Обнаружение конца однострочного комментария (конец строки)
            if (isLine && (text.charAt(i) == '\n' || text.charAt(i) == '\r')) {
                isLine = false;
                sb.append(text.charAt(i)); // Сохраняем символ перевода строки
                i++;
                continue;
            }
            // Пропускаем символы внутри комментариев
            if (isBlock || isLine) {
                i++;
                continue;
            }
            // Сохраняем символы вне комментариев
            sb.append(text.charAt(i));
            i++;
        }
        return sb.toString();
    }

    /**
     * Нормализация пробелов: заменяет все последовательности управляющих символов (<33)
     * на один пробел. Это включает пробелы, табуляции, переводы строк и т.д.
     * @param text исходный текст
     * @return текст с нормализованными пробелами
     */
    private static String normalizeLowChars(String text) {
        StringBuilder sb = new StringBuilder();
        boolean inSeq = false; // Флаг нахождения внутри последовательности управляющих символов
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                // Если встретили управляющий символ и мы не в последовательности - добавляем пробел
                if (!inSeq) sb.append(' ');
                inSeq = true;
            } else {
                // Обычный символ - добавляем как есть
                sb.append(c);
                inSeq = false;
            }
        }
        return sb.toString();
    }

    /**
     * Оптимизированный алгоритм Левенштейна с ограниченной полосой (banded)
     * Вычисляет расстояние редактирования между двумя строками, но рассматривает
     * только диагональную полосу шириной 2*max для ускорения вычислений
     *
     * @param a первая строка
     * @param b вторая строка
     * @param max максимальное допустимое расстояние (порог)
     * @return расстояние редактирования или max, если расстояние превышает порог
     */
    private static int levenshteinBanded(String a, String b, int max) {
        int lenA = a.length(), lenB = b.length();

        // Быстрая проверка: если разница длин превышает порог, сразу возвращаем max
        if (Math.abs(lenA - lenB) > max) return max;

        // Используем два массива для динамического программирования (экономия памяти)
        int[] prev = new int[lenB + 1], curr = new int[lenB + 1];

        // Инициализация первой строки матрицы
        for (int j = 0; j <= lenB; j++) prev[j] = j;

        // Заполнение матрицы построчно
        for (int i = 1; i <= lenA; i++) {
            curr[0] = i; // Стоимость превращения пустой строки в префикс a

            // Определяем диапазон столбцов для рассмотрения (только в пределах полосы)
            int from = Math.max(1, i - max);
            int to = Math.min(lenB, i + max);

            int localMin = curr[0]; // Минимальное значение в текущей строке

            for (int j = from; j <= to; j++) {
                // Стоимость замены: 0 если символы одинаковые, 1 если разные
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;

                // Вычисляем минимальную стоимость из трех операций:
                // - вставка (curr[j-1] + 1)
                // - удаление (prev[j] + 1)
                // - замена (prev[j-1] + cost)
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
                localMin = Math.min(localMin, curr[j]);
            }

            // Ранний выход: если минимальная стоимость в строке превышает порог
            if (localMin > max) return max;

            // Меняем массивы местами для следующей итерации
            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }
        return prev[lenB]; // Возвращаем итоговое расстояние
    }

    /**
     * Внутренний класс для хранения информации о файле
     * - относительный путь от src
     * - очищенный и нормализованный текст файла
     */
    static class FileText {
        final String relPath;  // Относительный путь файла
        final String text;     // Очищенный текст файла (без комментариев, с нормализованными пробелами)

        FileText(String relPath, String text) {
            this.relPath = relPath;
            this.text = text;
        }
    }
}