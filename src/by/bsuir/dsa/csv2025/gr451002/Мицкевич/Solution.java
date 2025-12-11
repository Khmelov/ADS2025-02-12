package by.bsuir.dsa.csv2025.gr451002.Мицкевич;

import java.util.Scanner;

public class Solution {

    /**
     * Вычисляет Z-функцию для строки s
     * Z[i] - длина наибольшего общего префикса строки s и суффикса s[i..n-1]
     * @param s входная строка
     * @return массив значений Z-функции
     */
    public static int[] computeSolution(String s) {
        int n = s.length();
        int[] z = new int[n];
        
        if (n == 0) {
            return z;
        }
        
        z[0] = n; // По определению, Z[0] = n
        
        int l = 0, r = 0; // Границы самого правого Z-блока
        
        for (int i = 1; i < n; i++) {
            if (i <= r) {
                // Используем уже вычисленные значения
                z[i] = Math.min(r - i + 1, z[i - l]);
            }
            
            // Пытаемся расширить Z-блок
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) {
                z[i]++;
            }
            
            // Обновляем границы самого правого Z-блока
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        
        return z;
    }

    /**
     * Находит все вхождения подстроки pattern в тексте text с помощью Z-функции
     * @param text текст для поиска
     * @param pattern подстрока для поиска
     * @return массив позиций вхождений (0-indexed)
     */
    public static int[] findOccurrences(String text, String pattern) {
        if (pattern.isEmpty()) {
            return new int[0];
        }
        
        // Создаем строку pattern + '$' + text для вычисления Z-функции
        String combined = pattern + "$" + text;
        int[] z = computeSolution(combined);
        
        int patternLen = pattern.length();
        int count = 0;
        
        // Подсчитываем количество вхождений
        for (int i = patternLen + 1; i < z.length; i++) {
            if (z[i] == patternLen) {
                count++;
            }
        }
        
        // Собираем позиции вхождений
        int[] occurrences = new int[count];
        int idx = 0;
        for (int i = patternLen + 1; i < z.length; i++) {
            if (z[i] == patternLen) {
                occurrences[idx++] = i - patternLen - 1;
            }
        }
        
        return occurrences;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Читаем текст и паттерн
        String text = scanner.nextLine();
        String pattern = scanner.nextLine();
        
        // Находим вхождения
        int[] occurrences = findOccurrences(text, pattern);
        
        // Выводим результаты
        System.out.println(occurrences.length);
        for (int pos : occurrences) {
            System.out.print(pos + " ");
        }
        if (occurrences.length > 0) {
            System.out.println();
        }
        
        scanner.close();
    }
}

