package by.it.group451003.avrusevich.lesson05;

import java.io.FileInputStream;
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

    static void swap(Segment segment1, Segment segment2) {
        Segment temp = segment1;
        segment1 = segment2;
        segment2 = temp;
    }

    private static int partition(Segment[] segments, int low, int high) {
        int middle = (low + high) / 2;
        Segment pivot = segments[middle];
        int leftIndex = low;
        int rightIndex = high;
        while (leftIndex <= rightIndex) {
            while (segments[leftIndex].compareTo(pivot) == -1)
                leftIndex++;
            while (segments[rightIndex].compareTo(pivot) == 1)
                rightIndex--;

            if (leftIndex <= rightIndex) {
                swap(segments[leftIndex], segments[rightIndex]);
                leftIndex++;
                rightIndex--;
            }
        }
        return leftIndex;
    }
    static void quickSort(Segment[] segments, int low, int high) {
        if (low < high) {
            int pi = partition(segments, low, high);
            quickSort(segments, low, pi - 1);
            quickSort(segments, pi + 1, high);
        }
    }

    //отрезок
    private class Segment  implements Comparable<Segment>{
        int start;
        int stop;

        Segment(int start, int stop){
            this.start = start;
            this.stop = stop;
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            if (this.start == o.start && this.stop == o.stop)
                return 0;
            else if (this.start > o.start || this.start == o.start && this.stop > o.stop)
                return 1;
            else
                return -1;
        }
    }


    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments=new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points=new int[m];
        int[] result=new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i]=new Segment(scanner.nextInt(),scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i]=scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        quickSort(segments, 0, n - 1);

        // Для каждой точки определяем количество отрезков, которым она принадлежит
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // Используем бинарный поиск для определения количества отрезков
            int left = 0;
            int right = n - 1;
            while (left <= right) {
                int mid = (right + left) / 2;

                if (segments[mid].start <= point && point <= segments[mid].stop) {
                    count++;
                    break;
                } else if (point < segments[mid].start) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }

            result[i] = count;
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/a_khmelev/lesson05/dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result=instance.getAccessory(stream);
        for (int index:result){
            System.out.print(index+" ");
        }
    }

}