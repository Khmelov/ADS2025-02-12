package by.it.group410902.grigorev.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        // Получаем поток ввода из файла "dataA.txt"
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");

        // Создаем экземпляр класса для вызова метода
        A_QSort instance = new A_QSort();

        // Вызываем метод getAccessory для определения принадлежности точек
        int[] result = instance.getAccessory(stream);

        // Выводим результат
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        // Создаем Scanner для считывания входных данных
        Scanner scanner = new Scanner(stream);

        // Читаем количество отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        // Читаем количество точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Заполняем массив отрезков
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        // Заполняем массив точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по началу (используется Comparable)
        Arrays.sort(segments);

        // Для каждой точки определяем, сколько отрезков ее покрывает
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            for (Segment segment : segments) {
                if (segment.start > point) break; // Оптимизация: дальнейшие отрезки не могут содержать точку
                if (segment.stop >= point) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    // Вложенный класс Segment представляет отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        // Конструктор сегмента, упорядочивает границы
        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        // Метод для сравнения отрезков по начальной точке
        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}

