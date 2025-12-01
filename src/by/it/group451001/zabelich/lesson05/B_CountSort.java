package by.it.group451001.zabelich.lesson05;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Чтение размера массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        // Чтение чисел в массив points
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Поскольку числа не превышают 10, создаем массив счетчиков размером 11 (от 0 до 10)
        int[] count = new int[11]; // Индексы 0-10, но числа от 1 до 10 (по условию)

        // Подсчет количества вхождений каждого числа
        for (int num : points) {
            count[num]++;
        }

        // Восстановление отсортированного массива на основе счетчиков
        int index = 0; // Индекс для заполнения исходного массива
        for (int num = 1; num <= 10; num++) { // Числа от 1 до 10
            while (count[num] > 0) { // Пока есть числа num
                points[index] = num; // Записываем число в исходный массив
                index++; // Переходим к следующей позиции
                count[num]--; // Уменьшаем счетчик
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }
}
