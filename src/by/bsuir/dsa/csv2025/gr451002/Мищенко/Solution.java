package by.bsuir.dsa.csv2025.gr451002.Мищенко;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Console helper for the Listen.Online hotline that finds every occurrence of
 * a trigger phrase inside a transcript using the prefix-function (failure function)
 * of Knuth-Morris-Pratt.
 */
public class Solution {

    /**
     * Builds the prefix-function array for the provided string.
     *
     * @param s string to analyze
     * @return pi array where pi[i] is the length of the longest proper prefix
     * that is also a suffix of s[0..i]
     */
    public static int[] prefixFunction(String s) {
        int n = s.length();
        int[] pi = new int[n];
        for (int i = 1; i < n; i++) {
            int j = pi[i - 1];
            while (j > 0 && s.charAt(i) != s.charAt(j)) {
                j = pi[j - 1];
            }
            if (s.charAt(i) == s.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }
        return pi;
    }

    /**
     * Finds all start positions of pattern inside text using prefix-function.
     *
     * @param pattern pattern to search, must be non-empty
     * @param text    text where the pattern needs to be found
     * @return list of zero-based start indexes of each occurrence
     */
    public static List<Integer> findOccurrences(String pattern, String text) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern must be non-empty");
        }
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null");
        }

        String combined = pattern + "#" + text;
        int[] pi = prefixFunction(combined);
        int m = pattern.length();
        List<Integer> result = new ArrayList<>();

        for (int i = m + 1; i < combined.length(); i++) {
            if (pi[i] == m) {
                int startIndex = i - 2 * m;
                result.add(startIndex);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String pattern = scanner.hasNextLine() ? scanner.nextLine() : "";
        String text = scanner.hasNextLine() ? scanner.nextLine() : "";
        scanner.close();

        List<Integer> occurrences = findOccurrences(pattern, text);
        System.out.println(pattern);
        System.out.println(text);
        System.out.println(occurrences.size());
        if (occurrences.isEmpty()) {
            System.out.println();
        } else {
            String indexes = occurrences.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(" "));
            System.out.println(indexes);
        }
    }
}







