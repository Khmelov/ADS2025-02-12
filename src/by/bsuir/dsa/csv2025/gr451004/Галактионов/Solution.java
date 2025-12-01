package by.bsuir.dsa.csv2025.gr451004.Галактионов;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите текст:");
        String text = scanner.nextLine();

        System.out.println("Введите образец для поиска:");
        String pattern = scanner.nextLine();

        List<Integer> result = findAllOccurrences(text, pattern);

        System.out.println("Результат поиска:");
        if (result.isEmpty()) {
            System.out.println("Вхождений не найдено");
        } else {
            for (int i = 0; i < result.size(); i++) {
                System.out.print(result.get(i));
                if (i < result.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        scanner.close();
    }

    public static List<Integer> findAllOccurrences(String text, String pattern) {
        List<Integer> result = new ArrayList<>();

        if (pattern == null || pattern.isEmpty() || pattern.length() > text.length()) {
            return result;
        }

        int[] prefixFunction = computePrefixFunction(pattern);
        int k = 0;

        for (int i = 0; i < text.length(); i++) {
            while (k > 0 && text.charAt(i) != pattern.charAt(k)) {
                k = prefixFunction[k - 1];
            }

            if (text.charAt(i) == pattern.charAt(k)) {
                k++;
            }

            if (k == pattern.length()) {
                result.add(i - pattern.length() + 1);
                k = prefixFunction[k - 1];
            }
        }

        return result;
    }

    private static int[] computePrefixFunction(String pattern) {
        int n = pattern.length();
        int[] pi = new int[n];

        if (n == 0) {
            return pi;
        }

        pi[0] = 0;

        for (int i = 1; i < n; i++) {
            int k = pi[i - 1];

            while (k > 0 && pattern.charAt(i) != pattern.charAt(k)) {
                k = pi[k - 1];
            }

            if (pattern.charAt(i) == pattern.charAt(k)) {
                k++;
            }

            pi[i] = k;
        }

        return pi;
    }
}