package by.it.group451003.kharkevich.lesson08;

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
        int w=scanner.nextInt(); //Чтение первого числа из входного потока. W — вместимость рюкзака.
        int n=scanner.nextInt(); //Чтение второго числа из входного потока. n — количество различных видов золотых слитков.
        int gold[]=new int[n]; //Создание массива gold целых чисел размером n для хранения веса каждого вида слитков.
        for (int i = 0; i < n; i++) { //заполняю массив
            gold[i]=scanner.nextInt();
        }

        int[][] dp = new int[n + 1][w + 1]; /*Двумерного массива dp для хранения промежуточных результатов.
        Размерность [n + 1][w + 1]: по количеству видов слитков (n) + 1 и по вместимости рюкзака (w) + 1.
        dp[i][j] будет хранить максимальный вес, который можно набрать, используя первые i видов слитков для рюкзака вместимостью j.*/

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= w; j++) {
                if (gold[i - 1] <= j) { //если вес текущего слитка (gold[i - 1]
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - gold[i - 1]] + gold[i - 1]); //Взять текущий слиток
                } else {
                    dp[i][j] = dp[i - 1][j]; //Не брать текущий слиток.
                }
            }
        }

        return dp[n][w];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
