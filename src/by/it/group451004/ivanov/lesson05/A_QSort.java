package by.it.group451004.ivanov.lesson05;

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
    2 3    //2 - число включений камер, 3 - число событий
    0 5    //первое включение
    7 10   //второе включение
    1 6 11 //события
    Sample Output:
    1 0 0  //число покрытий для каждого события
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
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) { //отрезки
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) { //события
            points[i] = scanner.nextInt();
        }
        quickSort(segments, 0, n-1);

        for (int i = 0; i < m; i++) {
            result[i] = 0;
            for (int j = 0; j < n && segments[j].start <= points[i]; j++){
                if (segments[j].stop >= points[i]) {
                    result[i]++;
                }
            }
        }
        return result;
    }

    void quickSort(Segment[] array, int left, int right) {
        if (right - left < 2) {
            if (array[left].compareTo(array[right]) > 0) {
                Segment temp = array[left];
                array[left] = array[right];
                array[right] = temp;
            }
        } else {
            int mid = left + (right - left) / 2;
            int i = left;
            int j = right;
            while (i < j) {
                while (array[i].compareTo(array[mid]) < 0) {
                    i++;
                }
                while (array[j].compareTo(array[mid]) > 0) {
                    j--;
                }
                if (i >= j) {
                    break;
                }
                Segment temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
            quickSort(array, left, j);
            quickSort(array, j + 1, right);
        }
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start < stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.stop = start;
                this.start = stop;
            }
        }
        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            } else {
                return Integer.compare(this.stop, o.stop);
            }
        }
    }
}
