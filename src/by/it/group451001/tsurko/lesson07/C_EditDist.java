package by.it.group451001.tsurko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна с выводом редакционного предписания
*/

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();
        int[][] dp = new int[m + 1][n + 1];
        String[][] ops = new String[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
            ops[i][0] = (i == 0) ? "" : ops[i - 1][0] + "-" + one.charAt(i - 1) + ",";
        }

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
            ops[0][j] = (j == 0) ? "" : ops[0][j - 1] + "+" + two.charAt(j - 1) + ",";
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                if (dp[i - 1][j] + 1 < dp[i][j - 1] + 1 && dp[i - 1][j] + 1 < dp[i - 1][j - 1] + cost) {
                    dp[i][j] = dp[i - 1][j] + 1;
                    ops[i][j] = ops[i - 1][j] + "-" + one.charAt(i - 1) + ",";
                } else if (dp[i][j - 1] + 1 < dp[i - 1][j] + 1 && dp[i][j - 1] + 1 < dp[i - 1][j - 1] + cost) {
                    dp[i][j] = dp[i][j - 1] + 1;
                    ops[i][j] = ops[i][j - 1] + "+" + two.charAt(j - 1) + ",";
                } else {
                    dp[i][j] = dp[i - 1][j - 1] + cost;
                    ops[i][j] = ops[i - 1][j - 1] + (cost == 0 ? "#" : "~" + two.charAt(j - 1)) + ",";
                }
            }
        }

        return ops[m][n];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
