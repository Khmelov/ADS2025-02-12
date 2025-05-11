package by.it.group451001.klevko.lesson05;

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

    /*void Sort(int L, int R){



    }*/

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
        class myQuickSort{
            public static void Do(Segment[] arr, int L, int R){
                if ((R - L) < 2) return;
                int Lnow = L, Rnow = R, center = (L+R)/2;
                Segment pivot;
                if (((arr[L].compareTo(arr[center]) >= 0)&&(arr[center].compareTo(arr[R]) >= 0))||
                        ((arr[L].compareTo(arr[center]) <= 0)&&(arr[center].compareTo(arr[R]) <= 0))) pivot = arr[center];
                else if (((arr[center].compareTo(arr[L]) >= 0)&&(arr[L].compareTo(arr[R]) >= 0))||
                        ((arr[center].compareTo(arr[L]) <= 0)&&(arr[L].compareTo(arr[R]) <= 0))) pivot = arr[L];
                else pivot = arr[R];

                while (Lnow < Rnow) {
                    while (arr[Lnow].compareTo(pivot) < 0) Lnow++;
                    while (arr[Rnow].compareTo(pivot) > 0) Rnow--;
                    if (Lnow < Rnow) {
                        Segment temp = arr[Lnow];
                        arr[Lnow] = arr[Rnow];
                        arr[Rnow] = temp;
                        Lnow++;
                        Rnow--;
                    }
                }
                Do(arr, L, Lnow-1);
                Do(arr, Rnow+1, R);
            }
        }

        myQuickSort.Do(segments, 0, segments.length-1);
        int index = 0;
        for (int i: points){
            int ans = 0;
            for (Segment now: segments) {
                if (i < now.start) break;
                if ((i >= now.start) && (i <= now.stop)) ans++;
            }
            result[index] = ans;
            index++;
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
        public int compareTo(Segment other) {
            //подумайте, что должен возвращать компаратор отрезков
            return Integer.compare(this.start, other.start);
            /*if (this.start == other.start) return 0;
            else if (this.start > other.start) return 1;
            else return -1;*/
        }
    }

}
