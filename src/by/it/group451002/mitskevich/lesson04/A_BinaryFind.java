package by.it.group451002.mitskevich.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
ЗАДАЧА:
Дан отсортированный по возрастанию массив A из n натуральных чисел (все элементы разные).
Дан набор из k чисел. Для каждого нужно определить индекс в массиве A (индексация с 1), либо -1, если такого элемента нет.
Решать задачу нужно с использованием бинарного поиска (O(log n) на один запрос).
*/

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

        // Чтение размера массива и самого массива
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Чтение количества запросов и самих значений
        int k = scanner.nextInt();
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();
            // Для каждого значения ищем индекс с помощью бинарного поиска
            result[i] = binarySearch(a, value);
        }

        return result;
    }

    // Классический бинарный поиск в отсортированном массиве
    int binarySearch(int[] a, int value) {
        int left = 0, right = a.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (a[mid] == value) {
                return mid + 1; // смещение, т.к. в задаче индексация с 1
            } else if (a[mid] < value) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // если не найдено
    }
}

