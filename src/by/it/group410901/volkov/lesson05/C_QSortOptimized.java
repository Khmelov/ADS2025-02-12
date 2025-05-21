package by.it.group410901.volkov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        // Создаем поток для чтения данных из файла
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        // Создаем экземпляр класса
        C_QSortOptimized instance = new C_QSortOptimized();
        // Получаем результат обработки данных
        int[] result = instance.getAccessory2(stream);
        // Выводим результат
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        // Инициализируем сканер для чтения данных
        Scanner scanner = new Scanner(stream);

        // Чтение количества отрезков (n) и количества точек (m)
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        // Инициализация массивов для хранения данных
        Segment[] segments = new Segment[n];  // Массив отрезков
        int[] points = new int[m];           // Массив точек
        int[] result = new int[m];           // Массив для результатов

        // Чтение данных об отрезках
        for (int i = 0; i < n; i++) {
            // Чтение начала и конца отрезка
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            // Создание отрезка с упорядоченными концами (start <= end)
            segments[i] = new Segment(Math.min(start, end), Math.max(start, end));
        }

        // Чтение координат точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортировка отрезков по начальной координате с использованием оптимизированной быстрой сортировки
        quickSort(segments, 0, segments.length - 1);

        // Для каждой точки определяем количество отрезков, которые ее покрывают
        for (int i = 0; i < m; i++) {
            int point = points[i];
            // Находим индекс первого отрезка, который потенциально может содержать точку
            int first = findFirstSegment(segments, point);

            // Если подходящих отрезков нет, записываем 0 и переходим к следующей точке
            if (first == -1) {
                result[i] = 0;
                continue;
            }

            // Подсчитываем количество отрезков, содержащих точку
            int count = 0;
            for (int j = first; j < segments.length && segments[j].start <= point; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    // Метод для оптимизированной быстрой сортировки (с 3-разбиением и элиминацией хвостовой рекурсии)
    private void quickSort(Segment[] segments, int low, int high) {
        // Продолжаем сортировку, пока есть что сортировать
        while (low < high) {
            // Выполняем разбиение массива
            int[] pivotIndices = partition(segments, low, high);

            // Рекурсивно сортируем левую часть (меньшие элементы)
            if (pivotIndices[0] - low < high - pivotIndices[1]) {
                quickSort(segments, low, pivotIndices[0] - 1);
                low = pivotIndices[1] + 1;  // Переходим к правой части
            } else {
                // Рекурсивно сортируем правую часть (большие элементы)
                quickSort(segments, pivotIndices[1] + 1, high);
                high = pivotIndices[0] - 1;  // Переходим к левой части
            }
            // Элиминация хвостовой рекурсии за счет замены рекурсии на цикл
        }
    }

    // Метод для разбиения массива (3-разбиение)
    private int[] partition(Segment[] segments, int low, int high) {
        // Выбираем опорный элемент (медиана из первого, среднего и последнего элементов)
        int mid = low + (high - low) / 2;
        if (segments[high].start < segments[low].start) swap(segments, low, high);
        if (segments[mid].start < segments[low].start) swap(segments, mid, low);
        if (segments[high].start < segments[mid].start) swap(segments, mid, high);
        Segment pivot = segments[mid];

        // Инициализация указателей
        int i = low;      // Указатель на элементы меньше опорного
        int j = high;     // Указатель на элементы больше опорного
        int k = low;      // Указатель для прохода по массиву

        // Разбиение массива
        while (k <= j) {
            int cmp = segments[k].compareTo(pivot);
            if (cmp < 0) {
                swap(segments, i++, k++);
            } else if (cmp > 0) {
                swap(segments, k, j--);
            } else {
                k++;
            }
        }

        // Возвращаем границы равных элементов
        return new int[]{i, j};
    }

    // Метод для поиска первого отрезка, который может содержать точку (бинарный поиск)
    private int findFirstSegment(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid;
                left = mid + 1;  // Ищем дальше в правой части
            } else {
                right = mid - 1;  // Ищем в левой части
            }
        }

        return result;
    }

    // Вспомогательный метод для обмена элементов массива
    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    // Класс для представления отрезка
    private class Segment implements Comparable<Segment> {
        int start;  // Начало отрезка
        int stop;   // Конец отрезка

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        // Реализация сравнения отрезков по начальной координате
        @Override
        public int compareTo(Segment other) {
            return Integer.compare(this.start, other.start);
        }
    }
}
