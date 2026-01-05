package by.it.group451004.matyrka.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_BinaryFind {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt");
        A_BinaryFind instance = new A_BinaryFind();
        int[] result = instance.findIndex(stream);
        for (int x : result) {
            System.out.print(x + " ");
        }
    }

    int[] findIndex(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        int k = scanner.nextInt();
        int[] result = new int[k];

        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();
            result[i] = binarySearch(a, value);
        }

        return result;
    }

    private int binarySearch(int[] a, int value) {
        int l = 0, r = a.length - 1;
        while (l <= r) {
            int m = (l + r) >>> 1;
            if (a[m] == value) return m + 1; // индекс с 1
            if (a[m] < value) l = m + 1;
            else r = m - 1;
        }
        return -1;
    }
}
