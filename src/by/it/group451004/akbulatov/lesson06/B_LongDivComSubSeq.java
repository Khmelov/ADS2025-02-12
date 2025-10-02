package by.it.group451004.akbulatov.lesson06;
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

    int getDivSeqSize(InputStream stream) {
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
                if ((m[i] % m[j] == 0) && (dp[i] < dp[j] + 1))
                    dp[i] = dp[j] + 1;

            if (maxLength < dp[i])
                maxLength = dp[i];
        }

        return maxLength;
    }
}