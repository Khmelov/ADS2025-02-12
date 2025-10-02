package by.it.group410901.volkov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Видеорегистраторы и площадь.
На площади установлена одна или несколько камер.
Известны данные о том, когда каждая из них включалась и выключалась (отрезки работы)
Известен список событий на площади (время начала каждого события).
Вам необходимо определить для каждого события сколько камер его записали.

В первой строке задано два целых числа:
    число включений камер (отрезки) 1<=n<=50000
    число событий (точки) 1<=m<=50000.

Следующие n строк содержат по два целых числа ai и bi (ai<=bi) -
координаты концов отрезков (время работы одной какой-то камеры).
Последняя строка содержит m целых чисел - координаты точек.
Все координаты не превышают 10E8 по модулю (!).

Точка считается принадлежащей отрезку, если она находится внутри него или на границе.

Для каждой точки в порядке их появления во вводе выведите,
скольким отрезкам она принадлежит.
    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        // Чтение количества отрезков и точек
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение отрезков
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            // Упорядочиваем начало и конец отрезка (на случай, если start > end)
            segments[i] = new Segment(Math.min(start, end), Math.max(start, end));
        }

        // Чтение точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем начала и концы отрезков отдельно
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }
        Arrays.sort(starts);
        Arrays.sort(ends);

        // Для каждой точки находим количество отрезков, которые её содержат
        for (int i = 0; i < m; i++) {
            int point = points[i];
            // Количество отрезков, у которых start <= point
            int left = binarySearchCount(starts, point, true);
            // Количество отрезков, у которых end < point
            int right = binarySearchCount(ends, point, false);
            // Разница дает количество отрезков, содержащих точку
            result[i] = left - right;
        }

        return result;
    }

    // Бинарный поиск для подсчета количества элементов <= (или <) заданного значения
    private int binarySearchCount(int[] array, int key, boolean isStart) {
        int left = 0;
        int right = array.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if ((isStart && array[mid] <= key) || (!isStart && array[mid] < key)) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    // Класс отрезка
    private class Segment {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }
    }
}
