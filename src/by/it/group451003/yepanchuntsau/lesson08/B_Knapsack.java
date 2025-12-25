package by.it.group451003.yepanchuntsau.lesson08;

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
        int W = scanner.nextInt();
        int n = scanner.nextInt();
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[c] = можно ли набрать ровно вес c, используя каждый слиток не более одного раза
        boolean[] dp = new boolean[W + 1];
        dp[0] = true;

        for (int w : gold) {
            if (w <= 0) continue;       // нулевые веса нам ничего не дают
            if (w > W) continue;        // этот слиток все равно не влезет

            // идем назад, чтобы не взять один и тот же слиток дважды
            for (int c = W; c >= w; c--) {
                if (dp[c - w]) dp[c] = true;
            }
        }

        // ищем максимальный достижимый вес ≤ W
        for (int c = W; c >= 0; c--) {
            if (dp[c]) return c;
        }
        return 0;
    }



    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}
