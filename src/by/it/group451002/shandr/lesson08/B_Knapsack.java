package by.it.group451002.shandr.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();  // вместимость рюкзака
        int n = scanner.nextInt();  // количество слитков
        int[] gold = new int[n];    // веса слитков

        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[i][j] = максимальный вес, который можно набрать
        // используя первые i слитков с вместимостью j
        int[][] dp = new int[n + 1][W + 1];

        // Заполняем таблицу динамического программирования
        for (int i = 1; i <= n; i++) {
            for (int weight = 1; weight <= W; weight++) {
                // Текущий слиток
                int currentGold = gold[i - 1];

                // Если текущий слиток можно положить в рюкзак
                if (currentGold <= weight) {
                    // Выбираем максимум между:
                    // 1. Берем текущий слиток + максимальный вес для оставшейся вместимости
                    // 2. Не берем текущий слиток
                    dp[i][weight] = Math.max(
                            dp[i - 1][weight - currentGold] + currentGold,
                            dp[i - 1][weight]
                    );
                } else {
                    // Не можем взять текущий слиток - используем предыдущий результат
                    dp[i][weight] = dp[i - 1][weight];
                }
            }
        }

        int result = dp[n][W];
        scanner.close();
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}