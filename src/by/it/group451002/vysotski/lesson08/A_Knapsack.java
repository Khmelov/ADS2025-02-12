package by.it.group451002.vysotski.lesson08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();  // capacity of knapsack
        int n = scanner.nextInt();  // number of gold bar types
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[i] = true if weight i can be achieved
        boolean[] dp = new boolean[W + 1];
        dp[0] = true;  // zero weight is always achievable

        // For each achievable weight, try to add each gold bar
        for (int weight = 0; weight <= W; weight++) {
            if (dp[weight]) {
                for (int i = 0; i < n; i++) {
                    int newWeight = weight + gold[i];
                    if (newWeight <= W) {
                        dp[newWeight] = true;
                    }
                }
            }
        }

        // Find the maximum achievable weight
        int result = 0;
        for (int weight = W; weight >= 0; weight--) {
            if (dp[weight]) {
                result = weight;
                break;
            }
        }

        return result;
    }

    // Alternative solution using more traditional DP approach
    int getMaxWeightAlternative(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();
        int n = scanner.nextInt();
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[i] = maximum weight achievable with capacity i
        int[] dp = new int[W + 1];

        for (int capacity = 1; capacity <= W; capacity++) {
            for (int i = 0; i < n; i++) {
                if (gold[i] <= capacity) {
                    dp[capacity] = Math.max(dp[capacity],
                            dp[capacity - gold[i]] + gold[i]);
                }
            }
        }

        return dp[W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}