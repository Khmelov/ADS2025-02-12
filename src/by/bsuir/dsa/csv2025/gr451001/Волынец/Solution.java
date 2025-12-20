package by.bsuir.dsa.csv2025.gr451001.Волынец;

import java.util.*;
        import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    static int solve(Scanner sc) {
        int WL = sc.nextInt();
        int WR = sc.nextInt();
        int B  = sc.nextInt();
        int n  = sc.nextInt();
        int[] w = new int[n];
        for (int i = 0; i < n; i++) w[i] = sc.nextInt();

        boolean[][] dp = new boolean[WL + 1][WR + 1];
        dp[0][0] = true;

        for (int wi : w) {
            boolean[][] next = new boolean[WL + 1][WR + 1];
            for (int L = 0; L <= WL; L++) {
                for (int R = 0; R <= WR; R++) {
                    if (!dp[L][R]) continue;
                    next[L][R] = true;
                    if (L + wi <= WL) next[L + wi][R] = true;
                    if (R + wi <= WR) next[L][R + wi] = true;
                }
            }
            dp = next;
        }

        int best = 0;
        for (int L = 0; L <= WL; L++) {
            for (int R = 0; R <= WR; R++) {
                if (dp[L][R] && Math.abs(L - R) <= B) {
                    best = Math.max(best, L + R);
                }
            }
        }
        return best;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int ans = solve(sc);
        System.out.println(ans);
    }

    private static int runOnce(String input) {
        Scanner sc = new Scanner(input);
        return solve(sc);
    }

    @Test
    public void allTests() {
        //Case 1
        assertEquals(0, runOnce("0 0 0 3\n1 2 3\n"));

        //Case 2
        assertEquals(5, runOnce("5 0 5 1\n5\n"));

        //Case 3
        assertEquals(7, runOnce("10 3 10 2\n4 7\n"));

        //Case 4
        assertEquals(14, runOnce("10 10 0 4\n3 3 4 4\n"));

        //Case 5
        assertEquals(16, runOnce("10 10 2 4\n6 6 2 2\n"));

        //Case 6
        assertEquals(25, runOnce("7 20 20 5\n7 6 5 4 3\n"));

        //Case 7
        assertEquals(18, runOnce("10 9 3 5\n5 5 4 4 3\n"));

        //Case 8
        assertEquals(1212, runOnce("1000 1000 5 4\n600 600 6 6\n"));

        //Case 9
        assertEquals(60, runOnce("100 10 100 6\n10 10 10 10 10 10\n"));

        //Case 10
        assertEquals(80, runOnce("100 100 1 3\n40 40 40\n"));
    }
}
