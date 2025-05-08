package by.it.group451002.sidarchuk.lesson05;

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
        // Сортируем отрезки
        Arrays.sort(segments);

        // Для каждой точки определяем количество подходящих отрезков
        for (int i = 0; i < m; i++) {
            result[i] = countOverlappingSegments(segments, points[i]);
        }

        return result;
    }

    // Метод для подсчета отрезков, которые содержат данную точку
    private int countOverlappingSegments(Segment[] segments, int point) {
        int count = 0;

        // Бинарный поиск для нахождения первого подходящего отрезка
        int leftIndex = binarySearchFirst(segments, point);

        if (leftIndex == -1) return 0;

        // Подсчет всех подходящих отрезков
        for (int i = leftIndex; i < segments.length; i++) {
            if (segments[i].start <= point && segments[i].stop >= point) {
                count++;
            } else if (segments[i].start > point) {
                break; // Выход из цикла, так как дальнейшие отрезки не могут перекрывать точку
            }
        }

        return count;
    }

    // Бинарный поиск для нахождения первого отрезка, который может перекрывать точку
    private int binarySearchFirst(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].stop < point) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return left < segments.length ? left : -1; // Возвращаем индекс первого подходящего отрезка или -1, если нет
    }



        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

    //отрезок
    private class Segment implements Comparable <Segment> {
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
