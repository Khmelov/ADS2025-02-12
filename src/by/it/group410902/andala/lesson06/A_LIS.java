package by.it.group410902.andala.lesson06;

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

        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // Считываем количество элементов последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt(); // Считываем элементы массива
        }

        int[] dp = new int[n]; // dp[i] — длина LIS, заканчивающейся на m[i]

        // Для каждого элемента вычисляем длину LIS, заканчивающейся на этом элементе
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // Минимальная длина LIS для каждого элемента — 1 (сам элемент)
            for (int j = 0; j < i; j++) {
                // Если предыдущий элемент меньше текущего — можно продолжить возрастающую подпоследовательность
                if (m[j] < m[i]) {
                    // Обновляем dp[i], если нашли более длинную последовательность
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        // Находим максимальное значение в dp — длину максимальной LIS
        int result = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > result) {
                result = dp[i];
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
