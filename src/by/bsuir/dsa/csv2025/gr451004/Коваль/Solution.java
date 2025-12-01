package by.bsuir.dsa.csv2025.gr451004.Коваль;

import java.util.Scanner;
public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Чтение входных данных
        int W = scanner.nextInt(); // вместимость рюкзака
        int n = scanner.nextInt(); // количество предметов

        int[] weights = new int[n];
        int[] values = new int[n];

        for (int i = 0; i < n; i++) {
            weights[i] = scanner.nextInt();
            values[i] = scanner.nextInt();
        }

        // Решение задачи о рюкзаке с помощью ДП
        int[][] dp = new int[n + 1][W + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= W; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(
                            dp[i - 1][w],
                            dp[i - 1][w - weights[i - 1]] + values[i - 1]
                    );
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        // Вывод результата
        System.out.println(dp[n][W]);
        scanner.close();
    }
}