package by.it.group410902.latipov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(Math.min(start, stop), Math.max(start, stop));
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!! РЕАЛИЗАЦИЯ !!!!!!!!!!!!!!!!!!!!!!!!!

        // Оптимизированная сортировка отрезков по началу
        optimizedQuickSort(segments, 0, segments.length - 1);

        // Для каждой точки находим количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            result[i] = countCoveringSegments(segments, points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Оптимизированная быстрая сортировка с трехразбиением и элиминацией хвостовой рекурсии
    private void optimizedQuickSort(Segment[] segments, int low, int high) {
        while (low < high) {
            int[] pivotIndices = threeWayPartition(segments, low, high);

            // Рекурсия для меньшей части, итерация для большей (элиминация хвостовой рекурсии)
            if (pivotIndices[0] - low < high - pivotIndices[1]) {
                optimizedQuickSort(segments, low, pivotIndices[0] - 1);
                low = pivotIndices[1] + 1;
            } else {
                optimizedQuickSort(segments, pivotIndices[1] + 1, high);
                high = pivotIndices[0] - 1;
            }
        }
    }

    // Трехразбиение (Dutch National Flag algorithm)
    private int[] threeWayPartition(Segment[] segments, int low, int high) {
        Segment pivot = segments[low + (high - low) / 2];
        int i = low;
        int j = low;
        int k = high;

        while (j <= k) {
            int cmp = segments[j].compareTo(pivot);
            if (cmp < 0) {
                swap(segments, i, j);
                i++;
                j++;
            } else if (cmp > 0) {
                swap(segments, j, k);
                k--;
            } else {
                j++;
            }
        }

        return new int[]{i, k};
    }

    // Подсчет отрезков, покрывающих точку с использованием бинарного поиска
    private int countCoveringSegments(Segment[] segments, int point) {
        // Находим первый отрезок, который может покрывать точку
        int firstCandidate = findFirstCandidate(segments, point);

        if (firstCandidate == -1) {
            return 0;
        }

        // Подсчитываем все отрезки, начиная с firstCandidate, которые покрывают точку
        int count = 0;
        for (int i = firstCandidate; i < segments.length && segments[i].start <= point; i++) {
            if (point >= segments[i].start && point <= segments[i].stop) {
                count++;
            }
        }

        return count;
    }

    // Бинарный поиск первого отрезка, который может покрывать точку
    private int findFirstCandidate(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (segments[mid].start <= point) {
                // Этот отрезок может покрывать точку, ищем еще более ранний
                result = mid;
                left = mid + 1;
            } else {
                // Начало отрезка после точки - ищем в левой части
                right = mid - 1;
            }
        }

        return result;
    }

    // Альтернативная версия с более эффективным подсчетом
    private int countCoveringSegmentsOptimized(Segment[] segments, int point) {
        int count = 0;

        // Бинарный поиск для нахождения области поиска
        int left = 0;
        int right = segments.length - 1;
        int startIndex = -1;

        // Находим первый отрезок, который начинается не позже точки
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                startIndex = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        if (startIndex == -1) {
            return 0;
        }

        // Теперь ищем конец области, где отрезки могут покрывать точку
        left = 0;
        right = startIndex;
        int firstCovering = startIndex + 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].stop >= point) {
                firstCovering = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        // Все отрезки от firstCovering до startIndex покрывают точку
        for (int i = firstCovering; i <= startIndex; i++) {
            if (segments[i].start <= point && point <= segments[i].stop) {
                count++;
            }
        }

        return count;
    }

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment other) {
            // Сравниваем по началу отрезка, а при равенстве - по концу
            if (this.start != other.start) {
                return Integer.compare(this.start, other.start);
            }
            return Integer.compare(this.stop, other.stop);
        }

        @Override
        public String toString() {
            return "[" + start + ", " + stop + "]";
        }
    }
}