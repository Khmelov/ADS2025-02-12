package by.it.group451004.romanovskaya.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        quickSortSegments(segments, 0, segments.length - 1);

        // Копируем и сортируем точки с индексами
        int[][] pointIndexPairs = new int[m][2]; // [][0] = value, [][1] = index
        for (int i = 0; i < m; i++) {
            pointIndexPairs[i][0] = points[i];
            pointIndexPairs[i][1] = i;
        }
        quickSortPoints(pointIndexPairs, 0, m - 1);

        for (int i = 0; i < m; i++) {
            int point = pointIndexPairs[i][0];
            int index = pointIndexPairs[i][1];
            result[index] = countSegmentsCoveringPoint(segments, point);
        }

        return result;
    }

    // Считаем сколько отрезков покрывают точку (бинпоиск по start и stop)
    private int countSegmentsCoveringPoint(Segment[] segments, int point) {
        int count = 0;
        for (Segment s : segments) {
            if (s.start > point) break;
            if (s.start <= point && s.stop >= point) {
                count++;
            }
        }
        return count;
    }

    // Быстрая сортировка отрезков
    private void quickSortSegments(Segment[] arr, int left, int right) {
        if (left >= right) return;
        Segment pivot = arr[(left + right) / 2];
        int i = left, j = right;
        while (i <= j) {
            while (arr[i].compareTo(pivot) < 0) i++;
            while (arr[j].compareTo(pivot) > 0) j--;
            if (i <= j) {
                Segment tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }
        quickSortSegments(arr, left, j);
        quickSortSegments(arr, i, right);
    }

    // Быстрая сортировка точек
    private void quickSortPoints(int[][] arr, int left, int right) {
        if (left >= right) return;
        int[] pivot = arr[(left + right) / 2];
        int i = left, j = right;
        while (i <= j) {
            while (arr[i][0] < pivot[0]) i++;
            while (arr[j][0] > pivot[0]) j--;
            if (i <= j) {
                int[] tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }
        quickSortPoints(arr, left, j);
        quickSortPoints(arr, i, right);
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
                if (start <= stop) {
                    this.start = start;
                    this.stop = stop;
                } else {
                    this.start = stop;
                    this.stop = start;
                }
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков

            return Integer.compare(this.start, o.start);
        }
    }

}
