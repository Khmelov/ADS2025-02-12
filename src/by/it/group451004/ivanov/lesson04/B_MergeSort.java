package by.it.group451004.ivanov.lesson04;

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
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }
        mergeSort(a, 0, n - 1);
        return a;
    }

    void mergeSort(int[] a, int minA, int maxA) {
        if (maxA - minA < 2) {
            if (a[maxA] < a[minA]) {
                int temp = a[minA];
                a[minA] = a[maxA];
                a[maxA] = temp;
            }
        } else {
            int mid = (maxA + minA) / 2;
            mergeSort(a, minA, mid);
            mergeSort(a, mid + 1, maxA);
            mergeSubarrays(a, minA, mid, maxA);
        }

    }

    void mergeSubarrays(int[] source, int minA, int maxA, int maxB) {
        int ai = minA;
        int bi = maxA + 1;
        int length = maxB - minA + 1;
        int[] buf = new int[length];
        for (int i = 0; i < length; i++) {
            if ((bi > maxB) || (source[ai] < source[bi])) {
                buf[i] = source[ai];
                ai++;
            } else {
                buf[i] = source[bi];
                bi++;
            }
        }
        System.arraycopy(buf, 0, source, minA, length);
//        for (int i = 0; i < length; i++) {
//            source[minA + i] = buf[i];
//        }
    }
}
