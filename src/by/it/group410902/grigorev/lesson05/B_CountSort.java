package by.it.group410902.grigorev.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_CountSort {

    public static void main(String[] args) throws FileNotFoundException {
        // Получаем поток ввода из файла "dataB.txt"
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");

        // Создаем экземпляр класса
        B_CountSort instance = new B_CountSort();

        // Выполняем сортировку
        int[] result = instance.countSort(stream);

        // Выводим отсортированный массив
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем количество чисел
        int n = scanner.nextInt();
        int[] points = new int[n];

        // Заполняем массив значениями
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Максимальное значение в массиве (по условию не превышает 10)
        int max = 10;
        int[] count = new int[max + 1];

        // Заполняем вспомогательный массив частоты чисел
        for (int num : points) {
            count[num]++;
        }

        // Заполняем исходный массив отсортированными значениями
        int index = 0;
        for (int i = 0; i <= max; i++) {
            while (count[i] > 0) {
                points[index++] = i;
                count[i]--;
            }
        }

        return points;
    }
}

