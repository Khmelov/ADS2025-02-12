package by.it.group451001.yarkovich.lesson05;

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

    public static void main(String[] args) {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //число отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // сортировка отрезков
        quickSort3Way(segments, 0, segments.length - 1);

        // для каждой точки ищем количество покрытий
        for (int i = 0; i < m; i++) {
            int point = points[i];
            result[i] = countSegmentsCoveringPoint(segments, point);
        }

        return result;
    }

    // Быстрая сортировка с трехразбиением
    void quickSort3Way(Segment[] a, int low, int high) {
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

    // Считаем сколько отрезков покрывают точку (через бинарный поиск и проверку по соседям)
    int countSegmentsCoveringPoint(Segment[] segments, int point) {
        int count = 0;
        int left = 0, right = segments.length - 1;
        int idx = -1;

        // бинарный поиск первого отрезка, который может содержать точку
        while (left <= right) {
            int mid = (left + right) / 2;
            if (segments[mid].start <= point && segments[mid].stop >= point) {
                idx = mid;
                break;
            } else if (segments[mid].start > point) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        if (idx == -1) return 0;

        // проверяем от найденной позиции влево и вправо
        int i = idx;
        while (i >= 0 && segments[i].start <= point) {
            if (segments[i].stop >= point) count++;
            i--;
        }
        i = idx + 1;
        while (i < segments.length && segments[i].start <= point) {
            if (segments[i].stop >= point) count++;
            i++;
        }

        return count;
    }

    // отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment o) {
            // Сортируем сначала по началу, если равны — по концу
            if (this.start != o.start) return Integer.compare(this.start, o.start);
            return Integer.compare(this.stop, o.stop);
        }
    }
}
