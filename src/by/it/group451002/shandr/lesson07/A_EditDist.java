package by.it.group451002.shandr.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
Дано:
    Три пары непустых строк длиной не более 100 символов.
Необходимо:
    Для каждой пары строк вывести расстояние редактирования (Левенштейна)
    методом динамического программирования.
*/

public class A_EditDist {

    /**
     * Возвращает edit distance (Левенштейна) между строками one и two.
     */
    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        // dp[i][j] = расстояние редактирования между
        // префиксом one[0..i-1] и two[0..j-1]
        int[][] dp = new int[n + 1][m + 1];

        // базовые случаи: пустая строка vs непустая
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;  // i удалений
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;  // j вставок
        }

        // заполнение таблицы
        for (int i = 1; i <= n; i++) {
            char c1 = one.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char c2 = two.charAt(j - 1);
                int cost = (c1 == c2) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1,      // удаление из one
                                dp[i][j - 1] + 1       // вставка в one (или удаление из two)
                        ),
                        dp[i - 1][j - 1] + cost    // замена (или совпадение)
                );
            }
        }

        return dp[n][m];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        // читаем и решаем три задачи подряд
        System.out.println(instance.getDistanceEdinting(
                scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(
                scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(
                scanner.nextLine(), scanner.nextLine()));
    }
}