package by.it.group410902.plekhova.lesson05;

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
        // Сортируем отрезки с использованием быстрой сортировки
        quickSort(segments, 0, n - 1);

        // Обрабатываем каждую точку
        for (int i = 0; i < m; i++) {
            result[i] = countSegmentsContainingPoint(segments, points[i]);
        }

        return result;
    }

    private void quickSort(Segment[] segments, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(segments, low, high);
            quickSort(segments, low, pivotIndex - 1);
            quickSort(segments, pivotIndex + 1, high);
        }
    }

    private int partition(Segment[] segments, int low, int high) {
        Segment pivot = segments[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (segments[j].compareTo(pivot) <= 0) {
                i++;
                swap(segments, i, j);
            }
        }

        swap(segments, i + 1, high);

        return i + 1;
    }

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    private int countSegmentsContainingPoint(Segment[] segments, int point) {
        // Бинарный поиск для нахождения первого подходящего отрезка
        int leftIndex = binarySearchFirstSegment(segments, point);

        if (leftIndex == -1) return 0; // Нет подходящих отрезков

        // Подсчет всех подходящих отрезков
        int count = 0;

        for (int i = leftIndex; i < segments.length && segments[i].start <= point && segments[i].stop >= point; i++) {
            count++;
        }

        return count;
    }

    private int binarySearchFirstSegment(Segment[] segments, int point) {
        // Бинарный поиск для нахождения первого отрезка с началом <= point и концом >= point
        int low = 0;
        int high = segments.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (segments[mid].start <= point && segments[mid].stop >= point) {
                return mid; // Найден подходящий отрезок
            } else if (segments[mid].start > point) {
                high = mid - 1; // Ищем в левой части
            } else {
                low = mid + 1; // Ищем в правой части
            }
        }

        return -1; // Не найдено подходящих отрезков
    }

    // Отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start); // Сравниваем по началу отрезка
        }
    }
}
