package by.it.group410902.kavtsevich.lesson05;

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
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(start, stop);
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        //Сортируем отрезки
        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            result[i] = binarySearchCount(segments, points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    private void quickSort(Segment[] array, int low, int high)// Быстрая сортировка с трех-разбиением
    {
        while (low < high) {
            int[] p = partition(array, low, high);
            if (p[0] - low < high - p[1]) {//сначала сортируем меньшую часть массива
                quickSort(array, low, p[0] - 1);
                low = p[1] + 1;
            } else {
                quickSort(array, p[1] + 1, high);
                high = p[0] - 1;
            }
        }
    }
    private int[] partition(Segment[] array, int low, int high) // Метод разбиения массива на три части
    {
        Segment pivot = array[high];
        int lt = low, gt = high;

        int i = low;
        while (i <= gt) {
            if (array[i].compareTo(pivot) < 0) {
                swap(array, lt++, i++);
            } else if (array[i].compareTo(pivot) > 0) {
                swap(array, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt, gt}; // Метод разбиения массива на три части
    }
    private void swap(Segment[] array, int i, int j) {// Метод обмена элементов
        Segment temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    private int binarySearchCount(Segment[] segments, int point) {// Бинарный поиск для нахождения отрезков, содержащих точку
        int count = 0;
        int left = 0, right = segments.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (point >= segments[mid].start && point <= segments[mid].stop) {
                count++;
                // Проверяем соседние элементы
                for (int i = mid - 1; i >= 0 && point >= segments[i].start && point <= segments[i].stop; i--) {
                    count++;
                }
                for (int i = mid + 1; i < segments.length && point >= segments[i].start && point <= segments[i].stop; i++) {
                    count++;
                }
                break;
            } else if (point < segments[mid].start) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return count;
    }
    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start > stop) {
                this.start = stop;
                this.stop = start;
            } else {
                this.start = start;
                this.stop = stop;
            }
        }

        @Override
        public int compareTo(Segment o) {
            if (o == null) {
                throw new IllegalArgumentException("Cannot compare to a null object");
            }
            //подумайте, что должен возвращать компаратор отрезков
            return Integer.compare(this.start, o.start);
        }
    }

}
