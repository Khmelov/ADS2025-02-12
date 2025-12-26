package by.it.group410901.kvitchenko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

// ## Класс SourceScannerB
// Расширяет SourceScannerA, наследуя myStringComparator и метод move().
public class SourceScannerB extends SourceScannerA{

    // ## Метод сбора информации (с обновленной логикой очистки)
    protected static void getInformation() throws IOException {
        // Список для хранения информации о размере и пути к файлу ("размер путь").
        ArrayList<String> size_directory = new ArrayList<>();

        // Получение пути к корневой директории "src".
        Path src = Path.of(System.getProperty("user.dir")
                + File.separator + "src" + File.separator);

        // Обход дерева файлов.
        try (Stream<Path> fileTrees = Files.walk(src)) {
            fileTrees.forEach(
                    directory -> {
                        // Обработка только Java-файлов.
                        if (directory.toString().endsWith(".java")) {
                            try {
                                char[] charArr;
                                String str = Files.readString(directory);

                                // Игнорирование файлов с JUnit тестами.
                                if (!str.contains("@Test") && !str.contains("org.junit.Test")) {

                                    // Удаление строк с объявлением пакета и импортов (более широкие регулярные выражения, чем в A).
                                    str = str.replaceAll("package.*;", "")
                                            .replaceAll("import.*;", "");

                                    // *** НОВАЯ ЛОГИКА ОЧИСТКИ ***

                                    // 1. Удаление многострочных комментариев (/* ... */).
                                    str = str.replaceAll("/\\*[\\w\\W\r\n\t]*?\\*/", "")
                                            // 2. Удаление однострочных комментариев (// ...).
                                            .replaceAll("//.*?\r\n\\s*", "");

                                    // 3. Удаление двойных пустых строк (замена \r\n\r\n на \r\n).
                                    while (str.contains("\r\n\r\n"))
                                        str = str.replaceAll("\r\n\r\n", "\r\n");

                                    // 4. Логика удаления "мусорных" символов (ASCII < 33) с начала/конца.
                                    if (!str.isEmpty() && (str.charAt(0) < 33 || str.charAt(str.length() - 1) < 33)) {
                                        charArr = str.toCharArray();
                                        int indexF = 0, indexL = charArr.length - 1;

                                        // Замена "мусорных" символов на 0 для последующей обрезки.
                                        while (indexF < charArr.length && charArr[indexF] < 33 && charArr[indexF] != 0)
                                            charArr[indexF++] = 0;
                                        while (indexL >= 0 && charArr[indexL] < 33 && charArr[indexL] != 0)
                                            charArr[indexL--] = 0;

                                        // Обрезка массива с помощью унаследованного метода move().
                                        str = new String(move(charArr));
                                    }

                                    // Добавление в список размера очищенного файла (в байтах) и относительного пути.
                                    size_directory.add(str.getBytes().length + " " + src.relativize(directory));
                                }
                            } catch (IOException e) {
                                // Обработка исключения.
                                if (System.currentTimeMillis() < 0) {
                                    System.err.println(directory);
                                }
                            }
                        }
                    }
            );

            // Сортировка списка по размеру с использованием унаследованного myStringComparator().
            Collections.sort(size_directory, new myStringComparator());

            // Вывод отсортированной информации.
            for (var info : size_directory)
                System.out.println(info);
        }
    }

    // Точка входа в программу.
    public static void main(String[] args) throws IOException {
        getInformation();
    }
}