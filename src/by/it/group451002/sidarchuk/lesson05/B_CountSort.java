package by.it.group451002.sidarchuk.lesson05;

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
        //размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        //читаем точки
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением сортировки подсчетом
        // Реализация сортировки подсчетом
        return countingSort(points, 10); // Максимальное значение 10
    }

    // Метод сортировки подсчетом
    private int[] countingSort(int[] array, int maxValue) {
        // Создаем массив счетчиков
        int[] count = new int[maxValue + 1];

        // Подсчитываем количество вхождений каждого числа
        for (int num : array) {
            count[num]++;
        }

        // Заполняем отсортированный массив
        int index = 0;
        for (int i = 0; i <= maxValue; i++) {
            while (count[i] > 0) {
                array[index++] = i;
                count[i]--;
            }
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return array;
    }

}
