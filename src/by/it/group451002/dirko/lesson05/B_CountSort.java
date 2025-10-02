package by.it.group451002.dirko.lesson05;

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

        // Если массив пустой, то возвращаем его
        if (n == 0) { return points; }

        // Находим максимальное и минимальное значение из заданного массива
        int max = points[0], min = points[0];
        for (int i = 1; i < n; i++) {
            if (points[i] > max) { max = points[i]; }
            else if (points[i] < min) { min = points[i]; }
        }

        // Создаем массив длиной [max - min + 1] и указываем количество повторений каждого числа
        // в сортируемом массиве
        int[] count = new int[max - min + 1];
        for (int point : points) { count[point-min]++; }

        // Проходимся по массиву с повторениями и сортируем изначальный массив
        int b = 0;
        for (int i = 0; i < count.length; i++) {
            for (int j = 0; j < count[i]; j++) { points[b++] = i + min; }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}
