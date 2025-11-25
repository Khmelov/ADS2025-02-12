package by.it.group410901.gutseva.lesson08;

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
        int w=scanner.nextInt();   // вместимость рюкзака
        int n=scanner.nextInt();   // количество слитков
        int gold[]=new int[n];
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }

        // dp[i] = максимальный вес, который можно набрать при вместимости i
        int[] dp = new int[w+1];

        // Перебираем все слитки
        for (int j = 0; j < n; j++) {
            //Пробегаем все возможные вместимости рюкзака (от большого к маленькому)
            int weight = gold[j];
            // идём справа налево, чтобы один слиток не использовался несколько раз
            for (int i = w; i >= weight; i--) {
                // выбираем максимум: без слитка или с ним
                dp[i] = Math.max(dp[i], dp[i - weight] + weight);
            }
        }

        int result = dp[w];  // максимальный вес при полной вместимости
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}
