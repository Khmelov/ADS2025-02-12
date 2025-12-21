package by.it.group451001.kazakov.lesson08;

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

        // создаём массив D, где D[i] будет хранить максимальный вес золота при вместимости i
        int D[] = new int[w + 1];

        // перебираем все возможные вместимости рюкзака от 1 до W
        for (int i = 1; i <= w; i++) {
            // перебираем все типы золотых слитков
            for (int j = 0; j < n; j++) {
                // если вес текущего слитка меньше либо равен текущей вместимости рюкзака
                if (gold[j] <= i) {
                    // обновляем D[i], выбирая максимум:
                    // 1) текущее значение D[i]
                    // 2) вес слитка gold[j] + лучший вес для (i - gold[j])
                    D[i] = Math.max(D[i], D[i - gold[j]] + gold[j]);
                }
            }
        }

        // результат — максимальный вес, который можно унести в рюкзаке с вместимостью W
        int result = D[w];
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