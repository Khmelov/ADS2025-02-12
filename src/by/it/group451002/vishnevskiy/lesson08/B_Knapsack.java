package by.it.group451002.vishnevskiy.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: рюкзак без повторов

Первая строка входа содержит целые числа
    1<=W<=100000     вместимость рюкзака
    1<=n<=300        число золотых слитков
                    (каждый можно использовать только один раз).
Следующая строка содержит n целых чисел, задающих веса каждого из слитков:
  0<=w[1]<=100000 ,..., 0<=w[n]<=100000

Найдите методами динамического программирования
максимальный вес золота, который можно унести в рюкзаке.

Sample Input:
10 3
1 4 8
Sample Output:
9

*/

public class B_Knapsack {

    int getMaxWeight(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();           // вместимость рюкзака
        int n = scanner.nextInt();           // количество золотых слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();     // веса каждого слитка
        }

        // dp[i] = максимальный вес золота, который можно положить в рюкзак вместимостью i
        int[] dp = new int[W + 1];

        // Проходим по каждому слитку
        for (int g : gold) {
            // Для 0/1 рюкзака (каждый слиток можно взять только один раз)
            // идём по вместимости в обратном порядке, чтобы не использовать слиток несколько раз
            for (int i = W; i >= g; i--) {
                // Выбираем максимум между:
                // 1) текущим значением dp[i] (не брать этот слиток)
                // 2) взять слиток g и добавить его к dp[i - g] (максимальный вес для оставшейся вместимости)
                dp[i] = Math.max(dp[i], dp[i - g] + g);
            }
        }

        // dp[W] содержит максимальный вес золота, который можно унести в рюкзак вместимостью W
        return dp[W];
    }




    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}
