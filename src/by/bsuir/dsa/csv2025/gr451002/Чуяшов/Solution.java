package by.bsuir.dsa.csv2025.gr451002.Чуяшов;

import java.util.*;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while (t-- > 0) {
            int n = sc.nextInt();
            int xorSum = 0;
            int countOnes = 0;
            boolean hasGreaterThanOne = false;

            for (int i = 0; i < n; i++) {
                int a = sc.nextInt();
                xorSum ^= a;
                if (a == 1) countOnes++;
                if (a > 1) hasGreaterThanOne = true;
            }

            boolean allZeroOrOne = !hasGreaterThanOne;

            String winner;
            if (allZeroOrOne) {
                winner = (countOnes % 2 == 0) ? "First" : "Second";
            } else {
                winner = (xorSum != 0) ? "First" : "Second";
            }
            System.out.println(winner);
        }
    }
}