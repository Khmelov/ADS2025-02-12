package by.it.group410902.varava.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();  // вместимость рюкзака
        int n = scanner.nextInt();  // количество типов слитков
        int[] gold = new int[n];    // веса слитков

        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Массив для динамического программирования
        // dp[i] = максимальный вес, который можно набрать для вместимости i
        int[] dp = new int[W + 1];

        // Инициализация: для вместимости 0 максимальный вес = 0
        dp[0] = 0;

        // Заполняем массив dp
        for (int w = 1; w <= W; w++) {
            // Изначально предполагаем, что для вместимости w нельзя ничего набрать
            dp[w] = dp[w - 1];  // как минимум можем взять то же, что и для w-1

            // Проверяем все типы слитков
            for (int i = 0; i < n; i++) {
                // Если текущий слиток помещается в рюкзак
                if (gold[i] <= w) {
                    // Пробуем добавить этот слиток и смотрим, улучшится ли результат
                    dp[w] = Math.max(dp[w], dp[w - gold[i]] + gold[i]);
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