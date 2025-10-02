package by.it.group410901.papou.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_CountSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Read array size
        int n = scanner.nextInt();
        int[] points = new int[n];

        // Read input array
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Counting Sort implementation
        // Since numbers are from 0 to 10, use array of size 11
        int[] count = new int[11]; // count array for numbers 0 to 10

        // Step 1: Count occurrences of each number
        for (int i = 0; i < n; i++) {
            count[points[i]]++;
        }

        // Step 2: Reconstruct sorted array
        int index = 0;
        for (int i = 0; i < count.length; i++) {
            while (count[i] > 0) {
                points[index++] = i;
                count[i]--;
            }
        }

        return points;
    }
}