package by.it.group451003.kishkov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение отрезков
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        // Чтение точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        quickSort(segments, 0, segments.length - 1);

        for (int i = 0; i < m; i++) {
            result[i] = countCoveringSegments(segments, points[i]);
        }

        scanner.close();
        return result;
    }

    private void quickSort(Segment[] segments, int low, int high) {
        while (low < high) {
            int[] pivot = partition(segments, low, high);
            quickSort(segments, low, pivot[0] - 1);
            low = pivot[1] + 1;
        }
    }

    private int[] partition(Segment[] segments, int low, int high) {
        Segment pivot = segments[low + (high - low) / 2];
        int i = low, j = low, k = high;

        while (j <= k) {
            int cmp = segments[j].compareTo(pivot);
            if (cmp < 0) {
                swap(segments, i++, j++);
            } else if (cmp > 0) {
                swap(segments, j, k--);
            } else {
                j++;
            }
        }
        return new int[]{i, k};
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private int countCoveringSegments(Segment[] segments, int point) {
        int first = findFirstCovering(segments, point);
        if (first == -1) return 0;

        int count = 1;
        for (int i = first + 1; i < segments.length && segments[i].start <= point; i++) {
            if (point <= segments[i].stop) count++;
        }
        return count;
    }

    private int findFirstCovering(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1, result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                if (point <= segments[mid].stop) {
                    result = mid;
                    right = mid - 1;  // Ищем самый левый
                } else {
                    left = mid + 1;
                }
            } else {
                right = mid - 1;
            }
        }
        return result;
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
