package by.it.group451003.galuzo.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        // Чтение числа отрезков (камер) и числа событий (точек)
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение отрезков работы камер
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            // Упорядочивание начала и конца отрезка, если они заданы в обратном порядке
            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
            }
            segments[i] = new Segment(start, end);
        }

        // Чтение точек событий
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортировка отрезков по начальной точке с помощью быстрой сортировки
        Arrays.sort(segments);

        // Для каждой точки события определяем количество отрезков, которые её покрывают
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;
            // Перебираем все отрезки, у которых начало <= точке
            // Поскольку отрезки отсортированы, можно использовать бинарный поиск для оптимизации
            for (Segment segment : segments) {
                if (segment.start > point) {
                    break; // Отрезки дальше будут иметь начало больше точки, их можно не проверять
                }
                if (segment.stop >= point) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    // Класс отрезка с реализацией интерфейса Comparable для сортировки
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            // Сравниваем отрезки по начальной точке
            return Integer.compare(this.start, o.start);
        }
    }
}