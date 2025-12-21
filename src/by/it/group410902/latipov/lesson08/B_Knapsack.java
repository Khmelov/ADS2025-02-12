package by.it.group410902.latipov.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt(); // вместимость рюкзака
        int n = scanner.nextInt(); // количество слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[i][j] - максимальный вес, который можно набрать
        // используя первые i слитков для рюкзака вместимостью j
        int[][] dp = new int[n + 1][W + 1];

        // Заполняем таблицу динамического программирования
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= W; j++) {
                // Если текущий слиток можно положить в рюкзак
                if (gold[i - 1] <= j) {
                    // Выбираем максимум между:
                    // 1. Не брать текущий слиток
                    // 2. Взять текущий слиток + максимальный вес для оставшейся вместимости
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - gold[i - 1]] + gold[i - 1]);
                } else {
                    // Если слиток слишком тяжелый, не берем его
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[n][W];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}