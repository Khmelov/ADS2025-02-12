package by.it.group410901.danilova.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: рюкзак с повторами

Первая строка входа содержит целые числа
    1<=W<=100000     вместимость рюкзака
    1<=n<=300        сколько есть вариантов золотых слитков
                     (каждый можно использовать множество раз).
Следующая строка содержит n целых чисел, задающих веса слитков:
  0<=w[1]<=100000 ,..., 0<=w[n]<=100000

Найдите методами динамического программирования
максимальный вес золота, который можно унести в рюкзаке.
*/

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();
        int n = scanner.nextInt();
        int[] weights = new int[n];

        for (int i = 0; i < n; i++) {
            weights[i] = scanner.nextInt();
        }

        boolean[] dp = new boolean[W + 1];
        dp[0] = true;

        for (int i = 0; i <= W; i++) {
            if (dp[i]) {
                for (int j = 0; j < n; j++) {
                    int newWeight = i + weights[j];
                    if (newWeight <= W) {
                        dp[newWeight] = true;
                    }
                }
            }
        }

        int result = 0;
        for (int i = W; i >= 0; i--) {
            if (dp[i]) {
                result = i;
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