package by.bsuir.dsa.csv2025.gr451003.КишкоВ;

import java.util.*;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class Solution {

    static Map<Character, Long> buildCharMask(String pattern) {
        Map<Character, Long> charMask = new HashMap<>();

        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            charMask.put(c, charMask.getOrDefault(c, 0L) | (1L << i));
        }

        return charMask;
    }

    static List<Integer> fuzzySearchBitap(String text, String pattern, int maxErrors) {
        int n = text.length();
        int m = pattern.length();
        List<Integer> result = new ArrayList<>();

        if (m == 0 || m > n || maxErrors < 0 || maxErrors >= m) {
            return result;
        }

        Map<Character, Long> charMask = buildCharMask(pattern);

        long[] dp = new long[maxErrors + 1];
        dp[0] = 0;

        for (int i = 0; i < n; i++) {
            char c = text.charAt(i);
            long mask = charMask.getOrDefault(c, 0L);

            long[] newDp = new long[maxErrors + 1];

            for (int e = maxErrors; e >= 0; e--) {
                long match = ((dp[e] << 1) | 1) & mask;
                newDp[e] |= match;

                if (e < maxErrors) {
                    long substitute = (dp[e] << 1) | 1;
                    newDp[e + 1] |= substitute;

                    long insert = (dp[e] << 1);
                    newDp[e + 1] |= insert;

                    long delete = dp[e];
                    newDp[e + 1] |= delete;
                }
            }

            for (int e = 0; e <= maxErrors; e++) {
                dp[e] = newDp[e];
            }

            long lastBit = 1L << (m - 1);
            for (int e = 0; e <= maxErrors; e++) {
                if ((dp[e] & lastBit) != 0) {
                    result.add(i - m + 1);
                    break;
                }
            }
        }

        Set<Integer> unique = new LinkedHashSet<>(result);
        List<Integer> sorted = new ArrayList<>(unique);
        Collections.sort(sorted);

        return sorted;
    }

    static String formatOutput(List<Integer> matches, String text, String pattern) {
        if (matches.isEmpty()) {
            return "none";
        }
        StringBuilder output = new StringBuilder();
        for (int pos : matches) {
            int end = Math.min(pos + pattern.length(), text.length());
            String found = text.substring(pos, end);
            output.append(pos).append(" ").append(found).append(" ");
        }
        return output.toString().trim();
    }

    @Test
    public void test1() {
        List<Integer> matches = fuzzySearchBitap("helloworld", "world", 0);
        String result = formatOutput(matches, "helloworld", "world");
        assertEquals("5 world", result);
    }

    @Test
    public void test2() {
        List<Integer> matches = fuzzySearchBitap("mississippi", "issi", 1);
        String result = formatOutput(matches, "mississippi", "issi");
        assertEquals("1 issi 2 ssis 4 issi 5 ssip", result);
    }

    @Test
    public void test3() {
        List<Integer> matches = fuzzySearchBitap("programming", "gram", 2);
        String result = formatOutput(matches, "programming", "gram");
        assertEquals("3 gram 4 ramm 5 ammi", result);
    }

    @Test
    public void test4() {
        List<Integer> matches = fuzzySearchBitap("abcabc", "abc", 1);
        String result = formatOutput(matches, "abcabc", "abc");
        assertEquals("0 abc 1 bca 3 abc", result);
    }

    @Test
    public void test5() {
        List<Integer> matches = fuzzySearchBitap("aaaa", "aa", 0);
        String result = formatOutput(matches, "aaaa", "aa");
        assertEquals("0 aa 1 aa 2 aa", result);
    }

    @Test
    public void test6() {
        List<Integer> matches = fuzzySearchBitap("kitten", "kitten", 0);
        String result = formatOutput(matches, "kitten", "kitten");
        assertEquals("0 kitten", result);
    }

    @Test
    public void test7() {
        List<Integer> matches = fuzzySearchBitap("hello", "sitting", 3);
        String result = formatOutput(matches, "hello", "sitting");
        assertEquals("none", result);
    }

    @Test
    public void test8() {
        List<Integer> matches = fuzzySearchBitap("aXbcYde", "abc", 2);
        String result = formatOutput(matches, "aXbcYde", "abc");
        assertEquals("0 aXb 1 Xbc 2 bcY", result);
    }

    @Test
    public void test9() {
        List<Integer> matches = fuzzySearchBitap("abcdefg", "cde", 0);
        String result = formatOutput(matches, "abcdefg", "cde");
        assertEquals("2 cde", result);
    }

    @Test
    public void test10() {
        List<Integer> matches = fuzzySearchBitap("abcdef", "xyz", 1);
        String result = formatOutput(matches, "abcdef", "xyz");
        assertEquals("none", result);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();
        String[] parts = input.split(" ");

        String text = parts[0];
        String pattern = parts[1];
        int maxErrors = Integer.parseInt(parts[2]);

        List<Integer> matches = fuzzySearchBitap(text, pattern, maxErrors);
        System.out.println(formatOutput(matches, text, pattern));
    }
}
