package by.it.group451004.matyrka.lesson08;

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
*/

public class B_Knapsack {

    int getMaxWeight(InputStream stream) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int w = scanner.nextInt(); // Вместимость рюкзака
        int n = scanner.nextInt(); // Количество слитков
        int gold[] = new int[n];   // Веса слитков

        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[i] - максимальный вес, который можно набрать для рюкзака вместимостью i
        // используя только первые j слитков (j обрабатывается в цикле)
        int[] dp = new int[w + 1];

        // Инициализация: для рюкзака вместимостью 0 максимальный вес = 0
        dp[0] = 0;

        // Обрабатываем каждый слиток по очереди
        for (int i = 0; i < n; i++) {
            int currentGold = gold[i];

            // Проходим по вместимостям от большей к меньшей
            // Это важно для рюкзака без повторов, чтобы не использовать один слиток дважды
            for (int capacity = w; capacity >= currentGold; capacity--) {
                // Для каждой вместимости выбираем максимум между:
                // 1. Текущим значением (без использования текущего слитка)
                // 2. Значением для вместимости (capacity - currentGold) + вес текущего слитка
                dp[capacity] = Math.max(dp[capacity], dp[capacity - currentGold] + currentGold);
            }
        }

        int result = dp[w];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
