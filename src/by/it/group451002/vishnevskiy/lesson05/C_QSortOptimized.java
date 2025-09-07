package by.it.group451002.vishnevskiy.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче A, но с оптимизациями.
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
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);

        // количество отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        // количество точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // читаем отрезки
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            // убеждаемся, что start <= stop
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }
            segments[i] = new Segment(start, stop);
        }

        // читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // сортируем отрезки по началу (и по стопу, если начало одинаковое)
        quickSort3Way(segments, 0, segments.length - 1);

        // для каждой точки считаем, сколько отрезков её покрывает
        for (int i = 0; i < m; i++) {
            int point = points[i];
            // бинарный поиск первого подходящего отрезка
            int count = 0;
            int left = 0, right = segments.length - 1;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].start <= point) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            // отрезки с индексами [0, right] имеют start <= point
            for (int j = 0; j <= right; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    // Быстрая сортировка с трехпутевым разбиением и устранением хвостовой рекурсии
    private void quickSort3Way(Segment[] a, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;
            Segment pivot = a[low];
            int i = low + 1;
            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(a, lt++, i++);
                } else if (cmp > 0) {
                    swap(a, i, gt--);
                } else {
                    i++;
                }
            }
            // рекурсивно сортируем меньшую часть, а затем двигаем low
            if ((lt - low) < (high - gt)) {
                quickSort3Way(a, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort3Way(a, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    // Обмен элементов
    private void swap(Segment[] a, int i, int j) {
        Segment tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    // Класс отрезков с компаратором
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start); // сравнение по старту
            }
            return Integer.compare(this.stop, o.stop); // сравнение по стопу при равных стартах
        }
    }
}
