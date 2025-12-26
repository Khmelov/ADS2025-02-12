package by.bsuir.dsa.csv2025.gr451003.Сироткин;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {
    public void insertionSort(int[] arr) {
        if (arr == null) {
            return;
        }

        int n = arr.length;
        if (n < 2) return;

        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (scanner.hasNextInt()) {
            int n = scanner.nextInt();
            int[] arr = new int[n];

            for (int i = 0; i < n; i++) {
                if (scanner.hasNextInt()) {
                    arr[i] = scanner.nextInt();
                }
            }

            Solution instance = new Solution();
            instance.insertionSort(arr);

            for (int i = 0; i < n; i++) {
                System.out.print(arr[i]);
                if (i < n - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        scanner.close();
    }

    String processStreamForTest(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        if (!scanner.hasNextInt()) {
            return "";
        }

        int n = scanner.nextInt();
        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            if (scanner.hasNextInt()) {
                arr[i] = scanner.nextInt();
            }
        }

        insertionSort(arr);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(arr[i]);
            if (i < n - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    @Test
    public void testExampleCase() {
        String input = "7\n101 102 104 103 105 107 106";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        String result = instance.processStreamForTest(stream);
        assertEquals("101 102 103 104 105 106 107", result);
    }

    @Test
    public void testRandomOrder() {
        String input = "5\n5 1 4 2 8";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        String result = instance.processStreamForTest(stream);
        assertEquals("1 2 4 5 8", result);
    }

    @Test
    public void testNearlySortedJitter() {
        String input = "7\n10 20 30 25 40 50 45";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        String result = instance.processStreamForTest(stream);
        assertEquals("10 20 25 30 40 45 50", result);
    }

    @Test
    public void testReverseOrder() {
        String input = "5\n9 8 7 6 5";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        String result = instance.processStreamForTest(stream);
        assertEquals("5 6 7 8 9", result);
    }

    @Test
    public void testAlreadySorted() {
        String input = "5\n1 2 3 4 5";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        String result = instance.processStreamForTest(stream);
        assertEquals("1 2 3 4 5", result);
    }

    @Test
    public void testDuplicates() {
        String input = "5\n300 100 300 200 100";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        String result = instance.processStreamForTest(stream);
        assertEquals("100 100 200 300 300", result);
    }

    @Test
    public void testSingleElement() {
        String input = "1\n42";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        String result = instance.processStreamForTest(stream);
        assertEquals("42", result);
    }

    @Test
    public void testLongArray() {
        String input = "100\n" +
                "1 2 3 5 4 6 7 8 9 10 12 11 13 14 15 16 17 19 18 20 21 22 23 24 25 26 27 28 30 29 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 50 49 51 52 53 54 55 56 57 58 59 60 61 62 63 64 65 66 67 68 69 70 71 72 75 74 73 76 77 78 79 80 81 82 83 84 85 86 87 88 89 90 91 92 93 94 95 96 97 99 98 100";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        String result = instance.processStreamForTest(stream);
        assertEquals("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61 62 63 64 65 66 67 68 69 70 71 72 73 74 75 76 77 78 79 80 81 82 83 84 85 86 87 88 89 90 91 92 93 94 95 96 97 98 99 100", result);
    }

    @Test
    public void testLargeGapValues() {
        String input = "5\n5000 12 999999 0 5001";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        String result = instance.processStreamForTest(stream);
        assertEquals("0 12 5000 5001 999999", result);
    }

    @Test
    public void testNegativeNumbers() {
        String input = "5\n0 -5 10 -2 3";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        String result = instance.processStreamForTest(stream);
        assertEquals("-5 -2 0 3 10", result);
    }
}