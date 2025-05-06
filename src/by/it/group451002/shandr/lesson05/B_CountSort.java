package by.it.group451002.shandr.lesson05;

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
        for (int value : result) {
            System.out.print(value + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        // читаем размер массива
        int n = scanner.nextInt();
        int[] a = new int[n];
        // читаем сами числа (диапазон 1..10)
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // создаём массив счётчиков для значений 1..10
        int MAX = 10;
        int[] count = new int[MAX + 1];  // count[0] не используется, числа с 1 до 10

        // подсчитываем вхождения
        for (int x : a) {
            count[x]++;
        }

        // восстанавливаем отсортированный массив
        int idx = 0;
        for (int value = 1; value <= MAX; value++) {
            int freq = count[value];
            for (int k = 0; k < freq; k++) {
                a[idx++] = value;
            }
        }

        return a;
    }
}