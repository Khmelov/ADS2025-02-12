package by.it.group410901.bandarzheuskaya.lesson08;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int W=scanner.nextInt();
        int n=scanner.nextInt();
        int[] gold=new int[n];
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }


        // dp[x] == true означает: суммарный вес x достижим (не превышая вместимость)
        boolean[] dp = new boolean[W + 1];
        dp[0] = true; // вес 0 всегда достижим (ничего не класть)

        // Для каждого типа слитка обновляем dp в порядке возрастания сумм
        // (это даёт возможность брать слиток несколько раз)
        for (int i = 0; i < n; i++) {
            int wt = gold[i];
            if (wt <= 0) continue;    // слиток нулевого веса не увеличит итог — пропускаем
            if (wt > W) continue;     // слиток тяжелее вместимости — не нужен

            for (int s = wt; s <= W; s++) {
                if (dp[s - wt]) dp[s] = true;
            }
        }

        // Найдём максимальный достижимый вес ≤ W
        for (int s = W; s >= 0; s--) {
            if (dp[s]) return s;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return 0;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
