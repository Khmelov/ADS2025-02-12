package by.it.group410902.menshikov.lesson05;

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
        int[] points = new int[m], result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt(), stop = scanner.nextInt();
            segments[i] = new Segment(Math.min(start, stop), Math.max(start, stop));
        }

        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        // Оптимизированная сортировка отрезков
        quickSort3Way(segments, 0, segments.length - 1);

        // Для каждой точки находим количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];
            // Бинарный поиск первого подходящего отрезка
            int first = findFirstSegment(segments, point);
            if (first == -1) {
                result[i] = 0;
                continue;
            }
            // Линейный подсчет оставшихся подходящих отрезков
            int count = 0;
            for (int j = first; j < segments.length && segments[j].start <= point; j++) {
                if (point <= segments[j].stop) count++;
            }
            result[i] = count;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Оптимизированная быстрая сортировка с 3-разбиением
    private void quickSort3Way(Segment[] segments, int low, int high) {
        while (low < high) {
            int[] pivots = partition3(segments, low, high);
            if (pivots[0] - low < high - pivots[1]) {
                quickSort3Way(segments, low, pivots[0] - 1);
                low = pivots[1] + 1;
            } else {
                quickSort3Way(segments, pivots[1] + 1, high);
                high = pivots[0] - 1;
            }
        }
    }

    private int[] partition3(Segment[] segments, int low, int high) {
        Segment pivot = segments[low];
        int lt = low, gt = high, i = low + 1;

        while (i <= gt) {
            int cmp = segments[i].compareTo(pivot);
            if (cmp < 0) swap(segments, lt++, i++);
            else if (cmp > 0) swap(segments, i, gt--);
            else i++;
        }
        return new int[]{lt, gt};
    }

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    // Бинарный поиск первого отрезка, который может содержать точку
    private int findFirstSegment(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1, res = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                res = mid;
                left = mid + 1;
            } else right = mid - 1;
        }
        return res;
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
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}
