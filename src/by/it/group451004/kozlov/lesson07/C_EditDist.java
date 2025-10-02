package by.it.group451004.kozlov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();
        int[][] dp = new int[n + 1][m + 1];

        // Initialize dp matrix
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        // Fill dp matrix
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }

        // Generate edit operations
        StringBuilder result = new StringBuilder();
        int i = n, j = m;

        while (i > 0 || j > 0) {
            if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                result.append("-").append(one.charAt(i - 1)).append(",");
                i--;
            } else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                result.append("+").append(two.charAt(j - 1)).append(",");
                j--;
            } else {
                if (i > 0 && j > 0) {
                    if (one.charAt(i - 1) == two.charAt(j - 1)) {
                        result.append("#,");
                    } else {
                        result.append("~").append(two.charAt(j - 1)).append(",");
                    }
                    i--;
                    j--;
                }
            }
        }

        // No need to reverse the result
        return result.toString();
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
