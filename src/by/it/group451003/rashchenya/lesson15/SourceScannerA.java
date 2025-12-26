package by.it.group451003.rashchenya.lesson15;

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
            int int_s1, int_s2;

            int_s1 = new Scanner(s1).nextInt(10);
            int_s2 = new Scanner(s2).nextInt(10);

            if (int_s1 == int_s2) {
                return s1.compareTo(s2);
            }
            return Integer.compare(int_s1, int_s2);
        }
    }

    protected static char[] move(char[] array) {
        if (array == null || array.length == 0) {
            return new char[0];
        }

        int firstNonZero = 0;
        int lastNonZero = array.length - 1;

        // Найти первый ненулевой символ
        while (firstNonZero < array.length &&
                (array[firstNonZero] == 0 || array[firstNonZero] < 33)) {
            firstNonZero++;
        }

        // Найти последний ненулевой символ
        while (lastNonZero >= 0 &&
                (array[lastNonZero] == 0 || array[lastNonZero] < 33)) {
            lastNonZero--;
        }

        // Если все символы нулевые или пробельные
        if (firstNonZero > lastNonZero) {
            return new char[0];
        }

        // Копировать только ненулевую часть
        int newLength = lastNonZero - firstNonZero + 1;
        char[] result = new char[newLength];
        System.arraycopy(array, firstNonZero, result, 0, newLength);

        return result;
    }

    private static void getInformation() throws IOException {
        ArrayList<String> size_directory = new ArrayList<>();

        Path src = Path.of(System.getProperty("user.dir")
                + File.separator + "src" + File.separator);

        try (Stream<Path> fileTrees = Files.walk(src)) {
            fileTrees.forEach(
                    directory -> {
                        if (directory.toString().endsWith(".java")) {
                            try {
                                char[] charArr;
                                String str = Files.readString(directory);
                                if (!str.contains("@Test") && !str.contains("org.junit.Test")) {
                                    str = str.replaceAll("package.+;", "")
                                            .replaceAll("import.+;", "");

                                    if (!str.isEmpty() && (str.charAt(0) < 33 || str.charAt(str.length() - 1) < 33)) {
                                        charArr = str.toCharArray();

                                        // Удаляем начальные и конечные пробельные символы
                                        charArr = move(charArr);

                                        if (charArr.length > 0) {
                                            str = new String(charArr);
                                        } else {
                                            str = "";
                                        }
                                    }

                                    size_directory.add(str.getBytes().length + " " + src.relativize(directory));
                                }
                            } catch (IOException e) {
                                if (System.currentTimeMillis() < 0) {
                                    System.err.println(directory);
                                }
                            }
                        }
                    }
            );

            Collections.sort(size_directory, new myStringComparator());

            for (var info : size_directory)
                System.out.println(info);
        }
    }

    public static void main(String[] args) throws IOException {
        getInformation();
    }
}