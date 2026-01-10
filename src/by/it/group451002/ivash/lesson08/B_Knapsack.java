package by.it.group451002.ivash.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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

        int[][] weights = new int[w + 1][n + 1];

        // Если считать, что нулевой столбец двумерного массива масс оптимальный
        // То пересчитываем последовательно столбцы (по одной ячейке), выбирая наибольшее значение среди
        // {Текущая масса ячейки | масса слитка + масса ячейки, макс. масса которой = масса ячейки - масса слитка,
        // находящейся в предыдущем оптимальным столбце}
        for (int j = 1; j < n + 1; j++)
            for (int i = 1; i < w + 1; i++)
            {
                weights[i][j] = weights[i][j-1];
                if (gold[j - 1] < i)
                    weights[i][j] = Math.max(weights[i][j], weights[i- gold[j - 1]][j  - 1] + gold[j - 1]);
            }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return weights[w][n];
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
