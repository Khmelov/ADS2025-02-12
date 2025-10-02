package by.it.group451004.struts.lesson05;

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

        quickSort(segments, 0, n - 1);
        for (int i = 0; i < m; i++) {
            int point = points[i], j = 0;
            while (j < n && segments[j].start <= point) {
                if (segments[j].stop >= point)
                    result[i]++;
                j++;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    void quickSort(Segment[] segments, int left, int right) {
        if (left >= right)
            return;

        Segment pivot = segments[(left + right) / 2];
        Segment[] temp = new Segment[(right - left) * 2 + 1];
        temp[temp.length / 2] = pivot;
        int pivotLIndex = temp.length / 2;
        int pivotRIndex = temp.length / 2;
        for (int i = left; i <= right; i++) {
            if (segments[i].compareTo(pivot) < 0) {
                temp[--pivotLIndex] = segments[i];
            } else if (segments[i].compareTo(pivot) > 0) {
                temp[++pivotRIndex] = segments[i];
            }
        }
        if (pivotLIndex - left >= 0) System.arraycopy(temp, pivotLIndex, segments, left, pivotRIndex - pivotLIndex);

        quickSort(segments, left, (left + right) / 2 - 1);
        quickSort(segments, (left + right) / 2 + 1, right);
    }

    //отрезок
    private static class Segment implements Comparable<Segment> {
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
            //подумайте, что должен возвращать компаратор отрезков
            if (o.start > this.start)
                return -1;
            if (o.start < this.start)
                return 1;
            return 0;
        }
    }

}
