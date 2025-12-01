package by.it.group451001.alexandrovich.lesson08;

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
        }/*
        int[][] g = new int[n+1][w];

        for (int i = 0; i < n; i++) {
            g[0][i] = 0;
        }
        int result = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < w; j++) {
                if (gold[i-1] > j){
                    g[i][j] = g[i-1][j];
                }
                else {
                    g[i][j] = Math.max(g[i-1][j], g[i-1][j-gold[i-1]] + gold[i-1]);
                }
                result = Math.max(result, g[i][j]);
            }
        }*/
        int[] g = new int[w];

        for (int i = 0; i < n; i++) {
            g[i] = 0;
        }
        int result = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < w; j++) {
                if (gold[i] <= j){
                    g[j] = Math.max(g[j], g[j-gold[i]] + gold[i]);
                }
                result = Math.max(result, g[j]);
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}
