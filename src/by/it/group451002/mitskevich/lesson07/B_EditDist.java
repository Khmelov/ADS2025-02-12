package by.it.group451002.mitskevich.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        int n = one.length(); // длина первой строки
        int m = two.length(); // длина второй строки

        // создаём таблицу (n+1) x (m+1) для хранения промежуточных результатов
        int[][] dp = new int[n + 1][m + 1];

        // Заполняем первую строку и первый столбец
        // Преобразование строки в пустую: i удалений или j вставок
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        // Заполнение таблицы: строка за строкой
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Если символы равны — замена не требуется (стоимость 0)
                int cost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;

                // Минимум из трёх операций:
                // удаление, вставка, замена
                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1,     // удаление
                                dp[i][j - 1] + 1      // вставка
                        ),
                        dp[i - 1][j - 1] + cost     // замена
                );
            }
        }

        // Ответ находится в правом нижнем углу таблицы
        return dp[n][m];
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
