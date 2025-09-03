package by.it.group451002.stsefanovich.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_LongNotUpSubSeq {

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // dp[i] stores the length of the longest non-increasing subsequence
        // that ends at index i
        int[] dp = new int[n];
        // prev[i] stores the previous index in the subsequence ending at i
        int[] prev = new int[n];

        // Initialize arrays
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // Each element is a subsequence of length 1
            prev[i] = -1; // No previous element initially
        }

        // Find the longest non-increasing subsequence
        int maxLength = 1; // Length of longest subsequence
        int maxIndex = 0;  // Index where longest subsequence ends

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Check if we can extend the subsequence ending at j
                if (m[j] >= m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            // Update maximum length and ending index
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                maxIndex = i;
            }
        }

        // Reconstruct the subsequence indices
        int[] indices = new int[maxLength];
        int currentIndex = maxIndex;
        for (int i = maxLength - 1; i >= 0; i--) {
            indices[i] = currentIndex + 1; // Convert to 1-based indexing
            currentIndex = prev[currentIndex];
        }

        // Print results
        System.out.println(maxLength);
        for (int i = 0; i < maxLength; i++) {
            System.out.print(indices[i] + " ");
        }
        System.out.println();

        return maxLength;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
    }
}