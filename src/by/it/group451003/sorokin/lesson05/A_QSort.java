package by.it.group451003.sorokin.lesson05;

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

        // Чтение количества отрезков и точек
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение отрезков
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(Math.min(start, stop), Math.max(start, stop));
        }

        // Чтение точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортировка отрезков по началу
        Arrays.sort(segments, (s1, s2) -> Integer.compare(s1.start, s2.start));

        // Для каждой точки находим количество отрезков, содержащих её
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // Используем бинарный поиск для оптимизации
            int left = 0;
            int right = n - 1;
            int foundIndex = -1;

            // Находим первый отрезок, который начинается после точки
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].start <= point) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            foundIndex = right;

            // Проверяем все отрезки до foundIndex
            for (int j = 0; j <= foundIndex; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    private class Segment {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }
    }
}