package by.it.group451002.gorbach.lesson05;

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
        quickSort(segments, 0, segments.length - 1);

        // Для каждой точки находим количество отрезков, содержащих её
        for (int i = 0; i < points.length; i++) {
            int point = points[i];
            // Находим первый отрезок, который может содержать точку
            int firstIndex = findFirstSegment(segments, point);
            if (firstIndex == -1) {
                result[i] = 0;
                continue;
            }
            // Считаем все подходящие отрезки
            result[i] = countContainingSegments(segments, firstIndex, point);
        }

        return result;
    }

    // Оптимизированная быстрая сортировка с 3-разбиением и элиминацией хвостовой рекурсии
    private void quickSort(Segment[] segments, int low, int high) {
        while (low < high) {
            int[] pivotIndices = partition3(segments, low, high);
            // Рекурсия для меньшей части, итерация для большей
            if (pivotIndices[0] - low < high - pivotIndices[1]) {
                quickSort(segments, low, pivotIndices[0] - 1);
                low = pivotIndices[1] + 1;
            } else {
                quickSort(segments, pivotIndices[1] + 1, high);
                high = pivotIndices[0] - 1;
            }
        }
    }

    // 3-разбиение для быстрой сортировки
    private int[] partition3(Segment[] segments, int low, int high) {
        Segment pivot = segments[low];
        int lt = low;   // Указатель на конец элементов < pivot
        int gt = high;   // Указатель на начало элементов > pivot
        int i = low + 1; // Текущий элемент

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

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
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

    // Подсчёт отрезков, содержащих точку
    private int countContainingSegments(Segment[] segments, int firstIndex, int point) {
        int count = 0;
        for (int i = firstIndex; i >= 0 && segments[i].start <= point; i--) {
            if (segments[i].stop >= point) {
                count++;
            }
        }
        return count;
    }

    // Класс отрезка с реализацией сравнения
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment other) {
            if (this.start != other.start) {
                return Integer.compare(this.start, other.start);
            }
            return Integer.compare(this.stop, other.stop);
        }
    }
}