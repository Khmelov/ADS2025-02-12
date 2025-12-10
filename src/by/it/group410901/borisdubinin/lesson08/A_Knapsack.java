package by.it.group410901.borisdubinin.lesson08;

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
        int weight = scanner.nextInt();
        int numberOfBars = scanner.nextInt();
        int gold[] = new int[numberOfBars];
        for (int i = 0; i < numberOfBars; i++) {
            gold[i] = scanner.nextInt();
        }

        boolean dp[] = new boolean[weight + 1];
        dp[0] = true;

        for(int i = 1; i <= weight; i++){
            for(int j = 0; j < numberOfBars; j++){
                if(gold[j] <= i && dp[i - gold[j]]) {
                    dp[i] = true;
                    break;
                }
            }
        }

        int result = 0;
        for(int i = 1; i < dp.length; i++){
            if(dp[i])
                result = i;
        }
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
