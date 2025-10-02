package by.it.group410902.habrukovich.lesson05;

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
        // Быстрая сортировка с 3-разбиением и без хвостовой рекурсии
        quickSort(segments, 0, segments.length - 1);

// Для каждой точки ищем, сколько отрезков её накрывает
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int idx = binarySearch(segments, point);
            int count = 0;
            if (idx != -1) {
                // идём влево
                for (int j = idx; j >= 0 && segments[j].start <= point; j--) {
                    if (segments[j].stop >= point) count++;
                }
                // идём вправо
                for (int j = idx + 1; j < n && segments[j].start <= point; j++) {
                    if (segments[j].stop >= point) count++;
                }
            }
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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
            Segment other = (Segment) o;
            if (this.start<other.start) return -1;
            else if (this.start>other.start) return 1;
            else {
                if (this.stop<other.stop) return -1;
                else if (this.stop>other.stop) return 1;
                else return 0;
            }
        }
    }
    private int binarySearch(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1;
        int res = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (segments[mid].start <= point && segments[mid].stop >= point) {
                res = mid;
                right = mid - 1; // ищем первый подходящий
            } else if (segments[mid].start > point) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return res;
    }
    private void quickSort(Segment[] a, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;
            Segment pivot = a[low];
            int i = low;
            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if (cmp < 0) swap(a, lt++, i++);
                else if (cmp > 0) swap(a, i, gt--);
                else i++;
            }
            if (lt - low < high - gt) {
                quickSort(a, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort(a, gt + 1, high);
                high = lt - 1;
            }
        }
    }
    private void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
