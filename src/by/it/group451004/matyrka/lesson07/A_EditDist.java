package by.it.group451004.matyrka.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    static String str1;
    static String str2;
    static int[][] dp;

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        Scanner scanner = new Scanner(stream);

        A_EditDist instance = new A_EditDist();

        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

    int getDistanceEdinting(String one, String two) {
        str1 = one;
        str2 = two;

        int n = str1.length();
        int m = str2.length();

        dp = new int[n + 1][m + 1];

        // инициализация значением -1
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                dp[i][j] = -1;
            }
        }

        // базовые случаи
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        return rec(n, m);
    }

    int rec(int i, int j) {
        if (dp[i][j] != -1)
            return dp[i][j];

        int insert = rec(i, j - 1) + 1;
        int delete = rec(i - 1, j) + 1;
        int replace = rec(i - 1, j - 1) + cost(i, j);

        dp[i][j] = Math.min(insert, Math.min(delete, replace));
        return dp[i][j];
    }

    int cost(int i, int j) {
        return (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
    }
}
