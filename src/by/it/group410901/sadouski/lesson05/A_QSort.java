package by.it.group410901.sadouski.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int[] starts = new int[n];
        int[] ends = new int[n];

        for (int i = 0; i < n; i++) {
            starts[i] = scanner.nextInt();
            ends[i] = scanner.nextInt();
        }

        int[] points = new int[m];
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        Arrays.sort(starts);
        Arrays.sort(ends);

        int[] result = new int[m];
        for (int i = 0; i < m; i++) {
            int point = points[i];
            // Number of segments where starts <= point
            int startedBeforeOrAt = binarySearch(starts, point, true);
            // Number of segments where ends < point
            int endedBefore = binarySearch(ends, point, false);

            result[i] = startedBeforeOrAt - endedBefore;
        }

        return result;
    }

    private int binarySearch(int[] array, int point, boolean isStart) {
        int left = 0;
        int right = array.length;
        while (left < right) {
            int mid = (left + right) / 2;
            if (isStart ? array[mid] <= point : array[mid] < point) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
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
            return Integer.compare(this.start, o.start);
        }
    }
}