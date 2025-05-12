package by.it.group451001.khomenkov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    private int[][] memo;

    private int calculateDistance(String one, String two, int i, int j) {
        if (i == 0) {
            return j;
        }
        if (j == 0) {
            return i;
        }
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            memo[i][j] = calculateDistance(one, two, i - 1, j - 1);
        } else {
            int insert = calculateDistance(one, two, i, j - 1) + 1;
            int delete = calculateDistance(one, two, i - 1, j) + 1;
            int replace = calculateDistance(one, two, i - 1, j - 1) + 1;
            memo[i][j] = Math.min(Math.min(insert, delete), replace);
        }
        return memo[i][j];
    }

    int getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();
        memo = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                memo[i][j] = -1;
            }
        }
        return calculateDistance(one, two, m, n);
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}