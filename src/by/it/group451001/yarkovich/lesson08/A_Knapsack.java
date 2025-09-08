package by.it.group451001.yarkovich.lesson08;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();  // Вместимость рюкзака
        int n = scanner.nextInt();  // Количество типов слитков
        int[] weights = new int[n]; // Веса слитков

        for (int i = 0; i < n; i++) {
            weights[i] = scanner.nextInt();
        }

        // Создаем массив для динамического программирования
        // dp[i] = true, если можно набрать вес i, иначе false
        boolean[] dp = new boolean[W + 1];
        dp[0] = true; // Нулевой вес всегда можно набрать (ничего не берем)

        int maxWeight = 0; // Максимальный достижимый вес

        // Заполняем массив dp
        for (int weight = 0; weight <= W; weight++) {
            if (dp[weight]) {
                // Если текущий вес достижим, обновляем максимальный вес
                maxWeight = Math.max(maxWeight, weight);

                // Пробуем добавить каждый тип слитка
                for (int i = 0; i < n; i++) {
                    int newWeight = weight + weights[i];
                    if (newWeight <= W) {
                        dp[newWeight] = true;
                    }
                }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return maxWeight;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}