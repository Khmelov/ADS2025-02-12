package by.it.group410902.jalilova.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием
        mergeSort(a, 0, a.length - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
    void mergeSort(int[] arr, int left, int right) {
        // базовое условие для рекурсии: если массив из одного элемента, то он уже отсортирован
        if (left < right) {
            int mid = (left + right) / 2;
            // рекурсивный вызов сортировки для левой части массива
            mergeSort(arr, left, mid);
            // рекурсивный вызов сортировки для правой части массива
            mergeSort(arr, mid + 1, right);
            // слияние отсортированных частей массива
            merge(arr, left, mid, right);
        }
    }

    void merge(int[] arr, int left, int mid, int right) {
        // вычисление размеров двух подмассивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // временные массивы для хранения левой и правой частей
        int[] L = new int[n1];
        int[] R = new int[n2];
        // копирование данных в временные массивы
        for (int i = 0; i < n1; i++)
            L[i] = arr[left + i];
        for (int j = 0; j < n2; j++)
            R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;
        // слияние двух массивов, пока есть элементы в обоих
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }
        // если в левой части остались элементы
        while (i < n1) {
            arr[k++] = L[i++];
        }
        // если в правой части остались элементы
        while (j < n2) {
            arr[k++] = R[j++];
        }
    }
}
