package by.it.group451001.steshits.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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
        quickSort(segments, 0, segments.length - 1);
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }
        Arrays.sort(ends);
        for (int i = 0; i < m; i++) {
            int x = points[i];
            int left = upperBound(starts, x);
            int right = lowerBound(ends, x);
            result[i] = left - right;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private static void swap(Segment[] array, int i, int j) {
        Segment temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static int medianOfThree(Segment[] array, int a, int b, int c) {
        if (array[a].compareTo(array[b]) > 0) {
            swap(array, a, b);
        }
        if (array[a].compareTo(array[c]) > 0) {
            swap(array, a, c);
        }
        if (array[b].compareTo(array[c]) > 0) {
            swap(array, b, c);
        }
        return b;
    }

    private static int[] partition(Segment[] array, int low, int high) {
        int mid = low + (high - low) / 2;
        int pivotIndex = medianOfThree(array, low, mid, high);
        swap(array, pivotIndex, high);
        Segment pivot = array[high];

        int i = low;
        int j = high;
        int k = low;

        while (k <= j) {
            int cmp = array[k].compareTo(pivot);
            if (cmp < 0) {
                swap(array, i++, k++);
            } else if (cmp > 0) {
                swap(array, k, j--);
            } else {
                k++;
            }
        }
        return new int[]{i, j};
    }

    private static void quickSort(Segment[] array, int low, int high) {
        while (low < high) {
            int[] pivotIndices = partition(array, low, high);
            if (pivotIndices[0] - low < high - pivotIndices[1]) {
                quickSort(array, low, pivotIndices[0] - 1);
                low = pivotIndices[1] + 1;
            } else {
                quickSort(array, pivotIndices[1] + 1, high);
                high = pivotIndices[0] - 1;
            }
        }
    }

    private static int upperBound(int[] array, int key) {
        int low = 0;
        int high = array.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (array[mid] <= key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    private static int lowerBound(int[] array, int key) {
        int low = 0;
        int high = array.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (array[mid] < key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Object o) {
            //подумайте, что должен возвращать компаратор отрезков
            Segment b = o instanceof Segment ? (Segment) o : null;
            if (start != b.start) {
                return Integer.compare(start, b.start);
            }
            return Integer.compare(stop, b.stop);
        }
    }

}
