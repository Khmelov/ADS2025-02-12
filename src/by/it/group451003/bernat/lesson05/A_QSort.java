package by.it.group451003.bernat.lesson05;

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
        // Загружаем входные данные из файла "dataA.txt"
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        // Вызываем метод для подсчета количества камер, записавших каждое событие
        int[] result = instance.getAccessory(stream);
        // Выводим результат: количество камер для каждой точки
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        // Создаем объект Scanner для чтения данных из входного потока
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // Считываем количество отрезков (камер)
        int n = scanner.nextInt();
        // Считываем количество точек (событий)
        int m = scanner.nextInt();
        // Создаем массив для хранения отрезков
        Segment[] segments = new Segment[n];
        // Создаем массив для хранения координат точек
        int[] points = new int[m];
        // Создаем массив для хранения результата (количество камер для каждой точки)
        int[] result = new int[m];

        // Читаем данные об отрезках (время работы камер)
        for (int i = 0; i < n; i++) {
            // Считываем начало и конец отрезка
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            // Создаем объект Segment и сохраняем его в массив
            segments[i] = new Segment(start, stop);
        }
        // Читаем координаты точек (время событий)
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Реализуем алгоритм "линейного сканирования" (sweep line)
        // Создаем массив событий для обработки всех точек и отрезков
        Event[] events = new Event[2 * n + m];
        int eventIndex = 0;
        // Добавляем события для начала и конца каждого отрезка
        for (int i = 0; i < n; i++) {
            // Событие начала отрезка (тип 0)
            events[eventIndex++] = new Event(segments[i].start, 0, -1);
            // Событие конца отрезка (тип 2)
            events[eventIndex++] = new Event(segments[i].stop, 2, -1);
        }
        // Добавляем события для точек (события, которые нужно проверить)
        for (int i = 0; i < m; i++) {
            // Событие точки (тип 1), сохраняем индекс точки для результата
            events[eventIndex++] = new Event(points[i], 1, i);
        }

        // Сортируем события по координате (при равных координатах: начало < точка < конец)
        // Это гарантирует правильный порядок обработки
        Arrays.sort(events);

        // Обрабатываем события, подсчитывая активные отрезки
        int activeSegments = 0; // Текущее количество активных камер
        for (Event event : events) {
            if (event.type == 0) {
                // Событие: начало отрезка, увеличиваем счетчик активных камер
                activeSegments++;
            } else if (event.type == 1) {
                // Событие: точка, сохраняем текущее количество активных камер для этой точки
                result[event.index] = activeSegments;
            } else {
                // Событие: конец отрезка, уменьшаем счетчик активных камер
                activeSegments--;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // Возвращаем массив с количеством камер для каждой точки
        return result;
    }

    // Класс для представления отрезка (время работы камеры)
    private class Segment implements Comparable<Segment> {
        int start; // Начало отрезка
        int stop;  // Конец отрезка

        Segment(int start, int stop) {
            // Если start > stop, меняем их местами для корректности
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        // Метод сравнения для сортировки отрезков (по началу, затем по концу)
        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

    // Класс для представления события в алгоритме линейного сканирования
    private class Event implements Comparable<Event> {
        long coord; // Координата события (время)
        int type;   // Тип события: 0 - начало отрезка, 1 - точка, 2 - конец отрезка
        int index;  // Индекс точки в массиве точек (для результата, -1 для отрезков)

        Event(long coord, int type, int index) {
            this.coord = coord;
            this.type = type;
            this.index = index;
        }

        // Метод сравнения для сортировки событий
        // Сначала по координате, затем по типу (начало < точка < конец)
        @Override
        public int compareTo(Event o) {
            if (this.coord != o.coord) {
                return Long.compare(this.coord, o.coord);
            }
            return Integer.compare(this.type, o.type);
        }
    }
}