package by.it.group451002.yeshmanski.lesson08;

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
        int W = scanner.nextInt();   // Вместимость рюкзака
        int n = scanner.nextInt();   // Количество слитков
        int gold[] = new int[n];     // Массив весов слитков
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        /*
          d[w][i] = максимальный вес золота,
                    который можно унести при вместимости рюкзака w.
        */
        int[][] d = new int[W + 1][n + 1];

        // Перебираем слитки
        for (int i = 1; i <= n; i++) {

            // Перебираем все возможные вместимости рюкзака
            for (int w = 1; w <= W; w++) {

                // Если не берём i-й слиток,
                // решение такое же, как если бы у нас было только i-1 слитков
                d[w][i] = d[w][i - 1];

                // Берём i-й слиток, если он помещается
                // (Массив gold[] нумеруется с 0,
                // а i идёт с 1 (смещение на -1))
                if (gold[i - 1] <= w) {
                    /*
                      Кладём слиток весом gold[i-1],
                        остаётся место w - gold[i-1],
                        и дальше можем использовать только первые i-1 слитков
                    */
                    d[w][i] = Math.max(
                            d[w][i],                                      // не берём слиток
                            d[w - gold[i - 1]][i - 1] + gold[i - 1]       // берём слиток
                                                                          // (оптимальный вес кроме слитка + слиток)
                    );
                }
            }
        }


        return d[W][n];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
