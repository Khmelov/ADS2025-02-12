package by.it.group451004.matyrka.lesson08;

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
*/

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //Для каждой возможной вместимости рюкзака от 0 до W вычисляем максимальный вес, который можно в него положить.
        Scanner scanner = new Scanner(stream);
        int w = scanner.nextInt(); // Вместимость рюкзака
        int n = scanner.nextInt(); // Количество видов слитков
        int gold[] = new int[n];   // Веса слитков

        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[i] - максимальный вес, который можно набрать для рюкзака вместимостью i
        int[] dp = new int[w + 1];

        // Инициализация: для рюкзака вместимостью 0 максимальный вес = 0
        dp[0] = 0;

        // Заполняем массив dp снизу вверх
        for (int capacity = 1; capacity <= w; capacity++) {
            // Изначально максимальный вес для текущей вместимости = предыдущему значению
            dp[capacity] = dp[capacity - 1];

            // Перебираем все виды слитков
            for (int i = 0; i < n; i++) {
                // Если текущий слиток помещается в рюкзак
                if (gold[i] <= capacity) {
                    // Пробуем добавить текущий слиток и смотрим, улучшит ли это результат
                    dp[capacity] = Math.max(dp[capacity], dp[capacity - gold[i]] + gold[i]);
                }
            }
        }

        int result = dp[w];
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
