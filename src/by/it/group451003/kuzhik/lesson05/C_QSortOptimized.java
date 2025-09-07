package by.it.group451003.kuzhik.lesson05;

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
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            segments[i] = new Segment(Math.min(a, b), Math.max(a, b));
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Быстрая сортировка с 3-разбиением
        quickSort3Way(segments, 0, n - 1);

        // Для каждой точки ищем количество отрезков, её содержащих
        for (int i = 0; i < m; i++) {
            int count = 0;
            int point = points[i];

            int index = binarySearchFirst(segments, point);

            if (index != -1) {
                for (int j = index; j < n && segments[j].start <= point; j++) {
                    if (segments[j].stop >= point) {
                        count++;
                    }
                }
            }

            result[i] = count;
        }

        return result;
    }

    // Быстрая сортировка с трёхразбиением и элиминацией хвостовой рекурсии
    private void quickSort3Way(Segment[] a, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;
            Segment pivot = a[low];
            int i = low + 1;
            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if (cmp < 0) swap(a, lt++, i++);
                else if (cmp > 0) swap(a, i, gt--);
                else i++;
            }
            // Рекурсивно сортируем меньшую часть, потом итеративно большую
            if (lt - low < high - gt) {
                quickSort3Way(a, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort3Way(a, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    // Бинарный поиск первого отрезка, который может содержать точку
    private int binarySearchFirst(Segment[] a, int point) {
        int left = 0, right = a.length - 1;
        int res = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (a[mid].start <= point) {
                if (a[mid].stop >= point) {
                    res = mid;
                    right = mid - 1; // ищем первый подходящий
                } else {
                    left = mid + 1;
                }
            } else {
                right = mid - 1;
            }
        }
        return res;
    }

    private void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // Отрезок с поддержкой сравнения по началу
    private static class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}
