package by.it.group451004.matyrka.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_CountSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int x : result) {
            System.out.print(x + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] count = new int[11]; // числа от 1 до 10

        for (int i = 0; i < n; i++) {
            count[scanner.nextInt()]++;
        }

        int[] result = new int[n];
        int idx = 0;
        for (int value = 1; value <= 10; value++) {
            while (count[value]-- > 0) {
                result[idx++] = value;
            }
        }

        return result;
    }
}
