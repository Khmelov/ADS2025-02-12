package by.it.group410901.kovalevich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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
        quickSort(segments, 0, n - 1);
        for (int i = 0; i < m; i++) {
            int j = 0;
            while (j < n && segments[j].start <= points[i]) {
                if (segments[j].stop >= points[i]) {
                    result[i] += 1;
                }
                j++;
            }
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            int cmp = Integer.compare(this.start, o.start);
            if (cmp != 0) {
                return cmp;
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

    public static void quickSort(Segment[] items, int low, int high) {
        if (low < high) {
            // Находим индекс опорного элемента (pivot)
            int pivotIndex = partition(items, low, high);

            // Рекурсивно сортируем левую и правую части
            quickSort(items, low, pivotIndex - 1);
            quickSort(items, pivotIndex + 1, high);
        }
    }

    private static int partition(Segment[] items, int low, int high) {
        // Используем последний элемент как опорный (pivot)
        Segment pivot = items[high];
        int i = low - 1; // Индекс меньшего элемента

        for (int j = low; j < high; j++) {
            // Если текущий элемент меньше или равен pivot
            if (items[j].compareTo(pivot) <= 0) {
                i++;
                // Меняем местами items[i] и items[j]
                swap(items, i, j);
            }
        }

        // Меняем местами items[i + 1] и pivot (items[high])
        swap(items, i + 1, high);

        return i + 1; // Возвращаем индекс опорного элемента
    }

    private static void swap(Segment[] items, int i, int j) {
        Segment temp = items[i];
        items[i] = items[j];
        items[j] = temp;
    }

}
