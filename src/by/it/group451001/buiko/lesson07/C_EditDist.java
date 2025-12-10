package by.it.group451001.buiko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация базовых случаев
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Заполнение таблицы dp
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Исправлено: добавлена недостающая закрывающая скобка
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }

        // Восстановление пути
        List<String> operations = new ArrayList<>();
        int i = m;
        int j = n;
        while (i > 0 || j > 0) {
            if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                operations.add("+" + two.charAt(j - 1));
                j--;
            } else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + (one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1)) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    operations.add("#");
                } else {
                    operations.add("~" + two.charAt(j - 1));
                }
                i--;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                operations.add("-" + one.charAt(i - 1));
                i--;
            } else {
                if (j == 0) {
                    operations.add("-" + one.charAt(i - 1));
                    i--;
                } else if (i == 0) {
                    operations.add("+" + two.charAt(j - 1));
                    j--;
                }
            }
        }

        Collections.reverse(operations);
        return String.join(",", operations) + ",";
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