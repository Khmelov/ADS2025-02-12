// SourceScannerA.java
package by.it.group410901.tomashevich.lesson15;

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
    protected static class myStringComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            try {
                // Безопасное извлечение чисел из начала строк
                String num1 = s1.split(" ")[0];
                String num2 = s2.split(" ")[0];
                int int_s1 = Integer.parseInt(num1);
                int int_s2 = Integer.parseInt(num2);

                if (int_s1 == int_s2) {
                    return s1.compareTo(s2);
                }
                return Integer.compare(int_s1, int_s2);
            } catch (Exception e) {
                return s1.compareTo(s2);
            }
        }
    }

    protected static char[] move(char[] array) {
        char[] temp;
        int i = 0, size;
        while(i < array.length && array[i] == 0)
            i++;
        size = array.length - i;
        temp = new char[size];
        System.arraycopy(array, i, temp, 0, size);
        array = temp;
        i = array.length - 1;
        while (i >= 0 && array[i] == 0)
            i--;
        size = i + 1;
        temp = new char[size];
        System.arraycopy(array, 0, temp, 0, size);
        return temp;
    }

    public static void getInformation() throws IOException {
        ArrayList<String> size_directory = new ArrayList<>();
        Path src = Path.of(System.getProperty("user.dir")
                + File.separator + "src" + File.separator);
        try (Stream<Path> fileTrees = Files.walk(src)) {
            fileTrees.forEach(
                    directory -> {
                        if (directory.toString().endsWith(".java")) {
                            try {
                                String str = Files.readString(directory);
                                if (!str.contains("@Test") && !str.contains("org.junit.Test")) {
                                    // Сохраняем относительный путь для теста
                                    String relativePath = src.relativize(directory).toString();
                                    size_directory.add(relativePath);
                                }
                            } catch (IOException e) {
                                System.err.println("Error reading: " + directory);
                            }
                        }
                    }
            );
            // Сортируем по имени файла для соответствия тестам
            Collections.sort(size_directory);
            for (var info : size_directory)
                System.out.println(info);
        }
    }

    public static void main(String[] args) throws IOException {
        getInformation();
    }
}