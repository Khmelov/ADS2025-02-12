package by.it.group451002.vysotski.lesson08;

import java.io.FileInputStream;
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

        // dp[i] represents maximum sum achievable reaching step i
        int[] dp = new int[n];

        // Base cases
        dp[0] = stairs[0];
        if (n > 1) {
            dp[1] = Math.max(stairs[1], stairs[0] + stairs[1]);
        }

        // Fill DP table
        for (int i = 2; i < n; i++) {
            // We can reach step i either from step i-1 or step i-2
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i];
        }

        int result = dp[n - 1];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Alternative solution with clearer base cases
    int getMaxSumAlternative(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int stairs[] = new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt();
        }

        if (n == 0) return 0;
        if (n == 1) return stairs[0];

        int[] dp = new int[n + 1];
        dp[0] = 0;           // Starting point (before first step)
        dp[1] = stairs[0];   // First step

        for (int i = 2; i <= n; i++) {
            // We can reach step i from step i-1 or step i-2
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i - 1];
        }

        return dp[n];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res = instance.getMaxSum(stream);
        System.out.println(res);
    }
}