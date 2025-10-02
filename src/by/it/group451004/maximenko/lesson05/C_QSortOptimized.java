package by.it.group451004.maximenko.lesson05;

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

        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            result[i] = countSegmentsContainingPoint(segments, points[i]);
        }

        return result;
    }

    void quickSort(Segment[] a, int low, int high) {
        while (low < high) {
            int[] mid = partition3(a, low, high);
            if (mid[0] - low < high - mid[1]) {
                quickSort(a, low, mid[0] - 1);
                low = mid[1] + 1;
            } else {
                quickSort(a, mid[1] + 1, high);
                high = mid[0] - 1;
            }
        }
    }

    int[] partition3(Segment[] a, int low, int high) {
        Segment pivot = a[low];
        int lt = low;
        int gt = high;
        int i = low + 1;
        while (i <= gt) {
            if (a[i].compareTo(pivot) < 0) {
                swap(a, lt++, i++);
            } else if (a[i].compareTo(pivot) > 0) {
                swap(a, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    int countSegmentsContainingPoint(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int count = 0;

        // Находим первый сегмент, который потенциально может содержать точку
        while (left <= right) {
            int mid = (left + right) / 2;
            if (segments[mid].start <= point) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // Теперь проверяем от найденного индекса влево
        for (int i = 0; i <= right; i++) {
            if (segments[i].start <= point && segments[i].stop >= point) {
                count++;
            }
        }

        return count;
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
        public int compareTo(Segment o) {
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            } else {
                return Integer.compare(this.stop, o.stop);
            }
        }
    }
}
