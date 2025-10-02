package by.it.group451003.kishkov.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].

    (Такая пара элементов называется инверсией массива.
    Количество инверсий в массиве является в некотором смысле
    его мерой неупорядоченности: например, в упорядоченном по неубыванию
    массиве инверсий нет вообще, а в массиве, упорядоченном по убыванию,
    инверсию образуют каждые (т.е. любые) два элемента.
    )

Sample Input:
5
2 3 9 2 9
Sample Output:
2
*/


public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int calc(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        int[] temp = new int[n];
        int inversions = mergeSortAndCount(a, temp, 0, a.length - 1);

        scanner.close();
        return inversions;
    }

    private int mergeSortAndCount(int[] a, int[] temp, int left, int right) {
        int inversions = 0;

        if (left < right) {
            int mid = left + (right - left) / 2;

            inversions += mergeSortAndCount(a, temp, left, mid);
            inversions += mergeSortAndCount(a, temp, mid + 1, right);

            inversions += mergeAndCount(a, temp, left, mid, right);
        }

        return inversions;
    }

    private int mergeAndCount(int[] a, int[] temp, int left, int mid, int right) {
        for (int i = left; i <= right; i++) {
            temp[i] = a[i];
        }

        int i = left;
        int j = mid + 1;
        int k = left;
        int inversions = 0;

        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                a[k++] = temp[i++];
            } else {
                a[k++] = temp[j++];

                inversions += (mid - i + 1);
            }
        }

        while (i <= mid) {
            a[k++] = temp[i++];
        }

        while (j <= right) {
            a[k++] = temp[j++];
        }

        return inversions;
    }
}
