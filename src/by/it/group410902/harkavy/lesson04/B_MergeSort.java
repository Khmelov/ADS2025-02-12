package by.it.group410902.harkavy.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

/*
Реализуйте сортировку слиянием для одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо отсортировать полученный массив.

Sample Input:
5
2 3 9 2 9
Sample Output:
2 2 3 9 9
*/
public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        // Подготовка к чтению данных
        Scanner scanner = new Scanner(stream);

        // Считываем размер массива
        int n = scanner.nextInt();

        // Считываем сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            // System.out.println(a[i]); // Можно раскомментировать для отладки
        }

        // Вызываем сортировку слиянием
        sort(a);

        return a;
    }

    // Рекурсивная сортировка слиянием
    public static void sort(int[] array) {
        // Базовый случай: если длина массива <= 1, он уже отсортирован
        if (array.length <= 1) {
            return;
        }

        // Делим массив пополам
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);

        // Рекурсивно сортируем каждую половину
        sort(left);
        sort(right);

        // Сливаем отсортированные половины обратно в оригинальный массив
        merge(array, left, right);
    }

    // Метод для слияния двух отсортированных массивов
    private static void merge(int[] array, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;

        // Пока есть элементы в обеих половинах
        while (i < left.length && j < right.length) {
            if (left[i] < right[j]) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }

        // Копируем оставшиеся элементы (если остались) из левой половины
        while (i < left.length) {
            array[k++] = left[i++];
        }

        // Копируем оставшиеся элементы из правой половины
        while (j < right.length) {
            array[k++] = right[j++];
        }
    }


}
