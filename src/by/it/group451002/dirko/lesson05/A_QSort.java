package by.it.group451002.dirko.lesson05;

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

        // Если количество отрезков или точек равно нулю, то возвращаем результат
        if (n == 0 || m == 0) { return result; }

        // Вызываем функцию сортировки для отрезков работы (в приоритете более раннее начало)
        quickSort(segments, 0, n - 1);

        // Для каждого события ищем подходящие отрезки, если начало события меньше начала отрезка, то завершаем цикл
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (segments[j].start <= points[i] && segments[j].stop >= points[i]) {
                    result[i]++;
                }
                else if (points[i] < segments[j].start) { break; }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Процедура для сортировки массива, используя QuickSort
    void quickSort(Segment[] A, int left, int right) {
        // Если левая граница не меньше правой, то прерываем функцию
        if (left >= right) return;

        // Вызываем функцию для частичной сортировки и получаем центральный элемент
        int m = partition(A, left, right);

        // Вызываем рекурсию для левой и правой половины относительно центрального элемента
        quickSort(A, left, m - 1);
        quickSort(A, m + 1, right);
    }

    // Вспомогательная функция для сортировки QuickSort
    int partition(Segment[] A, int left, int right) {
        // Выбираем опорный элемент (первый)
        Segment pivot = A[left];

        // Сравниваем текущий элемент с опорным, и при необходимости двигаем текущий ближе к опорному
        int j = left;
        for (int i = left + 1; i <= right; i++) {
            if (A[i].compareTo(pivot) == 1) {
                Segment temp = A[i];
                A[i] = A[++j];
                A[j] = temp;
            }
        }

        // Меняем опорный и центральный элементы местами
        Segment temp = A[left];
        A[left] = A[j];
        A[j] = temp;

        // Возвращаем индекс центрального элемента
        return j;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            }
            else {
                this.start = stop;
                this.stop = start;
            }
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке
        }

        @Override
        public int compareTo(Segment o) {
            // Если начало не меньше, то 1; иначе 0
            if (this.start <= o.start) { return 1; }
            return 0;
        }
    }

}
