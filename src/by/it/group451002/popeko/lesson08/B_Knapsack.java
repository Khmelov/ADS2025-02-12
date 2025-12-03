package by.it.group451002.popeko.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();  // вместимость рюкзака
        int n = scanner.nextInt();  // количество слитков
        int[] gold = new int[n];

        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Массив для динамического программирования
        boolean[][] dp = new boolean[n + 1][W + 1];

        // Базовый случай: вес 0 всегда можно набрать
        for (int i = 0; i <= n; i++) {
            dp[i][0] = true;
        }

        // Заполняем массив dp
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= W; j++) {
                // Если не берем текущий слиток
                dp[i][j] = dp[i - 1][j];

                // Если можем взять текущий слиток
                if (j >= gold[i - 1] && dp[i - 1][j - gold[i - 1]]) {
                    dp[i][j] = true;
                }
            }
        }

        // Ищем максимальный достижимый вес
        int result = 0;
        for (int j = W; j >= 0; j--) {
            if (dp[n][j]) {
                result = j;
                break;
            }
        }

        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}