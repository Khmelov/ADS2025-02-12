package by.it.group451004.matyrka.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int x : result) {
            System.out.print(x + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int m = scanner.nextInt();

        Segment[] segments = new Segment[n];
        for (int i = 0; i < n; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            segments[i] = new Segment(a, b);
        }

        int[] points = new int[m];
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // сортируем отрезки по началу
        Arrays.sort(segments);

        // массив концов отрезков
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            ends[i] = segments[i].stop;
        }
        Arrays.sort(ends);

        int[] result = new int[m];

        for (int i = 0; i < m; i++) {
            int p = points[i];

            int startsBefore = upperBoundStart(segments, p);
            int endsBefore = lowerBoundEnd(ends, p);

            result[i] = startsBefore - endsBefore;
        }

        return result;
    }

    // количество отрезков с start <= p
    private int upperBoundStart(Segment[] segments, int p) {
        int l = 0, r = segments.length;
        while (l < r) {
            int mid = (l + r) / 2;
            if (segments[mid].start <= p)
                l = mid + 1;
            else
                r = mid;
        }
        return l;
    }

    // количество отрезков с stop < p
    private int lowerBoundEnd(int[] ends, int p) {
        int l = 0, r = ends.length;
        while (l < r) {
            int mid = (l + r) / 2;
            if (ends[mid] < p)
                l = mid + 1;
            else
                r = mid;
        }
        return l;
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
