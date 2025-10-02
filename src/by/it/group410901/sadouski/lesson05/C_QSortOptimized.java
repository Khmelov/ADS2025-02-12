package by.it.group410901.sadouski.lesson05;

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
                    int temp = mid - 1;
                    while (temp >= 0 && segments[temp].start <= point && point <= segments[temp].stop) {
                        count++;
                        temp--;
                    }
                    temp = mid + 1;
                    while (temp < segments.length && segments[temp].start <= point && point <= segments[temp].stop) {
                        count++;
                        temp++;
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
            quickSort(segments, low, pivotIndices[0] - 1);
            low = pivotIndices[1] + 1;
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
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
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