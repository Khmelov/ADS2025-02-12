package by.it.group410901.kliaus.lesson06;

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

        // Чтение входных данных
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Динамическое программирование: создаём массив для хранения длин подпоследовательностей
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1;  // Каждый элемент сам по себе является подпоследовательностью длины 1
        }

        // Основной цикл для нахождения LIS
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (m[i] > m[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1); // Обновляем dp[i] если нашли большую подпоследовательность
                }
            }
        }

        // Ответом будет максимальная длина подпоследовательности
        int result = 0;
        for (int i = 0; i < n; i++) {
            result = Math.max(result, dp[i]);
        }

        return result;
    }
}
