package by.it.group410901.kovalevich.lesson05;

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
         quickSort(segments, 0, n-1);
        for (int i = 0; i < m; i++) {
            int j = 0;
            while (j<n && segments[j].start <= points[i]) {
                if (segments[j].stop >= points[i]) {
                    result[i] += 1;
                }
                j++;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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
            //подумайте, что должен возвращать компаратор отрезков
            int cmp = Integer.compare(this.start, o.start);
            if (cmp != 0) {
                return cmp;
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

    public static void quickSort(Segment[] items, int low, int high) {
        while (low < high) {
            // Находим индекс опорного элемента (pivot)
            int pivotIndex = partition(items, low, high);

            // Рекурсивно сортируем левую и правую части
            quickSort(items, low, pivotIndex - 1);
            //quickSort(items, pivotIndex + 1, high);
            low=pivotIndex+1;
        }
    }

    private static int partition(Segment[] items, int low, int high) {
        // Используем первый элемент как опорный (pivot)
        Segment pivot = items[low];
        int j = low; // Индекс меньшего элемента

        for (int i = low + 1; i < high; i++) {

            if (items[i].compareTo(pivot) < 0) {
                j++;
                // Меняем местами items[i] и items[j]
                swap(items, j, i);
            }
            if (items[i].compareTo(pivot) == 0) {
                j++;
            }

        }


        swap(items, low, j);

        return j;
    }

    private static void swap(Segment[] items, int i, int j) {
        Segment temp = items[i];
        items[i] = items[j];
        items[j] = temp;
    }

}

