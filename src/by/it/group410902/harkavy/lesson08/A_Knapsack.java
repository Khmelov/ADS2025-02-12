package by.it.group410902.harkavy.lesson08;

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
        int capacity = scanner.nextInt(); // Вместимость рюкзака
        int n = scanner.nextInt(); // Количество типов слитков
        int[] gold = new int[n]; // Массив весов слитков
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Создаем массив для хранения максимального веса для каждой вместимости от 0 до capacity
        int[] maxWeights = new int[capacity + 1];

        // Проходим по всем возможным вместимостям рюкзака от 1 до capacity
        for (int w = 1; w <= capacity; w++) {
            // Для каждой вместимости w ищем лучший вариант заполнения
            for (int i = 0; i < n; i++) {
                // Если текущий слиток gold[i] помещается в рюкзак вместимостью w
                if (gold[i] <= w) {
                    // Рассматриваем вариант: взять этот слиток.
                    // Общий вес будет равен весу этого слитка + максимальному весу,
                    // который мы могли унести в рюкзаке с оставшейся вместимостью (w - gold[i]).
                    int potentialWeight = maxWeights[w - gold[i]] + gold[i];

                    // Обновляем значение для текущей вместимости w, если найденный вариант лучше
                    if (potentialWeight > maxWeights[w]) {
                        maxWeights[w] = potentialWeight;
                    }
                }
            }
        }

        int result = maxWeights[capacity];
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
