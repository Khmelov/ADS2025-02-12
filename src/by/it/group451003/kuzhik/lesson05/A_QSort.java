package by.it.group451003.kuzhik.lesson05;

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

    public static void main(String[] args) {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt(); // количество отрезков
        int m = scanner.nextInt(); // количество точек

        int[] starts = new int[n];
        int[] ends = new int[n];
        int[] points = new int[m];

        for (int i = 0; i < n; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            if (a > b) {
                int temp = a;
                a = b;
                b = temp;
            }
            starts[i] = a;
            ends[i] = b;
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем начала и концы отрезков
        Arrays.sort(starts);
        Arrays.sort(ends);

        int[] result = new int[m];

        for (int i = 0; i < m; i++) {
            int point = points[i];

            int left = upperBound(starts, point); // сколько отрезков началось до или в этой точке
            int right = lowerBound(ends, point);  // сколько отрезков закончилось строго до этой точки

            result[i] = left - right;
        }

        return result;
    }

    // Индекс первого элемента > value
    private int upperBound(int[] a, int value) {
        int low = 0, high = a.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (a[mid] <= value) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // Индекс первого элемента >= value
    private int lowerBound(int[] a, int value) {
        int low = 0, high = a.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (a[mid] < value) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // Класс отрезка уже не нужен, но оставим пустым
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}
