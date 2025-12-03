package by.it.group451003.yepanchuntsau.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превышающие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<=n, для которых A[i]>A[j].
*/
public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        int result = instance.calc(stream);
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        long invCount = mergeSortCount(a, 0, n - 1);
        return (int) invCount;
    }

    private long mergeSortCount(int[] a, int left, int right) {
        if (left >= right) {
            return 0;
        }
        int mid = left + (right - left) / 2;
        long count = 0;
        count += mergeSortCount(a, left, mid);
        count += mergeSortCount(a, mid + 1, right);
        count += mergeAndCount(a, left, mid, right);
        return count;
    }

    private long mergeAndCount(int[] a, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(a, left, L, 0, n1);
        System.arraycopy(a, mid + 1, R, 0, n2);

        long inv = 0;
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                a[k++] = L[i++];
            } else {
                a[k++] = R[j++];
                inv += (n1 - i);
            }
        }
        while (i < n1) {
            a[k++] = L[i++];
        }
        while (j < n2) {
            a[k++] = R[j++];
        }
        return inv;
    }
}
