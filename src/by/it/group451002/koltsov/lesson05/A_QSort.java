package by.it.group451002.koltsov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;
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

Точка считается принадлежащей отрезку, если она находится внутри него или на границе

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

        // используя алгоритм QuickSort сортируем массив "segments"
        QuickSort(0, segments.length - 1, segments);

        // для каждой точки, до тех пор, пока начало записи камеры из массива segments < точки
        // если конец записи > точки добавляем к соответствующему элементу массива result 1
        for (int i = 0; i < points.length; i++) {
            int j = 0;
            while (j < segments.length && segments[j].start < points[i])
            {
                if (segments[j].stop >= points[i])
                    result[i]++;
                j++;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            }
            else {
                this.stop = start;
                this.start = stop;
            }
        }

        @Override
        public int compareTo(Segment o) {
            // если начало < и конец <,  то и сегмент <
            if (this.start < o.start || this.start == o.start && this.stop < o.stop)
                return -1;
            // если начало = и конец =,  то и сегмент =
            if (this.start == o.start && this.stop == o.stop)
                return 0;
            // иначе сегмент >
            return 1;
        }
    }

    public void QuickSort(int l, int r, Segment[] arr) {
        int i = l, j = r;
        Segment pivot = arr[(l + r + 1) / 2];
        Segment tempSeg = new Segment(0, 0);

        do {
            // ищем элемент >= опорного
            while (i <= j && arr[i].compareTo(pivot) == -1)
                i++;

            // ищем элемент <= опорного
            while (j >= i && arr[j].compareTo(pivot) == 1)
                j--;

            // если нужно, обмениваем элементы
            if (j >= i) {
                tempSeg = arr[i];
                arr[i] = arr[j];
                arr[j] = tempSeg;
                i++;
                j--;
            }

            // если есть что сортировать, заправшиваем сортировку левого/правого подмассива
            if (i > j) {
                if (j > l)
                    QuickSort(l, j, arr);
                if (i < r)
                    QuickSort(i, r, arr);
            }

        } while (i <= j);
    }
}
