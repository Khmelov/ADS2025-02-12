package by.it.group410902.siomchena.lesson05;

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

    int[] getAccessory2(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt(); // отрезки
        int m = scanner.nextInt(); // точки

        Segment[] segments = new Segment[n];
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортировка отрезков по началу
        quickSort3Way(segments, 0, segments.length - 1);

        // Создание массивов для бинарного поиска
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        // Сортируем массив концов для бинарного поиска
        quickSort(ends, 0, ends.length - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int s = upperBound(starts, point); // отрезки начались до или в этой точке
            int e = lowerBound(ends, point);   // отрезки закончились до этой точки
            result[i] = s - e;
        }

        return result;
    }

    // Трёхпутевой quicksort
    void quickSort3Way(Segment[] a, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;
            Segment v = a[low];
            int i = low + 1;
            while (i <= gt) {
                int cmp = a[i].compareTo(v);
                if (cmp < 0) swap(a, lt++, i++);
                else if (cmp > 0) swap(a, i, gt--);
                else i++;
            }
            if (lt - low < high - gt) {
                quickSort3Way(a, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort3Way(a, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // Быстрая сортировка для массива int
    void quickSort(int[] a, int low, int high) {
        if (low < high) {
            int pivot = partition(a, low, high);
            quickSort(a, low, pivot - 1);
            quickSort(a, pivot + 1, high);
        }
    }

    int partition(int[] a, int low, int high) {
        int pivot = a[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (a[j] <= pivot) {
                i++;
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        int temp = a[i + 1];
        a[i + 1] = a[high];
        a[high] = temp;
        return i + 1;
    }

    int upperBound(int[] a, int value) {
        int left = 0, right = a.length;
        while (left < right) {
            int mid = (left + right) / 2;
            if (a[mid] <= value) left = mid + 1;
            else right = mid;
        }
        return left;
    }

    int lowerBound(int[] a, int value) {
        int left = 0, right = a.length;
        while (left < right) {
            int mid = (left + right) / 2;
            if (a[mid] < value) left = mid + 1;
            else right = mid;
        }
        return left;
    }

    private static class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment other) {
            if (this.start != other.start) return Integer.compare(this.start, other.start);
            return Integer.compare(this.stop, other.stop);
        }
    }
}