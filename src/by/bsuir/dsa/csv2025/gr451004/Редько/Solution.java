package by.bsuir.dsa.csv2025.gr451004.Редько;

import java.util.Scanner;

public class Solution {

    static final long BASE = 31L;
    static final long MOD = (1L << 64);

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("test")) {
            runTests();
            return;
        }

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int q = sc.nextInt();
        sc.nextLine();
        String s = sc.nextLine();

        long[] prefixHash = new long[n + 1];
        long[] powBase = new long[n + 1];
        powBase[0] = 1;

        for (int i = 1; i <= n; i++) {
            prefixHash[i] = prefixHash[i - 1] * BASE + s.charAt(i - 1);
            powBase[i] = powBase[i - 1] * BASE;
        }

        for (int i = 0; i < q; i++) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);

            if (type == 1) {
                int l = Integer.parseInt(parts[1]);
                int r = Integer.parseInt(parts[2]);
                long hash = substringHash(prefixHash, powBase, l, r);
                System.out.println(hash);
            } else if (type == 2) {
                int a = Integer.parseInt(parts[1]);
                int b = Integer.parseInt(parts[2]);
                int c = Integer.parseInt(parts[3]);
                int d = Integer.parseInt(parts[4]);
                long hash1 = substringHash(prefixHash, powBase, a, b);
                long hash2 = substringHash(prefixHash, powBase, c, d);
                System.out.println(hash1 == hash2 ? "YES" : "NO");
            }
        }
        sc.close();
    }

    static long substringHash(long[] prefixHash, long[] powBase, int l, int r) {
        return prefixHash[r] - prefixHash[l - 1] * powBase[r - l + 1];
    }

    static long[] buildPrefixHash(String s) {
        int n = s.length();
        long[] prefixHash = new long[n + 1];
        long[] powBase = new long[n + 1];
        powBase[0] = 1;
        for (int i = 1; i <= n; i++) {
            prefixHash[i] = prefixHash[i - 1] * BASE + s.charAt(i - 1);
            powBase[i] = powBase[i - 1] * BASE;
        }
        return prefixHash;
    }

    static long[] buildPowBase(int n) {
        long[] powBase = new long[n + 1];
        powBase[0] = 1;
        for (int i = 1; i <= n; i++) powBase[i] = powBase[i - 1] * BASE;
        return powBase;
    }

    static void runTests() {
        System.out.println("Запуск тестов...\n");
        int passed = 0;
        int total = 10;

        try {
            testExample1();
            System.out.println("✓ testExample1 - PASSED");
            passed++;
        } catch (AssertionError e) {
            System.out.println("✗ testExample1 - FAILED: " + e.getMessage());
        }

        try {
            testExample2();
            System.out.println("✓ testExample2 - PASSED");
            passed++;
        } catch (AssertionError e) {
            System.out.println("✗ testExample2 - FAILED: " + e.getMessage());
        }

        try {
            testExample3();
            System.out.println("✓ testExample3 - PASSED");
            passed++;
        } catch (AssertionError e) {
            System.out.println("✗ testExample3 - FAILED: " + e.getMessage());
        }

        try {
            testExample4();
            System.out.println("✓ testExample4 - PASSED");
            passed++;
        } catch (AssertionError e) {
            System.out.println("✗ testExample4 - FAILED: " + e.getMessage());
        }

        try {
            testExample5();
            System.out.println("✓ testExample5 - PASSED");
            passed++;
        } catch (AssertionError e) {
            System.out.println("✗ testExample5 - FAILED: " + e.getMessage());
        }

        try {
            testExample6();
            System.out.println("✓ testExample6 - PASSED");
            passed++;
        } catch (AssertionError e) {
            System.out.println("✗ testExample6 - FAILED: " + e.getMessage());
        }

        try {
            testExample7();
            System.out.println("✓ testExample7 - PASSED");
            passed++;
        } catch (AssertionError e) {
            System.out.println("✗ testExample7 - FAILED: " + e.getMessage());
        }

        try {
            testExample8();
            System.out.println("✓ testExample8 - PASSED");
            passed++;
        } catch (AssertionError e) {
            System.out.println("✗ testExample8 - FAILED: " + e.getMessage());
        }

        try {
            testExample9();
            System.out.println("✓ testExample9 - PASSED");
            passed++;
        } catch (AssertionError e) {
            System.out.println("✗ testExample9 - FAILED: " + e.getMessage());
        }

        try {
            testExample10();
            System.out.println("✓ testExample10 - PASSED");
            passed++;
        } catch (AssertionError e) {
            System.out.println("✗ testExample10 - FAILED: " + e.getMessage());
        }

        System.out.println("\nРезультат: " + passed + "/" + total + " тестов пройдено");
        if (passed == total) {
            System.out.println("Все тесты успешно пройдены! ✓");
        }
    }

    static void assertEquals(long expected, long actual) {
        if (expected != actual) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }

    static void assertNotEquals(long val1, long val2) {
        if (val1 == val2) {
            throw new AssertionError("Expected values to be different but both are " + val1);
        }
    }

    static void testExample1() {
        String s = "abcab";
        long[] H = buildPrefixHash(s);
        long[] P = buildPowBase(s.length());
        assertEquals(96354, substringHash(H, P, 1, 3));
        assertNotEquals(substringHash(H, P, 1, 3), substringHash(H, P, 3, 5));
    }

    static void testExample2() {
        String s = "aaaaaa";
        long[] H = buildPrefixHash(s);
        long[] P = buildPowBase(s.length());
        assertEquals(2986048, substringHash(H, P, 2, 5));
        assertEquals(substringHash(H, P, 1, 3), substringHash(H, P, 4, 6));
        assertEquals(2869595232L, substringHash(H, P, 1, 6));
    }

    static void testExample3() {
        String s = "abacaba";
        long[] H = buildPrefixHash(s);
        long[] P = buildPowBase(s.length());
        assertEquals(88986141053L, substringHash(H, P, 1, 7));
        assertEquals(substringHash(H, P, 1, 3), substringHash(H, P, 5, 7));
        assertEquals(96383, substringHash(H, P, 3, 5));
    }

    static void testExample4() {
        String s = "aabb";
        long[] H = buildPrefixHash(s);
        long[] P = buildPowBase(s.length());
        assertEquals(3104, substringHash(H, P, 1, 2));
        assertNotEquals(substringHash(H, P, 1, 2), substringHash(H, P, 3, 4));
    }

    static void testExample5() {
        String s = "xyz";
        long[] H = buildPrefixHash(s);
        long[] P = buildPowBase(s.length());
        assertEquals(119193, substringHash(H, P, 1, 3));
        assertNotEquals(substringHash(H, P, 1, 1), substringHash(H, P, 2, 2));
        assertNotEquals(substringHash(H, P, 2, 3), substringHash(H, P, 1, 2));
    }

    static void testExample6() {
        String s = "bbbbb";
        long[] H = buildPrefixHash(s);
        long[] P = buildPowBase(s.length());
        assertEquals(93521890, substringHash(H, P, 1, 5));
        assertEquals(substringHash(H, P, 1, 3), substringHash(H, P, 2, 4));
    }

    static void testExample7() {
        String s = "abcdefgh";
        long[] H = buildPrefixHash(s);
        long[] P = buildPowBase(s.length());
        assertEquals(2987074, substringHash(H, P, 1, 4));
        assertNotEquals(substringHash(H, P, 1, 4), substringHash(H, P, 5, 8));
        assertEquals(3110210, substringHash(H, P, 5, 8));
    }

    static void testExample8() {
        String s = "ababab";
        long[] H = buildPrefixHash(s);
        long[] P = buildPowBase(s.length());
        assertEquals(2870519715L, substringHash(H, P, 1, 6));
        assertEquals(substringHash(H, P, 1, 2), substringHash(H, P, 3, 4));
        assertNotEquals(substringHash(H, P, 2, 5), substringHash(H, P, 1, 4));
    }

    static void testExample9() {
        String s = "racecar";
        long[] H = buildPrefixHash(s);
        long[] P = buildPowBase(s.length());
        assertEquals(104046983011L, substringHash(H, P, 1, 7));
        assertNotEquals(substringHash(H, P, 1, 3), substringHash(H, P, 5, 7));
    }

    static void testExample10() {
        String s = "hello";
        long[] H = buildPrefixHash(s);
        long[] P = buildPowBase(s.length());
        assertEquals(99162322, substringHash(H, P, 1, 5));
        assertNotEquals(substringHash(H, P, 1, 2), substringHash(H, P, 4, 5));
        assertEquals(substringHash(H, P, 2, 4), substringHash(H, P, 2, 4));
    }
}
