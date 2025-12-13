package by.it.group451004.rublevskaya.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;
/*
Видеорегистраторы и площадь.
На площади установлена одна или несколько камер.
Известны данные о том, когда каждая из них включалась и выключалась (отрезки работы)
Известен список событий на площади (время начала каждого события).
Вам необходимо определить для каждого события сколько камер его записали.

В первой строке задано два целых числа:
    число включений камер (отрезки) 1<=n<=50000
    число событий (точки) 1<=m<=50000.

Следующие n строк содержат по два целых числа ai и bi (ai<=bi) -
координаты концов отрезков (время работы одной какой-то камеры).
Последняя строка содержит m целых чисел - координаты точек.
Все координаты не превышают 10E8 по модулю (!).

Точка считается принадлежащей отрезку, если она находится внутри него или на границе.

Для каждой точки в порядке их появления во вводе выведите,
скольким отрезкам она принадлежит.
    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/
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

        // Число отрезков и точек
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        // Массивы для начала и конца отрезков
        int[] starts = new int[n];
        int[] ends = new int[n];

        // Чтение отрезков
        for (int i = 0; i < n; i++) {
            starts[i] = scanner.nextInt();
            ends[i] = scanner.nextInt();
        }

        // Чтение точек
        int[] points = new int[m];
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортировка начал и концов отрезков
        Arrays.sort(starts);
        Arrays.sort(ends);

        // Результат
        int[] result = new int[m];

        // Для каждой точки определяем количество охватывающих её отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Количество отрезков, которые начинаются до или в точке
            int countStarts = binarySearchCount(starts, point, true);

            // Количество отрезков, которые заканчиваются до точки
            int countEnds = binarySearchCount(ends, point, false);

            // Точка принадлежит отрезку, если он начинается до или в точке,
            // и заканчивается после или в точке
            result[i] = countStarts - countEnds;
        }

        return result;
    }

    /**
     * Бинарный поиск для подсчёта количества элементов <= (или <) заданного значения.
     *
     * @param array   Отсортированный массив
     * @param value   Значение для сравнения
     * @param includeEqual Флаг: true — считать элементы <= value, false — считать < value
     * @return Количество элементов, удовлетворяющих условию
     */
    private int binarySearchCount(int[] array, int value, boolean includeEqual) {
        int left = 0;
        int right = array.length;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (includeEqual ? array[mid] <= value : array[mid] < value) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }

    // Класс отрезка (не используется в текущей реализации)
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