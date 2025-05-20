package by.it.group451002.mitskevich.lesson05;

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

Точка считается принадлежащей отрезку, если она находится внутри него или на границе.

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

        // Считываем количество отрезков и точек
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Считываем отрезки
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            // Гарантируем, что start <= stop
            if (start > stop) {
                int tmp = start;
                start = stop;
                stop = tmp;
            }
            segments[i] = new Segment(start, stop);
        }

        // Считываем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по началу
        quickSort(segments, 0, segments.length - 1);

        // Для каждой точки считаем количество отрезков, которым она принадлежит
        for (int i = 0; i < m; i++) {
            int count = 0;
            for (Segment seg : segments) {
                if (seg.start > points[i]) {
                    break; // Оптимизация — дальше только больше start
                }
                if (points[i] >= seg.start && points[i] <= seg.stop) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    // Быстрая сортировка массива отрезков
    private void quickSort(Segment[] arr, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(arr, left, right);
            quickSort(arr, left, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, right);
        }
    }

    private int partition(Segment[] arr, int left, int right) {
        Segment pivot = arr[right];
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (arr[j].compareTo(pivot) <= 0) {
                i++;
                Segment temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        Segment temp = arr[i + 1];
        arr[i + 1] = arr[right];
        arr[right] = temp;
        return i + 1;
    }

    // Класс отрезка с реализацией сравнения по началу
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment other) {
            return Integer.compare(this.start, other.start);
        }
    }
}
