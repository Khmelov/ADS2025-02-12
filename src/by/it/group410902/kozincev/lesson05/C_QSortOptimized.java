package by.it.group410902.kozincev.lesson05;

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
        qSort(segments, 0, segments.length - 1);

        for (int i = 0; i < m; i++) {
            result[i] = countCoveringSegments(segments, points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    void swap(Segment[] arr, int i, int j){
        Segment tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    int[] partition3(Segment[] arr, int left, int right){
        Segment pivot = arr[left + (right - left) / 2];
        int i = left;
        int j = left;
        int k = right;

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

    void qSort(Segment[] arr, int left, int right){
        while (left < right) {
            int[] pivotIndices = partition3(arr, left, right);
            if (pivotIndices[0] - left < right - pivotIndices[1]) {
                qSort(arr, left, pivotIndices[0] - 1);
                left = pivotIndices[1] + 1;
            } else {
                qSort(arr, pivotIndices[1] + 1, right);
                right = pivotIndices[0] - 1;
            }
        }

    }

    private int countCoveringSegments(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int firstCoveringIndex = -1;

        // Бинарный поиск первого покрывающего отрезка
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                if (segments[mid].stop >= point) {
                    firstCoveringIndex = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                right = mid - 1;
            }
        }

        if (firstCoveringIndex == -1) {
            return 0;
        }

        // Линейный подсчет оставшихся покрывающих отрезков
        int count = 0;
        for (int i = firstCoveringIndex; i < segments.length && segments[i].start <= point; i++) {
            if (segments[i].stop >= point) {
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
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            return Integer.compare(this.start, o.start);
        }
    }

}
