package by.it.group410902.harkavy.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна

Дано:
    Три пары непустых строк длиной не более 100 символов.

Необходимо:
    Итерационно (методом динамического программирования) вычислить расстояние
    редактирования (Левенштейна) для каждой пары строк.
*/

public class B_EditDist {

    /**
     * Итеративно (bottom-up) вычисляет edit distance (Левенштейна)
     * между строками one и two.
     */
    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();
        // dp[i][j] = расстояние редактирования между префиксом one[0..i-1] и two[0..j-1]
        int[][] dp = new int[n + 1][m + 1];

        // Базовые случаи: сравнение с пустой строкой
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;  // i удалений
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;  // j вставок
        }

        // Вычисляем DP-таблицу
        for (int i = 1; i <= n; i++) {
            char c1 = one.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char c2 = two.charAt(j - 1);
                int cost = (c1 == c2) ? 0 : 1;
                // минимальная стоимость из трёх операций:
                // удаление, вставка, замена (или совпадение)
                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1,      // удаление c1
                                dp[i][j - 1] + 1       // вставка c2
                        ),
                        dp[i - 1][j - 1] + cost    // замена или совпадение
                );
            }
        }

        return dp[n][m];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        // Три пары строк подряд
        System.out.println(instance.getDistanceEdinting(
                scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(
                scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(
                scanner.nextLine(), scanner.nextLine()));
    }
}