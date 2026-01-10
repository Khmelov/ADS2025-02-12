package by.it.group410902.andala.lesson08;

import java.io.InputStream;
import java.util.Scanner;

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        // Считываем данные из файла:
        // W — вместимость рюкзака
        // n — количество типов слитков
        int W = scanner.nextInt();  // например, 15
        int n = scanner.nextInt();  // например, 3

        // Массив, где хранятся веса всех типов слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();  // читаем каждый вес (например, 2, 8, 16)
        }

        // dp[i] — это максимальный вес золота, который можно положить
        // в рюкзак вместимостью ровно i
        int[] dp = new int[W + 1];

        // Проходим по всем возможным вместимостям рюкзака от 1 до W
        for (int i = 1; i <= W; i++) {
            // Для каждой вместимости проверяем все типы слитков
            for (int g : gold) {
                // Если слиток весом g помещается в рюкзак вместимости i
                if (g <= i) {
                    // Выбираем лучший вариант:
                    // либо оставляем старое значение dp[i],
                    // либо добавляем этот слиток и получаем новое значение dp[i - g] + g
                    dp[i] = Math.max(dp[i], dp[i - g] + g);
                }
            }
        }

        // Возвращаем результат:
        // dp[W] — это максимальный вес золота, который поместится в рюкзак вместимости W
        return dp[W];
    }

    public static void main(String[] args) {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
