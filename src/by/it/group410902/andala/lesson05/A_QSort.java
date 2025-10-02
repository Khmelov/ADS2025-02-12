package by.it.group410902.andala.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        // Получаем входной поток данных из файла
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        // Получаем результат обработки данных
        int[] result = instance.getAccessory(stream);
        // Выводим результат
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        // Инициализируем сканер для чтения данных
        Scanner scanner = new Scanner(stream);
        // Читаем количество отрезков (камер)
        int n = scanner.nextInt();
        // Читаем количество событий (точек)
        int m = scanner.nextInt();
        // Создаем массив отрезков
        Segment[] segments = new Segment[n];
        // Создаем массив точек
        int[] points = new int[m];
        // Создаем массив для хранения результатов
        int[] result = new int[m];

        // Заполняем массив отрезков
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            // Создаем отрезок [start, end]
            segments[i] = new Segment(start, end);
        }

        // Заполняем массив точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по начальной точке
        Arrays.sort(segments);

        // Для каждой точки определяем количество отрезков, которые ее содержат
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;
            // Перебираем отрезки, пока их начало не превысит текущую точку
            for (Segment segment : segments) {
                if (segment.start > point) {
                    // Отрезки отсортированы, дальше проверять не нужно
                    break;
                }
                // Если отрезок содержит точку, увеличиваем счетчик
                if (segment.stop >= point) {
                    count++;
                }
            }
            // Записываем результат для текущей точки
            result[i] = count;
        }

        return result;
    }

    // Класс для представления отрезка [start, stop]
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        // Конструктор отрезка
        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        // Метод для сравнения отрезков по начальной точке
        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}