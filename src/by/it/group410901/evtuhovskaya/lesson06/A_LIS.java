package by.it.group410901.evtuhovskaya.lesson06;

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

    public int getSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем размер массива
        int n = scanner.nextInt();
        int[] m = new int[n];

        // Читаем массив
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Массив для хранения длин LIS
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // каждая сама по себе — LIS длины 1
        }

        // Динамическое программирование
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (m[j] < m[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        // Ищем максимум в dp
        int result = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > result) {
                result = dp[i];
            }
        }

        return result;
    }
}
