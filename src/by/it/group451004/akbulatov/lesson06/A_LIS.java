package by.it.group451004.akbulatov.lesson06;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_LIS {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++)
            m[i] = scanner.nextInt();
        scanner.close();

        int maxLength = 0;
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1;

            for (int j = 0; j < i; j++)
                if ((m[j] < m[i]) && (dp[i] < dp[j] + 1))
                    dp[i] = dp[j] + 1;

            if (maxLength < dp[i])
                maxLength = dp[i];
        }
        return maxLength;
    }
}