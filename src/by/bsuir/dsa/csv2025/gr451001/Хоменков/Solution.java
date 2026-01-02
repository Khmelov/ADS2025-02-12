package by.bsuir.dsa.csv2025.gr451001.Хоменков;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class Solution {

    public static int maxGap(int[] heights, int k) {
        int n = heights.length;
        if (n == 0) return 0;

        // Создаем массив пар (значение, исходный индекс)
        int[][] arr = new int[n][2];
        for (int i = 0; i < n; i++) {
            arr[i][0] = heights[i];
            arr[i][1] = i;
        }

        // Сортируем массив пар по значению
        mergeSort(arr, 0, n - 1);

        int maxDiff = 0;

        // Для каждого элемента ищем подходящие пары
        for (int i = 0; i < n; i++) {
            int currentHeight = arr[i][0];
            int currentIndex = arr[i][1];

            // Ищем справа элементы, которые находятся на достаточном расстоянии
            for (int j = n - 1; j > i; j--) {
                int otherHeight = arr[j][0];
                int otherIndex = arr[j][1];

                // Проверяем условие расстояния
                if (Math.abs(currentIndex - otherIndex) >= k) {
                    int diff = otherHeight - currentHeight;
                    if (diff > maxDiff) {
                        maxDiff = diff;
                    }
                    // Прерываем, так как в отсортированном массиве
                    // это максимальная разница для текущего currentHeight
                    break;
                }
            }
        }

        return maxDiff;
    }

    private static void mergeSort(int[][] arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    private static void merge(int[][] arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int[][] L = new int[n1][2];
        int[][] R = new int[n2][2];

        for (int i = 0; i < n1; i++) {
            L[i][0] = arr[l + i][0];
            L[i][1] = arr[l + i][1];
        }
        for (int i = 0; i < n2; i++) {
            R[i][0] = arr[m + 1 + i][0];
            R[i][1] = arr[m + 1 + i][1];
        }

        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (L[i][0] <= R[j][0]) {
                arr[k][0] = L[i][0];
                arr[k][1] = L[i][1];
                i++;
            } else {
                arr[k][0] = R[j][0];
                arr[k][1] = R[j][1];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k][0] = L[i][0];
            arr[k][1] = L[i][1];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k][0] = R[j][0];
            arr[k][1] = R[j][1];
            j++;
            k++;
        }
    }


    @Test
    public void testMaxK() {
        int[] heights = {10, 20, 5, 15, 25};
        int k = 4;
        int expected = 15;
        int actual = Solution.maxGap(heights, k);
        assertEquals(expected, actual);
    }

    @Test
    public void testMinNonZeroK() {
        int[] heights = {8, 3, 12, 6, 9, 15};
        int k = 1;
        int expected = 12;
        int actual = Solution.maxGap(heights, k);
        assertEquals(expected, actual);
    }

    @Test
    public void testReverseOrderWithLargeK() {
        int[] heights = {100, 90, 80, 70, 60, 50, 40, 30, 20, 10};
        int k = 8;
        int expected = 90;
        int actual = Solution.maxGap(heights, k);
        assertEquals(expected, actual);
    }

    @Test
    public void testSawtoothPattern() {
        int[] heights = {1, 100, 2, 99, 3, 98, 4, 97, 5, 96};
        int k = 2;
        int expected = 98;
        int actual = Solution.maxGap(heights, k);
        assertEquals(expected, actual);
    }

    @Test
    public void testTwoLocalMaxima() {
        int[] heights = {10, 50, 20, 60, 30, 70, 40, 80, 35, 45};
        int k = 3;
        int expected = 70;
        int actual = Solution.maxGap(heights, k);
        assertEquals(expected, actual);
    }

    @Test
    public void testPlateaus() {
        int[] heights = {5, 5, 10, 10, 1, 1, 8, 8, 12, 12};
        int k = 4;
        int expected = 11;
        int actual = Solution.maxGap(heights, k);
        assertEquals(expected, actual);
    }

    @Test
    public void testVeryLargeNumbers() {
        int[] heights = {Integer.MAX_VALUE - 1000, 1000, Integer.MAX_VALUE, 5000, 1};
        int k = 2;
        int expected = Integer.MAX_VALUE - 1;
        int actual = Solution.maxGap(heights, k);
        assertEquals(expected, actual);
    }
}