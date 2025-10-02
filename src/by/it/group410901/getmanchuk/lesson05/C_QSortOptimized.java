package by.it.group410901.getmanchuk.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Быстрая сортировка отрезков по началу (включая 3-разбиение)
        quickSort(segments, 0, segments.length - 1);

        // Для каждой точки ищем количество отрезков, в которые она входит
        for (int i = 0; i < m; i++) {
            result[i] = countContaining(segments, points[i]);
        }

        return result;
    }

    private int countContaining(Segment[] segs, int point) {
        int left = 0;
        int right = segs.length - 1;
        int firstIndex = -1;

        // бинарный поиск первого подходящего отрезка
        while (left <= right) {
            int mid = (left + right) / 2;
            if (segs[mid].start <= point) {
                if (segs[mid].stop >= point) {
                    firstIndex = mid;
                    right = mid - 1; // ищем левее
                } else {
                    left = mid + 1;
                }
            } else {
                right = mid - 1;
            }
        }

        if (firstIndex == -1) return 0;

        // с позиции firstIndex идем вправо и считаем все подходящие
        int count = 0;
        for (int i = firstIndex; i < segs.length; i++) {
            if (segs[i].start > point) break;
            if (segs[i].stop >= point) count++;
        }
        return count;
    }

    private void quickSort(Segment[] a, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;
            Segment pivot = a[low];
            int i = low;

            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if (cmp < 0) swap(a, lt++, i++);
                else if (cmp > 0) swap(a, i, gt--);
                else i++;
            }

            // хвостовая рекурсия — меньший участок сортируется рекурсивно
            if (lt - low < high - gt) {
                quickSort(a, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort(a, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

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
        public int compareTo(Segment other) {
            return Integer.compare(this.start, other.start);
        }
    }
}