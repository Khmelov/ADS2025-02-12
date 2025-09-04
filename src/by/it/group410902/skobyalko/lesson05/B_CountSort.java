package by.it.group410902.skobyalko.lesson05;

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
        Scanner scanner = new Scanner(stream);
        /// //////////
        int n = scanner.nextInt();
        int[] points = new int[n];
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Максимальное значение по условию задачи
        int maxVal = 10;

        // Массив для подсчета количества каждого числа
        int[] count = new int[maxVal + 1];

        // Подсчёт количества каждого числа
        for (int num : points) {
            count[num]++;
        }

        // Восстановление отсортированного массива
        int index = 0;
        for (int val = 0; val <= maxVal; val++) {
            while (count[val] > 0) {
                points[index++] = val;
                count[val]--;
            }
        }
/// //////////////////////////
        return points;
    }

}
