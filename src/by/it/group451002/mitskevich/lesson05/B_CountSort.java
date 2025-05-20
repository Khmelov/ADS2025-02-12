package by.it.group451002.mitskevich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача: Отсортировать массив натуральных чисел от 1 до 10, используя сортировку подсчётом (CountSort).
Сложность должна быть O(n), так как диапазон значений мал (всего от 1 до 10).
Пример:
Вход:
    5
    2 3 2 1 3
Выход:
    1 2 2 3 3
*/

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
        // Подготовка к чтению данных
        Scanner scanner = new Scanner(stream);

        // Читаем размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        // Читаем сам массив
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Максимально возможное значение в массиве — 10
        int maxValue = 10;

        // Массив для подсчёта количества каждого значения
        int[] count = new int[maxValue + 1]; // от 0 до 10

        // Подсчёт количества вхождений каждого значения
        for (int point : points) {
            count[point]++;
        }

        // Перезаписываем массив в отсортированном порядке
        int index = 0;
        for (int value = 1; value <= maxValue; value++) {
            for (int i = 0; i < count[value]; i++) {
                points[index++] = value;
            }
        }

        return points;
    }
}
