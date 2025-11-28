package by.it.group410902.derzhavskaya_ludmila.lesson05;

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
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        // Оптимизированная сортировка отрезков
        optimizedQuickSort(segments, 0, segments.length - 1);

        // Поиск количества отрезков для каждой точки
        for (int i = 0; i < m; i++) {
            result[i] = countSegmentsForPoint(segments, points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    // Оптимизированная быстрая сортировка с 3-разбиением и элиминацией хвостовой рекурсии
    private void optimizedQuickSort(Segment[] segments, int low, int high) {
        while (low < high) {
            int[] pivots = threeWayPartition(segments, low, high);
            // Рекурсивно сортируем меньшую часть, итеративно - большую
            if (pivots[0] - low < high - pivots[1]) {
                optimizedQuickSort(segments, low, pivots[0] - 1);
                low = pivots[1] + 1;
            } else {
                optimizedQuickSort(segments, pivots[1] + 1, high);
                high = pivots[0] - 1;
            }
        }
    }

    // 3-разбиение (Dutch National Flag algorithm)
    private int[] threeWayPartition(Segment[] segments, int low, int high) {
        Segment pivot = segments[low + (high - low) / 2];
        int i = low;
        int j = low;
        int k = high;

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

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    // Метод для подсчета отрезков, содержащих точку
    private int countSegmentsForPoint(Segment[] segments, int point) {
        // Находим первый отрезок, где start > point
        int firstGreater = findFirstGreater(segments, point);
        // Все отрезки до firstGreater имеют start <= point
        // Теперь нужно проверить среди них те, у которых stop >= point
        return countValidSegments(segments, point, firstGreater);
    }

    // Бинарный поиск первого отрезка с start > point
    private int findFirstGreater(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start > point) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    // Подсчет отрезков с start <= point и stop >= point
    private int countValidSegments(Segment[] segments, int point, int endIndex) {
        int count = 0;
        for (int i = 0; i < endIndex; i++) {
            if (segments[i].stop >= point) {
                count++;
            }
        }
        return count;
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }
        }

        @Override
        public int compareTo(Object o) {
            //подумайте, что должен возвращать компаратор отрезков
            return 0;
        }
    }

}
