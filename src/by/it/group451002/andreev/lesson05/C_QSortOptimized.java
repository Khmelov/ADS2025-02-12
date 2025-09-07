package by.it.group451002.andreev.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Класс C_QSortOptimized решает задачу подсчета количества отрезков,
 * которые содержат заданные точки. Используется быстрая сортировка с трехразбиением
 * для оптимизации времени выполнения и бинарный поиск для эффективного поиска покрывающих отрезков.
 */
public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataC.txt"
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();

        // Выполняем поиск количества покрытых точек и выводим результат
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    /**
     * Метод getAccessory2 читает данные, сортирует отрезки и вычисляет количество покрытых точек.
     * @param stream входной поток данных
     * @return массив, содержащий количество покрывающих отрезков для каждой точки
     * @throws FileNotFoundException если файл не найден
     */
    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем количество отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        // Читаем количество точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Читаем начало и конец каждого отрезка
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(Math.min(start, stop), Math.max(start, stop)); // Убеждаемся, что start <= stop
        }

        // Читаем координаты точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Оптимизированная быстрая сортировка с трехразбиением
        optimizedQuickSort(segments, 0, segments.length - 1);

        // Для каждой точки определяем количество покрывающих ее отрезков
        for (int i = 0; i < m; i++) {
            result[i] = countCoveringSegments(segments, points[i]);
        }

        return result;
    }

    /**
     * Метод optimizedQuickSort выполняет быструю сортировку с трехразбиением.
     * @param segments массив отрезков
     * @param low индекс начала массива
     * @param high индекс конца массива
     */
    private void optimizedQuickSort(Segment[] segments, int low, int high) {
        while (low < high) {
            int[] pivotIndices = threeWayPartition(segments, low, high);

            // Элиминация хвостовой рекурсии для оптимального использования памяти
            if (pivotIndices[0] - low < high - pivotIndices[1]) {
                optimizedQuickSort(segments, low, pivotIndices[0] - 1);
                low = pivotIndices[1] + 1;
            } else {
                optimizedQuickSort(segments, pivotIndices[1] + 1, high);
                high = pivotIndices[0] - 1;
            }
        }
    }

    /**
     * Метод threeWayPartition выполняет трехразбиение массива.
     * @param segments массив отрезков
     * @param low индекс начала массива
     * @param high индекс конца массива
     * @return массив с индексами разделенных частей
     */
    private int[] threeWayPartition(Segment[] segments, int low, int high) {
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

    /**
     * Метод swap выполняет обмен двух элементов в массиве.
     * @param segments массив отрезков
     * @param i индекс первого элемента
     * @param j индекс второго элемента
     */
    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    /**
     * Метод countCoveringSegments выполняет бинарный поиск
     * и подсчитывает количество отрезков, покрывающих точку.
     * @param segments отсортированный массив отрезков
     * @param point точка для поиска
     * @return количество покрывающих отрезков
     */
    private int countCoveringSegments(Segment[] segments, int point) {
        // Бинарный поиск первого отрезка, у которого start > point
        int left = 0;
        int right = segments.length - 1;
        int firstAfter = segments.length;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start > point) {
                firstAfter = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        // Подсчет количества покрывающих отрезков
        int count = 0;
        if (firstAfter > 0) {
            left = 0;
            right = firstAfter - 1;
            int firstCovering = firstAfter;

            // Бинарный поиск первого отрезка, у которого stop >= point
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].stop >= point) {
                    firstCovering = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            count = firstAfter - firstCovering;
        }

        return count;
    }

    /**
     * Класс Segment представляет собой отрезок с началом и концом.
     * Используется для сортировки отрезков по их начальной точке.
     */
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
