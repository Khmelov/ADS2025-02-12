package by.bsuir.dsa.csv2025.gr451003.Алексеюк;



import java.util.Scanner;

public class Solution {

    public static String countingSort(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        int[] count = new int[26];

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int index = c - 'a';
            count[index]++;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            char currentChar = (char) ('a' + i);
            for (int j = 0; j < count[i]; j++) {
                result.append(currentChar);
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        if (input == null || input.isEmpty()) {
            System.out.println("");
        } else {
            String sortedString = countingSort(input);
            System.out.println(sortedString);
        }

        scanner.close();
    }
} 







