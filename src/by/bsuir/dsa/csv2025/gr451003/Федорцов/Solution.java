package by.bsuir.dsa.csv2025.gr451003.Федорцов;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            if (scanner.hasNextInt()) {
                int n = scanner.nextInt();
                int[] arr = new int[n];

                for (int i = 0; i < n; i++) {
                    arr[i] = scanner.nextInt();
                }

                selectionSort(arr);

                for (int i = 0; i < n; i++) {
                    System.out.print(arr[i]);
                    if (i < n - 1) {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
        }
    }

    public static void selectionSort(int[] arr) {
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            int min_idx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[min_idx]) {
                    min_idx = j;
                }
            }

            int temp = arr[min_idx];
            arr[min_idx] = arr[i];
            arr[i] = temp;
        }
    }

    public static String sortArrayForTest(String input) {
        InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        try (Scanner scanner = new Scanner(stream)) {
            if (!scanner.hasNextInt()) return "";
            int n = scanner.nextInt();
            int[] arr = new int[n];

            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }

            selectionSort(arr);

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < n; i++) {
                result.append(arr[i]);
                if (i < n - 1) {
                    result.append(" ");
                }
            }
            return result.toString();
        }
    }

    @Test
    public void testAll() {
        assertEquals("Test 1 failed", "1 2 3 4 5", sortArrayForTest("5\n3 1 4 2 5"));

        assertEquals("Test 2 failed", "5 6 7 11 12 13", sortArrayForTest("6\n12 11 13 5 6 7"));

        assertEquals("Test 3 failed", "1 11 12 22 25 34 64 90", sortArrayForTest("8\n64 34 25 12 22 11 90 1"));

        assertEquals("Test 4 failed", "1 2 3 4 5 6 7 8 9 10", sortArrayForTest("10\n5 2 8 1 9 3 7 4 6 10"));

        assertEquals("Test 5 failed", "25 50 75 100 125 150 175", sortArrayForTest("7\n100 50 25 75 150 125 175"));

        assertEquals("Test 6 failed", "6 7 8 9", sortArrayForTest("4\n9 8 7 6"));

        assertEquals("Test 7 failed", "1 2 3 4 5 6 7 8 9", sortArrayForTest("9\n1 3 5 7 9 2 4 6 8"));

        assertEquals("Test 8 failed", "5 10 15", sortArrayForTest("3\n15 5 10"));

        assertEquals("Test 9 failed", "1 12 23 33 34 44 45 56 67 78 89 90", sortArrayForTest("12\n23 1 45 67 89 34 56 78 90 12 33 44"));

        assertEquals("Test 10 failed", "11 99", sortArrayForTest("2\n99 11"));
    }
}