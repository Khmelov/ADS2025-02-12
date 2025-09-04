package by.it.group451002.andreev.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Класс A_LIS реализует поиск наибольшей возрастающей подпоследовательности (Longest Increasing Subsequence - LIS)
 * с использованием динамического программирования.
 */
public class A_LIS {

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataA.txt"
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();

        // Вычисляем длину наибольшей возрастающей подпоследовательности
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    /**
     * Метод getSeqSize читает массив из входного потока и определяет длину LIS.
     * @param stream входной поток данных
     * @return длина наибольшей возрастающей подпоследовательности
     * @throws FileNotFoundException если файл не найден
     */
    int getSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем размер массива
        int n = scanner.nextInt();
        int[] m = new int[n];

        // Заполняем массив данными
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Массив dp, где dp[i] хранит длину наибольшей возрастающей подпоследовательности,
        // заканчивающейся на индексе i
        int[] dp = new int[n];
        int maxLength = 1;

        // Заполняем массив dp
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // Минимальная длина подпоследовательности — 1
            for (int j = 0; j < i; j++) {
                // Если текущий элемент больше предыдущего, обновляем dp[i]
                if (m[j] < m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                }
            }
            // Обновляем максимальную длину LIS
            maxLength = Math.max(maxLength, dp[i]);
        }

        return maxLength;
    }
}
