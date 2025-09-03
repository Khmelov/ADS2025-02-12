package by.it.group451004.romanovskaya.lesson05;

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
        quickSort(segments, 0, segments.length - 1);

        for (int i = 0; i < m; i++) {
            result[i] = countContainingSegments(segments, points[i]);
        }

        return result;
    }

    private int countContainingSegments(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int firstIndex = -1;

        // бинарный поиск первого отрезка, который может содержать точку
        while (left <= right) {
            int mid = (left + right) / 2;
            if (segments[mid].start <= point) {
                if (segments[mid].stop >= point) {
                    firstIndex = mid;
                }
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        if (firstIndex == -1) return 0;

        int count = 0;
        // влево от firstIndex
        for (int i = firstIndex; i >= 0 && segments[i].start <= point; i--) {
            if (segments[i].stop >= point) {
                count++;
            }
        }
        // вправо от firstIndex + 1
        for (int i = firstIndex + 1; i < segments.length && segments[i].start <= point; i++) {
            if (segments[i].stop >= point) {
                count++;
            }
        }

        return count;
    }

    private void quickSort(Segment[] a, int lo, int hi) {
        while (lo < hi) {
            int lt = lo, gt = hi;
            Segment v = a[lo];
            int i = lo + 1;
            while (i <= gt) {
                int cmp = a[i].compareTo(v);
                if (cmp < 0) swap(a, lt++, i++);
                else if (cmp > 0) swap(a, i, gt--);
                else i++;
            }

            if (lt - lo < hi - gt) {
                quickSort(a, lo, lt - 1);
                lo = gt + 1; // tail recursion elimination
            } else {
                quickSort(a, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    private void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
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
            if (this.start != other.start) {
                return Integer.compare(this.start, other.start);
            } else {
                return Integer.compare(this.stop, other.stop);
            }
        }
    }
}

