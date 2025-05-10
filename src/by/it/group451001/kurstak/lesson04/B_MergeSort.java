package by.it.group451001.kurstak.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        // Подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        // Считываем размер массива
        int n = scanner.nextInt();
        // Считываем сам массив (натуральные числа)
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Реализуем сортировку слиянием
        a = mergeSort(a);

        return a;
    }

    /**
     * Рекурсивная функция сортировки слиянием.
     * Если массив имеет размер 0 или 1, он уже отсортирован.
     * Иначе, массив делится пополам, каждая часть сортируется, после чего происходит слияние.
     */
    private int[] mergeSort(int[] a) {
        if (a.length <= 1) {
            return a;
        }
        int mid = a.length / 2;
        int[] left = new int[mid];
        int[] right = new int[a.length - mid];

        // Копируем левую часть массива
        for (int i = 0; i < mid; i++) {
            left[i] = a[i];
        }
        // Копируем правую часть массива
        for (int i = mid; i < a.length; i++) {
            right[i - mid] = a[i];
        }

        left = mergeSort(left);
        right = mergeSort(right);
        return merge(left, right);
    }

    /**
     * Функция слияния двух отсортированных массивов.
     * Производит "слияние" массивов left и right в один отсортированный массив.
     */
    private int[] merge(int[] left, int[] right) {
        int[] merged = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;

        // Слияние отсортированных массивов
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                merged[k++] = left[i++];
            } else {
                merged[k++] = right[j++];
            }
        }

        // Копируем оставшиеся элементы из left, если они остались
        while (i < left.length) {
            merged[k++] = left[i++];
        }
        // Копируем оставшиеся элементы из right, если они остались
        while (j < right.length) {
            merged[k++] = right[j++];
        }
        return merged;
    }
}
