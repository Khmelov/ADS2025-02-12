package by.it.group451001.khokhlov.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        instance.getNotUpSeqSize(stream);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // dp[i] — длина наибольшей невозрастающей подпоследовательности, заканчивающейся на i-м элементе
        int[] dp = new int[n];
        // prev[i] — индекс предыдущего элемента в подпоследовательности
        int[] prev = new int[n];
        Arrays.fill(dp, 1);
        Arrays.fill(prev, -1);

        int maxLength = 1;
        int lastIndex = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (a[j] >= a[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }

            if (dp[i] > maxLength) {
                maxLength = dp[i];
                lastIndex = i;
            }
        }

        // Восстановим путь
        List<Integer> sequence = new ArrayList<>();
        while (lastIndex != -1) {
            sequence.add(lastIndex + 1); // +1 для индексации с 1
            lastIndex = prev[lastIndex];
        }

        Collections.reverse(sequence);

        // Вывод результата
        System.out.println(maxLength);
        for (int idx : sequence) {
            System.out.print(idx + " ");
        }
        System.out.println();

        return maxLength;
    }
}
