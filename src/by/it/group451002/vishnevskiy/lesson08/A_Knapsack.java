package by.it.group451002.vishnevskiy.lesson08;

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


Sample Input:
10 3
1 4 8
Sample Output:
10

Sample Input 2:

15 3
2 8 16
Sample Output 2:
14

*/

public class A_Knapsack {

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

        // Проходим все возможные вместимости рюкзака от 1 до W
        for (int i = 1; i <= W; i++) {
            // Перебираем все слитки
            for (int g : gold) {
                // Если слиток помещается в рюкзак текущей вместимости i
                if (g <= i) {
                    // Выбираем максимум между текущим значением dp[i]
                    // и вариантом: взять этот слиток (dp[i - g] + g)
                    dp[i] = Math.max(dp[i], dp[i - g] + g);
                }
            }
        }

        // dp[W] содержит максимальный вес золота, который можно унести в рюкзак вместимостью W
        return dp[W];
    }



    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
