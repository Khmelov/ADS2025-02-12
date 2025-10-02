package by.it.group410901.bukshta.lesson05;

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
        if (stream == null) {
            stream = System.in; // Fallback to standard input
        }
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        // Initialize scanner
        Scanner scanner = new Scanner(stream);

        // Read number of segments and points
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        // Create events array
        Event[] events = new Event[2 * n + m];
        int eventIndex = 0;

        // Read segments and create start/end events
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            Segment segment = new Segment(start, stop);
            events[eventIndex++] = new Event(segment.start, 0, -1); // Start event
            events[eventIndex++] = new Event(segment.stop, 2, -1);  // End event
        }
    //Считываем количество камер n, событий m, интервалы работы камер
    // (отрезки) и моменты событий (точки).
        // Read points and create point events
        int[] points = new int[m];
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
            events[eventIndex++] = new Event(points[i], 1, i); // Point event
        }
    //Создаём массив событий:
    //Для каждой камеры: событие начала (включение) и конца (выключение).
    //Для каждой точки: событие проверки (подсчёт активных камер).
    //Всего событий: 2n (начала и концы) + m (точки).
        // Sort events using QuickSort (Arrays.sort uses QuickSort internally)
        Arrays.sort(events);
        //Сортируем события по времени. При равных временах приоритет: начала камер, затем точки, затем концы.
        // Process events to count active segments for each point
        int[] result = new int[m];
        int activeSegments = 0;
        for (Event event : events) {
            if (event.type == 0) { // Segment start
                activeSegments++;
            } else if (event.type == 2) { // Segment end
                activeSegments--;
            } else { // Point (type == 1)
                result[event.pointIndex] = activeSegments;
            }
        }
        //Проходим по событиям, поддерживая счётчик активных камер:
        //Начало камеры: увеличиваем счётчик.
        //Конец камеры: уменьшаем счётчик.
        //Точка: записываем текущее значение счётчика в результат.
        //Возвращаем массив с количеством камер для каждой точки.
        scanner.close();
        return result;
    }

    // Event class to represent segment starts, ends, and points
    private class Event implements Comparable<Event> {
        int time;      // Time of the event
        int type;      // 0 = segment start, 1 = point, 2 = segment end
        int pointIndex; // Index of point in input (for points only, else -1)

        Event(int time, int type, int pointIndex) {
            this.time = time;
            this.type = type;
            this.pointIndex = pointIndex;
        }

        @Override
        public int compareTo(Event o) {
            // Sort by time; if equal, prioritize start (0), then point (1), then end (2)
            if (this.time != o.time) {
                return Integer.compare(this.time, o.time);
            }
            return Integer.compare(this.type, o.type);
        }
    }

    // Segment class
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // Ensure start <= stop
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            // Compare by start; if equal, compare by stop
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}