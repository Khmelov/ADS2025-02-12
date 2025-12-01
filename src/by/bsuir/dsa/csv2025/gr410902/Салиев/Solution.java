package by.bsuir.dsa.csv2025.gr410902.Салиев;

import java.util.Arrays;

public class Solution {

    // --- Двунаправленный пузырёк — Cocktail Shaker Sort ---
    static void cocktailBubbleSort(int[] a) {
        int n = a.length;
        boolean swapped = true;
        int left = 0;
        int right = n - 1;

        while (swapped) {
            swapped = false;

            // проход слева направо
            for (int i = left; i < right; i++) {
                if (a[i] > a[i + 1]) {
                    int tmp = a[i];
                    a[i] = a[i + 1];
                    a[i + 1] = tmp;
                    swapped = true;
                }
            }
            right--;

            if (!swapped) break;

            swapped = false;

            // проход справа налево
            for (int i = right; i > left; i--) {
                if (a[i - 1] > a[i]) {
                    int tmp = a[i];
                    a[i] = a[i - 1];
                    a[i - 1] = tmp;
                    swapped = true;
                }
            }
            left++;
        }
    }

    // --- вывод тестов ---
    static void printTest(int testNum, int[] input) {
        int[] actual = Arrays.copyOf(input, input.length);
        cocktailBubbleSort(actual);

        System.out.println("Test " + testNum + ":");
        System.out.println("Sample Input:");
        System.out.println(input.length);
        for (int i = 0; i < input.length; i++) {
            System.out.print(input[i] + (i == input.length - 1 ? "\n" : " "));
        }

        System.out.println("Sample Output:");
        for (int i = 0; i < actual.length; i++) {
            System.out.print(actual[i] + (i == actual.length - 1 ? "\n\n" : " "));
        }
    }

    public static void main(String[] args) {

        // 1. Один элемент
        printTest(1, new int[]{42});

        // 2. Два элемента уже по возрастанию
        printTest(2, new int[]{1, 2});

        // 3. Два элемента по убыванию
        printTest(3, new int[]{5, -3});

        // 4. Все элементы одинаковые
        printTest(4, new int[]{7, 7, 7, 7});

        // 5. Уже отсортированный по возрастанию массив
        printTest(5, new int[]{-5, -3, -1, 0, 2, 4});

        // 6. Полностью по убыванию (худший случай)
        printTest(6, new int[]{10, 8, 6, 4, 2, 0});

        // 7. Смешанные + дубликаты
        printTest(7, new int[]{3, -1, 3, 0, -1, 5});

        // 8. Много нулей и отрицательных
        printTest(8, new int[]{0, 0, 0, -1, 2, -3, 0});

        // 9. Очень большие по модулю числа
        printTest(9, new int[]{1000000, -1000000, 500000, -500000, 0});

        // 10. Почти отсортированный массив (одна инверсия)
        printTest(10, new int[]{1, 3, 2, 4, 5, 6});
    }
}
