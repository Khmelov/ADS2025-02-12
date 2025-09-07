package by.it.group410902.sinyutin.lesson04;

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

    static void merge(int[] arr, int[] firstHalf, int[] secondHalf) {
        int i = 0, j = 0, k = 0;

        while (i < firstHalf.length && j < secondHalf.length) {
            if (firstHalf[i] <= secondHalf[j]) {
                arr[k++] = firstHalf[i++];
            } else {
                arr[k++] = secondHalf[j++];
            }
        }

        while (i < firstHalf.length) {
            arr[k++] = firstHalf[i++];
        }

        while (j < secondHalf.length) {
            arr[k++] = secondHalf[j++];
        }
    }

    static void mergeSort(int[] arr) {
        if (arr.length <= 1) return;

        int mid = arr.length / 2;
        int[] firstHalf = new int[mid];
        int[] secondHalf = new int[arr.length - mid];

        System.arraycopy(arr, 0, firstHalf, 0, mid);
        System.arraycopy(arr, mid, secondHalf, 0, arr.length - mid);

        mergeSort(firstHalf);
        mergeSort(secondHalf);

        merge(arr, firstHalf, secondHalf);
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

        mergeSort(a);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}