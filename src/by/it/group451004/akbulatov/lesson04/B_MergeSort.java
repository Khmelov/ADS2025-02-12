package by.it.group451004.akbulatov.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        int[] result = instance.getMergeSort(stream);
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
        }

        int[] temp = new int[n];
        mergeSort(a, 0, n - 1, temp);
        return a;
    }

    void mergeSort(int[] array, int left, int right, int[] temp) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(array, left, mid, temp);
            mergeSort(array, mid + 1, right, temp);
            merge(array, left, mid, right, temp);
        }
    }

    void merge(int[] array, int left, int mid, int right, int[] temp) {
        if (right + 1 - left >= 0)
            System.arraycopy(array, left, temp, left, right + 1 - left);

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j])
                array[k++] = temp[i++];
            else
                array[k++] = temp[j++];
        }

        while (i <= mid)
            array[k++] = temp[i++];

        while (j <= right)
            array[k++] = temp[j++];
    }
}