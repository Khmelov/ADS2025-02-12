package by.it.group410902.derzhavskaya_ludmila.lesson05;

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

        // Сортировка отрезков с помощью быстрой сортировки
        quickSort(segments, 0, segments.length - 1);

        // Для каждой точки находим количество отрезков, которые её содержат
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;
            // Используем бинарный поиск для оптимизации
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
            // Проверяем все отрезки до left
            for (int j = 0; j < left; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }
            result[i] = count;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private void quickSort(Segment[] segments, int low, int high)
    {
        if (low < high) {
            // Находим индекс опорного элемента
            int pi = partition(segments, low, high);

            // Рекурсивно сортируем элементы до и после опорного
            quickSort(segments, low, pi - 1);
            quickSort(segments, pi + 1, high);
        }
    }
    // Вспомогательный метод для быстрой сортировки
    private int partition(Segment[] segments, int low, int high) {
        Segment pivot = segments[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (segments[j].compareTo(pivot) <= 0) {
                i++;
                // Меняем местами segments[i] и segments[j]
                Segment temp = segments[i];
                segments[i] = segments[j];
                segments[j] = temp;
            }
        }
        // Меняем местами segments[i+1] и segments[high] (опорный элемент)
        Segment temp = segments[i + 1];
        segments[i + 1] = segments[high];
        segments[high] = temp;

        return i + 1;
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
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков

            return 0;
        }
    }

}
