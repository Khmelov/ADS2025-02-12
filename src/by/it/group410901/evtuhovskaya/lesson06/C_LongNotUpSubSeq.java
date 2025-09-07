package by.it.group410901.evtuhovskaya.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    public int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        int[] dp = new int[n];       // dp[i] — длина подпоследовательности, заканчивающейся на i
        int[] prev = new int[n];     // prev[i] — откуда пришли в dp[i]
        Arrays.fill(dp, 1);
        Arrays.fill(prev, -1);

        int maxLen = 1;
        int lastIndex = 0;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (m[j] >= m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                lastIndex = i;
            }
        }

        // Восстановим индексы
        List<Integer> sequence = new ArrayList<>();
        int cur = lastIndex;
        while (cur != -1) {
            sequence.add(cur + 1); // +1, т.к. индексация с 1
            cur = prev[cur];
        }

        Collections.reverse(sequence);
        System.out.println(maxLen);
        for (int index : sequence) {
            System.out.print(index + " ");
        }
        System.out.println();

        return maxLen;
    }
}
