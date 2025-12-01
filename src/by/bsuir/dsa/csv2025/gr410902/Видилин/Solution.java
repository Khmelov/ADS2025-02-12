package by.bsuir.dsa.csv2025.gr410902.Видилин;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class Solution {

    // --- сортировка и бинарный поиск ---
    static int binarySearchDesc(int[] a, int left, int right, int x) {
        while (left <= right) {
            int mid = (left + right) / 2;
            if (a[mid] >= x)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return left;
    }

    static void insertionSortBinary(int[] a) {
        int n = a.length;
        for (int i = 1; i < n; i++) {
            int x = a[i];
            int pos = binarySearchDesc(a, 0, i - 1, x);

            for (int j = i; j > pos; j--)
                a[j] = a[j - 1];

            a[pos] = x;
        }
    }

    // --- проверка результата ---
    static void printTest(int testNum, int[] input) {
        int[] actual = Arrays.copyOf(input, input.length);
        insertionSortBinary(actual);

        System.out.println("Test " + testNum + ":");
        System.out.println("Sample Input:");
        System.out.println(input.length);
        for (int i = 0; i < input.length; i++) {
            System.out.print(input[i] + (i == input.length - 1 ? "\n" : " "));
        }

        System.out.println("Sample Output:");
        for (int i = 0; i < actual.length; i++) {
            System.out.print(actual[i] + (i == actual.length - 1 ? "\n\n" : " "));
        }
    }

    public static void main(String[] args) {

        printTest(1, new int[]{1, 2, 3, 4});
        printTest(2, new int[]{4, 3, 2, 1});
        printTest(3, new int[]{5, 12, -3, 7, 7, 0, 25, 19, -10, 4});
        printTest(4, new int[]{5, 5, 5});
        printTest(5, new int[]{10, -1, 0, -1, 2});
        printTest(6, new int[]{100});
        printTest(7, new int[]{0, -1, -5, 3, 2});
        printTest(8, new int[]{8, 5, 8, 1, 0});
        printTest(9, new int[]{2, 9, 1, 6, 4, 8, 3});
        printTest(10, new int[]{7, 2, 7, 9, 0, 3});
    }

    @Test
    public void test1() {
        int[] input = {1, 2, 3, 4};
        int[] expected = {4, 3, 2, 1};
        Solution.insertionSortBinary(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void test2() {
        int[] input = {4, 3, 2, 1};
        int[] expected = {4, 3, 2, 1};
        Solution.insertionSortBinary(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void test3() {
        int[] input = {5, 12, -3, 7, 7, 0, 25, 19, -10, 4};
        int[] expected = {25, 19, 12, 7, 7, 5, 4, 0, -3, -10};
        Solution.insertionSortBinary(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void test4() {
        int[] input = {5, 5, 5};
        int[] expected = {5, 5, 5};
        Solution.insertionSortBinary(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void test5() {
        int[] input = {10, -1, 0, -1, 2};
        int[] expected = {10, 2, 0, -1, -1};
        Solution.insertionSortBinary(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void test6() {
        int[] input = {100};
        int[] expected = {100};
        Solution.insertionSortBinary(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void test7() {
        int[] input = {0, -1, -5, 3, 2};
        int[] expected = {3, 2, 0, -1, -5};
        Solution.insertionSortBinary(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void test8() {
        int[] input = {8, 5, 8, 1, 0};
        int[] expected = {8, 8, 5, 1, 0};
        Solution.insertionSortBinary(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void test9() {
        int[] input = {2, 9, 1, 6, 4, 8, 3};
        int[] expected = {9, 8, 6, 4, 3, 2, 1};
        Solution.insertionSortBinary(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void test10() {
        int[] input = {7, 2, 7, 9, 0, 3};
        int[] expected = {9, 7, 7, 3, 2, 0};
        Solution.insertionSortBinary(input);
        assertArrayEquals(expected, input);
    }
}