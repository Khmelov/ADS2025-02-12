package by.it.group410902.jalilova.lesson08;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int w = scanner.nextInt();
        int n = scanner.nextInt();
        int gold[] = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }
        // dp[i][j] = максимальный вес, который можно набрать,
        // используя первые i слитков при вместимости j
        int[][] dp = new int[n + 1][w + 1];


        for (int j = 0; j <= w; j++) {
            dp[0][j] = 0;
        }

        for (int i = 0; i <= n; i++) {
            dp[i][0] = 0;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= w; j++) {


                if (gold[i - 1] > j) {
                    // если текущий слиток тяжелее доступной вместимости,
                    // то его взять нельзя значит оставляем старое значение
                    dp[i][j] = dp[i - 1][j];
                } else {
                    // иначе выбираем максимум:
                    // 1) не брать этот слиток
                    // 2) взять этот слиток
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - gold[i - 1]] + gold[i - 1]);
                }
            }
        }

        return dp[n][w];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}
