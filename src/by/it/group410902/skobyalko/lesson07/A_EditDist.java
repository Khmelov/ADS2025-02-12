package by.it.group410902.skobyalko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    private int[][] memo;

    int getDistanceEdinting(String one, String two) {
        // !!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length();
        int m = two.length();
        memo = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                memo[i][j] = -1;  // инициализация массива мемоизации
            }
        }
        int result = dp(one, two, n, m);
        /// !!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Рекурсивный метод с мемоизацией для вычисления расстояния редактирования
    private int dp(String one, String two, int i, int j) {
        if (i == 0) return j;
        if (j == 0) return i;

        if (memo[i][j] != -1) return memo[i][j];

        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            memo[i][j] = dp(one, two, i - 1, j - 1);
        } else {
            int insert = dp(one, two, i, j - 1) + 1;
            int delete = dp(one, two, i - 1, j) + 1;
            int replace = dp(one, two, i - 1, j - 1) + 1;
            memo[i][j] = Math.min(insert, Math.min(delete, replace));
        }
        return memo[i][j];
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
