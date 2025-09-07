package by.it.group451001.yarkovich.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна (итерационное решение)
*/

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        // dp[i][j] — расстояние редактирования между first i символами строки one и first j символами строки two
        int[][] dp = new int[n + 1][m + 1];

        // Инициализация первых строк и столбцов
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i; // удаление всех i символов
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j; // вставка всех j символов
        }

        // Заполнение таблицы
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1,      // удаление
                                dp[i][j - 1] + 1       // вставка
                        ),
                        dp[i - 1][j - 1] + cost      // замена
                );
            }
        }

        return dp[n][m];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // ab ab → 0
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // short ports → 3
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // distance editing → 5
    }
}
