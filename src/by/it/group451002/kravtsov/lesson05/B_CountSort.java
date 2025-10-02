package by.it.group451002.kravtsov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.

При сортировке реализуйте метод со сложностью O(n)

Пример: https://karussell.wordpress.com/2010/03/01/fast-integer-sorting-algorithm-on/
Вольный перевод: http://programador.ru/sorting-positive-int-linear-time/
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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);

        //!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!

        int n = scanner.nextInt(); // Считываем количество элементов массива
        int[] points = new int[n]; // Создаём массив для хранения чисел

        // Определяем минимальное и максимальное значения в массиве
        int minValue = Integer.MAX_VALUE, maxValue = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt(); // Заполняем массив числами
            minValue = Math.min(minValue, points[i]); // Обновляем минимальное значение
            maxValue = Math.max(maxValue, points[i]); // Обновляем максимальное значение
        }

        // Сортировка подсчётом
        int range = maxValue - minValue + 1; // Вычисляем диапазон значений
        int[] count = new int[range]; // Создаём массив для подсчёта количества элементов

        // Заполняем массив подсчёта
        for (int num : points) count[num - minValue]++;

        int index = 0;
        // Восстанавливаем отсортированный массив
        for (int i = 0; i < range; i++) {
            while (count[i]-- > 0) points[index++] = i + minValue; // Вставляем отсортированные значения
        }

        //!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!

        return points;
    }
}

