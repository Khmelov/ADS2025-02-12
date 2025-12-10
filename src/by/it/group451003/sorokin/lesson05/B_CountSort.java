package by.it.group451003.sorokin.lesson05;

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

        // Чтение размера массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        // Чтение элементов массива
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Поскольку числа не превышают 10, создаем массив счетчиков размером 11
        // (индексы от 0 до 10)
        int[] count = new int[11];

        // Подсчитываем количество вхождений каждого числа
        for (int num : points) {
            count[num]++;
        }

        // Восстанавливаем отсортированный массив
        int index = 0;
        for (int i = 0; i < count.length; i++) {
            while (count[i] > 0) {
                points[index++] = i;
                count[i]--;
            }
        }

        return points;
    }
}