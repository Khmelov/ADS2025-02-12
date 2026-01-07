package by.it.group451004.kaberdin.lesson05;

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
    2 3    //2 - число включений камер, 3 - число событий
    0 5    //первое включение
    7 10   //второе включение
    1 6 11 //события
    Sample Output:
    1 0 0  //число покрытий для каждого события
*/

public class A_QSort {

    // Главный метод программы
    public static void main(String[] args) throws FileNotFoundException {
        // Получаем входной поток данных из файла
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        // Создаем экземпляр класса
        A_QSort instance = new A_QSort();
        // Получаем результат обработки данных
        int[] result = instance.getAccessory(stream);
        // Выводим результат
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    // Метод для обработки данных и подсчета покрытий точек отрезками
    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        // Создаем сканер для чтения данных
        Scanner scanner = new Scanner(stream);
        // Читаем количество отрезков (n) и количество точек (m)
        int n = scanner.nextInt();
        // Создаем массив отрезков
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        // Создаем массив точек и результатов
        int[] points = new int[m];
        int[] result = new int[m];

        // Заполняем массив отрезков
        for (int i = 0; i < n; i++) { //отрезки
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        // Заполняем массив точек
        for (int i = 0; i < m; i++) { //события
            points[i] = scanner.nextInt();
        }
        // Сортируем отрезки с помощью быстрой сортировки
        quickSort(segments, 0, n-1);

        // Для каждой точки считаем количество покрывающих ее отрезков
        for (int i = 0; i < m; i++) {
            result[i] = 0;
            // Проверяем только те отрезки, начало которых <= текущей точки
            for (int j = 0; j < n && segments[j].start <= points[i]; j++){
                // Если точка попадает в отрезок, увеличиваем счетчик
                if (segments[j].stop >= points[i]) {
                    result[i]++;
                }
            }
        }
        return result;
    }

    // Реализация быстрой сортировки для массива отрезков
    void quickSort(Segment[] array, int left, int right) {
        // Базовый случай для маленьких подмассивов
        if (right - left < 2) {
            if (array[left].compareTo(array[right]) > 0) {
                Segment temp = array[left];
                array[left] = array[right];
                array[right] = temp;
            }
        } else {
            // Выбираем средний элемент как опорный
            int mid = left + (right - left) / 2;
            int i = left;
            int j = right;
            // Процесс разделения
            while (i < j) {
                // Пропускаем элементы меньше опорного
                while (array[i].compareTo(array[mid]) < 0) {
                    i++;
                }
                // Пропускаем элементы больше опорного
                while (array[j].compareTo(array[mid]) > 0) {
                    j--;
                }
                if (i >= j) {
                    break;
                }
                // Меняем местами элементы
                Segment temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
            // Рекурсивно сортируем подмассивы
            quickSort(array, left, j);
            quickSort(array, j + 1, right);
        }
    }

    // Внутренний класс для представления отрезков
    private class Segment implements Comparable<Segment> {
        int start;  // Начало отрезка
        int stop;   // Конец отрезка

        // Конструктор отрезка (упорядочивает начало и конец)
        Segment(int start, int stop) {
            if (start < stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.stop = start;
                this.start = stop;
            }
        }

        // Реализация сравнения отрезков для сортировки
        @Override
        public int compareTo(Segment o) {
            // Сначала сравниваем по началу, затем по концу
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            } else {
                return Integer.compare(this.stop, o.stop);
            }
        }
    }
}