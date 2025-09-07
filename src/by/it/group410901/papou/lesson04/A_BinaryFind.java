package by.it.group410901.papou.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_BinaryFind {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt");
        A_BinaryFind instance = new A_BinaryFind();
        int[] result = instance.findIndex(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] findIndex(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Чтение размера отсортированного массива
        int n = scanner.nextInt();
        // Чтение отсортированного массива
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Чтение количества чисел для поиска
        int k = scanner.nextInt();
        int[] result = new int[k];
        // Чтение чисел для поиска и выполнение бинарного поиска
        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();
            result[i] = binarySearch(a, value);
        }

        return result;
    }

    // Реализация бинарного поиска
    private int binarySearch(int[] a, int key) {
        int left = 0;
        int right = a.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // Избегаем переполнения
            if (a[mid] == key) {
                return mid + 1; // Возвращаем индекс с учетом смещения (1-based)
            } else if (a[mid] < key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // Элемент не найден
    }
}