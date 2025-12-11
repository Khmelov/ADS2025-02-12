package by.it.group410901.garkusha.lesson05;

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

        // Сортируем отрезки на месте быстрой сортировкой с 3-разбиением
        qsort(segments, 0, n - 1);

        // Для каждой точки считаем количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            result[i] = countSegmentsCoveringPoint(segments, points[i]);
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Быстрая сортировка с 3-разбиением и элиминацией хвостовой рекурсии
    private void qsort(Segment[] array, int left, int right) {
        while (left < right) {
            int lt = left, gt = right;
            Segment pivot = array[left];
            int i = left + 1;
            while (i <= gt) {
                int cmp = array[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(array, lt++, i++);
                } else if (cmp > 0) {
                    swap(array, i, gt--);
                } else {
                    i++;
                }
            }
            // Рекурсивный вызов на меньшую часть, хвостовая рекурсия на большую
            if (lt - left < right - gt) {
                qsort(array, left, lt - 1);
                left = gt + 1; // хвостовая рекурсия
            } else {
                qsort(array, gt + 1, right);
                right = lt - 1; // хвостовая рекурсия
            }
        }
    }

    private void swap(Segment[] array, int i, int j) {
        Segment temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Бинарный поиск первого отрезка, у которого start <= point
    private int binarySearchFirstSegment(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1;
        int result = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (segments[mid].start <= point) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    // Подсчёт количества отрезков, покрывающих точку
    private int countSegmentsCoveringPoint(Segment[] segments, int point) {
        int idx = binarySearchFirstSegment(segments, point);
        if (idx == -1) return 0;

        int count = 0;

        // Идём влево от idx (проверка на покрытие точки)
        for (int i = idx; i >= 0; i--) {
            if (segments[i].start <= point && segments[i].stop >= point) {
                count++;
            } else {
                break;
            }
        }
        return count;
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

            Segment other = (Segment) o;
            if (this.start != other.start)
                return Integer.compare(this.start, other.start);
            else
                return Integer.compare(this.stop, other.stop);
        }
    }

}
