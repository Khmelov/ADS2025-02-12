package by.it.group451003.sorokin.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_LIS {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] sequence = new int[n];

        for (int i = 0; i < n; i++) {
            sequence[i] = scanner.nextInt();
        }

        // Массив для хранения длин НВП, заканчивающихся в каждой позиции
        int[] dp = new int[n];

        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }

        // Вычисляем длину НВП для каждой позиции
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (sequence[j] < sequence[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                }
            }
        }

        int maxLength = 0;
        for (int length : dp) {
            if (length > maxLength) {
                maxLength = length;
            }
        }

        return maxLength;
    }
}