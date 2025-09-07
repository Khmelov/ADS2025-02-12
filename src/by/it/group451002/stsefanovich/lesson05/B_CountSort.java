package by.it.group451002.stsefanovich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_CountSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] points = new int[n];

        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Максимальное значение чисел — 10
        int max = 10;
        int[] count = new int[max + 1];

        // Подсчет частоты каждого числа
        for (int i = 0; i < n; i++) {
            count[points[i]]++;
        }

        // Накопленные суммы для определения позиций
        for (int i = 1; i <= max; i++) {
            count[i] += count[i - 1];
        }

        // Формирование отсортированного массива
        int[] result = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            result[--count[points[i]]] = points[i];
        }

        return result;
    }
}