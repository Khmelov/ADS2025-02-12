package by.it.group451001.khomenkov.lesson05;

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

        int[] count = new int[11]; // Поскольку числа не превышают 10

        // Подсчитываем количество каждого числа
        for (int num : points) {
            count[num]++;
        }

        // Восстанавливаем отсортированный массив
        int index = 0;
        for (int num = 1; num <= 10; num++) {
            while (count[num] > 0) {
                points[index++] = num;
                count[num]--;
            }
        }

        return points;
    }
}