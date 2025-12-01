package by.bsuir.dsa.csv2025.gr451002.Стефанович;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {

    /**
     * Вычисляет префикс-функцию для строки pattern
     * @param pattern строка, для которой вычисляется префикс-функция
     * @return массив значений префикс-функции
     */
    public static int[] computePrefixFunction(String pattern) {
        int m = pattern.length();
        int[] pi = new int[m];
        pi[0] = 0;
        
        int j = 0;
        for (int i = 1; i < m; i++) {
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = pi[j - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }
        
        return pi;
    }

    /**
     * Находит все вхождения подстроки pattern в строку s с использованием алгоритма KMP
     * @param s строка, в которой ищется подстрока
     * @param pattern подстрока, которую нужно найти
     * @return список позиций (0-indexed), где начинается pattern в s
     */
    public static List<Integer> findSubstring(String s, String pattern) {
        List<Integer> positions = new ArrayList<>();
        
        if (pattern.isEmpty()) {
            return positions;
        }
        
        int[] pi = computePrefixFunction(pattern);
        int n = s.length();
        int m = pattern.length();
        
        int j = 0; // указатель в pattern
        for (int i = 0; i < n; i++) {
            while (j > 0 && s.charAt(i) != pattern.charAt(j)) {
                j = pi[j - 1];
            }
            if (s.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            if (j == m) {
                // Найдено вхождение на позиции i - m + 1
                positions.add(i - m + 1);
                j = pi[j - 1]; // продолжаем поиск следующего вхождения
            }
        }
        
        return positions;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Читаем строку и подстроку из консоли
        String s = scanner.nextLine();
        String pattern = scanner.nextLine();

        // Вычисляем префикс-функцию
        int[] prefixFunction = computePrefixFunction(pattern);
        
        // Находим все вхождения
        List<Integer> positions = findSubstring(s, pattern);

        // Выводим префикс-функцию
        for (int i = 0; i < prefixFunction.length; i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(prefixFunction[i]);
        }
        System.out.println();

        // Выводим количество вхождений
        System.out.println(positions.size());

        // Выводим позиции вхождений
        for (int i = 0; i < positions.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(positions.get(i));
        }
        System.out.println();

        scanner.close();
    }
}

