package by.bsuir.dsa.csv2025.gr410901.Корнеев;



import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.experimental.runners.Enclosed;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;

@RunWith(Enclosed.class)
public class Solution{

    public static class InsertionSort {

        /**
         * Выполняет сортировку вставками и выводит пошаговые результаты.
         *
         * @param arr Массив для сортировки
         */
        public void sort(int[] arr) {
            int n = arr.length;
            System.out.println("Original array: " + arrayToString(arr));
            if (n <= 1) {
                System.out.println("Sorted array: " + arrayToString(arr));
                return;
            }
            for (int i = 1; i < n; i++) {
                int current = arr[i];
                int j = i - 1;
                while (j >= 0 && arr[j] > current) {
                    arr[j + 1] = arr[j];
                    j--;
                }
                arr[j + 1] = current;
                System.out.println("After iteration " + i + ": " + arrayToString(arr));
            }
            System.out.println("Sorted array: " + arrayToString(arr));
        }

        /**
         * Вспомогательный метод для форматирования массива в строку [x, y, z]
         */
        private String arrayToString(int[] arr) {
            return Arrays.toString(arr);
        }

        /**
         * Основная точка входа. Считывает данные и запускает сортировку.
         */
        public static void main(String[] args) {
            InsertionSort instance = new InsertionSort();
            if (args.length > 0) {
                try {
                    instance.readAndSort(args[0]);
                } catch (FileNotFoundException e) {
                    System.err.println("File not found: " + args[0]);
                }
            } else {
                try {
                    instance.readAndSort(null);
                } catch (FileNotFoundException e) {
                    // Игнорируем, так как читаем из System.in
                }
            }
        }

        /**
         * Считывает данные (N, затем N элементов) и вызывает sort().
         *
         * @param filename Имя файла или null для чтения из System.in
         */
        public void readAndSort(String filename) throws FileNotFoundException {
            Scanner scanner;
            if (filename != null) {
                scanner = new Scanner(new File(filename)).useLocale(Locale.ENGLISH);
            } else {
                scanner = new Scanner(System.in).useLocale(Locale.ENGLISH);
            }
            if (scanner.hasNextInt()) {
                int n = scanner.nextInt();
                int[] arr = new int[n];
                for (int i = 0; i < n; i++) {
                    if (scanner.hasNextInt()) {
                        arr[i] = scanner.nextInt();
                    }
                }
                sort(arr);
            }
            scanner.close();
        }
    }

    public static class TestInsertionSort {

        private final Class<?> mainClass = InsertionSort.class;

