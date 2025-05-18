package by.it.group451001.yarkovich.lesson05;

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

        // Поскольку числа не превышают 10, создаем массив счетчиков размером 11
        // (индексы от 0 до 10)
        int[] count = new int[11];

        // 1. Подсчитываем количество вхождений каждого числа
        for (int num : points) {
            count[num]++;
        }

        // 2. Вычисляем кумулятивные суммы (для определения позиций чисел)
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // 3. Создаем отсортированный массив
        int[] sorted = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int num = points[i];
            sorted[count[num] - 1] = num;
            count[num]--;
        }

        // Копируем отсортированный массив обратно в points
        System.arraycopy(sorted, 0, points, 0, n);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }
}