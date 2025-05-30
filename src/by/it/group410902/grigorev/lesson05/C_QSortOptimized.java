package by.it.group410902.grigorev.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        // Получаем поток ввода из файла "dataC.txt"
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");

        // Создаем экземпляр класса
        C_QSortOptimized instance = new C_QSortOptimized();

        // Выполняем подсчет пересечений
        int[] result = instance.getAccessory2(stream);

        // Выводим результат
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
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

        // Сортируем отрезки по началу
        Arrays.sort(segments);

        // Для каждой точки ищем число пересекающих ее отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Бинарный поиск первого отрезка, начинающегося после точки
            int left = 0;
            int right = n;
            while (left < right) {
                int mid = (left + right) / 2;
                if (segments[mid].start > point) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            int firstAfter = left;

            // Подсчет отрезков, пересекающих точку
            int count = 0;
            for (int j = 0; j < firstAfter; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}
