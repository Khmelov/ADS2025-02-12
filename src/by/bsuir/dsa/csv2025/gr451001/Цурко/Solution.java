package by.bsuir.dsa.csv2025.gr451001.Цурко;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class Solution {

    // Manacher for even-length palindromes: d[i] = radius of even palindrome centered between i-1 and i
    // Palindrome spans [i - d[i], i + d[i) ; length = 2*d[i]
    static int[] manacherEven(String s) {
        int n = s.length();
        int[] d = new int[n];
        int l = 0, r = -1;
        for (int i = 0; i < n; i++) {
            int k = (i > r) ? 0 : Math.min(d[l + r - i + 1], r - i + 1);
            while (i - k - 1 >= 0 && i + k < n && s.charAt(i - k - 1) == s.charAt(i + k)) k++;
            d[i] = k;
            if (i + k - 1 > r) {
                l = i - k;
                r = i + k - 1;
            }
        }
        return d;
    }

    // Find the longest even palindrome that touches either left boundary (left == 0)
    // or right boundary (right == n-1). Return its length and which side it touches.
    static class BorderPal {
        int len;      // palindrome length (even)
        int side;     // -1 = none, 0 = left boundary, 1 = right boundary
        BorderPal(int len, int side) { this.len = len; this.side = side; }
    }

    static BorderPal longestBorderEven(String s) {
        int n = s.length();
        int[] d = manacherEven(s);
        int bestLen = 0;
        int bestSide = -1;

        for (int i = 0; i < n; i++) {
            int rad = d[i];
            if (rad == 0) continue;
            int left = i - rad;           // inclusive
            int right = i + rad - 1;      // inclusive

            // touches left boundary
            if (left == 0) {
                int len = 2 * rad;
                if (len > bestLen) { bestLen = len; bestSide = 0; }
            }
            // touches right boundary
            if (right == n - 1) {
                int len = 2 * rad;
                if (len > bestLen) { bestLen = len; bestSide = 1; }
            }
        }
        return new BorderPal(bestLen, bestSide);
    }

    static String fold(String s) {
        while (s.length() > 1) {
            BorderPal bp = longestBorderEven(s);
            if (bp.len < 2 || bp.side == -1) break;

            int cut = bp.len / 2;
            if (bp.side == 0) {
                // remove half from left
                s = s.substring(cut);
            } else {
                // remove half from right
                s = s.substring(0, s.length() - cut);
            }
            // reverse after each removal
            s = new StringBuilder(s).reverse().toString();
        }
        return s;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        if (s == null) return;
        s = s.trim();
        System.out.print(fold(s).length());
    }

    @Test
    public void test1() {
        String input = "MYYM";
        int actual = Solution.fold(input).length();
        int expected = 2;
        assertEquals(expected, actual);
    }

    @Test
    public void test2() {
        String input = "ABXYYXBASFHUIIUKK";
        int actual = Solution.fold(input).length();
        int expected = 12;
        assertEquals(expected, actual);
    }

    @Test
    public void test3() {
        String input = "ABCDDCBA";
        int actual = Solution.fold(input).length();
        int expected = 4;
        assertEquals(expected, actual);
    }

    @Test
    public void test4() {
        String input = "KULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL";
        int actual = Solution.fold(input).length();
        int expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    public void test5() {
        String input = "OHIIHOKIIKJJJJJJJJJJJJJ";
        int actual = Solution.fold(input).length();
        int expected = 8;
        assertEquals(expected, actual);
    }
}