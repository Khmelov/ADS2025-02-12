package by.it.group451004.volkov.lesson04;

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
        // Подготовка к чтению данных
        Scanner scanner = new Scanner(stream);

        // Читаем размер отсортированного массива и сам массив A[1...n]
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 1; i <= n; i++) {
            a[i - 1] = scanner.nextInt();
        }

        // Читаем число запросов и сами искомые элементы
        int k = scanner.nextInt();
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();
            // Реализуем бинарный поиск индекса для value
            result[i] = binarySearch(a, value);
        }

        return result;
    }

    /**
     * Выполняет бинарный поиск элемента value в отсортированном массиве a.
     * Возвращает 1-индексированное положение элемента или -1, если элемент не найден.
     */
    private int binarySearch(int[] a, int value) {
        int left = 0;
        int right = a.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) / 2);
            if (a[mid] == value) {
                return mid + 1; // возвращаем с 1-индексацией
            } else if (a[mid] < value) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // если элемент не найден
    }
}
