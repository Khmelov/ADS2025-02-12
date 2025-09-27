package by.it.group451002.buyel.lesson05;

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
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        QuickSortSegments(segments, 0, segments.length-1);

        QuickSortPoints(points, 0, points.length-1);

        // Бинарный поиск всех для всех точек
        for (int i = 0; i < points.length; i++) {
            int mid, left, right;
            left = 0;
            right = segments.length - 1;

            // Пока левый индекс не больше правого
            while (left <= right) {
                mid = (left + right) / 2;

                // если попали
                if (segments[mid].start <= points[i] && segments[mid].stop >= points[i]) {
                    result[i]++;
                    break;
                }
                // Если значение меньше найденого числа
                else if (segments[mid].start > points[i]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Сортировка QuickSort для диапазона start -> stop
    private void QuickSortSegments(Segment[] array, int left, int right) {
        if (left >= right) {
            return;
        }

        // Разделение массива array на 2 части
        int mid = PartitionSegments(array, left, right);
        QuickSortSegments(array, left, mid - 1);   // левая часть
        QuickSortSegments(array, mid + 1, right);   // правая часть
    }

    private int PartitionSegments(Segment[] thisArray, int thisLeft, int thisRight) {
        Segment tempSeg = thisArray[thisLeft];
        int index = thisLeft;

        for (int i = thisLeft; i <= thisRight; i++) {
            // Если tempSeg > thisArray[i]
            if (tempSeg.compareTo(thisArray[i]) > 0) {
                index++;

                Segment swap = thisArray[index];
                thisArray[index] = thisArray[i];
                thisArray[i] = swap;
            }
        }

        Segment swap = thisArray[thisLeft];
        thisArray[thisLeft] = thisArray[index];
        thisArray[index] = swap;

        return index;
    }

    // QuickSort для точек
    private void QuickSortPoints(int[] array, int left, int right) {
        if (left >= right) {
            return;
        }

        // Разделение массива array на 2 части
        int mid = PartitionPoints(array, left, right);
        QuickSortPoints(array, left, mid - 1);   // левая часть
        QuickSortPoints(array, mid + 1, right);   // правая часть
    }

    private int PartitionPoints(int[] thisPoints, int thisLeft, int thisRight) {
        int tempPoint = thisPoints[thisLeft];
        int index = thisLeft;

        for (int i = thisLeft + 1; i <= thisRight; i++) {
            if (thisPoints[i] < tempPoint) {
                index++;

                int swap = thisPoints[index];
                thisPoints[index] = thisPoints[i];
                thisPoints[i] = swap;
            }
        }

        int swap = thisPoints[thisLeft];
        thisPoints[thisLeft] = thisPoints[index];
        thisPoints[index] = swap;

        return index;
    }


    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start < stop) {
                this.start = start;
                this.stop = stop;
            }
            else {
                this.start = stop;
                this.stop = start;
            }
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            return this.start - o.start;
        }
    }

}
