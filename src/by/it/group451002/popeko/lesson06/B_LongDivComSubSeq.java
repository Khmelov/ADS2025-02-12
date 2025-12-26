package by.it.group451002.popeko.lesson06;

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

        // Читаем размер массива
        int n = scanner.nextInt();
        int[] m = new int[n];

        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // dp[i] - длина наибольшей кратной подпоследовательности, заканчивающейся на m[i]
        int[] dp = new int[n];
        int maxLength = 1;

        for (int i = 0; i < n; i++) {
            dp[i] = 1; // Минимальная длина = 1 (сам элемент)
            for (int j = 0; j < i; j++) {
                if (m[i] % m[j] == 0) { // Кратность
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }

        return maxLength;
    }
}
