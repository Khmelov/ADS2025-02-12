package by.it.group451004.levkovich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Stack;

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
            result[i] = 0;
            for (int j = 0; j < n && segments[j].start <= points[i]; j++){ //вот сюда бы бинарный поиск для максимального старта
                if (segments[j].stop >= points[i]) {
                    result[i]++;
                }
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    void quickSort(Segment[] array, int left, int right) {
        Stack<StackLimits> stack = new Stack<>();
        stack.push(new StackLimits(left, right));
        while (!stack.isEmpty()) {
            StackLimits current = stack.pop();
            left = current.left;
            right = current.right;
            if (right - left < 2) {
                if (array[left].compareTo(array[right]) > 0) {
                    Segment temp = array[left];
                    array[left] = array[right];
                    array[right] = temp;
                }
            } else {
                partitionArray(array, left, right, stack);
            }
        }
    }

    void partitionArray(Segment[] array, int left, int right, Stack<StackLimits> stack) {
        Segment mid = array[left + (right - left) / 2];
        int i = left;
        int j = right;

        while (i < j) {
            while (i <= right && array[i].compareTo(mid) < 0) {
                i++;
            }
            while (j >= left && array[j].compareTo(mid) >= 0) {
                j--;
            }
            if (i >= j) {
                break;
            }
            Segment temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            i++;
            j--;
        }
        stack.push(new StackLimits(left, j));
        //продолжение разбития
        j++;
        i = right;
        while (i > j) {
            while (j <= right && array[j].compareTo(mid) == 0) {
                j++;
            }
            if (i > j && array[i].compareTo(mid) == 0) {
                Segment temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
            i++;
        }
        stack.push(new StackLimits(j, right));
    }

    private class StackLimits {
        int left;
        int right;
        public StackLimits(int left, int right) {
            this.left = left;
            this.right = right;
        }
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
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            } else {
                return Integer.compare(this.stop, o.stop);
            }
        }
    }

}
