package by.it.group451004.kahnovich.lesson01.lesson04;

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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
            System.out.println(arr[i]);
        }

        mergeSort(arr, 0, arr.length - 1);
        int i = 0;
        return arr;
    }

    // Метод для слияния двух отсортированных подмассивов в один отсортированный
    void merge(int[] arr, int left, int mid, int right) {
        // Копируем левую половину массива: от left до mid (включительно)
        int[] arrFirst = Arrays.copyOfRange(arr, left, mid + 1);
        // Копируем правую половину массива: от mid+1 до right (включительно)
        int[] arrSecond = Arrays.copyOfRange(arr, mid + 1, right + 1);

        // Индексы для прохода по левому (i), правому (j) массивам и общему массиву (k)
        int i = 0, j = 0;
        int k = left;

        // Пока есть элементы в обеих частях
        while (i < arrFirst.length && j < arrSecond.length) {
            // Если текущий элемент из левой части меньше или равен правому — берём его
            if (arrFirst[i] <= arrSecond[j]) {
                arr[k] = arrFirst[i];
                i++;
            } else {
                // Иначе берём элемент из правой части
                arr[k] = arrSecond[j];
                j++;
            }
            k++; // Перемещаем указатель в результирующем массиве
        }

        // Копируем оставшиеся элементы из левой части (если они есть)
        while (i < arrFirst.length) {
            arr[k] = arrFirst[i];
            i++;
            k++;
        }

        // Копируем оставшиеся элементы из правой части (если они есть)
        while (j < arrSecond.length) {
            arr[k] = arrSecond[j];
            j++;
            k++;
        }
    }

    // Рекурсивная сортировка слиянием
    void mergeSort(int[] arr, int left, int right) {
        // Базовый случай: массив из одного элемента уже отсортирован
        if (left >= right)
            return;

        // Находим середину массива
        int mid = left + (right - left) / 2;

        // Рекурсивно сортируем левую половину
        mergeSort(arr, left, mid);

        // Рекурсивно сортируем правую половину
        mergeSort(arr, mid + 1, right);

        // Сливаем две отсортированные половины
        merge(arr, left, mid, right);
    }

}
