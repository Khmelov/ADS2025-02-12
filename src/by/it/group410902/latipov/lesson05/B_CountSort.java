package by.it.group410902.latipov.lesson05;

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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        //читаем точки
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        //тут реализуйте логику задачи с применением сортировки подсчетом
        int[] sortedArray = countingSort(points);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return sortedArray;
    }

    private int[] countingSort(int[] array) {
        // По условию числа не превышают 10
        int maxValue = 10;

        // Создаем массив для подсчета (от 1 до 10 включительно)
        int[] count = new int[maxValue + 1];

        // Подсчитываем количество каждого элемента
        for (int i = 0; i < array.length; i++) {
            count[array[i]]++;
        }

        // Восстанавливаем отсортированный массив
        int index = 0;
        for (int i = 1; i <= maxValue; i++) {
            while (count[i] > 0) {
                array[index] = i;
                index++;
                count[i]--;
            }
        }

        return array;
    }
}