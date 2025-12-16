package by.it.group451001.khomenkov.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt(); // вместимость рюкзака
        int n = scanner.nextInt(); // количество вариантов слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Создаем массив для динамического программирования
        int[] dp = new int[W + 1];

        // Инициализируем dp[0] = 0
        dp[0] = 0;

        // Заполняем массив dp
        for (int w = 1; w <= W; w++) {
            dp[w] = dp[w - 1]; // начинаем с предыдущего значения
            for (int i = 0; i < n; i++) {
                if (gold[i] <= w) {
                    // Пробуем добавить слиток и обновляем значение
                    int candidate = dp[w - gold[i]] + gold[i];
                    if (candidate > dp[w]) {
                        dp[w] = candidate;
                    }
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