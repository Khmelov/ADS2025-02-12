package by.it.group410901.sadouski.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    private int[][] dp;

    int getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();
        dp = new int[m + 1][n + 1];

        // Инициализация массива dp значением -1 (не вычислено)
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j] = -1;
            }
        }

        return calculateDistance(one, two, m, n);
    }

    private int calculateDistance(String one, String two, int i, int j) {
        if (dp[i][j] != -1) {
            return dp[i][j];
        }

        if (i == 0) {
            dp[i][j] = j;
            return j;
        }
        if (j == 0) {
            dp[i][j] = i;
            return i;
        }

        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            dp[i][j] = calculateDistance(one, two, i - 1, j - 1);
        } else {
            int insert = calculateDistance(one, two, i, j - 1);
            int delete = calculateDistance(one, two, i - 1, j);
            int replace = calculateDistance(one, two, i - 1, j - 1);
            dp[i][j] = 1 + Math.min(Math.min(insert, delete), replace);
        }

        return dp[i][j];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}