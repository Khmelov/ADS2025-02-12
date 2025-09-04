package by.it.group451002.kravtsov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

/*
//Видеорегистраторы и площадь.
//На площади установлена одна или несколько камер.
//Известны данные о том, когда каждая из них включалась и выключалась (отрезки работы)
//Известен список событий на площади (время начала каждого события).
//Вам необходимо определить для каждого события сколько камер его записали.
//
//В первой строке задано два целых числа:
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
        // Открываем файл dataA.txt для чтения
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();

        // Получаем массив результатов
        int[] result = instance.getAccessory(stream);

        // Выводим количество камер для каждой точки
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        // Создаём объект Scanner для чтения данных
        Scanner scanner = new Scanner(stream);

        // Читаем количество отрезков (работающих камер)
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        // Читаем количество событий (точек)
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Заполняем массив отрезков
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt(); // Начало работы камеры
            int stop = scanner.nextInt();  // Конец работы камеры

            // Убедимся, что start <= stop
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }

            // Добавляем отрезок в массив
            segments[i] = new Segment(start, stop);
        }

        // Читаем координаты точек событий
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем массив отрезков по началу работы
        // Сортируем диапазоны по началу записи
        Arrays.sort(segments);

        // Для каждой точки считаем количество перекрытий с отрезками
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // Проверяем, входит ли точка в отрезки
            for (Segment segment : segments) {
                if (segment.start > point) {
                    break; // Если начало отрезка больше точки, остальные тоже не подходят
                }
                if (segment.stop >= point) {
                    count++; // Увеличиваем счётчик, если точка в пределах отрезка
                }
            }

            // Записываем результат
            result[i] = count;
        }

        return result;
    }





    // Класс для представления отрезков работы камер
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start); // Сортируем по началу отрезка
        }
    }
}
