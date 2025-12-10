package by.bsuir.dsa.csv2025.gr410902.Меньшиков;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Scanner;

public class Solution {
    static final long MOD = 1000000007;

    static long binpow(long a, long n) {
        long res = 1;
        a %= MOD;
        while (n > 0) {
            if ((n & 1) == 1) {
                res = (res * a) % MOD;
            }
            a = (a * a) % MOD;
            n >>= 1;
        }
        return res;
    }

    static long modInverse(long x) {
        return binpow(x, MOD - 2);
    }

    static long solve(int n) {
        long[] factorial = new long[2 * n + 1];
        factorial[0] = 1;

        for (int i = 1; i <= 2 * n; i++) {
            factorial[i] = (factorial[i - 1] * i) % MOD;
        }

        long numerator = (factorial[n] * factorial[n]) % MOD;
        long denominator = factorial[2 * n];

        long result = (numerator * modInverse(denominator)) % MOD;

        return result;
    }

    @Test
    public void testAllCases() {
        int[] testInputs = {1, 2, 3, 5, 7, 10, 15, 25, 50, 100};
        long[] expectedOutputs = {500000004L, 166666668L, 850000006L, 3968254L, 951340333L, 497548121L, 555573462L, 362723886L, 122667988L, 599849895L};

        for (int i = 0; i < testInputs.length; i++) {
            assertEquals("Test case " + (i + 1) + " failed for n=" + testInputs[i], expectedOutputs[i], solve(testInputs[i]));
        }
    }

    public static void main(String[] args) {
        System.out.println("Введите n:");

        Scanner sc = new Scanner(System.in);
        System.out.print("n = ");
        int n = sc.nextInt();

        if (n < 1 || n > 100000) {
            System.out.println("Ошибка: n должно быть в диапазоне [1, 100000]");
        } else {
            long result = solve(n);
            System.out.println("Результат: " + result);
        }

        sc.close();
    }
}