package by.it.group451002.stsefanovich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
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

        // Простая быстрая сортировка
        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;
            for (Segment segment : segments) {
                if (segment.start <= point && point <= segment.stop) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    private void quickSort(Segment[] segments, int low, int high) {
        if (low < high) {
            int pi = partition(segments, low, high);
            quickSort(segments, low, pi - 1);
            quickSort(segments, pi + 1, high);
        }
    }

    private int partition(Segment[] segments, int low, int high) {
        Segment pivot = segments[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (segments[j].compareTo(pivot) <= 0) {
                i++;
                swap(segments, i, j);
            }
        }
        swap(segments, i + 1, high);
        return i + 1;
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
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}