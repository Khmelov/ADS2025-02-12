package by.it.group451002.koltsov.lesson05;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел

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

        // ищем минимальный и максимальный элементы
        int maxElem = 0;
        int minElem = Integer.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            if (points[i] > maxElem)
                maxElem = points[i];
            if (points[i] < minElem)
                minElem = points[i];
        }

        int[] reps = new int[maxElem - minElem + 1];

        // подсчитываем повторения
        for (int i = 0; i < points.length; i++)
            reps[points[i] - minElem]++;

        // перезаписываем массив
        int index = 0;
        for (int i = 0; i < reps.length; i++)
            for (int j = 0; j < reps[i]; j++)
                points[index++] = i + minElem;

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}
