package by.it.group451002.mitskevich.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация базовых случаев
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // удалить все символы
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // вставить все символы
        }

        // Заполнение таблицы расстояний
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1,        // удаление
                                dp[i][j - 1] + 1),       // вставка
                        dp[i - 1][j - 1] + cost            // замена или совпадение
                );
            }
        }

        // Восстановление пути
        StringBuilder result = new StringBuilder();
        int i = m, j = n;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                result.insert(0, "#,");
                i--;
                j--;
            } else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                result.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            } else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                result.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                result.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            }
        }

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
