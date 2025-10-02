package by.it.group451001.serganovskij.lesson08;

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
        int w=scanner.nextInt();
        int n=scanner.nextInt();
        int gold[]=new int[n];
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }

        // Создаем массив для динамического программирования
        // dp[i] будет true, если вес i можно набрать данными слитками
        boolean[] dp = new boolean[w + 1];

        // Базовый случай: вес 0 всегда можно набрать (ничего не кладем в рюкзак)
        dp[0] = true;

        // Перебираем все слитки
        for (int i = 0; i < n; i++) {
            // Для каждого слитка перебираем все веса от веса текущего слитка до вместимости рюкзака
            for (int j = gold[i]; j <= w; j++) {
                // Если вес (j - вес текущего слитка) достижим,
                // то и вес j тоже достижим путем добавления текущего слитка
                if (dp[j - gold[i]]) {
                    dp[j] = true;
                }
            }
        }

        int result = 0;
        // Ищем максимальный достижимый вес, начиная с максимального (w) и двигаясь вниз
        for (int i = w; i >= 0; i--) {
            if (dp[i]) {
                result = i;
                break;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }
}