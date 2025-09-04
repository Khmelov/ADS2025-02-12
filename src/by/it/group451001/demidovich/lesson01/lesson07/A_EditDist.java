package by.it.group451001.demidovich.lesson01.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
Дано:
    Две строки длиной до 100, содержащие строчные латинские буквы.
Необходимо:
    Решить задачу методами динамического программирования
    Рекурсивно вычислить расстояние редактирования двух строк
Sample Input 1:
ab
ab
Sample Output 1:
0
Sample Input 2:
short
ports
Sample Output 2:
3
Sample Input 3:
distance
editing
Sample Output 3:
5
*/

public class A_EditDist {

    private int[][] dp;

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();
        dp = new int[n + 1][m + 1];

        // инициализация массива -1 (невычисленные значения)
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                dp[i][j] = -1;
            }
        }

        return compute(one, two, n, m);
    }

    private int compute(String s1, String s2, int i, int j) {
        if (dp[i][j] != -1) {
            return dp[i][j];
        }

        if (i == 0) {
            dp[i][j] = j; // нужно j вставок
        } else if (j == 0) {
            dp[i][j] = i; // нужно i удалений
        } else {
            int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

            int insert = compute(s1, s2, i, j - 1) + 1;
            int delete = compute(s1, s2, i - 1, j) + 1;
            int replace = compute(s1, s2, i - 1, j - 1) + cost;

            dp[i][j] = Math.min(Math.min(insert, delete), replace);
        }

        return dp[i][j];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // Output 1
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // Output 2
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // Output 3
    }
}