package by.it.group410902.kukhto.les5;

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

        // Чтение отрезков
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(Math.min(start, stop), Math.max(start, stop));
        }
        // Оптимизированная быстрая сортировка
        optimizedQuickSort(segments, 0, segments.length - 1);

        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        // Поиск количества отрезков для каждой точки
        for (int i = 0; i < m; i++) {
            int point = points[i];
            // Находим первый отрезок, который может содержать точку
            int first = findFirstSegment(segments, point);

            if (first == -1) {
                result[i] = 0;
                continue;
            }

            // Подсчитываем все подходящие отрезки начиная с first
            int count = 0;
            for (int j = first; j < segments.length && segments[j].start <= point; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }
            result[i] = count;
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    // Оптимизированная быстрая сортировка с 3-разбиением и элиминацией хвостовой рекурсии
    private void optimizedQuickSort(Segment[] arr, int low, int high) {
        while (low < high) {
            int[] pivots = threeWayPartition(arr, low, high);

            // Рекурсия для меньшей части, итерация для большей
            if (pivots[0] - low < high - pivots[1]) {
                optimizedQuickSort(arr, low, pivots[0] - 1);
                low = pivots[1] + 1;
            } else {
                optimizedQuickSort(arr, pivots[1] + 1, high);
                high = pivots[0] - 1;
            }
        }
    }
    // 3-разбиение
    private int[] threeWayPartition(Segment[] arr, int low, int high) {
        Segment pivot = arr[low + (high - low) / 2];
        int i = low;
        int j = low;
        int k = high;

        while (j <= k) {
            int cmp = arr[j].compareTo(pivot);
            if (cmp < 0) {
                swap(arr, i++, j++);
            } else if (cmp > 0) {
                swap(arr, j, k--);
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
    // Бинарный поиск первого отрезка, который может содержать точку
    private int findFirstSegment(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
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
        public int compareTo(Segment o) {
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

}
