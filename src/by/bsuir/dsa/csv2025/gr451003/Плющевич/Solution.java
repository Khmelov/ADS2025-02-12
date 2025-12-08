package by.bsuir.dsa.csv2025.gr451003.Плющевич;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/**
 * Main
 *
 * Консольная программа + встроенный JUnit тест.
 *
 * Описание задачи:
 * Дана строка `text` и длина `k`. Нужно найти все индексы i таких, что подстрока
 * text.substring(i, i + k) может стать палиндромом заменой не более одного символа.
 *
 * Ввод (через Scanner):
 * - первая строка (или токен) — строка text
 * - второй токен/строка — целое k
 *
 * Вывод:
 * - если найдено >0 индексов — вывести их через пробел в одной строке
 * - иначе вывести строку "(пусто)"
 *
 * Замечание по определению "почти-палиндром":
 * Рассматриваем пары символов (l, r) в подстроке. Если число пар, в которых символы не равны,
 * не превышает 1 — такая подстрока считается подходящей.
 */
public class Solution {

    /**
     * Проверка: можно ли превратить строку s в палиндром заменой <= 1 символа.
     *
     * Алгоритм:
     * - стандартный двухуказательный проход по краям внутрь;
     * - считаем количество пар (i, j) с s[i] != s[j];
     * - если их <= 1, возвращаем true, иначе false.
     */
    public static boolean isAlmostPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        int mismatches = 0;

        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                mismatches++;
                if (mismatches > 1) return false;
            }
            left++;
            right--;
        }
        return true;
    }

    /**
     * Возвращает список начальных индексов всех подстрок длины k_len,
     * которые являются "почти-палиндромами" по определению выше.
     */
    public static List<Integer> findAlmostPalindromes(String text, int k_len) {
        List<Integer> result = new ArrayList<>();
        if (text == null) return result;
        int n = text.length();
        if (k_len <= 0 || k_len > n) return result;

        for (int i = 0; i <= n - k_len; i++) {
            String sub = text.substring(i, i + k_len);
            if (isAlmostPalindrome(sub)) result.add(i);
        }
        return result;
    }

    /**
     * main: консольный интерфейс.
     * Читает строку (можно включать пробелы), затем читает целое k.
     * Примеры ввода:
     *   abccba
     *   4
     * или в одной строке:
     *   abccba 4
     *
     * Выводит найденные индексы или "(пусто)".
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) {
            System.out.println("(пусто)");
            return;
        }
        String line = sc.nextLine().trim();
        String text;
        int k;

        // Если первая линия содержит и текст, и число (через пробел), попробуем разобрать
        String[] parts = line.split("\\s+");
        if (parts.length >= 2 && isInteger(parts[parts.length - 1])) {
            // Последний токен — число k, всё остальное — текст (включая пробелы между токенами)
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts.length - 1; i++) {
                if (i > 0) sb.append(' ');
                sb.append(parts[i]);
            }
            text = sb.toString();
            k = Integer.parseInt(parts[parts.length - 1]);
        } else {
            // Иначе: первая строка — текст, далее читаем целое k
            text = line;
            if (!sc.hasNextInt()) {
                // Нет числа — считаем, что нет подстрок
                System.out.println("(пусто)");
                return;
            }
            k = sc.nextInt();
        }

        List<Integer> result = findAlmostPalindromes(text, k);

        if (result.isEmpty()) {
            System.out.println("(пусто)");
        } else {
            StringBuilder out = new StringBuilder();
            for (int i = 0; i < result.size(); i++) {
                if (i > 0) out.append(' ');
                out.append(result.get(i));
            }
            System.out.println(out.toString());
        }
    }

    @Test
    public void testAlmostPalindromes() {
        assertEquals(Arrays.asList(0), findAlmostPalindromes("abca", 4));
        assertEquals(Arrays.asList(0,1,2,3,4), findAlmostPalindromes("abcdefg", 3));
        assertEquals(Arrays.asList(0,1), findAlmostPalindromes("abcd", 3));
        assertEquals(Arrays.asList(), findAlmostPalindromes("", 1));
        assertEquals(Arrays.asList(0), findAlmostPalindromes("a", 1));
        assertEquals(Arrays.asList(), findAlmostPalindromes("abcdef", 6));
        assertEquals(Arrays.asList(0), findAlmostPalindromes("racecar", 7));
        assertEquals(Arrays.asList(1), findAlmostPalindromes("abccba", 4));
        assertEquals(Arrays.asList(0), findAlmostPalindromes("xyzzyx", 6));
        assertEquals(Arrays.asList(0,1,2), findAlmostPalindromes("abxba", 3));
        assertEquals(Arrays.asList(), findAlmostPalindromes("abc", 5));
        assertEquals(Arrays.asList(0,1,2,3), findAlmostPalindromes("aaaaa", 2));
    }

     private static boolean isInteger(String s) {
        if (s == null || s.isEmpty()) return false;
        int i = 0;
        if (s.charAt(0) == '-' || s.charAt(0) == '+') {
            if (s.length() == 1) return false;
            i = 1;
        }
        for (; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }
}