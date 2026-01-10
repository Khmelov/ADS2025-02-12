package by.it.group451003.fedorcov.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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

        scanner.close();

        return countInversions(a);
    }

    private int countInversions(int[] array) {
        return mergeSortAndCount(array, 0, array.length - 1);
    }

    private int mergeSortAndCount(int[] array, int left, int right) {
        int count = 0;

        if (left < right) {
            int mid = left + (right - left) / 2;

            count += mergeSortAndCount(array, left, mid);
            count += mergeSortAndCount(array, mid + 1, right);

            count += mergeAndCount(array, left, mid, right);
        }

        return count;
    }

    private int mergeAndCount(int[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, mid + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;
        int swaps = 0;

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
                swaps += (mid + 1) - (left + i);
            }
        }

        while (i < n1) {
            array[k++] = leftArray[i++];
        }

        while (j < n2) {
            array[k++] = rightArray[j++];
        }

        return swaps;
    }
}