package by.it.group451002.kravtsov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
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

        // Сортируем отрезки с помощью QuickSort с 3-разбиением
        quickSort(segments, 0, n - 1);

        // Подсчёт количества перекрытий через бинарный поиск
        for (int i = 0; i < m; i++) {
            result[i] = countSegments(segments, points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Метод быстрой сортировки с 3-разбиением (QuickSort с 3-разделением)
    private void quickSort(Segment[] arr, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;  // lt — левая граница, gt — правая граница
            Segment pivot = arr[low]; // Выбираем опорный элемент (pivot)
            int i = low;  // Начальный индекс для обхода массива

            // Основной цикл разбиения (3-частное разделение)
            while (i <= gt) {
                if (arr[i].compareTo(pivot) < 0) swap(arr, lt++, i++);  // Перемещаем меньшие элементы влево
                else if (arr[i].compareTo(pivot) > 0) swap(arr, i, gt--);  // Перемещаем большие элементы вправо
                else i++;  // Пропускаем элементы, равные pivot
            }

            // Хвостовая рекурсия: сначала сортируем левую часть,
            // затем продолжаем обработку правой части, заменяя рекурсивный вызов на итерацию
            quickSort(arr, low, lt - 1);  // Рекурсивно сортируем элементы меньше pivot
            low = gt + 1;  // Перемещаем границу правой части, избегая лишней рекурсии
        }
    }

    // Бинарный поиск первого подходящего отрезка
    private int countSegments(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1;

        // Бинарный поиск: ищем первый сегмент, у которого start > point
        while (left <= right) {
            int mid = left + (right - left) / 2;  // Вычисляем середину массива
            if (segments[mid].start > point) right = mid - 1;  // Ищем в левой части
            else left = mid + 1;  // Ищем в правой части
        }

        int count = 0;
        // Подсчитываем количество сегментов, которые охватывают точку
        for (int i = right; i >= 0 && segments[i].stop >= point; i--) {
            count++;  // Увеличиваем счётчик, если точка входит в диапазон сегмента
        }

        return count;  // Возвращаем количество перекрытий
}


    // Метод обмена элементов
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Класс отрезка
    private class Segment implements Comparable<Segment> {
        int start, stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}