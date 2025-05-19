package by.it.group451002.jasko.lesson05;

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
        // Получаем входной поток данных из файла
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        // Создаем экземпляр класса для обработки данных
        A_QSort instance = new A_QSort();
        // Получаем результат обработки
        int[] result = instance.getAccessory(stream);
        // Выводим результат для каждой точки
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        // Чтение количества отрезков (периодов работы камер)
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        // Чтение количества точек (моментов событий)
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение данных о периодах работы камер
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            // Создаем отрезок, упорядочивая начало и конец
            segments[i] = new Segment(Math.min(start, stop), Math.max(start, stop));
        }

        // Чтение моментов времени событий
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по времени начала записи с помощью быстрой сортировки
        quickSort(segments, 0, segments.length - 1);

        // Для каждой точки определяем количество отрезков (камер), которые ее содержат
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // Проверяем каждый отрезок (период работы камеры)
            for (Segment segment : segments) {
                // Если точка попадает в отрезок (включительно границы)
                if (point >= segment.start && point <= segment.stop) {
                    count++;
                }
                // Если точка меньше начала отрезка,
                // дальше проверять не нужно (отрезки отсортированы)
                if (point < segment.start) {
                    break;
                }
            }
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Реализация быстрой сортировки для массива отрезков
    private void quickSort(Segment[] segments, int low, int high) {
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
        // Выбираем последний элемент как опорный
        Segment pivot = segments[high];
        int i = low - 1; // Индекс меньшего элемента

        for (int j = low; j < high; j++) {
            // Если текущий элемент меньше или равен опорному
            if (segments[j].compareTo(pivot) <= 0) {
                i++;
                // Меняем местами отрезки
                Segment temp = segments[i];
                segments[i] = segments[j];
                segments[j] = temp;
            }
        }

        // Меняем местами опорный элемент и элемент на позиции i+1
        Segment temp = segments[i + 1];
        segments[i + 1] = segments[high];
        segments[high] = temp;

        return i + 1; // Возвращаем индекс опорного элемента
    }

    // Класс, представляющий отрезок времени работы камеры
    private static class Segment implements Comparable<Segment> {
        int start; // Время начала работы камеры
        int stop;  // Время окончания работы камеры

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
            // Конструктор получает упорядоченные значения
        }

        // Метод сравнения отрезков для сортировки
        @Override
        public int compareTo(Segment o) {
            // Сравниваем сначала по началу отрезка
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            } else {
                // Если начала равны, сравниваем по концу отрезка
                return Integer.compare(this.stop, o.stop);
            }
        }
    }
}