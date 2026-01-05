package by.it.group410902.varava.lesson02.lesson01.lesson05;

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
    public class QuickSortSegments {

        void swap(Segment[] array, int a, int b) {
            Segment temp = array[a];
            array[a] = array[b];
            array[b] = temp;
        }

        int partition(Segment[] array, int start, int end) {
            Segment pivot = array[end];
            int i = start - 1;
            for (int j = start; j < end; j++) {
                if (array[j].compareTo(pivot) <= 0) {
                    i++;
                    swap(array, i, j);
                }
            }
            swap(array, i + 1, end);
            return i + 1;
        }

        void quickSort(Segment[] array, int start, int end) {
            if (start < end) {
                int pivot = partition(array, start, end);
                quickSort(array, start, pivot - 1);
                quickSort(array, pivot + 1, end);
            }
        }
    }

    public class QuickSortIntegers {

        void swap(int[] array, int a, int b) {
            int temp = array[a];
            array[a] = array[b];
            array[b] = temp;
        }

        int partition(int[] array, int start, int end) {
            int pivot = array[end];
            int i = start - 1;
            for (int j = start; j < end; j++) {
                if (array[j] <= pivot) {
                    i++;
                    swap(array, i, j);
                }
            }
            swap(array, i + 1, end);
            return i + 1;
        }

        void quickSort(int[] array, int start, int end) {
            if (start < end) {
                int pivot = partition(array, start, end);
                quickSort(array, start, pivot - 1);
                quickSort(array, pivot + 1, end);
            }
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
        QuickSortSegments segmentSorter = new QuickSortSegments();
        segmentSorter.quickSort(segments, 0, n - 1);

        QuickSortIntegers pointSorter = new QuickSortIntegers();
        pointSorter.quickSort(points, 0, m - 1);
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (points[i] >= segments[j].start && points[i] <= segments[j].stop) {
                    result[i]++;
                }
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
            this.start = start;
            this.stop = stop;


            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }

        }
    }