        @Test
        public void testAllCases() {
            // Тест 1
            runTest(1, "6\n5 2 4 6 1 3\n",
                    "Original array: [5, 2, 4, 6, 1, 3]\n" +
                            "After iteration 1: [2, 5, 4, 6, 1, 3]\n" +
                            "After iteration 2: [2, 4, 5, 6, 1, 3]\n" +
                            "After iteration 3: [2, 4, 5, 6, 1, 3]\n" +
                            "After iteration 4: [1, 2, 4, 5, 6, 3]\n" +
                            "After iteration 5: [1, 2, 3, 4, 5, 6]\n" +
                            "Sorted array: [1, 2, 3, 4, 5, 6]\n");

            // Тест 2
            runTest(2, "5\n1 2 3 4 5\n",
                    "Original array: [1, 2, 3, 4, 5]\n" +
                            "After iteration 1: [1, 2, 3, 4, 5]\n" +
                            "After iteration 2: [1, 2, 3, 4, 5]\n" +
                            "After iteration 3: [1, 2, 3, 4, 5]\n" +
                            "After iteration 4: [1, 2, 3, 4, 5]\n" +
                            "Sorted array: [1, 2, 3, 4, 5]\n");

            // Тест 3
            runTest(3, "5\n5 4 3 2 1\n",
                    "Original array: [5, 4, 3, 2, 1]\n" +
                            "After iteration 1: [4, 5, 3, 2, 1]\n" +
                            "After iteration 2: [3, 4, 5, 2, 1]\n" +
                            "After iteration 3: [2, 3, 4, 5, 1]\n" +
                            "After iteration 4: [1, 2, 3, 4, 5]\n" +
                            "Sorted array: [1, 2, 3, 4, 5]\n");

            // Тест 4
            runTest(4, "1\n42\n",
                    "Original array: [42]\n" +
                            "Sorted array: [42]\n");

            // Тест 5
            runTest(5, "2\n2 1\n",
                    "Original array: [2, 1]\n" +
                            "After iteration 1: [1, 2]\n" +
                            "Sorted array: [1, 2]\n");

            // Тест 6
            runTest(6, "4\n7 7 7 7\n",
                    "Original array: [7, 7, 7, 7]\n" +
                            "After iteration 1: [7, 7, 7, 7]\n" +
                            "After iteration 2: [7, 7, 7, 7]\n" +
                            "After iteration 3: [7, 7, 7, 7]\n" +
                            "Sorted array: [7, 7, 7, 7]\n");

            // Тест 7
            runTest(7, "6\n3 1 2 1 3 2\n",
                    "Original array: [3, 1, 2, 1, 3, 2]\n" +
                            "After iteration 1: [1, 3, 2, 1, 3, 2]\n" +
                            "After iteration 2: [1, 2, 3, 1, 3, 2]\n" +
                            "After iteration 3: [1, 1, 2, 3, 3, 2]\n" +
                            "After iteration 4: [1, 1, 2, 3, 3, 2]\n" +
                            "After iteration 5: [1, 1, 2, 2, 3, 3]\n" +
                            "Sorted array: [1, 1, 2, 2, 3, 3]\n");

            // Тест 8
            runTest(8, "8\n8 3 6 1 9 4 2 7\n",
                    "Original array: [8, 3, 6, 1, 9, 4, 2, 7]\n" +
                            "After iteration 1: [3, 8, 6, 1, 9, 4, 2, 7]\n" +
                            "After iteration 2: [3, 6, 8, 1, 9, 4, 2, 7]\n" +
                            "After iteration 3: [1, 3, 6, 8, 9, 4, 2, 7]\n" +
                            "After iteration 4: [1, 3, 6, 8, 9, 4, 2, 7]\n" +
                            "After iteration 5: [1, 3, 4, 6, 8, 9, 2, 7]\n" +
                            "After iteration 6: [1, 2, 3, 4, 6, 8, 9, 7]\n" +
                            "After iteration 7: [1, 2, 3, 4, 6, 7, 8, 9]\n" +
                            "Sorted array: [1, 2, 3, 4, 6, 7, 8, 9]\n");

            // Тест 9
            runTest(9, "5\n-3 2 -5 0 -1\n",
                    "Original array: [-3, 2, -5, 0, -1]\n" +
                            "After iteration 1: [-3, 2, -5, 0, -1]\n" +
                            "After iteration 2: [-5, -3, 2, 0, -1]\n" +
                            "After iteration 3: [-5, -3, 0, 2, -1]\n" +
                            "After iteration 4: [-5, -3, -1, 0, 2]\n" +
                            "Sorted array: [-5, -3, -1, 0, 2]\n");

            // Тест 10
            runTest(10, "7\n10 20 5 15 25 0 30\n",
                    "Original array: [10, 20, 5, 15, 25, 0, 30]\n" +
                            "After iteration 1: [10, 20, 5, 15, 25, 0, 30]\n" +
                            "After iteration 2: [5, 10, 20, 15, 25, 0, 30]\n" +
                            "After iteration 3: [5, 10, 15, 20, 25, 0, 30]\n" +
                            "After iteration 4: [5, 10, 15, 20, 25, 0, 30]\n" +
                            "After iteration 5: [0, 5, 10, 15, 20, 25, 30]\n" +
                            "After iteration 6: [0, 5, 10, 15, 20, 25, 30]\n" +
                            "Sorted array: [0, 5, 10, 15, 20, 25, 30]\n");
        }

        /**
         * Утилитный метод для выполнения теста путем перенаправления System.in и System.out.
         */
        private void runTest(int testNumber, String input, String expectedOutput) {
            InputStream originalIn = System.in;
            PrintStream originalOut = System.out;
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                System.setIn(inputStream);
                System.setOut(new PrintStream(outputStream));
                try {
                    mainClass.getMethod("main", String[].class).invoke(null, (Object) new String[]{});
                } catch (Exception e) {
                    throw new RuntimeException("Error during main execution: " + e.getMessage(), e);
                }
                String actual = outputStream.toString().replace("\r\n", "\n").trim();
                String expected = expectedOutput.replace("\r\n", "\n").trim();
                if (!actual.equals(expected)) {
                    fail("Test " + testNumber + " failed!\nExpected:\n" + expected + "\nActual:\n" + actual);
                }
            } catch (Exception e) {
                fail("Test " + testNumber + " error: " + e.getMessage());
            } finally {
                System.setIn(originalIn);
                System.setOut(originalOut);
            }
        }
    }
}