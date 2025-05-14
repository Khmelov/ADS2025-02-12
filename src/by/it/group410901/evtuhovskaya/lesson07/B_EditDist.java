package by.it.group410901.evtuhovskaya.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();

        int[][] dp = new int[m + 1][n + 1];

        // Инициализация базовых случаев
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // Для преобразования в пустую строку
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // Для преобразования из пустой строки
        }

        // Заполняем матрицу итеративно
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    // Если символы совпадают, берем значение по диагонали
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Иначе выбираем минимальное из трех вариантов + 1
                    dp[i][j] = 1 + Math.min(
                            Math.min(dp[i][j - 1],   // Вставка
                                    dp[i - 1][j]),   // Удаление
                            dp[i - 1][j - 1]         // Замена
                    );
                }
            }
        }

        return  dp[m][n];
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