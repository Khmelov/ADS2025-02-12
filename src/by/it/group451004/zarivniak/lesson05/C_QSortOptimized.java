package by.it.group451004.zarivniak.lesson05;

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
            result[i] = countSegmentsForPoint(segments, points[i]);
        }

        return result;
    }

    private void quickSort(Segment[] segments, int left, int right) {
        while (left < right) {
            int[] pivotInfo = partition(segments, left, right);
            int lt = pivotInfo[0];
            int gt = pivotInfo[1];

            if (lt - left < right - gt) {
                quickSort(segments, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort(segments, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    private int[] partition(Segment[] segments, int left, int right) {
        int pivot = segments[left].start;
        int lt = left;
        int gt = right;
        int i = left + 1;

        while (i <= gt) {
            if (segments[i].start < pivot) {
                swap(segments, lt++, i++);
            } else if (segments[i].start > pivot) {
                swap(segments, i, gt--);
            } else {
                i++;
            }
        }

        return new int[]{lt, gt};
    }

    private int countSegmentsForPoint(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        int count = 0;
        for (int i = right; i >= 0 && segments[i].start <= point; i--) {
            if (segments[i].stop >= point) {
                count++;
            }
        }

        return count;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment other) {
            return Integer.compare(this.start, other.start);
        }
    }

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }
}