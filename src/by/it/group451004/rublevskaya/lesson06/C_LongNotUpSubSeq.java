package by.it.group451004.rublevskaya.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
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

        // Общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];

        // Чтение всей последовательности
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Массив для хранения длин подпоследовательностей
        int[] dp = new int[n];
        // Массив для восстановления индексов
        int[] prev = new int[n];
        // Список для хранения текущей наибольшей подпоследовательности
        ArrayList<Integer> sequence = new ArrayList<>();

        // Инициализация
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            prev[i] = -1;
        }

        // Заполнение массива dp
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (m[j] >= m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
        }

        // Находим максимальное значение в массиве dp
        int maxLength = 0;
        int lastIndex = -1;
        for (int i = 0; i < n; i++) {
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                lastIndex = i;
            }
        }

        // Восстановление подпоследовательности
        while (lastIndex != -1) {
            sequence.add(lastIndex + 1); // Индексация с 1
            lastIndex = prev[lastIndex];
        }

        // Вывод результата
        System.out.println(maxLength);
        for (int i = sequence.size() - 1; i >= 0; i--) {
            System.out.print(sequence.get(i) + " ");
        }

        return maxLength;
    }
}