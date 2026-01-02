package by.it.group451003.mihlin.lesson05;

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

        // Чтение данных
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение отрезков
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            segments[i] = new Segment(Math.min(start, end), Math.max(start, end));
        }

        // Чтение точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Оптимизированная сортировка отрезков
        quickSort3Way(segments, 0, segments.length - 1);

        // Поиск количества отрезков для каждой точки
        for (int i = 0; i < m; i++) {
            result[i] = countSegmentsContainingPoint(segments, points[i]);
        }

        return result;
    }

    // 3-разбиение для быстрой сортировки (без хвостовой рекурсии)
    private void quickSort3Way(Segment[] segments, int low, int high) {
        while (low < high) {
            int[] pivotIndices = partition3(segments, low, high);
            quickSort3Way(segments, low, pivotIndices[0] - 1);
            low = pivotIndices[1] + 1; // Элиминация хвостовой рекурсии
        }
    }

    // Метод 3-разбиения
    private int[] partition3(Segment[] segments, int low, int high) {
        Segment pivot = segments[low];
        int lt = low, gt = high;
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

    // Подсчет отрезков, содержащих точку (с бинарным поиском)
    private int countSegmentsContainingPoint(Segment[] segments, int point) {
        // Находим первый отрезок, который может содержать точку
        int left = 0;
        int right = segments.length - 1;
        int firstContaining = segments.length;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                firstContaining = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // Теперь проверяем все отрезки до firstContaining
        int count = 0;
        for (int i = 0; i <= firstContaining; i++) {
            if (segments[i].stop >= point) {
                count++;
            }
        }

        return count;
    }

    // Вспомогательный метод для обмена элементов
    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    // Класс отрезка с улучшенным компаратором
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            // Сначала сравниваем по началу, потом по концу
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}