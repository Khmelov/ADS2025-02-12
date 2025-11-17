package by.it.group451002.shutko.lesson08;

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

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt(); // вместимость рюкзака
        int n = scanner.nextInt(); // количество вариантов слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Создаем массив для динамического программирования (максимальный вес золота, который можно унести)
        int[] dp = new int[W + 1];

        // Инициализируем dp[0] = 0
        dp[0] = 0;

        // Заполняем массив dp
        for (int w = 1; w <= W; w++) {
            dp[w] = dp[w - 1]; // Начинаем с предыдущего значения
            for (int i = 0; i < n; i++) { // Перебираем все типы слитков
                if (gold[i] <= w) {       // Если слиток можно положить в рюкзак размера w (его вес <= w)
                    // Пробуем добавить слиток и обновляем значение
                    int candidate = dp[w - gold[i]] + gold[i];
                    if (candidate > dp[w]) {  // Если этот вариант лучше текущего найденного решения для w, обновляем его
                        dp[w] = candidate;
                    }
                }
            }
        }

        return dp[W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
