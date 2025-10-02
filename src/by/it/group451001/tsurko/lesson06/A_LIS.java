package by.it.group451001.tsurko.lesson06;

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
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];

        // читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // массив для хранения длины наибольшей возрастающей подпоследовательности
        int[] dp = new int[n];

        // каждая позиция сама по себе является подпоследовательностью длиной 1
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }

        // проверяем каждую пару элементов и обновляем dp-массив
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (m[j] < m[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        // находим максимальную длину подпоследовательности
        int result = 0;
        for (int i = 0; i < n; i++) {
            result = Math.max(result, dp[i]);
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}