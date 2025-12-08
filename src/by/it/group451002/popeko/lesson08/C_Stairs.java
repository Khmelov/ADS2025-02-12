package by.it.group451002.popeko.lesson08;

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
        if (n == 1) return stairs[0];

        // Массив для динамического программирования
        int[] dp = new int[n + 1];

        // Базовые случаи
        dp[0] = 0;                    // старт
        dp[1] = stairs[0];           // первая ступенька

        // Заполняем массив dp
        for (int i = 2; i <= n; i++) {
            // Максимум из: пришли с i-1 ступеньки или с i-2 ступеньки
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i - 1];
        }

        int result = dp[n];

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