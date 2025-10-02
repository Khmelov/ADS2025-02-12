package by.it.group410901.getmanchuk.lesson06;

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
        int n = scanner.nextInt();      // длина массива
        int[] m = new int[n];           // сам массив
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // dp[i] — максимальная длина кратной подпоследовательности, заканчивающейся на m[i]
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // каждая сама по себе — подпоследовательность длины 1
            for (int j = 0; j < i; j++) {
                if (m[i] % m[j] == 0 && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                }
            }
        }

        // находим максимум в dp[]
        int result = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > result) {
                result = dp[i];
            }
        }

        return result;
    }
}