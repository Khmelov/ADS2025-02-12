package by.it.group410902.barbashova.lesson05;

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
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            //гарантируем правильный порядок концов отрезка
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }
            segments[i] = new Segment(start, stop);
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        // Сортируем отрезки по началу с помощью оптимизированной быстрой сортировки
        quickSortThreeWay(segments, 0, segments.length - 1);

        // Для каждой точки находим количество отрезков, которые ее содержат
        for (int i = 0; i < m; i++) {
            int point = points[i];
            result[i] = countSegmentsContainingPoint(segments, point);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Оптимизированная быстрая сортировка с 3-разбиением и элиминацией хвостовой рекурсии
    private void quickSortThreeWay(Segment[] arr, int low, int high) {
        while (low < high) {
            int[] pivots = threeWayPartition(arr, low, high);

            // Рекурсия для меньшей части, итерация для большей (элиминация хвостовой рекурсии)
            if (pivots[0] - low < high - pivots[1]) {
                quickSortThreeWay(arr, low, pivots[0] - 1);
                low = pivots[1] + 1;
            } else {
                quickSortThreeWay(arr, pivots[1] + 1, high);
                high = pivots[0] - 1;
            }
        }
    }

    // 3-разбиение (Dutch National Flag algorithm)
    private int[] threeWayPartition(Segment[] arr, int low, int high) {
        Segment pivot = arr[low];
        int i = low;
        int j = low;
        int k = high;

        while (j <= k) {
            int cmp = arr[j].compareTo(pivot);

            if (cmp < 0) {
                swap(arr, i, j);
                i++;
                j++;
            } else if (cmp > 0) {
                swap(arr, j, k);
                k--;
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

    // Подсчет отрезков, содержащих точку
    private int countSegmentsContainingPoint(Segment[] segments, int point) {
        // Находим первый отрезок, который может содержать точку (бинарный поиск)
        int firstIndex = findFirstPotentialSegment(segments, point);

        if (firstIndex == -1) {
            return 0;
        }

        // Подсчитываем все отрезки, содержащие точку, начиная с найденного индекса
        int count = 0;
        for (int i = firstIndex; i < segments.length && segments[i].start <= point; i++) {
            if (segments[i].stop >= point) {
                count++;
            }
        }

        return count;
    }

    // Бинарный поиск первого отрезка, который может содержать точку
    private int findFirstPotentialSegment(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (segments[mid].start <= point) {
                // Этот отрезок может содержать точку, ищем самый левый подходящий
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
        public int compareTo(Segment other) {
            // Сравниваем по началу отрезка
            return Integer.compare(this.start, other.start);
        }

        public boolean contains(int point) {
            return point >= start && point <= stop;
        }
    }

}