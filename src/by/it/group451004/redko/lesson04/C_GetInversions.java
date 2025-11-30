package by.it.group451004.redko.lesson04;

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

Головоломка (т.е. не обязательно).
Попробуйте обеспечить скорость лучше, чем O(n log n) за счет многопоточности.
Докажите рост производительности замерами времени.
Большой тестовый массив можно прочитать свой или сгенерировать его программно.
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

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        // Read array size
        int n = scanner.nextInt();
        // Read array elements
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Count inversions using modified merge sort
        return countInversions(a, 0, a.length - 1);
    }

    private int countInversions(int[] arr, int left, int right) {
        int invCount = 0;

        if (left < right) {
            int mid = left + (right - left) / 2;

            // Count inversions in left subarray
            invCount += countInversions(arr, left, mid);

            // Count inversions in right subarray
            invCount += countInversions(arr, mid + 1, right);

            // Count inversions during merge
            invCount += mergeAndCount(arr, left, mid, right);
        }

        return invCount;
    }

    private int mergeAndCount(int[] arr, int left, int mid, int right) {
        // Create temporary arrays
        int[] leftArray = new int[mid - left + 1];
        int[] rightArray = new int[right - mid];

        // Copy data to temporary arrays
        for (int i = 0; i < leftArray.length; i++) {
            leftArray[i] = arr[left + i];
        }
        for (int j = 0; j < rightArray.length; j++) {
            rightArray[j] = arr[mid + 1 + j];
        }

        int i = 0, j = 0, k = left;
        int invCount = 0;

        // Merge the temporary arrays back into arr[left..right]
        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] <= rightArray[j]) {
                arr[k++] = leftArray[i++];
            } else {
                arr[k++] = rightArray[j++];
                // All remaining elements in leftArray will be greater than rightArray[j]
                invCount += (mid + 1) - (left + i);
            }
        }

        // Copy remaining elements of leftArray if any
        while (i < leftArray.length) {
            arr[k++] = leftArray[i++];
        }

        // Copy remaining elements of rightArray if any
        while (j < rightArray.length) {
            arr[k++] = rightArray[j++];
        }

        return invCount;
    }
}