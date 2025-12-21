package by.it.group451004.ivanov.lesson08;
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
        Scanner scanner = new Scanner(stream);
        int w = scanner.nextInt();
        int n = scanner.nextInt();
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }
        scanner.close();

        if (w == 0) {
            return 0;
        }

        boolean[] dp = new boolean[w + 1];
        dp[0] = true;

        for (int i = 0; i <= w; i++) {
            if (dp[i]) {
                for (int j = 0; j < n; j++) {
                    if (gold[j] == 0) {
                        continue;
                    }
                    int next = i + gold[j];
                    if (next == w) {
                        return w;
                    }
                    if (next < w && !dp[next]) {
                        dp[next] = true;
                    }
                }
            }
        }

        for (int i = w - 1; i >= 0; i--) {
            if (dp[i]) {
                return i;
            }
        }

        return 0;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }
}