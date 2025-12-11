package by.it.group451001.zhynko.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: рюкзак с повторами

Первая строка входа содержит целые числа
    1<=W<=100000 вместимость рюкзака
    1<=n<=300 сколько есть вариантов золотых слитков
                     (каждый можно использовать множество раз).
Следующая строка содержит n целых чисел, задающих веса слитков:
  0<=w[1]<=100000,..., 0<=w[n]<=100000

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
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int w=scanner.nextInt();
        int n=scanner.nextInt();
        int[] gold =new int[n];
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }

        int[][] dp = new int [w + 1][n + 1];

        for (int i = 0;i <= w; i++)
            dp[i][0] = 0;

        for (int j = 1; j <= n; j++){
            int currentGoldValue = gold[j - 1];
            for (int i = 0; i <= w; i++){
                int temp = 0;
                if (currentGoldValue <= i)
                    temp = dp[i - currentGoldValue][j] + currentGoldValue;
                dp[i][j] = Math.max(temp,dp[i][j - 1]) ;
            }
        }

        return dp[w][n];
        //!!!!!!!!!!!!!!!!!!!!!!!!!  КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
