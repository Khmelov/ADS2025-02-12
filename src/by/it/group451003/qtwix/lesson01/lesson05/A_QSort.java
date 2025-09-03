package by.it.group451003.qtwix.lesson01.lesson05;

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
            result[i] = 0;
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        QSort(segments, 0, n - 1);
        for (int i = 0; i < m; i++) {
            int j = 0;
            while(j < n && points[i] > segments[j].start) {
                if(points[i] < segments[j].stop) {
                    result[i]++;
                }
                j++;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    void QSort(Segment[] segments, int start, int end) {
        if (start == end) {
            return;
        }
        int n = end - start + 1;
        int mid = (start + end) / 2;
        Segment temp = segments[mid], temp1;
        int i = start, j = end;
        while (i < j)
        {
            while(segments[i].compareTo(temp) < 0) {
                ++i;
            }
            while(segments[j].compareTo(temp) > 0) {
                --j;
            }
            if (i <= j) {
                temp1 = segments[i];
                segments[i] = segments[j];
                segments[j] = temp1;
                ++i;
                --j;
            }
        }

        //System.out.println(start + " " + end + " " + i + " " + j);
        if(i < end)
        {
            QSort(segments, i, end);
        }
        if(j > start)
        {
            QSort(segments, start, j);
        }
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
            //подумайте, что должен возвращать компаратор отрезков
            if(start > o.start) {
                return 1;
            }
            if(start < o.start) {
                return -1;
            }
            return 0;
        }
    }
}
