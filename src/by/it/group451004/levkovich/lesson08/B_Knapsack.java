package by.it.group451004.levkovich.lesson08;

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
        int w=scanner.nextInt();
        int n=scanner.nextInt();
        int gold[]=new int[n];
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }

        // Создаем массив для динамического программирования
        // dp[i] будет true, если вес i можно набрать данными слитками без повторов
        boolean[] dp = new boolean[w + 1];

        // Базовый случай: вес 0 всегда можно набрать (ничего не кладем в рюкзак)
        dp[0] = true;

        // Перебираем все слитки по одному разу
        for (int i = 0; i < n; i++) {
            // Для каждого слитка перебираем веса от максимального (w) до веса текущего слитка
            // Это важно: идем в обратном порядке, чтобы избежать повторного использования слитков
            for (int j = w; j >= gold[i]; j--) {
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
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}
