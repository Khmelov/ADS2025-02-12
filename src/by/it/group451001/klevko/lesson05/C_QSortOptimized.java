package by.it.group451001.klevko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
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
            static Segment[] sortArr;
            public static void Do(Segment[] arr, int L, int R){
                sortArr = arr;
                DoStep(L, R);
            }
            private static void swap(int index1, int index2){
                Segment temp = sortArr[index1];
                sortArr[index1] = sortArr[index2];
                sortArr[index2] = temp;
            }
            private static void DoStep(int L, int R){

                while ((R - L) > 1) {
                    int Lnow = L, Rnow = R, i = R-1, center = (L + R) / 2;
                    //choose pivot
                    Segment pivot;
                    if (((sortArr[L].compareTo(sortArr[center]) >= 0) && (sortArr[center].compareTo(sortArr[R]) >= 0)) ||
                            ((sortArr[L].compareTo(sortArr[center]) <= 0) && (sortArr[center].compareTo(sortArr[R]) <= 0)))
                        swap(center, R);
                    else if (((sortArr[center].compareTo(sortArr[L]) >= 0) && (sortArr[L].compareTo(sortArr[R]) >= 0)) ||
                            ((sortArr[center].compareTo(sortArr[L]) <= 0) && (sortArr[L].compareTo(sortArr[R]) <= 0))) swap(L, R);
                    pivot = sortArr[R];
                    //start sort
                    while (i >= Lnow) {
                        if (sortArr[i].compareTo(pivot) < 0) {
                            swap(i, Lnow);
                            ++Lnow;
                        } else if (sortArr[i].compareTo(pivot) > 0) {
                            swap(i, Rnow);
                            --i;
                            --Rnow;
                        } else --i;
                    }
                    if ((Lnow-L) > (R-Rnow)) {
                        DoStep(Rnow, R);
                        R = (Lnow - 1);
                    }
                    else {
                        DoStep(L, Lnow - 1);
                        L = Rnow;
                    }
                }
            }
        }
        class binFind{
            public static int Find(Segment[] arr, int value){
                int L = 0;
                int R = arr.length-1;
                int ans = -1;
                boolean stop = true;
                while ((stop) && (L <= R)) {
                    int check = (L + R) / 2;
                    if (arr[check].start <= value){
                        if (check != R){
                            if (arr[check+1].start > value) {
                                ans = check;
                                stop = false;
                            }
                        } else {
                            ans = check;
                            stop = false;
                        }

                    }
                    if (stop) {
                        if (arr[check].start > value) {
                            R = check-1;
                        } else if (arr[check].start < value) {
                            L = check+1;
                        }
                    }
                }
                return ans;
            }
        }
        myQuickSort.Do(segments, 0, segments.length-1);

        int index = 0;
        for (int i: points) {
            int ans = 0;
            int start = binFind.Find(segments, i);
            if (start != -1) {
                while (start >= 0){
                    if (segments[start].stop >= i) ++ans;
                    --start;
                }
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
        }

        @Override
        public int compareTo(Segment other) {
            //подумайте, что должен возвращать компаратор отрезков
            return Integer.compare(this.start, other.start);
        }
    }

}
