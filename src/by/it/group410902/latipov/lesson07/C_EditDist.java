package by.it.group410902.latipov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int m = one.length();
        int n = two.length();

        // Создаем матрицу для динамического программирования
        int[][] dp = new int[m + 1][n + 1];
        char[][] operations = new char[m + 1][n + 1];

        // Инициализация базовых случаев
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
            operations[i][0] = '-'; // удаление
        }

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
            operations[0][j] = '+'; // вставка
        }

        // Заполняем матрицу
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                    operations[i][j] = '#'; // копирование/совпадение
                } else {
                    int deleteCost = dp[i - 1][j] + 1;
                    int insertCost = dp[i][j - 1] + 1;
                    int replaceCost = dp[i - 1][j - 1] + 1;

                    dp[i][j] = Math.min(Math.min(deleteCost, insertCost), replaceCost);

                    // Определяем, какая операция была использована
                    if (dp[i][j] == deleteCost) {
                        operations[i][j] = '-'; // удаление
                    } else if (dp[i][j] == insertCost) {
                        operations[i][j] = '+'; // вставка
                    } else {
                        operations[i][j] = '~'; // замена
                    }
                }
            }
        }

        // Восстанавливаем последовательность операций
        StringBuilder result = new StringBuilder();
        int i = m, j = n;

        while (i > 0 || j > 0) {
            char op = operations[i][j];

            switch (op) {
                case '#': // совпадение
                    result.append("#,");
                    i--;
                    j--;
                    break;
                case '~': // замена
                    result.append("~").append(two.charAt(j - 1)).append(",");
                    i--;
                    j--;
                    break;
                case '+': // вставка
                    result.append("+").append(two.charAt(j - 1)).append(",");
                    j--;
                    break;
                case '-': // удаление
                    result.append("-").append(one.charAt(i - 1)).append(",");
                    i--;
                    break;
            }
        }

        return result.toString();
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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