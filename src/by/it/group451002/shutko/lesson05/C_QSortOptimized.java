package by.it.group451002.shutko.lesson05;

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
//Отрезки сортируются по их начальной точке, что позволяет использовать бинарный поиск для поиска подходящих отрезков.
//Поиск отрезков для каждой точки:
//Для каждой точки выполняется бинарный поиск, чтобы найти первый отрезок, который может содержать данную точку.
//В бинарном поиске проверяется, находится ли точка в пределах отрезка. Если отрезок содержит точку, результат для этой
// точки устанавливается в 1.
//Если отрезок не содержит точку, бинарный поиск продолжается до тех пор, пока не будут проверены все подходящие отрезки.
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
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортировка отрезков по начальной точке
        Arrays.sort(segments);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int left = 0, right = n - 1;

            // Бинарный поиск для нахождения первого подходящего отрезка
            while (left <= right) {
                int mid = (left + right) / 2;
                if (segments[mid].start <= point && segments[mid].stop >= point) {
                    result[i] = 1;
                    break;
                } else if (segments[mid].start > point) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }

        return result;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }

}
