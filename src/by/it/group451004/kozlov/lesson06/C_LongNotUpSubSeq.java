package by.it.group451004.kozlov.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.println(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];

        // Читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Реализация задачи методом динамического программирования
        int[] dp = new int[n];
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // Минимальная длина подпоследовательности — 1
            prev[i] = -1; // Предшествующий элемент отсутствует
        }

        int maxLength = 0;
        int maxIndex = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (m[i] <= m[j] && dp[i] < dp[j] + 1) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                maxIndex = i;
            }
        }

        // Восстанавливаем индексы подпоследовательности
        List<Integer> indices = new ArrayList<>();
        while (maxIndex != -1) {
            indices.add(maxIndex + 1); // Индексы начинаются с 1
            maxIndex = prev[maxIndex];
        }

        // Печатаем длину подпоследовательности
        System.out.println(maxLength);

        // Печатаем индексы подпоследовательности в обратном порядке
        for (int i = indices.size() - 1; i >= 0; i--) {
            System.out.print(indices.get(i) + " ");
        }

        return maxLength;
    }
}
