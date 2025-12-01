package by.bsuir.dsa.csv2025.gr451004.Коваленко;


import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;


public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int n = 4; // Example input
        int result = solution.totalNQueens(n);
        System.out.println("Total solutions for " + n + "-Queens: " + result);
    }

    private int help(int i, int n, int[] col, int[] topLeft, int[] topRight) {
        if (i == n) return 1;
        int count = 0;
        for (int j = 0; j < n; j++) {
            if ((col[j] == 0) && (topLeft[i - j + n - 1] == 0) && (topRight[i + j] == 0)) {
                col[j] = 1;
                topLeft[i - j + n - 1] = 1;
                topRight[i + j] = 1;

                count += help(i + 1, n, col, topLeft, topRight);

                col[j] = 0;
                topLeft[i - j + n - 1] = 0;
                topRight[i + j] = 0;
            }
        }
        return count;
    }

    public int totalNQueens(int n) {
        if (n <= 0) return 0;

        int[] col = new int[n];
        Arrays.fill(col, 0);
        int[] topLeft = new int[2 * n - 1];
        Arrays.fill(topLeft, 0);
        int[] topRight = new int[2 * n - 1];
        Arrays.fill(topRight, 0);

        return help(0, n, col, topLeft, topRight);
    }

    @Test
    public void testNQueens1() {
        assertEquals(1, totalNQueens(1));
    }

    @Test
    public void testNQueens2() {
        assertEquals(0, totalNQueens(2));
    }

    @Test
    public void testNQueens3() {
        assertEquals(0, totalNQueens(3));
    }

    @Test
    public void testNQueens4() {
        assertEquals(2, totalNQueens(4));
    }

    @Test
    public void testNQueens5() {
        assertEquals(10, totalNQueens(5));
    }

    @Test
    public void testNQueens6() {
        assertEquals(4, totalNQueens(6));
    }

    @Test
    public void testNQueens7() {
        assertEquals(40, totalNQueens(7));
    }

    @Test
    public void testNQueens8() {
        assertEquals(92, totalNQueens(8));
    }

    @Test
    public void testNQueens9() {
        assertEquals(352, totalNQueens(9));
    }

    @Test
    public void testNQueens10() {
        assertEquals(724, totalNQueens(10));
    }
}