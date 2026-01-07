package by.it.group451003.fedorcov.lesson05;

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
        int m = scanner.nextInt();

        Segment[] segments = new Segment[n];
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

        scanner.close();

        quickSort(segments, 0, segments.length - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            result[i] = countCoveringSegments(segments, point);
        }

        return result;
    }

    private void quickSort(Segment[] arr, int left, int right) {
        while (left < right) {
            int[] pivotIndices = partition(arr, left, right);
            quickSort(arr, left, pivotIndices[0] - 1);
            left = pivotIndices[1] + 1;
        }
    }

    private int[] partition(Segment[] arr, int left, int right) {
        Segment pivot = arr[left];
        int lt = left;
        int gt = right;
        int i = left + 1;

        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
            if (cmp < 0) {
                swap(arr, lt++, i++);
            } else if (cmp > 0) {
                swap(arr, i, gt--);
            } else {
                i++;
            }
        }

        return new int[]{lt, gt};
    }

    private int countCoveringSegments(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int firstCovering = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                firstCovering = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        if (firstCovering == -1) return 0;

        int count = 0;
        for (int i = 0; i <= firstCovering; i++) {
            if (segments[i].stop >= point) {
                count++;
            }
        }

        return count;
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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