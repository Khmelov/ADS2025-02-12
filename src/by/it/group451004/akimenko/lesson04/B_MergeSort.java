package lesson04;

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

        mergeSort(a);


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    public static void mergeSort(int[] array) {
        if (array.length < 2) {
            return;
        }
        int[] helper = new int[array.length];
        mergeSort(array, 0, array.length - 1, helper);
    }

    private static void mergeSort(int[] array, int left, int right, int[] helper) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(array, left, mid, helper);
            mergeSort(array, mid + 1, right, helper);
            merge(array, left, mid, right, helper);
        }
    }

    private static void merge(int[] array, int left, int mid, int right, int[] helper) {
        // Копируем обе части во вспомогательный массив
        for (int i = left; i <= right; i++) {
            helper[i] = array[i];
        }

        int i = left;
        int j = mid + 1;
        int k = left;

        // Сливаем части обратно в исходный массив
        while (i <= mid && j <= right) {
            if (helper[i] <= helper[j]) {
                array[k] = helper[i];
                i++;
            } else {
                array[k] = helper[j];
                j++;
            }
            k++;
        }

        // Копируем остатки левой части
        while (i <= mid) {
            array[k] = helper[i];
            k++;
            i++;
        }

        // Копируем остатки правой части (если есть)
        while (j <= right) {
            array[k] = helper[j];
            k++;
            j++;
        }
    }
}
