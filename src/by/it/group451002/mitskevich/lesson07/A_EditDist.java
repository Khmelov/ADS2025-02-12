package by.it.group451002.mitskevich.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    int getDistanceEdinting(String one, String two) {
        int n = one.length(); // длина первой строки
        int m = two.length(); // длина второй строки

        // создаём таблицу (n+1)x(m+1) для хранения промежуточных расстояний
        int[][] dp = new int[n + 1][m + 1];

        // базовые случаи: расстояние до пустой строки
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i; // i удалений
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j; // j вставок
        }

        // основной цикл: заполняем таблицу
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // если символы равны — стоимость замены = 0
                int cost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;

                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1,     // удаление
                                dp[i][j - 1] + 1      // вставка
                        ),
                        dp[i - 1][j - 1] + cost     // замена
                );
            }
        }

        // ответ — в правом нижнем углу таблицы
        return dp[n][m];
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

