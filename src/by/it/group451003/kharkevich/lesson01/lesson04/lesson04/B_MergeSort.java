package by.it.group451003.kharkevich.lesson01.lesson04.lesson04;

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

    void merge(int[] a, int l1, int r1, int l2, int r2) {
        int i1 = l1, i2 = l2;
        int[] merged = new int[r2 - l1 + 1];
        int k = 0;

        while (i1 <= r1 && i2 <= r2) {
            if (a[i1] <= a[i2])
                merged[k++] = a[i1++];
            else
                merged[k++] = a[i2++];
        }

        while (i1 <= r1)
            merged[k++] = a[i1++];

        while (i2 <= r2)
            merged[k++] = a[i2++];

        for (int i = l1, j = 0; i <= r2; i++, j++)
            a[i] = merged[j];
    }
    void mergeSort(int[] a, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;

            mergeSort(a, l, m);
            mergeSort(a, m + 1, r);

            merge(a, l, m, m + 1, r);
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a=new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }


        mergeSort(a, 0, n - 1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

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



}
