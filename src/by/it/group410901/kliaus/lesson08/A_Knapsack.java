package by.it.group410901.kliaus.lesson08;

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
        int w = scanner.nextInt(); // вместимость рюкзака
        int n = scanner.nextInt(); // количество слитков
        int gold[] = new int[n];   // массив весов слитков
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt(); // считываем каждый вес
        }

        // Массив dp, где dp[i] = максимальный вес, который можно набрать в рюкзаке вместимости i
        int[] dp = new int[w + 1];

        // Динамическое программирование
        for (int i = 0; i <= w; i++) { // для каждого веса рюкзака от 0 до W
            for (int j = 0; j < n; j++) { // пробуем каждый слиток
                if (gold[j] <= i) { // если слиток помещается в рюкзак
                    dp[i] = Math.max(dp[i], dp[i - gold[j]] + gold[j]); // обновляем максимальный вес
                }
            }
        }

        int result = dp[w]; // результат — максимальный вес для полного рюкзака
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
