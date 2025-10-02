package by.it.group451002.vishnevskiy.lesson05;

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
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // читаем размер массива
        int n = scanner.nextInt();

        // создаем массив для хранения исходных чисел
        int[] points = new int[n];

        // читаем n чисел в массив points
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // создаем массив для подсчета количества вхождений чисел от 0 до 10
        int[] count = new int[11]; // индекс i хранит количество чисел, равных i

        // проходим по массиву и увеличиваем счетчик для каждого числа
        for (int point : points) {
            count[point]++;
        }

        // восстанавливаем отсортированный массив из массива подсчетов
        int index = 0; // текущая позиция в массиве points
        for (int i = 0; i <= 10; i++) { // для каждого возможного значения от 0 до 10
            while (count[i] > 0) { // пока есть элементы с таким значением
                points[index++] = i; // записываем значение i в массив
                count[i]--; // уменьшаем количество оставшихся
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points; // возвращаем отсортированный массив
    }
}
