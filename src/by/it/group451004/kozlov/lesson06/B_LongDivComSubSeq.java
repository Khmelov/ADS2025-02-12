package by.it.group451004.kozlov.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_LongDivComSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];

        // Читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Динамическое программирование для решения задачи
        int[] dp = new int[n];
        int result = 0;

        for (int i = 0; i < n; i++) {
            dp[i] = 1; // Минимальная длина подпоследовательности — 1
            for (int j = 0; j < i; j++) {
                if (m[i] % m[j] == 0) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            result = Math.max(result, dp[i]);
        }

        return result;
    }
}
