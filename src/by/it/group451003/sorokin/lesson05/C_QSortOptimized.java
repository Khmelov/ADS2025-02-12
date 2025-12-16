package by.it.group451003.sorokin.lesson05;

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

        // Чтение количества отрезков и точек
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
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

        // Оптимизированная сортировка отрезков по началу
        quickSort3Way(segments, 0, segments.length - 1);

        // Для каждой точки находим количество отрезков, содержащих её
        for (int i = 0; i < m; i++) {
            int point = points[i];
            result[i] = countSegmentsContainingPoint(segments, point);
        }

        return result;
    }

    // Метод для подсчета отрезков, содержащих точку
    private int countSegmentsContainingPoint(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int count = 0;

        // Бинарный поиск первого отрезка, который может содержать точку
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // Теперь проверяем все отрезки от 0 до right
        for (int i = 0; i <= right; i++) {
            if (segments[i].stop >= point) {
                count++;
            }
        }

        return count;
    }

    // Оптимизированная быстрая сортировка с 3-разбиением
    private void quickSort3Way(Segment[] segments, int low, int high) {
        while (low < high) {
            int[] pivotIndices = partition3Way(segments, low, high);

            // Рекурсия для меньшей части
            if (pivotIndices[0] - low < high - pivotIndices[1]) {
                quickSort3Way(segments, low, pivotIndices[0] - 1);
                low = pivotIndices[1] + 1; // Элиминация хвостовой рекурсии
            } else {
                quickSort3Way(segments, pivotIndices[1] + 1, high);
                high = pivotIndices[0] - 1; // Элиминация хвостовой рекурсии
            }
        }
    }

    // 3-разбиение массива
    private int[] partition3Way(Segment[] segments, int low, int high) {
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

    // Вспомогательный метод для обмена элементов
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
            return Integer.compare(this.start, o.start);
        }
    }
}