package by.it.group410902.andala.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();  // вместимость рюкзака
        int n = scanner.nextInt();  // количество слитков
        int[] gold = new int[n];    // массив весов каждого слитка

        // читаем веса слитков
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[i] — максимальный вес, который можно набрать при вместимости i
        int[] dp = new int[W + 1];

        // перебираем каждый слиток
        for (int g : gold) {
            // идём от конца к началу (чтобы один слиток не использовать дважды)
            for (int i = W; i >= g; i--) {
                // сравниваем:
                // 1) если не брать этот слиток — dp[i]
                // 2) если взять — dp[i - g]
                // выбираем максимум из двух вариантов
                dp[i] = Math.max(dp[i], dp[i - g] + g);
            }
        }

        // возвращаем максимальный вес при полной вместимости рюкзака
        return dp[W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
