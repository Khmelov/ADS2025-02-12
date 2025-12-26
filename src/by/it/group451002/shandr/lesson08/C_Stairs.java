package by.it.group451002.shandr.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_Stairs {

    int getMaxSum(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int stairs[] = new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt();
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        if (n == 0) return 0;
        if (n == 1) return Math.max(0, stairs[0]);

        // dp[i] - максимальная сумма на i-й ступеньке
        int[] dp = new int[n];

        // Базовые случаи
        dp[0] = stairs[0];
        if (n > 1) {
            dp[1] = Math.max(stairs[1], stairs[0] + stairs[1]);
        }

        // Заполняем массив dp
        for (int i = 2; i < n; i++) {
            // Можем прийти на i-ю ступеньку либо с (i-1)-й, либо с (i-2)-й
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i];
        }

        int result = dp[n - 1];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res = instance.getMaxSum(stream);
        System.out.println(res);
    }
}