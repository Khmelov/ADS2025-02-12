package by.bsuir.dsa.csv2025.gr451002.Карбанович;


import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/*
Задача: k-й по величине неотрицательный элемент массива

Первая строка входа содержит два целых числа:
    1 <= n <= 100000 — количество элементов в массиве
    1 <= k <= n      — номер элемента, который нужно найти

Вторая строка содержит n целых чисел — элементы массива.

Найдите k-й по величине неотрицательный элемент массива (нумерация с 1).
Если неотрицательных элементов меньше k, выведите -1.

Примеры:

Sample Input 1:
5 2
7 -3 10 1 -5

Sample Output 1:
7

Sample Input 2:
6 4
13 1 -9 25 4 -7

Sample Output 2:
25

 */

public class Solution {

    // main: ввод данных и вывод результата
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt(); // число элементов
        int k = sc.nextInt(); // индекс k

        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }

        System.out.println(kthNonNegative(a, k));
    }

    // Метод возвращает k-й по величине неотрицательный элемент
    public static int kthNonNegative(int[] a, int k) {
        if (a == null) {
            throw new IllegalArgumentException("Array is null");
        }
        if (k < 1) {
            throw new IllegalArgumentException("k is out of range");
        }

        // Копируем массив и фильтруем только неотрицательные числа
        int countNonNeg = 0;
        for (int num : a) {
            if (num >= 0) countNonNeg++;
        }

        if (countNonNeg < k) return -1; // если не хватает неотрицательных чисел

        int[] nonNeg = new int[countNonNeg];
        int index = 0;
        for (int num : a) {
            if (num >= 0) nonNeg[index++] = num;
        }

        // Сортируем массив неотрицательных чисел с помощью Heapsort
        heapSort(nonNeg);

        return nonNeg[k - 1]; // k-й элемент (1-индексация)
    }

    // Heapsort: сортировка массива по возрастанию
    private static void heapSort(int[] a) {
        int n = a.length;

        // Построение max-кучи
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(a, n, i);
        }

        // Извлечение максимума и перестановка его в конец массива
        for (int i = n - 1; i > 0; i--) {
            swap(a, 0, i);
            heapify(a, i, 0);
        }
    }

    // Вспомогательный метод: "просеивание вниз" для max-кучи
    private static void heapify(int[] a, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && a[left] > a[largest]) largest = left;
        if (right < n && a[right] > a[largest]) largest = right;

        if (largest != i) {
            swap(a, i, largest);
            heapify(a, n, largest);
        }
    }

    // Метод для обмена элементов массива
    private static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    // --------------------- Тесты ---------------------
    @Test
    public void testSimple() {
        int[] a = {7, -3, 10, 1, -5};
        assertEquals(7, Solution.kthNonNegative(a, 2));
    }

    @Test
    public void testKFirst() {
        int[] a = {10, -7, 1, 8};
        assertEquals(1, Solution.kthNonNegative(a, 1));
    }

    @Test
    public void testKLast() {
        int[] a = {10, -7, 1, 8};
        assertEquals(10, Solution.kthNonNegative(a, 3));
    }

    @Test
    public void testNotEnoughNonNegatives() {
        int[] a = {-5, -1, -10, 0};
        assertEquals(-1, Solution.kthNonNegative(a, 2));
    }

    @Test
    public void testAllNonNegatives() {
        int[] a = {1, 2, 3, 4, 5};
        assertEquals(4, Solution.kthNonNegative(a, 4));
    }

    @Test
    public void testMixed() {
        int[] a = {-2, 3, 0, -1, 4};
        assertEquals(4, Solution.kthNonNegative(a, 3));
    }


    @Test
    public void testSingleElementNonNeg() {
        int[] a = {42};
        assertEquals(42, Solution.kthNonNegative(a, 1));
    }

    @Test
    public void testSingleElementNeg() {
        int[] a = {-42};
        assertEquals(-1, Solution.kthNonNegative(a, 1));
    }

    @Test
    public void testEmptyArray() {
        int[] a = {};
        assertEquals(-1, Solution.kthNonNegative(a, 1));
    }

    @Test
    public void testKTooLarge() {
        int[] a = {0, 2, -1, 3};
        assertEquals(-1, Solution.kthNonNegative(a, 5));
    }
}
