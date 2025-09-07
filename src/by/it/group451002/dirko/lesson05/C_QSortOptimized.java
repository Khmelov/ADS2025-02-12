package by.it.group451002.dirko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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

        // Если количество отрезков или точек равно нулю, то возвращаем результат
        if (n == 0 || m == 0) { return result; }

        // Вызываем функцию сортировки для отрезков работы (в приоритете более раннее начало)
        quickSort(segments, 0, n - 1);

        // Для каждого события ищем подходящие отрезки
        for (int i = 0; i < m; i++) {
            // Ищем бинарным поиском отрезок работы у которого начало <= событие
            boolean isFinded = false;
            int left = 0, right = n - 1, mid = -1;
            while (left <= right) {
                mid = (left + right) / 2;
                if (points[i] < segments[mid].start) { right = mid - 1; }
                else {
                    isFinded = true;
                    break;
                }
            }
            if (isFinded) {
                // Ищем справа подходящие отрезки, пока начало отрезка работы <= событие
                for (int j = mid; j <= right; j++) {
                    if (points[i] >= segments[j].start) {
                        if (points[i] <= segments[j].stop) { result[i]++; }
                    }
                    else { break; }
                }
                // Ищем слева подходящие отрезки, пока начало отрезка работы <= событие
                for (int j = mid - 1; j >= left; j--) {
                    if (points[i] >= segments[j].start) {
                        if (points[i] <= segments[j].stop) { result[i]++; }
                    }
                    else { break; }
                }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Процедура для сортировки массива, используя QuickSort
    void quickSort(Segment[] A, int left, int right) {
        // Выполняем цикл, пока левая граница меньше правой
        while (left < right) {
            // Вызываем функцию для частичной сортировки и получаем левую и правую границы
            int[] m = partition(A, left, right);

            // Вызываем рекурсию для левой половины относительно центрального элемента
            quickSort(A, left, m[0] - 1);

            // Меняем левую границу, чтобы элиминировать хвостовую рекурсию
            left = m[1] + 1;
        }
    }

    // Вспомогательная функция для сортировки QuickSort
    int[] partition(Segment[] A, int left, int right) {
        // Выбираем опорный элемент (первый)
        Segment pivot = A[left];

        // Сравниваем текущий элемент с опорным, и при необходимости (<) двигаем текущий ближе к опорному
        int j = left;
        for (int i = left + 1; i <= right; i++) {
            if (A[i].compareTo(pivot) == 1) {
                Segment temp = A[i];
                A[i] = A[++j];
                A[j] = temp;
            }
        }

        // Меняем опорный и центральный элементы местами
        Segment temp = A[left];
        A[left] = A[j];
        A[j] = temp;

        // Сравниваем текущий элемент с опорным, и при необходимости (==) двигаем текущий ближе к опорному
        int k = j;
        for (int i = k + 1; i <= right; i++) {
            if (A[i].compareTo(pivot) == -1) {
                temp = A[i];
                A[i] = A[++k];
                A[k] = temp;
            }
        }

        // Возвращаем индекс центрального элемента
        return new int[]{j, k};
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start <= stop) {
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
            // Если начало не меньше, то 1; иначе 0
            if (this.start < ((Segment) o).start) { return 1; }
            if (this.start == ((Segment) o).start) { return -1; }
            return 0;
        }
    }

}
