package by.bsuir.dsa.csv2025.gr451001.Волынец;

import java.util.*;

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
        if (args != null && args.length > 0 && "test".equalsIgnoreCase(args[0])) {
            int fails = runTests();
            if (fails == 0) {
                System.out.println("\nRESULT: OK (all tests passed)");
                System.exit(0);
            } else {
                System.out.println("\nRESULT: FAIL (" + fails + " failed)");
                System.exit(1);
            }
            return;
        }

        Scanner sc = new Scanner(System.in);
        int ans = solve(sc);
        System.out.println(ans);
    }

    static int runTests() {
        int fails = 0;

        List<TestCase> cases = List.of(
                new TestCase("0 0 0 3\n1 2 3\n", "0", "Case 1"),
                new TestCase("5 0 5 1\n5\n", "5", "Case 2"),
                new TestCase("10 3 10 2\n4 7\n", "7", "Case 3"),
                new TestCase("10 10 0 4\n3 3 4 4\n", "14", "Case 4"),
                new TestCase("10 10 2 4\n6 6 2 2\n", "16", "Case 5"),
                new TestCase("7 20 20 5\n7 6 5 4 3\n", "25", "Case 6"),
                new TestCase("10 9 3 5\n5 5 4 4 3\n", "18", "Case 7"),
                new TestCase("1000 1000 5 4\n600 600 6 6\n", "1212", "Case 8"),
                new TestCase("100 10 100 6\n10 10 10 10 10 10\n", "60", "Case 9"),
                new TestCase("100 100 1 3\n40 40 40\n", "80", "Case 10")
        );

        System.out.println("Running " + cases.size() + " tests...\n");

        int idx = 1;
        for (TestCase tc : cases) {
            String actual = String.valueOf(runOnce(tc.input)).trim();
            boolean ok = actual.equals(tc.expected.trim());
            if (ok) {
                System.out.println(tc.name + ": PASS");
            } else {
                System.out.println(tc.name + ": FAIL");
                System.out.println("Input:");
                System.out.println(tc.input);
                System.out.println("Expected: " + tc.expected);
                System.out.println("Actual:   " + actual);
                fails++;
            }
            idx++;
        }

        return fails;
    }

    static int runOnce(String input) {
        Scanner sc = new Scanner(input);
        return solve(sc);
    }
    static class TestCase {
        final String input;
        final String expected;
        final String name;
        TestCase(String input, String expected, String name) {
            this.input = input;
            this.expected = expected;
            this.name = name;
        }
    }
}
