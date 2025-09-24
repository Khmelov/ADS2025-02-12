package by.it.group410901.evtuhovskaya.lesson08;

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
        int W = scanner.nextInt();  // вместимость рюкзака
        int n = scanner.nextInt();  // количество слитков
        int[] gold = new int[n];

        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Создаем массив dp, где dp[i] - максимальный вес для вместимости i
        int[] dp = new int[W + 1];

        // Заполняем массив dp
        for (int i = 0; i < n; i++) {
            for (int j = W; j >= gold[i]; j--) {
                if (dp[j - gold[i]] + gold[i] > dp[j]) {
                    dp[j] = dp[j - gold[i]] + gold[i];
                }
            }
        }

        return dp[W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}