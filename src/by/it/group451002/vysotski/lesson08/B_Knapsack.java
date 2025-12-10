package by.it.group451002.vysotski.lesson08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();  // capacity of knapsack
        int n = scanner.nextInt();  // number of gold bars
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[i][j] = maximum weight achievable using first i items with capacity j
        int[][] dp = new int[n + 1][W + 1];

        // Build the DP table
        for (int i = 1; i <= n; i++) {
            for (int capacity = 1; capacity <= W; capacity++) {
                // If current gold bar doesn't fit, skip it
                if (gold[i - 1] > capacity) {
                    dp[i][capacity] = dp[i - 1][capacity];
                } else {
                    // Choose maximum between:
                    // 1. Not taking the current gold bar
                    // 2. Taking the current gold bar + maximum weight with remaining capacity
                    dp[i][capacity] = Math.max(dp[i - 1][capacity],
                            dp[i - 1][capacity - gold[i - 1]] + gold[i - 1]);
                }
            }
        }

        return dp[n][W];
    }

    // Space-optimized solution using 1D array
    int getMaxWeightOptimized(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();
        int n = scanner.nextInt();
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[j] = maximum weight achievable with capacity j
        int[] dp = new int[W + 1];

        for (int i = 0; i < n; i++) {
            // Traverse backwards to avoid reusing the same item
            for (int capacity = W; capacity >= gold[i]; capacity--) {
                dp[capacity] = Math.max(dp[capacity], dp[capacity - gold[i]] + gold[i]);
            }
        }

        return dp[W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}