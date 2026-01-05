package by.it.group451004.matyrka.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int x : result) {
            System.out.print(x + " ");
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

        quickSort(segments, 0, n - 1);

        int[] result = new int[m];
        for (int i = 0; i < m; i++) {
            int p = points[i];
            int idx = firstCover(segments, p);
            int cnt = 0;
            while (idx < n && segments[idx].start <= p) {
                if (segments[idx].stop >= p) cnt++;
                idx++;
            }
            result[i] = cnt;
        }

        return result;
    }

    private int firstCover(Segment[] a, int p) {
        int l = 0, r = a.length - 1, res = a.length;
        while (l <= r) {
            int m = (l + r) >>> 1;
            if (a[m].start <= p) {
                res = m;
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return res;
    }

    private void quickSort(Segment[] a, int l, int r) {
        while (l < r) {
            int i = l, j = r;
            Segment pivot = a[l + (r - l) / 2];
            while (i <= j) {
                while (a[i].compareTo(pivot) < 0) i++;
                while (a[j].compareTo(pivot) > 0) j--;
                if (i <= j) {
                    Segment tmp = a[i];
                    a[i] = a[j];
                    a[j] = tmp;
                    i++;
                    j--;
                }
            }
            if (j - l < r - i) {
                quickSort(a, l, j);
                l = i;
            } else {
                quickSort(a, i, r);
                r = j;
            }
        }
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
            if (this.start != o.start)
                return Integer.compare(this.start, o.start);
            return Integer.compare(this.stop, o.stop);
        }
    }
}
