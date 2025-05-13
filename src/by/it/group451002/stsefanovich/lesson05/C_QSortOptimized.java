package by.it.group451002.stsefanovich.lesson05;

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
            int point = points[i];
            int left = binarySearch(segments, point);
            int count = 0;
            for (int j = left; j < n && segments[j].start <= point; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    private void quickSort(Segment[] segments, int low, int high) {
        while (low < high) {
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

            if (lt - low < high - gt) {
                quickSort(segments, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort(segments, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    private int binarySearch(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1;
        int result = segments.length;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
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
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}