package by.it.group410902.grigorev.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_LongDivComSubSeq {
    public static void main(String[] args) throws FileNotFoundException {
        // Получаем поток ввода из файла "dataB.txt"
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");

        // Создаем экземпляр класса
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();

        // Определяем длину наибольшей делимой последовательности
        int result = instance.getDivSeqSize(stream);

        // Выводим результат
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем количество элементов
        int n = scanner.nextInt();
        int[] m = new int[n];

        // Заполняем массив входными значениями
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Массив dp[i] хранит длину наибольшей подпоследовательности, заканчивающейся на i,
        // где каждый следующий элемент делится на предыдущий
        int[] dp = new int[n];
        int max = 0;

        // Заполняем массив dp
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (m[i] % m[j] == 0 && dp[j] + 1 > dp[i]) {
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
