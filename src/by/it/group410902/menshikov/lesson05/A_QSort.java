package by.it.group410902.menshikov.lesson05;

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
        int[] points = new int[m], result = new int[m];

        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            int start = scanner.nextInt(), stop = scanner.nextInt();
            //упорядочиваем отрезки
            segments[i] = new Segment(Math.min(start, stop), Math.max(start, stop));
        }

        //читаем точки
        for (int i = 0; i < m; i++) points[i] = scanner.nextInt();
        //сортируем отрезки по началу
        quickSort(segments, 0, segments.length - 1);

        //для каждой точки считаем количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;
            //используем бинарный поиск для оптимизации
            int l = 0, r = segments.length - 1;
            while (l <= r) {
                int mid = l + (r - l) / 2;
                if (segments[mid].start <= point) l = mid + 1;
                else r = mid - 1;
            }

            //проверяем все подходящие отрезки
            for (int j = 0; j <= r; j++) {
                if (segments[j].start <= point && point <= segments[j].stop) {
                    count++;
                }
            }
            result[i] = count;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    //реализация быстрой сортировки
    private void quickSort(Segment[] segments, int low, int high) {
        if (low < high) {
            int pi = partition(segments, low, high);
            quickSort(segments, low, pi - 1);
            quickSort(segments, pi + 1, high);
        }
    }

    private int partition(Segment[] segments, int low, int high) {
        Segment pivot = segments[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (segments[j].compareTo(pivot) <= 0) {
                i++;
                Segment temp = segments[i];
                segments[i] = segments[j];
                segments[j] = temp;
            }
        }
        Segment temp = segments[i + 1];
        segments[i + 1] = segments[high];
        segments[high] = temp;
        return i + 1;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            return Integer.compare(this.start, o.start);
        }
    }
}
