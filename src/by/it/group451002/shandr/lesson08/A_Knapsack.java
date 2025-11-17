package by.it.group451002.shandr.lesson08;

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

        // dp[i] = true, если можно набрать вес i
        boolean[] dp = new boolean[W + 1];
        dp[0] = true;  // вес 0 всегда можно набрать

        // Проходим по всем возможным весам от 0 до W
        for (int weight = 0; weight <= W; weight++) {
            // Если текущий вес можно набрать
            if (dp[weight]) {
                // Пробуем добавить каждый тип слитка
                for (int i = 0; i < n; i++) {
                    int newWeight = weight + gold[i];
                    if (newWeight <= W) {
                        dp[newWeight] = true;
                    }
                }
            }
        }

        // Ищем максимальный достижимый вес
        int result = 0;
        for (int weight = W; weight >= 0; weight--) {
            if (dp[weight]) {
                result = weight;
                break;
            }
        }

        scanner.close();
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}