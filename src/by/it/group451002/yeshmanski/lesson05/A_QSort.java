package by.it.group451002.yeshmanski.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/**
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

    int Partition(Segment[] Arr,int l, int r){
        Segment x = Arr[l];
        int j=l;
        Segment temp;
        for(int i=l+1;i<=r;i++){
            if(Arr[i].compareTo(x)<=0){
                j++;
                temp = Arr[j];
                Arr[j] = Arr[i];
                Arr[i] = temp;
            }
        }
        temp = Arr[l];
        Arr[l] = Arr[j];
        Arr[j] = temp;
        return j;
    }
    void QSort(Segment[] Arr, int l,int r){
        if(l<r){
            int m = Partition(Arr,l,r);
            QSort(Arr,l,m-1);
            QSort(Arr,m + 1,r);
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {

        Scanner scanner = new Scanner(stream);

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
        QSort(segments,0,n-1);

        //Для каждой точки проверяются все отрезки, которые начинаются до или на этой точке.
        //Если точка находится внутри отрезка, увеличивается счётчик.
        for (int i = 0;i < m;i++) {
            result[i] = 0;
            for (int j = 0; (j < n) && (segments[j].start <= points[i]); j++) {
                if (points[i] <= segments[j].stop) {
                    result[i]++;
                }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (stop > start){
                this.start = start;
                this.stop = stop;
            }else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }

}