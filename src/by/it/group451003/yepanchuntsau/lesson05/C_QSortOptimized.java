package by.it.group451003.yepanchuntsau.lesson05;

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
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        int[] points = new int[m];
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        int[] result = new int[m];

        int[] starts = new int[n];
        int[] ends   = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i]   = segments[i].stop;
        }
        quick3sort(starts, 0, n - 1);
        quick3sort(ends,   0, n - 1);
        for (int i = 0; i < m; i++) {
            int p = points[i];
            int countStart     = upperBound(starts, p);
            int countEndBefore = lowerBound(ends, p);
            result[i] = countStart - countEndBefore;
        }
        return result;
    }


    private void quick3sort(int[] a, int lo, int hi) {
        while (lo < hi) {
            int pivot = a[lo + (hi - lo) / 2];
            int lt = lo, i = lo, gt = hi;
            while (i <= gt) {
                if (a[i] < pivot)        swap(a, lt++, i++);
                else if (a[i] > pivot)   swap(a, i, gt--);
                else                      i++;
            }
            if (lt - lo < hi - gt) {
                quick3sort(a, lo, lt - 1);
                lo = gt + 1;
            } else {
                quick3sort(a, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    private void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }


    private int lowerBound(int[] arr, int key) {
        int l = 0, r = arr.length;
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (arr[mid] < key) l = mid + 1;
            else                r = mid;
        }
        return l;
    }

    private int upperBound(int[] arr, int key) {
        int l = 0, r = arr.length;
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (arr[mid] <= key) l = mid + 1;
            else                 r = mid;
        }
        return l;
    }

    private static class Segment implements Comparable<Segment> {
        int start, stop;
        Segment(int s, int e) {
            // корректируем порядок концов, если нужно
            if (s <= e) {
                this.start = s; this.stop = e;
            } else {
                this.start = e; this.stop = s;
            }
        }
        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) return Integer.compare(this.start, o.start);
            return Integer.compare(this.stop, o.stop);
        }
    }
}
