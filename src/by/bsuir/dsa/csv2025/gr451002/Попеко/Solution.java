package by.bsuir.dsa.csv2025.gr451002.Попеко;

import java.util.Scanner;
import org.junit.Test;
import org.junit.Assert;

public class Solution {

    public static boolean isWinningPosition(int[] piles) {
        return calculateNimSum(piles) != 0;
    }

    public static int calculateNimSum(int[] piles) {
        int result = 0;
        for (int pile : piles) {
            result ^= pile;
        }
        return result;
    }

    // ========== JUnit TESTS ==========

    @Test
    public void testWinningPosition1() {
        int[] piles = {3, 4, 5};
        Assert.assertTrue(isWinningPosition(piles));
    }

    @Test
    public void testLosingPosition1() {
        int[] piles = {1, 2, 3};
        Assert.assertFalse(isWinningPosition(piles));
    }

    @Test
    public void testSinglePile() {
        int[] piles = {5};
        Assert.assertTrue(isWinningPosition(piles));
    }

    @Test
    public void testAllOnesOdd() {
        int[] piles = {1, 1, 1};
        Assert.assertTrue(isWinningPosition(piles));
    }

    @Test
    public void testAllOnesEven() {
        int[] piles = {1, 1};
        Assert.assertFalse(isWinningPosition(piles));
    }

    // ========== MAIN PROGRAM ==========

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        for (int i = 1; i <= 10; i++) {
            String input = scanner.nextLine().trim();

            try {
                String[] parts = input.split("\\s+");
                int[] piles = new int[parts.length];

                for (int j = 0; j < parts.length; j++) {
                    piles[j] = Integer.parseInt(parts[j]);
                }

                boolean isWinning = isWinningPosition(piles);
                System.out.println( (isWinning ? "win" : "lose"));

            } catch (NumberFormatException e) {

            }
        }


        scanner.close();
    }
}