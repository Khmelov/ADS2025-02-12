package by.bsuir.dsa.csv2025.gr410901.Галицкий;

import java.util.*;
import java.io.*;

public class Solution {
    static final long MOD1 = 1_000_000_007L;
    static final long MOD2 = 1_000_000_009L;
    static final long BASE = 911382323L; // большое случайное простое основание

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String S = sc.nextLine();
        String P = sc.nextLine();
        sc.close();

        int n = S.length();
        int m = P.length();

        if (m > n) {
            System.out.println("-1");
            return;
        }

        // Предпосчитаем степени базы
        long[] pow1 = new long[n + 1];
        long[] pow2 = new long[n + 1];
        pow1[0] = 1; pow2[0] = 1;
        for (int i = 1; i <= n; i++) {
            pow1[i] = pow1[i - 1] * BASE % MOD1;
            pow2[i] = pow2[i - 1] * BASE % MOD2;
        }

        // Хеши строки S
        long[] h1 = new long[n + 1];
        long[] h2 = new long[n + 1];
        for (int i = 0; i < n; i++) {
            h1[i + 1] = (h1[i] * BASE + S.charAt(i)) % MOD1;
            h2[i + 1] = (h2[i] * BASE + S.charAt(i)) % MOD2;
        }

        // Хеш шаблона P
        long ph1 = 0, ph2 = 0;
        for (int i = 0; i < m; i++) {
            ph1 = (ph1 * BASE + P.charAt(i)) % MOD1;
            ph2 = (ph2 * BASE + P.charAt(i)) % MOD2;
        }

        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i <= n - m; i++) {
            long cur1 = (h1[i + m] - h1[i] * pow1[m] % MOD1 + MOD1) % MOD1;
            long cur2 = (h2[i + m] - h2[i] * pow2[m] % MOD2 + MOD2) % MOD2;
            if (cur1 == ph1 && cur2 == ph2) {
                ans.add(i);
            }
        }

        if (ans.isEmpty()) {
            System.out.println("-1");
        } else {
            System.out.println(ans.size());
            for (int i = 0; i < ans.size(); i++) {
                System.out.print(ans.get(i));
                System.out.print(i + 1 < ans.size() ? " " : "\n");
            }
        }
    }
}