package by.it.group410901.getmanchuk.lesson06;

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
        int n = scanner.nextInt();      // читаем длину массива
        int[] m = new int[n];           // сам массив чисел
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();   // заполняем массив
        }

        // Массив для хранения длины LIS, заканчивающейся на i-м элементе
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // минимальная длина LIS — сам элемент
            for (int j = 0; j < i; j++) {
                if (m[j] < m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                }
            }
        }

        // Найдём максимальное значение в dp[] — это и будет длина LIS
        int result = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > result) {
                result = dp[i];
            }
        }

        return result;
    }
}
