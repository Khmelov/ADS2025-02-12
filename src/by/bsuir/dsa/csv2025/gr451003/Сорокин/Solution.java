package by.bsuir.dsa.csv2025.gr451003.Сорокин;

import java.io.InputStream;
import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    public static class SortResult {
        int[] sortedArray;
        int comparisons;
        int swaps;

        public SortResult(int[] sortedArray, int comparisons, int swaps) {
            this.sortedArray = sortedArray;
            this.comparisons = comparisons;
            this.swaps = swaps;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = scanner.nextInt();
        }

        Solution instance = new Solution();
        SortResult result = instance.insertionSort(array);

        for (int i = 0; i < result.sortedArray.length; i++) {
            System.out.print(result.sortedArray[i]);
            if (i < result.sortedArray.length - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
        System.out.println("Comparisons: " + result.comparisons + ", Swaps: " + result.swaps);

        scanner.close();
    }

    SortResult insertionSort(int[] arr) {
        int[] array = arr.clone();
        int comparisons = 0;
        int swaps = 0;

        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0) {
                comparisons++;
                if (array[j] > key) {
                    array[j + 1] = array[j];
                    swaps++;
                    j--;
                } else {
                    break;
                }
            }

            array[j + 1] = key;
        }

        return new SortResult(array, comparisons, swaps);
    }

    @Test
    public void checkA() throws Exception {
        testCase(1, new int[]{1, 2, 3, 4, 5}, 4, 0);
        testCase(2, new int[]{1, 2, 3, 4, 5}, 10, 10);
        testCase(3, new int[]{1, 2, 3, 4, 5, 6}, 12, 9);
        testCase(4, new int[]{42}, 0, 0);
        testCase(5, new int[]{1, 2}, 1, 1);
        testCase(6, new int[]{2, 2, 2, 2, 2}, 4, 0);
        testCase(7, new int[]{-5, -4, -3, -2, -1}, 9, 6);
        testCase(8, new int[]{1, 2, 3, 4, 5, 6}, 7, 2);
        testCase(9, new int[]{25, 50, 75, 100, 125, 150, 175}, 8, 4);
        testCase(10, new int[]{1, 2}, 1, 0);
    }

    private void testCase(int testNumber, int[] expectedArray, int expectedComparisons, int expectedSwaps) throws Exception {
        String testData = getTestData(testNumber);
        InputStream inputStream = new java.io.ByteArrayInputStream(testData.getBytes());
        Scanner scanner = new Scanner(inputStream);

        int n = scanner.nextInt();
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = scanner.nextInt();
        }
        scanner.close();

        Solution instance = new Solution();
        SortResult result = instance.insertionSort(array);

        assertArrayEquals("Test " + testNumber + " failed", expectedArray, result.sortedArray);
        assertEquals("Test " + testNumber + " comparisons failed", expectedComparisons, result.comparisons);
        assertEquals("Test " + testNumber + " swaps failed", expectedSwaps, result.swaps);
    }

    private String getTestData(int testNumber) {
        switch(testNumber) {
            case 1: return "5\n1 2 3 4 5";
            case 2: return "5\n5 4 3 2 1";
            case 3: return "6\n5 2 4 6 1 3";
            case 4: return "1\n42";
            case 5: return "2\n2 1";
            case 6: return "5\n2 2 2 2 2";
            case 7: return "5\n-3 -1 -5 -2 -4";
            case 8: return "6\n1 3 2 4 6 5";
            case 9: return "7\n100 50 25 75 125 150 175";
            case 10: return "2\n1 2";
            default: return "";
        }
    }
}