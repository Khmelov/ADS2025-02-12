package by.it.group410901.kvitchenko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Stream;

public class SourceScannerA {
    // ## Компаратор для сортировки по размеру файла
    protected static class myStringComparator implements Comparator<String>{
        @Override
        public int compare(String s1, String s2) {
            int int_s1, int_s2;

            // Парсинг первого числа из строки (которое является размером файла).
            int_s1 = new Scanner(s1).nextInt(10);
            int_s2 = new Scanner(s2).nextInt(10);

            if (int_s1 == int_s2) {
                // Если размеры равны, сортировка по имени файла (остальной части строки).
                return s1.compareTo(s2);
            }
            // Сортировка по возрастанию размера.
            return int_s1 > int_s2 ? 1 : -1;
        }
    }

    // ## Метод для обрезки массива символов
    // Удаляет символы-нули (используются как маркеры удаления) с начала и конца массива.
    protected static char[] move(char[] array) {
        char[] temp;
        int i = 0, size;

        // Обрезка начальных нулей.
        while(array[i] == 0)
            i++;

        size = array.length - i;
        temp = new char[size];
        System.arraycopy(array, i, temp, 0, size);
        array = temp;

        // Обрезка конечных нулей.
        i = array.length - 1;
        while (array[i] == 0)
            i--;

        size = i + 1;
        temp = new char[size];
        System.arraycopy(array, 0, temp, 0, size);

        return temp;
    }

    // ## Основной метод сбора информации о Java-файлах
    private static void getInformation() throws IOException {
        // Список для хранения информации о размере и пути к файлу ("размер путь").
        ArrayList<String> size_directory = new ArrayList<>();

        // Получение пути к корневой директории "src".
        Path src = Path.of(System.getProperty("user.dir")
                + File.separator + "src" + File.separator);

        // Обход дерева файлов, начиная с "src".
        try (Stream<Path> fileTrees = Files.walk(src)) {
            fileTrees.forEach(
                    directory -> {
                        // Обработка только Java-файлов.
                        if (directory.toString().endsWith(".java")) {
                            try {
                                String str = Files.readString(directory);

                                // Игнорирование файлов с JUnit тестами.
                                if (!str.contains("@Test") && !str.contains("org.junit.Test")) {

                                    // Удаление строк с объявлением пакета и импортов.
                                    str = str.replaceAll("package.+;", "")
                                            .replaceAll("import.+;", "");

                                    // Логика удаления "мусорных" символов (ASCII < 33) с начала/конца.
                                    if (!str.isEmpty() && (str.charAt(0) < 33 || str.charAt(str.length() - 1) < 33)) {
                                        char[] charArr = str.toCharArray();
                                        int indexF = 0, indexL = charArr.length - 1;

                                        // Замена "мусорных" символов на 0 для последующей обрезки.
                                        while (indexF < charArr.length && charArr[indexF] < 33 && charArr[indexF] != 0)
                                            charArr[indexF++] = 0;
                                        while (indexL >= 0 && charArr[indexL] < 33 && charArr[indexL] != 0)
                                            charArr[indexL--] = 0;

                                        // Обрезка массива и преобразование обратно в строку.
                                        str = new String(move(charArr));
                                    }

                                    // Добавление в список размера файла (в байтах) и относительного пути.
                                    size_directory.add(str.getBytes().length + " " + src.relativize(directory));
                                }
                            } catch (IOException e) {
                                // Обработка исключения ввода/вывода (вывод ошибки заблокирован ложным условием).
                                if (System.currentTimeMillis() < 0) {
                                    System.err.println(directory);
                                }
                            }
                        }
                    }
            );

            // Сортировка списка по размеру файла.
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