package by.bsuir.dsa.csv2025.gr451001.Ромейко;

import org.junit.Test;

import java.util.Scanner;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class Solution {
    public static int minCoins(int[] coins, int needSummary) {
        int INF = needSummary + 1;
        int[] dp = new int[needSummary + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (int coin : coins) {
            for (int i = coin; i <= needSummary; i++) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }

        return dp[needSummary] > needSummary ? -1 : dp[needSummary];
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int needSummary = scanner.nextInt();

        int[] coins = new int[n];
        for (int i = 0; i < n; i++) {
            coins[i] = scanner.nextInt();
        }

        scanner.close();

        int answer = minCoins(coins, needSummary);

        System.out.println(answer);
    }

    @Test
    public void test1() {
        int[] coins = {1, 2, 5};
        assertEquals(3, Solution.minCoins(coins, 12));
    }

    @Test
    public void test2() {
        int[] coins = {3, 4, 5};
        assertEquals(2, Solution.minCoins(coins, 8));
    }

    @Test
    public void test3() {
        int[] coins = {7, 15};
        assertEquals(0, Solution.minCoins(coins, 0));
    }

    @Test
    public void test4() {
        int[] coins = {50, 20, 1};
        assertEquals(4, Solution.minCoins(coins, 80));
    }

    @Test
    public void test5() {
        int[] coins = {1, 7, 9, 11};
        assertEquals(91, Solution.minCoins(coins, 999));
    }
}