package by.it.group410902.latipov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int m = one.length();
        int n = two.length();

        // Создаем матрицу для динамического программирования
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация базовых случаев
        // dp[i][0] = i - чтобы преобразовать первые i символов в пустую строку (удаление)
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        // dp[0][j] = j - чтобы преобразовать пустую строку в первые j символов (вставка)
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Заполняем матрицу по строкам
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Если символы совпадают, стоимость не увеличивается
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Выбираем минимальную стоимость из трех возможных операций:
                    int deleteCost = dp[i - 1][j] + 1;     // удаление символа из первой строки
                    int insertCost = dp[i][j - 1] + 1;     // вставка символа в первую строку
                    int replaceCost = dp[i - 1][j - 1] + 1; // замена символа

                    dp[i][j] = Math.min(Math.min(deleteCost, insertCost), replaceCost);
                }
            }
        }

        return dp[m][n];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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