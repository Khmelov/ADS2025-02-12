package by.it.group451001.khokhlov.lesson05;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = scanner.nextInt();
        int[] points = new int[n];

        // Чтение чисел
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Так как числа не превышают 10, создаем массив счетчиков размером 11
        // (индексы от 0 до 10)
        int[] count = new int[11];

        // Подсчет количества каждого числа
        for (int num : points) {
            count[num]++;
        }

        // Заполняем исходный массив отсортированными числами
        int index = 0;
        for (int num = 0; num < count.length; num++) {
            while (count[num] > 0) {
                points[index++] = num;
                count[num]--;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }
}