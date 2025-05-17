package by.it.group410902.bobrovskaya.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

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

        // Быстрая сортировка с трех-разбиением
        quickSort(segments, 0, segments.length - 1);

        // Поиск принадлежности точек отрезкам с бинарным поиском
        for (int i = 0; i < m; i++) {
            result[i] = countSegments(segments, points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    // Реализация быстрой сортировки с трех-разбиением
    private void quickSort(Segment[] arr, int low, int high) {
        while (low < high) {
            int lt = low, gt = high, i = low + 1; //Разделяем массив на три части
            Segment pivot = arr[low]; //Выбираем опорный элемент, первый в массиве

            while (i <= gt) {
                if (arr[i].compareTo(pivot) < 0) swap(arr, lt++, i++);
                else if (arr[i].compareTo(pivot) > 0) swap(arr, i, gt--);
                else i++;
            }

            quickSort(arr, low, lt - 1); //Сортируем левую часть рекурсивно.
            low = gt + 1; // Элиминация хвостовой рекурсии
        }
    }
    // Метод бинарного поиска для поиска первого подходящего отрезка
    private int countSegments(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point && point <= segments[mid].stop) {
                int count = 1;
                // Проверяем соседние элементы, которые тоже включают точку
                for (int i = mid - 1; i >= 0 && segments[i].start <= point && point <= segments[i].stop; i--) count++;
                for (int i = mid + 1; i < segments.length && segments[i].start <= point && point <= segments[i].stop; i++) count++;
                return count;
            }
            if (point < segments[mid].start) right = mid - 1;
            else left = mid + 1;
        }
        return 0;
    }
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }
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
