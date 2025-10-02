package by.it.group410902.grigorev.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_EditDist {
    int getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();
        int[][] dp = new int[m+1][n+1];

        // Логика идентична коду A_EditDist.java
        for(int i = 0; i <= m; i++) {
            for(int j = 0; j <= n; j++) {
                if(i == 0) dp[i][j] = j;
                else if(j == 0) dp[i][j] = i;
                else if(one.charAt(i-1) == two.charAt(j-1)) dp[i][j] = dp[i-1][j-1];
                else dp[i][j] = 1 + Math.min(Math.min(dp[i][j-1], dp[i-1][j]), dp[i-1][j-1]);
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Получаем входные данные
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);

        // Выводим расстояние редактирования для трех пар строк
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
