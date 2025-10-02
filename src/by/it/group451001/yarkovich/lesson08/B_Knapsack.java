package by.it.group451001.yarkovich.lesson08;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();  // Вместимость рюкзака
        int n = scanner.nextInt();  // Количество слитков
        int[] weights = new int[n]; // Веса слитков

        for (int i = 0; i < n; i++) {
            weights[i] = scanner.nextInt();
        }

        // Создаем двумерный массив для динамического программирования
        // dp[i][j] - максимальный вес, который можно набрать
        // используя первые i предметов с вместимостью j
        int[][] dp = new int[n + 1][W + 1];

        // Заполняем таблицу dp
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= W; j++) {
                // Если текущий предмет не помещается в рюкзак
                if (weights[i - 1] > j) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    // Выбираем максимум между:
                    // 1. Не брать текущий предмет
                    // 2. Взять текущий предмет + оптимальное заполнение оставшегося места
                    dp[i][j] = Math.max(dp[i - 1][j],
                            dp[i - 1][j - weights[i - 1]] + weights[i - 1]);
                }
            }
        }

        int result = dp[n][W];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}