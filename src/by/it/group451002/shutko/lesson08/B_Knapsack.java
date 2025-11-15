package by.it.group451002.shutko.lesson08;

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
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Создаем многомерный массив для динамического программирования
        int[][] dp = new int[n + 1][W + 1];

        // Инициализируем первую строку и первый столбец нулями
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 0;
        }
        for (int j = 0; j <= W; j++) {
            dp[0][j] = 0;
        }

        // Заполняем массив dp, рассматривая все подмножества слитков и все вместимости
        for (int i = 1; i <= n; i++) { // i - количество рассмотренных слитков
            for (int j = 1; j <= W; j++) { // j - текущая рассматриваемая вместимость
                if (gold[i - 1] <= j) {  // gold[i-1] - вес текущего i-го слитка (принимаем решение)
                    // Можем взять текущий слиток
                    dp[i][j] = Math.max(
                            dp[i - 1][j], // не брать текущий слиток, Тогда вес остаётся таким же
                            dp[i - 1][j - gold[i - 1]] + gold[i - 1] // взять текущий слиток
                    );
                } else {
                    // Не можем взять текущий слиток (слишком тяжелый)
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[n][W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
