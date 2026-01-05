package by.it.group451004.matyrka.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        instance.getNotUpSeqSize(stream);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        long[] a = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = scanner.nextLong();
        }

        // dp[len] — минимальный возможный последний элемент
        long[] dp = new long[n + 1];
        int[] pos = new int[n + 1];     // индекс элемента
        int[] prev = new int[n + 1];    // для восстановления

        int length = 0;

        for (int i = 1; i <= n; i++) {
            int l = 1, r = length;
            while (l <= r) {
                int mid = (l + r) / 2;
                if (dp[mid] >= a[i])
                    l = mid + 1;
                else
                    r = mid - 1;
            }

            dp[l] = a[i];
            pos[l] = i;
            prev[i] = pos[l - 1];

            if (l > length)
                length = l;
        }

        // восстановление ответа
        int[] answer = new int[length];
        int p = pos[length];
        for (int i = length - 1; i >= 0; i--) {
            answer[i] = p;
            p = prev[p];
        }

        // вывод
        System.out.println(length);
        for (int i = 0; i < length; i++) {
            System.out.print(answer[i] + " ");
        }

        return length;
    }
}
