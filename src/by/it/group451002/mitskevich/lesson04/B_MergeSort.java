package by.it.group451002.mitskevich.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
ЗАДАЧА:
Реализовать сортировку слиянием (Merge Sort) для одномерного массива.
Вход:
- Первая строка: число 1 <= n <= 10000 — количество элементов
- Вторая строка: n натуральных чисел, не превышающих 10^9
Выход:
- Отсортированный массив

Пример:
Ввод:   5
        2 3 9 2 9
Вывод:  2 2 3 9 9
Сложность алгоритма должна быть не хуже O(n log n).
*/

public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        int[] result = instance.getMergeSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Чтение размера массива
        int n = scanner.nextInt();

        // Чтение самого массива
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Сортировка массива методом слияния
        mergeSort(a, 0, n - 1);

        return a;
    }

    // Метод для рекурсивной сортировки слиянием
    void mergeSort(int[] a, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            // Сортируем левую и правую половины
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);

            // Сливаем отсортированные части
            merge(a, left, mid, right);
        }
    }

    // Метод слияния двух отсортированных подмассивов
    void merge(int[] a, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        // Копируем данные во временные массивы
        for (int i = 0; i < n1; i++) {
            L[i] = a[left + i];
        }
        for (int i = 0; i < n2; i++) {
            R[i] = a[mid + 1 + i];
        }

        // Сливаем временные массивы обратно в a[left...right]
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                a[k++] = L[i++];
            } else {
                a[k++] = R[j++];
            }
        }

        // Копируем оставшиеся элементы, если есть
        while (i < n1) {
            a[k++] = L[i++];
        }
        while (j < n2) {
            a[k++] = R[j++];
        }
    }
}

