package by.it.group451002.shandr.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче A.
    По сравнению с задачей A доработайте алгоритм так, чтобы
    1) он оптимально использовал время и память:
        - за стек отвечает элиминация хвостовой рекурсии
        - за сам массив отрезков — сортировка на месте
        - рекурсивные вызовы должны проводиться на основе 3-разбиения
    2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
       для определения границ (сколько отрезков начинается не позже точки
       и сколько заканчивается строго до точки), а разность даёт ответ.
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
        for (int count : result) {
            System.out.print(count + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        // Читаем n и m из первой строки
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        // Массив отрезков
        Segment[] segments = new Segment[n];
        for (int i = 0; i < n; i++) {
            int s = scanner.nextInt();
            int e = scanner.nextInt();
            segments[i] = new Segment(s, e);
        }
        // Массив точек
        int[] points = new int[m];
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по start (3-way quicksort, хвостовая элиминация)
        quickSortSegments(segments, 0, n - 1);
        // Собираем и сортируем отдельно массив концов
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            stops[i] = segments[i].stop;
        }
        quickSortInts(stops, 0, n - 1);

        // Для каждой точки считаем:
        // countStart  = # отрезков с start ≤ p
        // countFinish = # отрезков с stop  < p
        // ответ = countStart – countFinish
        int[] result = new int[m];
        for (int i = 0; i < m; i++) {
            int p = points[i];
            int countStart  = upperBoundStart(segments, p);
            int countFinish = lowerBoundStop(stops, p);
            result[i] = countStart - countFinish;
        }
        return result;
    }

    // 3-way quicksort для Segment[] по полю start + хвостовая элиминация
    private void quickSortSegments(Segment[] a, int lo, int hi) {
        while (lo < hi) {
            int lt = lo, i = lo, gt = hi;
            int pivot = a[lo + (hi - lo) / 2].start;
            while (i <= gt) {
                if (a[i].start < pivot)      swap(a, lt++, i++);
                else if (a[i].start > pivot) swap(a, i, gt--);
                else                          i++;
            }
            // решаем, какую часть рекурсивно, какую хвостом
            if (lt - lo < hi - gt) {
                quickSortSegments(a, lo, lt - 1);
                lo = gt + 1;
            } else {
                quickSortSegments(a, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    private void swap(Segment[] a, int i, int j) {
        Segment tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    // 3-way quicksort для int[] + хвостовая элиминация
    private void quickSortInts(int[] a, int lo, int hi) {
        while (lo < hi) {
            int lt = lo, i = lo, gt = hi;
            int pivot = a[lo + (hi - lo) / 2];
            while (i <= gt) {
                if (a[i] < pivot)      { int t = a[lt]; a[lt++] = a[i]; a[i++] = t; }
                else if (a[i] > pivot) { int t = a[i]; a[i] = a[gt]; a[gt--] = t; }
                else                   { i++; }
            }
            if (lt - lo < hi - gt) {
                quickSortInts(a, lo, lt - 1);
                lo = gt + 1;
            } else {
                quickSortInts(a, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    // возвращает # отрезков с start ≤ key
    private int upperBoundStart(Segment[] a, int key) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid].start <= key) lo = mid + 1;
            else                      hi = mid;
        }
        return lo;
    }

    // возвращает # значений stops < key
    private int lowerBoundStop(int[] a, int key) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] < key) lo = mid + 1;
            else               hi = mid;
        }
        return lo;
    }

    // класс-отрезок без лишних интерфейсов
    private static class Segment {
        final int start, stop;
        Segment(int s, int e) {
            if (s <= e) { start = s; stop = e; }
            else        { start = e; stop = s; }
        }
    }
}