package by.it.group451003.plyushchevich.lesson05;

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


    // Быстрая сортировка с 3-разбиением по start
    static void quickSort3(Segment[] arr, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;
            Segment pivot = arr[low];
            int i = low + 1;
            while (i <= gt) {
                int cmp = arr[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(arr, i++, lt++); // элементы меньше опорного
                } else if (cmp > 0) {
                    swap(arr, i, gt--); // элементы больше опорного
                } else {
                    i++; // элементы равны, продолжаем
                }
            }
            quickSort3(arr, low, lt - 1);
            low = gt + 1; // хвостовая рекурсия
        }
    }

    static void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static int countCoveringSegments(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int first = segments.length;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (segments[mid].start > point) {
                first = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        int count = 0;
        for (int i = 0; i < first; i++) {
            if (segments[i].stop >= point) {
                count++;
            }
        }

        return count;
    }

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

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        quickSort3(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            result[i] = countCoveringSegments(segments, points[i]);
        }

        return result;
    }


    //отрезок
    private class Segment implements Comparable <Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start < stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            } else {
                return Integer.compare(this.stop, o.stop);
            }
        }
    }

}
