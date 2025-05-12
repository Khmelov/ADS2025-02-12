package by.it.group451001.khokhlov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение отрезков
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(Math.min(start, stop), Math.max(start, stop));
        }

        // Чтение точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Оптимизированная сортировка отрезков
        quickSort(segments, 0, segments.length - 1);

        // Для каждой точки находим количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            result[i] = countCoveringSegments(segments, points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Быстрая сортировка с 3-разбиением и элиминацией хвостовой рекурсии
    private void quickSort(Segment[] segments, int low, int high) {
        while (low < high) {
            int[] pivot = partition3(segments, low, high);
            if (pivot[0] - low < high - pivot[1]) {
                quickSort(segments, low, pivot[0] - 1);
                low = pivot[1] + 1;
            } else {
                quickSort(segments, pivot[1] + 1, high);
                high = pivot[0] - 1;
            }
        }
    }

    // 3-разбиение (Dutch National Flag algorithm)
    private int[] partition3(Segment[] segments, int low, int high) {
        Segment pivot = segments[low];
        int lt = low;
        int gt = high;
        int i = low + 1;

        while (i <= gt) {
            int cmp = segments[i].compareTo(pivot);
            if (cmp < 0) {
                swap(segments, lt++, i++);
            } else if (cmp > 0) {
                swap(segments, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    // Подсчет отрезков, покрывающих точку
    private int countCoveringSegments(Segment[] segments, int point) {
        // Находим первый отрезок, который начинается после точки
        int firstAfter = findFirstAfter(segments, point);

        // Все отрезки до firstAfter потенциально могут покрывать точку
        int count = 0;
        for (int i = 0; i < firstAfter; i++) {
            if (segments[i].stop >= point) {
                count++;
            }
        }
        return count;
    }

    // Бинарный поиск первого отрезка, который начинается после точки
    private int findFirstAfter(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
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
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}