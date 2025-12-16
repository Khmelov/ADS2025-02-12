package by.it.group451002.popeko.lesson05;

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

        // Читаем размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        // Читаем числа
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Создаём массив для подсчёта частоты чисел (от 1 до 10)
        int[] count = new int[11]; // 0 не используем, т.к. числа от 1 до 10

        // Подсчитываем количество каждого числа
        for (int num : points) {
            count[num]++;
        }

        // Заполняем массив отсортированными числами
        int index = 0;
        for (int i = 1; i <= 10; i++) {
            while (count[i] > 0) {
                points[index++] = i;
                count[i]--;
            }
        }

        return points;
    }
}
