package by.bsuir.dsa.csv2025.gr451002.Курейчик;

import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    public static boolean isWinningPosition(int n) {
        return n % 4 != 0;
    }

    public static int findWinningMove(int n) {
        if (n % 4 == 0) {
            return -1;
        }
        return n % 4;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        boolean isWinning = isWinningPosition(n);
        int move = findWinningMove(n);

        System.out.println(n);
        System.out.println(isWinning);
        System.out.println(move);

        scanner.close();
    }

    @Test
    public void testWinningPositions() {
        assertTrue(Solution.isWinningPosition(1));
        assertTrue(Solution.isWinningPosition(2));
        assertTrue(Solution.isWinningPosition(3));
        assertTrue(Solution.isWinningPosition(5));
        assertTrue(Solution.isWinningPosition(6));
        assertTrue(Solution.isWinningPosition(7));
        assertTrue(Solution.isWinningPosition(9));
        assertTrue(Solution.isWinningPosition(10));
        assertTrue(Solution.isWinningPosition(11));
        assertTrue(Solution.isWinningPosition(17));
    }

    @Test
    public void testLosingPositions() {
        assertFalse(Solution.isWinningPosition(0));
        assertFalse(Solution.isWinningPosition(4));
        assertFalse(Solution.isWinningPosition(8));
        assertFalse(Solution.isWinningPosition(12));
        assertFalse(Solution.isWinningPosition(20));
    }

    @Test
    public void testWinningMoves() {
        assertEquals(1, Solution.findWinningMove(1));
        assertEquals(2, Solution.findWinningMove(2));
        assertEquals(3, Solution.findWinningMove(3));
        assertEquals(1, Solution.findWinningMove(5));
        assertEquals(2, Solution.findWinningMove(6));
        assertEquals(3, Solution.findWinningMove(7));
        assertEquals(1, Solution.findWinningMove(9));
        assertEquals(2, Solution.findWinningMove(10));
        assertEquals(3, Solution.findWinningMove(11));
        assertEquals(1, Solution.findWinningMove(17));
    }

    @Test
    public void testNoWinningMove() {
        assertEquals(-1, Solution.findWinningMove(0));
        assertEquals(-1, Solution.findWinningMove(4));
        assertEquals(-1, Solution.findWinningMove(8));
        assertEquals(-1, Solution.findWinningMove(12));
        assertEquals(-1, Solution.findWinningMove(20));
    }
}
