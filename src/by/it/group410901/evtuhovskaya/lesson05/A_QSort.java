package by.it.group410901.evtuhovskaya.lesson05;

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
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            segments[i] = new Segment(Math.min(start, end), Math.max(start, end));
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем начала и концы отрезков
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }
        Arrays.sort(starts);
        Arrays.sort(ends);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            // Количество отрезков, которые начались до или в точке
            int left = binarySearchCount(starts, point, true);
            // Количество отрезков, которые закончились до точки (исключая те, которые включают точку)
            int right = binarySearchCount(ends, point, false);
            result[i] = left - right;
        }

        return result;
    }

    private int binarySearchCount(int[] array, int point, boolean isStart) {
        int left = 0;
        int right = array.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (isStart) {
                if (array[mid] <= point) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            } else {
                if (array[mid] < point) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
        }
        return left;
    }

    private class Segment {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }
    }
}