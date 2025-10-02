package by.it.group410902.sinyutin.lesson06;
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

    int getNotUpSeqSize(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++)
            m[i] = scanner.nextInt();

        int maxLength = 0;
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1;

            for (int j = 0; j < i; j++)
                if ((m[j] >= m[i]) && (dp[i] < dp[j] + 1))
                    dp[i] = dp[j] + 1;

            if (maxLength < dp[i])
                maxLength = dp[i];
        }
        return maxLength;
    }
}
