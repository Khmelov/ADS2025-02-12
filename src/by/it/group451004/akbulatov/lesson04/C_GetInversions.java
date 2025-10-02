package by.it.group451004.akbulatov.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        int result = instance.calc(stream);
        System.out.print(result);
    }

    int calc(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++)
            a[i] = scanner.nextInt();

        return mergeSortAndCount(a, 0, n - 1);
    }

    private int mergeSortAndCount(int[] a, int left, int right) {
        if (left >= right)
            return 0;

        int mid = (left + right) / 2;
        int invCount = 0;
        invCount += mergeSortAndCount(a, left, mid);
        invCount += mergeSortAndCount(a, mid + 1, right);
        invCount += mergeAndCount(a, left, mid, right);
        return invCount;
    }

    private int mergeAndCount(int[] a, int left, int mid, int right) {
        int[] leftArr = Arrays.copyOfRange(a, left, mid + 1);
        int[] rightArr = Arrays.copyOfRange(a, mid + 1, right + 1);
        int i = 0, j = 0, k = left;
        int invCount = 0;

        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i] <= rightArr[j])
                a[k++] = leftArr[i++];
            else {
                a[k++] = rightArr[j++];
                invCount += (mid + 1 - left - i);
            }
        }

        while (i < leftArr.length)
            a[k++] = leftArr[i++];

        while (j < rightArr.length)
            a[k++] = rightArr[j++];

        return invCount;
    }
}