package by.it.group410901.kalach.lesson05;

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
        // Подготовка к чтению данных
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
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(Math.min(start, stop), Math.max(start, stop));
        }
        // Читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки с помощью оптимизированной быстрой сортировки
        quickSort(segments, 0, n - 1);

        // Обрабатываем каждую точку
        for (int i = 0; i < m; i++) {
            result[i] = countSegmentsForPoint(segments, points[i]);
        }

        return result;
    }

    // Оптимизированная быстрая сортировка с 3-разбиением и элиминацией хвостовой рекурсии
    private void quickSort(Segment[] segments, int low, int high) {
        while (low < high) {
            // Используем 3-разбиение
            int[] partition = partition(segments, low, high);
            int lt = partition[0];
            int gt = partition[1];

            // Элиминация хвостовой рекурсии: рекурсивно обрабатываем меньшую часть,
            // а большую обрабатываем итеративно
            if (lt - low < high - gt) {
                quickSort(segments, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort(segments, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    // 3-разбиение (Dutch Flag Partitioning)
    private int[] partition(Segment[] segments, int low, int high) {
        Segment pivot = segments[low];
        int lt = low; // Граница элементов < pivot
        int gt = high; // Граница элементов > pivot
        int i = low; // Текущий элемент

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

    // Обмен элементов
    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    // Подсчет отрезков, покрывающих точку, с использованием бинарного поиска
    private int countSegmentsForPoint(Segment[] segments, int point) {
        // Бинарный поиск для нахождения первого отрезка, где start <= point
        int left = binarySearchFirst(segments, point);
        if (left == -1) {
            return 0; // Нет отрезков, покрывающих точку
        }

        // Подсчитываем все отрезки, где start <= point и stop >= point
        int count = 0;
        for (int i = left; i < segments.length && segments[i].start <= point; i++) {
            if (segments[i].stop >= point) {
                count++;
            }
        }
        return count;
    }

    // Бинарный поиск первого отрезка, где start <= point
    private int binarySearchFirst(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid; // Возможный кандидат
                left = mid + 1; // Ищем дальше вправо
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    // Отрезок
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