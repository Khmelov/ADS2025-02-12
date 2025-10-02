package by.it.group451004.zybko.lesson05;

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
        quickSort(segments, 0, segments.length - 1);

        // Для каждой точки находим количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];
            // Находим первый подходящий отрезок бинарным поиском
            int first = findFirstSegment(segments, point);

            if (first == -1) {
                result[i] = 0;
                continue;
            }

            // Идем вправо, пока отрезки покрывают точку
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

    // Оптимизированная быстрая сортировка с 3-разбиением
    private void quickSort(Segment[] segments, int low, int high) {
        while (low < high) {
            int[] pivot = partition3(segments, low, high);
            if (pivot[0] - low < high - pivot[1]) {
                quickSort(segments, low, pivot[0] - 1);
                low = pivot[1] + 1;
            } else {
                quickSort(segments, pivot[1] + 1, high);
                high = pivot[0] - 1;
            }
        }
    }

    // 3-разбиение (Dutch National Flag)
    private int[] partition3(Segment[] segments, int low, int high) {
        Segment pivot = segments[low];
        int i = low, j = high;
        int k = low;

        while (k <= j) {
            int cmp = segments[k].compareTo(pivot);
            if (cmp < 0) {
                swap(segments, i++, k++);
            } else if (cmp > 0) {
                swap(segments, k, j--);
            } else {
                k++;
            }
        }
        return new int[]{i, j};
    }

    // Бинарный поиск первого отрезка, который может содержать точку
    private int findFirstSegment(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1;
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

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
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
            return 0;
        }
    }

}
