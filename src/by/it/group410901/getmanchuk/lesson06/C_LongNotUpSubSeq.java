package by.it.group410901.getmanchuk.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // dp[i] — длина наибольшей невозрастающей подпоследовательности, заканчивающейся в i
        int[] dp = new int[n];
        // prev[i] — индекс предыдущего элемента в подпоследовательности для восстановления пути
        int[] prev = new int[n];

        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            prev[i] = -1;
            for (int j = 0; j < i; j++) {
                if (a[j] >= a[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
        }

        // находим индекс конца наибольшей подпоследовательности
        int maxLength = 0;
        int lastIndex = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                lastIndex = i;
            }
        }

        // восстанавливаем путь по prev[]
        int[] indices = new int[maxLength];
        int pos = maxLength - 1;
        while (lastIndex != -1) {
            indices[pos--] = lastIndex + 1; // индексы с 1
            lastIndex = prev[lastIndex];
        }

        // Вывод
        System.out.println(maxLength);
        for (int i = 0; i < maxLength; i++) {
            System.out.print(indices[i] + " ");
        }
        System.out.println();

        return maxLength;
    }
}