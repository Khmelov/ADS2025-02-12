package by.it.group451003.sorokin.lesson06;

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

        // Чтение длины последовательности
        int n = scanner.nextInt();
        int[] sequence = new int[n];

        // Чтение последовательности
        for (int i = 0; i < n; i++) {
            sequence[i] = scanner.nextInt();
        }

        // Массив для хранения длин НКП, заканчивающихся в каждой позиции
        int[] dp = new int[n];

        // Заполняем массив dp: изначально каждая позиция - это подпоследовательность длины 1
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }

        // Вычисляем длину НКП для каждой позиции
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (sequence[i] % sequence[j] == 0 && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                }
            }
        }

        // Находим максимальное значение в массиве dp
        int maxLength = 0;
        for (int length : dp) {
            if (length > maxLength) {
                maxLength = length;
            }
        }

        return maxLength;
    }
}