package by.it.group410901.getmanchuk.lesson05;

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
        int n = scanner.nextInt(); // количество чисел
        int[] points = new int[n];

        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt(); // читаем сами числа
        }

        // Подсчет значений от 1 до 10
        int[] count = new int[11]; // 0 индекс не используется

        for (int point : points) {
            count[point]++;
        }

        // Формируем отсортированный массив
        int index = 0;
        for (int i = 1; i <= 10; i++) {
            for (int j = 0; j < count[i]; j++) {
                points[index++] = i;
            }
        }

        return points;
    }
}