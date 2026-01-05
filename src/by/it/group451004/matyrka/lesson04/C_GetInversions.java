package by.it.group451004.matyrka.lesson04;

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

        return (int) mergeSortCount(a, 0, n - 1, new int[n]);
    }

    private long mergeSortCount(int[] a, int l, int r, int[] temp) {
        if (l >= r) return 0;
        int m = (l + r) >>> 1;
        long inv = 0;
        inv += mergeSortCount(a, l, m, temp);
        inv += mergeSortCount(a, m + 1, r, temp);
        inv += mergeCount(a, l, m, r, temp);
        return inv;
    }

    private long mergeCount(int[] a, int l, int m, int r, int[] temp) {
        int i = l, j = m + 1, k = l;
        long inv = 0;

        while (i <= m && j <= r) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
                inv += (m - i + 1);
            }
        }
        while (i <= m) temp[k++] = a[i++];
        while (j <= r) temp[k++] = a[j++];
        for (int x = l; x <= r; x++) a[x] = temp[x];

        return inv;
    }
}
