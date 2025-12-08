package by.it.group451003.sorokin.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Для хранения длин подпоследовательностей
        int[] dp = new int[n];
        // Для восстановления подпоследовательности
        int[] prev = new int[n];

        // Инициализация
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            prev[i] = -1;
            for (int j = 0; j < i; j++) {
                if (m[j] >= m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
        }

        // Находим максимальную длину и последний индекс
        int maxLength = 0;
        int lastIndex = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                lastIndex = i;
            }
        }

        // Восстанавливаем подпоследовательность
        List<Integer> indices = new ArrayList<>();
        int current = lastIndex;
        while (current != -1) {
            indices.add(current + 1); // +1 для индексации с 1
            current = prev[current];
        }
        Collections.reverse(indices);

        // Выводим результат
        System.out.println(maxLength);
        for (int i = 0; i < indices.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(indices.get(i));
        }
        System.out.println();

        return maxLength;
    }
}
