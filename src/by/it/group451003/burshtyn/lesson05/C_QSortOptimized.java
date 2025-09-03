package by.it.group451003.burshtyn.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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

        // 1) Читаем вход
        int n = scanner.nextInt();            // число отрезков
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();            // число точек
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(
                    scanner.nextInt(),
                    scanner.nextInt()
            );
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // 2) Извлекаем два примитивных массива: starts[] и ends[]
        int[] starts = new int[n];
        int[] ends   = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i]   = segments[i].stop;
        }

        // 3) Сортируем оба массива in‑place трёхпутёвым QuickSort’ом
        quickSort3Way(starts, 0, n - 1);
        quickSort3Way(ends,   0, n - 1);

        // 4) Для каждой точки считаем:
        //    countStarts = # starts <= point
        //    countEndsBefore = # ends   <  point
        //    результат = countStarts - countEndsBefore
        for (int i = 0; i < m; i++) {
            int p = points[i];
            int countStarts     = countLessOrEqual(starts, p);
            int countEndsBefore = countLess(ends, p);
            result[i] = countStarts - countEndsBefore;
        }

        return result;
    }

    // ==== 3‑way QuickSort с элиминацией хвостовой рекурсии ====
    private void quickSort3Way(int[] a, int lo, int hi) {
        while (lo < hi) {
            int lt = lo, gt = hi;
            int pivot = a[lo + (hi - lo) / 2];
            int i = lo;
            // 3‑путевое разбиение
            while (i <= gt) {
                if (a[i] < pivot)      swap(a, lt++, i++);
                else if (a[i] > pivot) swap(a, i, gt--);
                else                   i++;
            }
            // Сначала рекурсия на меньшую часть
            if (lt - lo < hi - gt) {
                quickSort3Way(a, lo, lt - 1);
                lo = gt + 1;       // элиминируем хвостовую рекурсию
            } else {
                quickSort3Way(a, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    private void swap(int[] a, int i, int j) {
        int tmp = a[i]; a[i] = a[j]; a[j] = tmp;
    }

    // ==== Бинарный поиск ====

    // число элементов <= x
    private int countLessOrEqual(int[] a, int x) {
        int lo = 0, hi = a.length - 1, idx = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] <= x) {
                idx = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return idx + 1;
    }

    // число элементов < x
    private int countLess(int[] a, int x) {
        int lo = 0, hi = a.length - 1, idx = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] < x) {
                idx = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return idx + 1;
    }

    // ==== Класс отрезка (для чтения) ====
    private class Segment implements Comparable<Segment> {
        int start, stop;
        Segment(int s, int e) { start = s; stop = e; }

        @Override
        public int compareTo(Segment o) {
            // если нужно было бы сортировать сам массив сегментов
            return Integer.compare(this.start, o.start);
        }
    }
}
