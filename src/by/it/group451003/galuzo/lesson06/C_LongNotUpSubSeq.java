package by.it.group451003.galuzo.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Чтение входных данных
        int n = scanner.nextInt();
        int[] sequence = new int[n];
        for (int i = 0; i < n; i++) {
            sequence[i] = scanner.nextInt();
        }

        // Инициализация массивов для динамического программирования
        int[] dp = new int[n];  // Длины подпоследовательностей
        int[] prev = new int[n]; // Для восстановления подпоследовательности
        Arrays.fill(dp, 1);
        Arrays.fill(prev, -1);

        // Заполнение массивов
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (sequence[j] >= sequence[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
        }

        // Находим максимальную длину
        int maxLength = 0;
        for (int length : dp) {
            if (length > maxLength) {
                maxLength = length;
            }
        }

        return maxLength;
    }
}