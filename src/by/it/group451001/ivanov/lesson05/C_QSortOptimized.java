package by.it.group451001.ivanov.lesson05;

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

        // Сортируем отрезки с помощью оптимизированной быстрой сортировки
        quickSort(segments, 0, n - 1);

        // Для каждой точки находим количество отрезков, которые её содержат
        for (int i = 0; i < m; i++) {
            result[i] = countSegmentsContainingPoint(segments, points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Оптимизированная быстрая сортировка с 3-разбиением
    private void quickSort(Segment[] arr, int low, int high) {
        while (low < high) {
            // Разбиение массива
            int[] pivots = partition(arr, low, high);

            // Рекурсивно сортируем меньшую часть
            if (pivots[0] - low < high - pivots[1]) {
                quickSort(arr, low, pivots[0] - 1);
                low = pivots[1] + 1; // Элиминация хвостовой рекурсии
            } else {
                quickSort(arr, pivots[1] + 1, high);
                high = pivots[0] - 1; // Элиминация хвостовой рекурсии
            }
        }
    }

    // Разбиение массива на 3 части: < опорного, = опорному, > опорного
    private int[] partition(Segment[] arr, int low, int high) {
        Segment pivot = arr[low + (high - low) / 2]; // Выбор опорного элемента (медиана)
        int i = low;
        int lt = low;
        int gt = high;

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

    // Подсчет количества отрезков, содержащих точку, с использованием бинарного поиска
    private int countSegmentsContainingPoint(Segment[] segments, int point) {
        int count = 0;
// Бинарный поиск первого отрезка, который может содержать точку
        int left = 0;
        int right = segments.length - 1;
        int firstPotentialSegment = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                firstPotentialSegment = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // Если не нашли отрезок, начало которого <= point
        if (firstPotentialSegment == -1) {
            return 0;
        }

        // Проверяем все потенциальные отрезки
        for (int i = 0; i <= firstPotentialSegment; i++) {
            if (segments[i].start <= point && point <= segments[i].stop) {
                count++;
            }
        }

        return count;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
            // Обеспечиваем, что start <= stop
            if (start > stop) {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment o) {
            // Сортировка по началу отрезка, затем по концу
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            } else {
                return Integer.compare(this.stop, o.stop);
            }
        }
    }
}