package by.it.group451002.mishchenko.lesson05;

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
        // Загружаем входные данные из файла dataA.txt, который должен находиться в том же пакете
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        // Выводим результат: для каждой точки выводится число отрезков, её покрывающих
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        // Создаем Scanner для считывания входных данных
        Scanner scanner = new Scanner(stream);
        // Читаем количество отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        // Читаем количество точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Считываем отрезки: для каждого отрезка проверяем, какая из двух точек меньше,
        // чтобы корректно задать начало и конец
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(start, stop);
        }

        // Считываем точки, для которых нужно определить покрытие отрезками
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Создаем два массива для хранения всех начальных и всех конечных точек отрезков
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        // Сортируем массивы с помощью реализации быстрой сортировки
        quickSort(starts, 0, n - 1);
        quickSort(ends, 0, n - 1);

        // Для каждой точки определяем количество отрезков, которые её покрывают
        // cntStart: число отрезков, начальных координат которых меньше или равны x
        // cntEnd: число отрезков, конечных координат которых меньше x
        // Разница между этими значениями и есть количество отрезков, содержащих точку x.
        for (int i = 0; i < m; i++) {
            int x = points[i];
            int cntStart = upperBound(starts, x);
            int cntEnd = lowerBound(ends, x);
            result[i] = cntStart - cntEnd;
        }

        return result;
    }

    /**
     * Быстрая сортировка для массива целых чисел.
     *
     * @param arr Массив, который требуется отсортировать.
     * @param low Нижняя граница (начальный индекс).
     * @param high Верхняя граница (конечный индекс).
     */
    private void quickSort(int[] arr, int low, int high) {
        // Если диапазон содержит хотя бы два элемента
        if (low < high) {
            // Получаем индекс опорного элемента после разбиения массива
            int pivotIndex = partition(arr, low, high);
            // Рекурсивно сортируем левую часть массива
            quickSort(arr, low, pivotIndex - 1);
            // Рекурсивно сортируем правую часть массива
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    /**
     * Функция для разбиения массива относительно опорного элемента.
     *
     * @param arr Массив, который требуется разбить.
     * @param low Нижняя граница текущего диапазона.
     * @param high Верхняя граница текущего диапазона; элемент arr[high] используется в качестве опорного.
     * @return Индекс, куда установлен опорный элемент после разбиения.
     */
    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high]; // Выбираем опорный элемент
        int i = low - 1;       // Индекс для отслеживания конца части с элементами, меньше pivot
        for (int j = low; j < high; j++) {
            // Если текущий элемент меньше или равен опорному, перемещаем его в левую часть
            if (arr[j] <= pivot) {
                i++;
                // Меняем местами arr[i] и arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        // Меняем местами опорный элемент с элементом (i+1), чтобы разместить его на правильную позицию
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    /**
     * Реализация функции upperBound – поиск первого индекса,
     * для которого значение элемента больше заданного ключа.
     *
     * @param arr Отсортированный массив.
     * @param key Искомое значение.
     * @return Первую позицию, где элемент массива строго больше key.
     */
    private int upperBound(int[] arr, int key) {
        int low = 0;
        int high = arr.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (arr[mid] <= key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    /**
     * Реализация функции lowerBound – поиск первого индекса,
     * для которого значение элемента не меньше заданного ключа.
     *
     * @param arr Отсортированный массив.
     * @param key Искомое значение.
     * @return Первую позицию, где элемент массива больше или равен key.
     */
    private int lowerBound(int[] arr, int key) {
        int low = 0;
        int high = arr.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (arr[mid] < key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    /**
     * Вложенный класс для представления отрезка.
     * Каждый отрезок задаётся двумя точками, но гарантируется,
     * что значение поля start будет меньше или равно значению поля stop.
     */
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // Обеспечиваем, что начало отрезка всегда меньше или равно концу
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            // Сначала сравниваем по начальной точке, а при равенстве – по конечной
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            } else {
                return Integer.compare(this.stop, o.stop);
            }
        }
    }
}
