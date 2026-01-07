package by.it.group451004.matyrka.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        int[] result = instance.getMergeSort(stream);
        for (int x : result) {
            System.out.print(x + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        mergeSort(a, 0, n - 1, new int[n]);
        return a;
    }

    private void mergeSort(int[] a, int l, int r, int[] temp) {
        if (l >= r) return;
        int m = (l + r) >>> 1;
        mergeSort(a, l, m, temp);
        mergeSort(a, m + 1, r, temp);
        merge(a, l, m, r, temp);
    }

    private void merge(int[] a, int l, int m, int r, int[] temp) {
        int i = l, j = m + 1, k = l;
        while (i <= m && j <= r) {
            if (a[i] <= a[j]) temp[k++] = a[i++];
            else temp[k++] = a[j++];
        }
        while (i <= m) temp[k++] = a[i++];
        while (j <= r) temp[k++] = a[j++];
        for (int x = l; x <= r; x++) a[x] = temp[x];
    }
}
