package by.it.group410902.sinyutin.lesson05;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

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

        Segment[] segments = new Segment[n];
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++)
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());

        for (int i = 0; i < m; i++)
            points[i] = scanner.nextInt();

        Arrays.sort(segments);
        for (int j = 0; j < m; j++) {
            int currentPoint = points[j];
            int count = 0;
            for (int i = 0; i < n; i++) {
                Segment currentSegment = segments[i];

                if (currentSegment.start > currentPoint)
                    break;

                if (currentPoint <= currentSegment.stop)
                    count++;

            }
            result[j] = count;
        }
        return result;
    }

    private static class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start < stop) {
                this.start = start;
                this.stop = stop;
            }
            else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment other) {
            if (this.start != other.start)
                return Integer.compare(this.start, other.start);

            return Integer.compare(this.stop, other.stop);
        }
    }
}