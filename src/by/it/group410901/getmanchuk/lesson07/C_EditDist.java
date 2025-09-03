package by.it.group410901.getmanchuk.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Stack;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();

        // Создаем матрицу для хранения расстояний
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация базовых случаев
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Заполнение матрицы
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j] + 1,     // Удаление
                                    dp[i][j - 1] + 1),    // Вставка
                            dp[i - 1][j - 1] + 1           // Замена
                    );
                }
            }
        }

        // Восстановление операций
        Stack<String> operations = new Stack<>();
        int i = m, j = n;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                operations.push("#"); // Совпадение
                i--;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                operations.push("-" + one.charAt(i - 1)); // Удаление
                i--;
            } else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                operations.push("+" + two.charAt(j - 1)); // Вставка
                j--;
            } else if (i > 0 && j > 0) {
                operations.push("~" + two.charAt(j - 1)); // Замена
                i--;
                j--;
            }
        }

        // Формирование результата
        StringBuilder result = new StringBuilder();
        while (!operations.isEmpty()) {
            result.append(operations.pop()).append(",");
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