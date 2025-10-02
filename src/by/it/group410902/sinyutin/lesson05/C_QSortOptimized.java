package by.it.group410902.sinyutin.lesson05;

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

        for (int i = 0; i < n; i++)
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());

        for (int i = 0; i < m; i++)
            points[i] = scanner.nextInt();

        quickSort(segments, 0, segments.length - 1);

        for (int i = 0; i < m; i++) {
            int p = points[i];
            int lastIndex = findLastIndex(segments, p);
            if (lastIndex == -1)
                result[i] = 0;
            else {
                int count = 0;
                for (int j = 0; j <= lastIndex; j++) {
                    if (segments[j].stop >= p)
                        count++;
                }
                result[i] = count;
            }
        }
        return result;
    }

    private void quickSort(Segment[] arr, int left, int right) {
        while (left < right) {
            int[] pivots = partition(arr, left, right);
            if ((pivots[0] - left) < (right - pivots[1])) {
                quickSort(arr, left, pivots[0] - 1);
                left = pivots[1] + 1;
            }
            else {
                quickSort(arr, pivots[1] + 1, right);
                right = pivots[0] - 1;
            }
        }
    }

    private int[] partition(Segment[] arr, int left, int right) {
        int mid = (left + right) >>> 1;
        int lt = left, gt = right;
        Segment pivot = arr[mid];
        swap(arr, mid, left);

        int i = left;
        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);

            if (cmp < 0)
                swap(arr, i++, lt++);
            else if (cmp > 0)
                swap(arr, i, gt--);
            else
                i++;
        }
        return new int[]{lt, gt};
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private int findLastIndex(Segment[] segments, int point) {
        int low = 0;
        int high = segments.length - 1;
        int ans = -1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (segments[mid].start <= point) {
                ans = mid;
                low = mid + 1;
            } else
                high = mid - 1;
        }
        return ans;
    }

    private static class Segment implements Comparable<Segment> {
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
