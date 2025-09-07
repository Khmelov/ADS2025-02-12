package by.it.group451001.khomenkov.lesson05;

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

        quickSort(segments, 0, segments.length - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int left = 0;
            int right = segments.length - 1;
            int count = 0;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].start <= point && point <= segments[mid].stop) {
                    count++;
                    int l = mid - 1;
                    while (l >= 0 && segments[l].start <= point && point <= segments[l].stop) {
                        count++;
                        l--;
                    }
                    int r = mid + 1;
                    while (r < segments.length && segments[r].start <= point && point <= segments[r].stop) {
                        count++;
                        r++;
                    }
                    break;
                } else if (segments[mid].start > point) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            result[i] = count;
        }

        return result;
    }

    private void quickSort(Segment[] segments, int low, int high) {
        while (low < high) {
            int[] pivotIndices = partition(segments, low, high);
            if (pivotIndices[0] - low < high - pivotIndices[1]) {
                quickSort(segments, low, pivotIndices[0] - 1);
                low = pivotIndices[1] + 1;
            } else {
                quickSort(segments, pivotIndices[1] + 1, high);
                high = pivotIndices[0] - 1;
            }
        }
    }

    private int[] partition(Segment[] segments, int low, int high) {
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
            } else {
                return Integer.compare(this.stop, o.stop);
            }
        }
    }
}