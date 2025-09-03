package by.it.group410901.gutseva.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

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
        // Чтение количества отрезков (n) и количества точек (m)
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение отрезков (временных интервалов работы камер)
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            // Создаем отрезок, автоматически упорядочивая начало и конец
            segments[i] = new Segment(start, stop);
        }

        // Чтение точек (моментов времени событий)
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

        // Сортировка массивов начал и концов отрезков
        Arrays.sort(starts);
        Arrays.sort(ends);

        // Для каждой точки вычисляем количество покрывающих её отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Количество отрезков, которые начались до или в момент точки
            int left = binarySearchCount(starts, point, true);

            // Количество отрезков, которые закончились до точки, (не включая точку)
            int right = binarySearchCount(ends, point, false);

            // Разница дает количество отрезков, содержащих точку
            result[i] = left - right;
        }

        return result;
    }

    // Вспомогательная функция для бинарного поиска количества элементов <= или < x
    private int binarySearchCount(int[] array, int x, boolean includeEqual) {
        int left = 0;
        int right = array.length - 1;
        int count = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (includeEqual ? array[mid] <= x : array[mid] < x) {
                count = mid + 1;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return count;
    }

    // Класс отрезка с автоматическим упорядочиванием начала и конца
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // Автоматически упорядочиваем начало и конец отрезка
            this.start = Math.min(start,stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            // Сравниваем отрезки по началу
            return Integer.compare(this.start, o.start);
        }
    }
}
