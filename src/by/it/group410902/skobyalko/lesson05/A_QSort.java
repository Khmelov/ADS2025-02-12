package by.it.group410902.skobyalko.lesson05;

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
        /// //////////////////////////////
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            // если концы отрезков пришли в обратном порядке, меняем местами
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }
            segments[i] = new Segment(start, stop);
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Отдельно собираем массивы начал и концов
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        Arrays.sort(starts);
        Arrays.sort(ends);

        // Для каждой точки считаем сколько отрезков начинаются не позже неё
        // и сколько отрезков заканчиваются раньше неё
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int countStarts = upperBound(starts, point); // число стартов <= point
            int countEnds = lowerBound(ends, point);     // число концов < point
            result[i] = countStarts - countEnds;
        }

        return result;
    }
/// ///////////////////////
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            // сортируем по началу, если равны - по концу
            if (this.start != o.start)
                return Integer.compare(this.start, o.start);
            return Integer.compare(this.stop, o.stop);
        }
    }

    // Возвращает индекс первого элемента, который строго больше key
    // Работает аналог upper_bound в C++
    private int upperBound(int[] array, int key) {
        int left = 0, right = array.length;
        while (left < right) {
            int mid = (left + right) / 2;
            if (array[mid] <= key)
                left = mid + 1;
            else
                right = mid;
        }
        return left;
    }

    // Возвращает индекс первого элемента, который не меньше key
    // Работает аналог lower_bound в C++
    private int lowerBound(int[] array, int key) {
        int left = 0, right = array.length;
        while (left < right) {
            int mid = (left + right) / 2;
            if (array[mid] < key)
                left = mid + 1;
            else
                right = mid;
        }
        return left;
    }
}
