package by.it.group451002.popeko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

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

        // Число отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        // Число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Читаем отрезки
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        // Читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по началу (быстрая сортировка с трехразбиением)
        quickSort(segments, 0, n - 1);

        // Подсчитываем, сколько камер записало каждую точку
        for (int i = 0; i < m; i++) {
            result[i] = countSegments(segments, points[i]);
        }

        return result;
    }

    // Метод трехразбиения для быстрой сортировки
    void quickSort(Segment[] arr, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;
            Segment pivot = arr[low];
            int i = low;

            while (i <= gt) {
                if (arr[i].start < pivot.start) {
                    swap(arr, lt++, i++);
                } else if (arr[i].start > pivot.start) {
                    swap(arr, i, gt--);
                } else {
                    i++;
                }
            }

            quickSort(arr, low, lt - 1);
            low = gt + 1;  // Элиминация хвостовой рекурсии
        }
    }

    // Метод бинарного поиска первого подходящего отрезка
    int countSegments(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start > point) {
                right = mid - 1;
            } else if (segments[mid].stop < point) {
                left = mid + 1;
            } else {
                // Найден первый отрезок, теперь считаем все
                int count = 0;
                for (int i = mid; i >= 0 && segments[i].start <= point; i--) {
                    if (segments[i].stop >= point) count++;
                }
                for (int i = mid + 1; i < segments.length && segments[i].start <= point; i++) {
                    if (segments[i].stop >= point) count++;
                }
                return count;
            }
        }
        return 0;
    }

    void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Отрезок
    private static class Segment implements Comparable<Segment> {
        int start, stop;

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
