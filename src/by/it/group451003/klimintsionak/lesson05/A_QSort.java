package by.it.group451003.klimintsionak.lesson05;

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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков
        int n = scanner.nextInt();
        //число точек
        int m = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем отрезки
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(start, stop);
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        //создаем события для sweep line
        Event[] events = new Event[2 * n + m];
        int eventIndex = 0;
        //добавляем события для начала и конца отрезков
        for (int i = 0; i < n; i++) {
            events[eventIndex++] = new Event(segments[i].start, 0, -1); // начало отрезка
            events[eventIndex++] = new Event(segments[i].stop, 2, -1);  // конец отрезка
        }
        //добавляем события для точек
        for (int i = 0; i < m; i++) {
            events[eventIndex++] = new Event(points[i], 1, i); // точка
        }

        //сортируем события по координате, затем по типу (start < point < end)
        Arrays.sort(events);

        //проходим по событиям, считая активные отрезки
        int activeSegments = 0;
        for (Event event : events) {
            if (event.type == 0) {
                //начало отрезка
                activeSegments++;
            } else if (event.type == 1) {
                //точка, записываем количество активных отрезков
                result[event.index] = activeSegments;
            } else {
                //конец отрезка
                activeSegments--;
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
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

    //класс для событий
    private class Event implements Comparable<Event> {
        long coord; //координата события
        int type;   //0 - начало отрезка, 1 - точка, 2 - конец отрезка
        int index;  //индекс точки в массиве точек (для результата)

        Event(long coord, int type, int index) {
            this.coord = coord;
            this.type = type;
            this.index = index;
        }

        @Override
        public int compareTo(Event o) {
            if (this.coord != o.coord) {
                return Long.compare(this.coord, o.coord);
            }
            return Integer.compare(this.type, o.type);
        }
    }
}