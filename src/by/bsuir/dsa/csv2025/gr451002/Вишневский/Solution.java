package by.bsuir.dsa.csv2025.gr451002.Вишневский;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Реализация игры Moore's K-Nim (нормальная игра).
 *
 * Предоставляет:
 * - isWinningKnim(...) — проверка, является ли позиция выигрышной.
 * - computeModBits(...) — вычисление векторов битов по модулю (k+1) для отладки.
 *
 * Алгоритм основан на теореме Мура:
 * Позиция проигрышная тогда и только тогда, когда для КАЖДОГО бита
 * количество куч, у которых этот бит = 1, дает остаток 0 по модулю (k+1).
 *
 * Пример:
 * k = 1 → обычный ним → модуль (1+1) = 2 → XOR.
 */
public class Solution {

    /**
     * Определяет, является ли позиция выигрышной в Moore's K-Nim.
     *
     * @param piles массив размеров кучек (каждое число ≥ 0)
     * @param k максимальное число куч, которые можно уменьшить за один ход (k ≥ 1)
     * @return true, если позиция выигрышная (N-position),
     *         false, если проигрышная (P-position)
     */
    public static boolean isWinningKnim(int[] piles, int k) {

        // Пустая позиция — конечная → проигрышная
        if (piles == null || piles.length == 0) {
            return false;
        }

        // Проверка корректности k
        if (k <= 0) {
            throw new IllegalArgumentException("k must be >= 1");
        }

        // -------------------------------
        // 1. Находим максимальное количество бит,
        //    которое потребуется для анализа всех чисел.
        //    Это нужно, чтобы знать, сколько битовых разрядов проверять.
        // -------------------------------
        int maxBits = 0;
        for (int p : piles) {
            if (p < 0)
                throw new IllegalArgumentException("pile sizes must be non-negative");

            // Количество бит у числа p
            maxBits = Math.max(maxBits, Integer.toBinaryString(p).length());
        }

        // -------------------------------
        // 2. Подсчитываем, сколько куч имеют бит = 1 в каждом разряде
        // -------------------------------
        int[] bitCount = new int[maxBits];

        for (int p : piles) {
            for (int bit = 0; bit < maxBits; bit++) {

                // Проверяем bit-й бит числа p:
                // (p >> bit) & 1 == 1 → значит этот бит установлен
                if (((p >> bit) & 1) == 1) {
                    bitCount[bit]++;
                }
            }
        }

        // -------------------------------
        // 3. Применяем главное правило Мура:
        //    Позиция проигрышная ↔ для всех битов count % (k+1) == 0.
        //
        // Если найден хотя бы один бит,
        // где count % (k+1) != 0 → позиция выигрышная.
        // -------------------------------
        for (int count : bitCount) {
            if (count % (k + 1) != 0) {
                return true; // Нашли ненулевой остаток → выигрышная позиция
            }
        }

        // Все остатки равны 0 → проигрышная позиция
        return false;
    }

    /**
     * Вспомогательный метод:
     * возвращает массив остатков bitCount[i] % (k+1).
     *
     * Нужен для визуальной проверки логики работы isWinningKnim.
     */
    public static int[] computeModBits(int[] piles, int k) {
        int maxBits = 0;

        // Определяем нужное количество бит
        for (int p : piles) {
            maxBits = Math.max(maxBits, Integer.toBinaryString(p).length());
        }

        // Подсчитываем количество единичных битов
        int[] bitCount = new int[maxBits];
        for (int p : piles) {
            for (int bit = 0; bit < maxBits; bit++) {
                if (((p >> bit) & 1) == 1) {
                    bitCount[bit]++;
                }
            }
        }

        // Вычисляем bitCount[i] % (k+1)
        int[] modBits = new int[maxBits];
        for (int i = 0; i < maxBits; i++) {
            modBits[i] = bitCount[i] % (k + 1);
        }

        return modBits;
    }

    /**
     * Интерактивный ввод:
     * - спрашивает у пользователя размеры куч
     * - спрашивает k
     * - показывает остатки битов mod (k+1)
     * - выводит, выигрышная ли позиция.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Запрос количества куч
        System.out.print("Введите количество кучек: ");
        int n = sc.nextInt();

        // Запрос размеров куч
        int[] piles = new int[n];
        System.out.println("Введите размеры кучек: ");
        for (int i = 0; i < n; i++) {
            piles[i] = sc.nextInt();
        }

        // Запрос параметра k
        System.out.print("Введите k (до скольких кучек можно уменьшить за ход): ");
        int k = sc.nextInt();

        // Проверяем позицию
        boolean winning = isWinningKnim(piles, k);

        // Получаем вектор остатков для отображения
        int[] modBits = computeModBits(piles, k);

        System.out.println("\nСумма bitCount mod (k+1): " + Arrays.toString(modBits));

        // Вывод результата
        if (winning) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }

        sc.close();
    }
}
