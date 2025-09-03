package by.it.group410901.getmanchuk.lesson05;

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
        int n = scanner.nextInt(); // количество отрезков
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt(); // количество точек
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(start, stop);
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        Arrays.sort(segments); // сортируем отрезки по началу

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;
            for (Segment seg : segments) {
                if (seg.start > point) break; // дальше все отрезки будут позже этой точки
                if (seg.stop >= point) count++; // точка принадлежит отрезку
            }
            result[i] = count;
        }

        return result;
    }

    private static class Segment implements Comparable<Segment> {
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
            return Integer.compare(this.start, o.start); // сортировка по началу
        }
    }
}