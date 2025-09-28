package by.it.group451001.khomenkov.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt(); // вместимость рюкзака
        int n = scanner.nextInt(); // количество слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Создаем массив для динамического программирования
        int[][] dp = new int[n + 1][W + 1];

        // Инициализируем первую строку и первый столбец нулями
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 0;
        }
        for (int j = 0; j <= W; j++) {
            dp[0][j] = 0;
        }

        // Заполняем массив dp
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= W; j++) {
                if (gold[i - 1] <= j) {
                    // Можем взять текущий слиток
                    dp[i][j] = Math.max(
                            dp[i - 1][j], // не брать текущий слиток
                            dp[i - 1][j - gold[i - 1]] + gold[i - 1] // взять текущий слиток
                    );
                } else {
                    // Не можем взять текущий слиток (слишком тяжелый)
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[n][W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}