package by.bsuir.dsa.csv2025.gr451003.Раббимов;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите строку для анализа:");
        String input = scanner.nextLine();

        scanner.close();

        List<String> result = findValidSubstrings(input);

        System.out.println("Найдено валидных подстрок: " + result.size());
        for (int i = 0; i < result.size(); i++) {
            System.out.println((i + 1) + ". " + result.get(i));
        }
    }

    public static List<String> findValidSubstrings(String input) {
        List<String> result = new ArrayList<>();

        if (input == null || input.isEmpty()) {
            return result;
        }

        for (int start = 0; start < input.length(); start++) {
            char firstChar = input.charAt(start);

            if (!isUppercaseLetter(firstChar))
                continue;

            Alphabet alphabet = getAlphabet(firstChar);

            StringBuilder currentSubstring = new StringBuilder();
            currentSubstring.append(firstChar);

            for (int end = start + 1; end < input.length() && currentSubstring.length() < 15; end++) {
                char currentChar = input.charAt(end);
                Alphabet currentAlphabet = getAlphabet(currentChar);

                if (currentAlphabet != alphabet) {
                    break;
                }

                if (isUppercaseLetter(currentChar))
                    break;

                currentSubstring.append(currentChar);
            }
            if ((currentSubstring.length() >= 3) && (currentSubstring.length() <= 15)) {
                result.add(currentSubstring.toString());
            }
        }

        return result;
    }

    private static boolean isUppercaseLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'А' && c <= 'Я') || c == 'Ё';
    }

    private static Alphabet getAlphabet(char c) {
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
            return Alphabet.LATIN;
        } else if ((c >= 'А' && c <= 'Я') || (c >= 'а' && c <= 'я') || c == 'Ё' || c == 'ё') {
            return Alphabet.CYRILLIC;
        } else {
            return Alphabet.OTHER;
        }
    }

    private enum Alphabet {
        CYRILLIC, LATIN, OTHER
    }
}


