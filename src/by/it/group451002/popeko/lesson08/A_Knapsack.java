package by.it.group451002.popeko.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();  // вместимость рюкзака
        int n = scanner.nextInt();  // количество видов слитков
        int[] gold = new int[n];

        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Массив для динамического программирования
        boolean[] dp = new boolean[W + 1];
        dp[0] = true;  // вес 0 всегда можно набрать

        // Заполняем массив dp
        for (int i = 1; i <= W; i++) {
            for (int j = 0; j < n; j++) {
                if (gold[j] <= i && dp[i - gold[j]]) {
                    dp[i] = true;
                    break;  // можно не проверять остальные слитки
                }
            }
        }

        // Ищем максимальный достижимый вес
        int result = 0;
        for (int i = W; i >= 0; i--) {
            if (dp[i]) {
                result = i;
                break;
            }
        }

        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}