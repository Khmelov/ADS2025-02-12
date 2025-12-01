package by.it.group451004.redko.lesson05;

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

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение отрезков
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(start, stop);
        }

        // Чтение точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Оптимизированная сортировка отрезков
        quickSort(segments, 0, segments.length - 1);

        // Поиск количества покрытий для каждой точки
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // Бинарный поиск первого подходящего отрезка
            int left = 0;
            int right = segments.length - 1;
            int firstMatch = -1;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].start <= point) {
                    if (segments[mid].stop >= point) {
                        firstMatch = mid;
                        right = mid - 1; // Ищем самый левый подходящий
                    } else {
                        left = mid + 1;
                    }
                } else {
                    right = mid - 1;
                }
            }

            // Если нашли хотя бы один подходящий отрезок
            if (firstMatch != -1) {
                // Проверяем все отрезки правее, которые могут содержать точку
                for (int j = firstMatch; j < segments.length && segments[j].start <= point; j++) {
                    if (segments[j].stop >= point) {
                        count++;
                    }
                }
            }

            result[i] = count;
        }

        return result;
    }

    // Оптимизированная быстрая сортировка с 3-разбиением
    private void quickSort(Segment[] arr, int low, int high) {
        while (low < high) {
            int[] pivot = partition(arr, low, high);
            quickSort(arr, low, pivot[0] - 1);
            low = pivot[1] + 1; // Элиминация хвостовой рекурсии
        }
    }

    private int[] partition(Segment[] arr, int low, int high) {
        Segment pivot = arr[low];
        int lt = low;
        int gt = high;
        int i = low + 1;

        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
            if (cmp < 0) {
                swap(arr, lt++, i++);
            } else if (cmp > 0) {
                swap(arr, i, gt--);
            } else {
                i++;
            }
        }

        return new int[]{lt, gt};
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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
