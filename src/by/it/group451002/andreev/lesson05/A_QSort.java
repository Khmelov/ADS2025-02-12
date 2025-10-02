package by.it.group451002.andreev.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Класс A_QSort решает задачу подсчета количества отрезков,
 * которые содержат заданные точки. Используется бинарный поиск и сортировка.
 */
public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataA.txt"
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();

        // Выполняем поиск количества покрытых точек и выводим результат
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    /**
     * Метод getAccessory читает данные, сортирует отрезки и вычисляет количество покрытых точек.
     * @param stream входной поток данных
     * @return массив, содержащий количество покрывающих отрезков для каждой точки
     * @throws FileNotFoundException если файл не найден
     */
    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем количество отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        // Читаем количество точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Читаем начало и конец каждого отрезка
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            // Убеждаемся, что start <= stop
            segments[i] = new Segment(Math.min(start, stop), Math.max(start, stop));
        }

        // Читаем координаты точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по начальной координате
        Arrays.sort(segments);

        // Для каждой точки определяем количество покрывающих ее отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // Бинарный поиск первого отрезка, у которого start > point
            int left = 0;
            int right = n - 1;
            int firstAfter = n; // По умолчанию считаем, что таких отрезков нет

            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].start > point) {
                    firstAfter = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }

            // Проверяем все отрезки перед firstAfter, возможно они покрывают точку
            for (int j = 0; j < firstAfter; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }

            // Сохраняем количество покрывающих отрезков
            result[i] = count;
        }

        return result;
    }

    /**
     * Класс Segment представляет собой отрезок с началом и концом.
     * Используется для сортировки отрезков по их начальной точке.
     */
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
