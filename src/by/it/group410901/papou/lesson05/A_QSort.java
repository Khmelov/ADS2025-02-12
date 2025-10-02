package by.it.group410901.papou.lesson05;

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

        // Read input
        int n = scanner.nextInt(); // number of segments
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt(); // number of points
        int[] points = new int[m];
        int[] result = new int[m];

        // Read segments
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(start, stop);
        }

        // Read points
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Sort segments by start time
        Arrays.sort(segments);

        // Process each point
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // Count segments that cover this point
            for (Segment segment : segments) {
                // If segment starts after point, no further segments will cover it
                if (segment.start > point) {
                    break;
                }
                // Check if point is within segment (including boundaries)
                if (point >= segment.start && point <= segment.stop) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // Ensure start is always less than or equal to stop
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            // Sort by start time, if equal then by end time
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}