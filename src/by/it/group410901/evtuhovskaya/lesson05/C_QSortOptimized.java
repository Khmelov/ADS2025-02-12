package by.it.group410901.evtuhovskaya.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            segments[i] = new Segment(Math.min(start, end), Math.max(start, end));
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Оптимизированная сортировка отрезков по началу (3-разбиение)
        sortSegments(segments, 0, segments.length - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            // Бинарный поиск первого отрезка, который может содержать точку
            int first = findFirstSegment(segments, point);
            if (first == -1) {
                result[i] = 0;
                continue;
            }
            // Линейный подсчёт всех подходящих отрезков
            int count = 0;
            for (int j = first; j < segments.length && segments[j].start <= point; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    private void sortSegments(Segment[] segments, int low, int high) {
        while (low < high) {
            int[] pivotIndices = partition3(segments, low, high);
            sortSegments(segments, low, pivotIndices[0] - 1);
            low = pivotIndices[1] + 1; // Элиминация хвостовой рекурсии
        }
    }

    private int[] partition3(Segment[] segments, int low, int high) {
        Segment pivot = segments[low];
        int lt = low;
        int gt = high;
        int i = low + 1;

        while (i <= gt) {
            int cmp = segments[i].compareTo(pivot);
            if (cmp < 0) {
                swap(segments, lt++, i++);
            } else if (cmp > 0) {
                swap(segments, i, gt--);
            } else {
                i++;
            }
        }

        return new int[]{lt, gt};
    }

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    private int findFirstSegment(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

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