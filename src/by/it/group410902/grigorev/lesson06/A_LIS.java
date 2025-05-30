package by.it.group410902.grigorev.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_LIS {
    public static void main(String[] args) throws FileNotFoundException {
        // Получаем поток ввода из файла "dataA.txt"
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");

        // Создаем экземпляр класса
        A_LIS instance = new A_LIS();

        // Определяем длину наибольшей возрастающей последовательности
        int result = instance.getSeqSize(stream);

        // Выводим результат
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем количество элементов
        int n = scanner.nextInt();
        int[] m = new int[n];

        // Заполняем массив входными значениями
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Массив dp[i] хранит длину наибольшей возрастающей подпоследовательности, заканчивающейся на i
        int[] dp = new int[n];
        int max = 0;

        // Заполняем массив dp
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (m[j] < m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                }
            }
            // Обновляем максимальную длину
            if (dp[i] > max) {
                max = dp[i];
            }
        }

        return max;
    }
}

