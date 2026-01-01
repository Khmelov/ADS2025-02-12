package by.it.group451003.bernat.lesson08;


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

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt(); // вместимость рюкзака
        int n = scanner.nextInt(); // количество слитков
        int[] weights = new int[n];

        for (int i = 0; i < n; i++) {
            weights[i] = scanner.nextInt();
        }

        // Создаем двумерный массив для динамического программирования
        int[][] dp = new int[n + 1][W + 1];

        // Заполняем таблицу dp
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= W; j++) {
                // Если текущий слиток можно положить в рюкзак
                if (weights[i - 1] <= j) {
                    // Выбираем максимум между:
                    // 1. Взять текущий слиток + максимальный вес для оставшейся вместимости
                    // 2. Не брать текущий слиток
                    dp[i][j] = Math.max(
                            dp[i - 1][j - weights[i - 1]] + weights[i - 1],
                            dp[i - 1][j]
                    );
                } else {
                    // Если слиток слишком тяжелый, не берем его
                    dp[i][j] = dp[i - 1][j];
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
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}
