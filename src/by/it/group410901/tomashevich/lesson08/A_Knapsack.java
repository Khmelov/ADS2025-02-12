package by.it.group410901.tomashevich.lesson08;

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
        int w = scanner.nextInt(); // вместимость рюкзака
        int n = scanner.nextInt(); // количество вариантов слитков
        int gold[] = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt(); // читаем веса слитков
        }
        scanner.close();

        // dp[i] = можно ли набрать вес i
        boolean[] dp = new boolean[w + 1];
        dp[0] = true; // вес 0 всегда достижим (ничего не берем)

        // Перебираем все веса и пытаемся добавлять к ним каждый слиток
        for (int i = 0; i <= w; i++) {
            if (dp[i]) { // если вес i достижим
                for (int weight : gold) {
                    if (i + weight <= w) {
                        dp[i + weight] = true; // значит достижим и i+weight
                    }
                }
            }
        }

        // Находим максимальный вес, который можно набрать
        for (int i = w; i >= 0; i--) {
            if (dp[i]) {
                return i; // сразу возвращаем первый подходящий (максимальный)
            }
        }
        return 0;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
