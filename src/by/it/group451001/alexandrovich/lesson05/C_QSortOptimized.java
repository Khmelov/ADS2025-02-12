package by.it.group451001.alexandrovich.lesson05;

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

        segments = quickSort(segments, 0, n-1);

        for (int i = 0; i < m; i++){
            int point = points[i];
            int count = 0;
            for (int j = 0; (j<n)&&(point >= segments[j].start); j++){
                if (point<= segments[j].stop) count++;
            }
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    Segment[] quickSort(Segment[] A, int l, int r)
    {
        if (l >= r) return A;
        while (l<r) {
            Segment temp;
            int x = A[(l + r)/2].start;
            int i = l;
            int j = r;
            while (i<=j) {
                while (A[i].start < x) {
                    i++;
                }
                while (A[j].start > x) {
                    j--;
                }
                if (i <= j) {
                    if (i<j) {
                        temp = A[i];
                        A[i] = A[j];
                        A[j] = temp;
                    }
                    i++;
                    j--;
                }
            }
            while (i<=r && A[i].start == x) {
                i++;
            }
            while (j >= l && A[j].start == x) {
                j--;
            }
            quickSort(A, l, j);
            l = i;
        }

        return A;
    }

    int partition(Segment[] A, int l, int r)
    {
        Segment temp;
        int x = A[(l + r)/2].start;
        int i = l;
        int j = r;
        while (i<=j) {
            while (A[i].start < x) {
                i++;
            }
            while (A[j].start > x) {
                j--;
            }
            if (i <= j) {
                if (i<j) {
                    temp = A[i];
                    A[i] = A[j];
                    A[j] = temp;
                }
                i++;
                j--;
            }
        }

        return (i+j)/2;
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Object o) {
            //подумайте, что должен возвращать компаратор отрезков
            return 0;
        }
    }

}
