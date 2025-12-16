package by.it.group451003.mihlin.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

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

        // Чтение количества отрезков (камер)
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        // Чтение количества точек (событий)
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение отрезков (время работы камер)
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            // Упорядочиваем отрезок, если концы заданы в обратном порядке
            segments[i] = new Segment(Math.min(start, end), Math.max(start, end));
        }

        // Чтение точек (время событий)
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по начальной точке
        Arrays.sort(segments);

        // Для каждой точки считаем количество отрезков, которые её содержат
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // Используем бинарный поиск для оптимизации
            int left = 0;
            int right = n - 1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].start <= point) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            // Теперь проверяем все отрезки до right
            for (int j = 0; j <= right; j++) {
                if (segments[j].start <= point && point <= segments[j].stop) {
                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    // Класс отрезка с реализацией Comparable для сортировки
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            // Сортируем отрезки по начальной точке
            return Integer.compare(this.start, o.start);
        }
    }
}