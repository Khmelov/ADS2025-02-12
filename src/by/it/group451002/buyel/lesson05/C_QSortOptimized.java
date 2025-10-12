package by.it.group451002.buyel.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
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
        //в классе отрезка Segment реализуйте нужный для этой задачи

        QuickSortSegments(segments, 0, segments.length-1);

        QuickSortPoints(points, 0, points.length-1);

        //ArrayList<Integer> indexes = new ArrayList<>();

        // Бинарный поиск всех для всех точек
        for (int i = 0; i < points.length; i++) {
            int mid, left, right;
            left = 0;
            right = segments.length - 1;
            Segment[] copySegments = segments;

            // Пока левый индекс не больше правого
            while (left <= right) {
                mid = (left + right) / 2;

                // если попали
                if (copySegments[mid].start <= points[i] && copySegments[mid].stop >= points[i]) {
                    result[i]++;

                    Segment[] newArray = new Segment[copySegments.length-1];
                    int index = 0;
                    for (int j = 0; j < copySegments.length; j++) {
                        if (j != mid) {
                            newArray[index++] = copySegments[j];
                        }
                    }

                    copySegments = newArray;
                    left = 0;
                    right = copySegments.length - 1;
                }
                // Если значение меньше найденного числа
                else if (copySegments[mid].start > points[i]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }

            //indexes.clear();
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Сортировка QuickSort для диапазона start -> stop
    private void QuickSortSegments(Segment[] array, int left, int right) {
        if (left >= right) {
            return;
        }

        // Разделение массива array на 2 части
        int mid = PartitionSegments(array, left, right);
        QuickSortSegments(array, left, mid - 1);   // левая часть
        QuickSortSegments(array, mid + 1, right);   // правая часть
    }

    private int PartitionSegments(Segment[] thisArray, int thisLeft, int thisRight) {
        Segment tempSeg = thisArray[thisLeft];
        int index = thisLeft;

        for (int i = thisLeft; i <= thisRight; i++) {
            // Если tempSeg > thisArray[i]
            if (tempSeg.compareTo(thisArray[i]) > 0) {
                index++;

                Segment swap = thisArray[index];
                thisArray[index] = thisArray[i];
                thisArray[i] = swap;
            }
        }

        Segment swap = thisArray[thisLeft];
        thisArray[thisLeft] = thisArray[index];
        thisArray[index] = swap;

        return index;
    }

    // QuickSort для точек
    private void QuickSortPoints(int[] array, int left, int right) {
        if (left >= right) {
            return;
        }

        // Разделение массива array на 2 части
        int mid = PartitionPoints(array, left, right);
        QuickSortPoints(array, left, mid - 1);   // левая часть
        QuickSortPoints(array, mid + 1, right);   // правая часть
    }

    private int PartitionPoints(int[] thisPoints, int thisLeft, int thisRight) {
        int tempPoint = thisPoints[thisLeft];
        int index = thisLeft;

        for (int i = thisLeft + 1; i <= thisRight; i++) {
            if (thisPoints[i] < tempPoint) {
                index++;

                int swap = thisPoints[index];
                thisPoints[index] = thisPoints[i];
                thisPoints[i] = swap;
            }
        }

        int swap = thisPoints[thisLeft];
        thisPoints[thisLeft] = thisPoints[index];
        thisPoints[index] = swap;

        return index;
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start < stop) {
                this.start = start;
                this.stop = stop;
            }
            else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Object o) {
            //подумайте, что должен возвращать компаратор отрезков
            if (o instanceof Segment other) {
                return this.start - other.start;
            }
            return 0;
        }
    }

}
