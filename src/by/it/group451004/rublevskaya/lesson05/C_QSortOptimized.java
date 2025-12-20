package by.it.group451004.rublevskaya.lesson05;

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
        Scanner scanner = new Scanner(stream);

        // Число отрезков и точек
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение отрезков
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        // Чтение точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортировка отрезков по началу
        quickSort(segments, 0, n - 1);

        // Подсчёт количества подходящих отрезков для каждой точки
        for (int i = 0; i < m; i++) {
            int point = points[i];
            result[i] = countOverlappingSegments(segments, point);
        }

        return result;
    }

    // Быстрая сортировка с 3-разбиением
    private void quickSort(Segment[] segments, int low, int high) {
        while (low < high) {
            int[] partition = partition(segments, low, high);
            int lt = partition[0], gt = partition[1];

            // Рекурсивно сортируем меньшую часть
            if (lt - low < high - gt) {
                quickSort(segments, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort(segments, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    // Разбиение массива на три части: меньше, равно и больше опорного элемента
    private int[] partition(Segment[] segments, int low, int high) {
        Segment pivot = segments[high];
        int lt = low, gt = high, i = low;

        while (i <= gt) {
            int cmp = segments[i].compareTo(pivot);
            if (cmp < 0) {
                swap(segments, lt++, i++);
            } else if (cmp > 0) {
                swap(segments, i, gt--);
            } else {
                i++;
            }
        }

        return new int[]{lt, gt};
    }

    // Подсчёт количества отрезков, которым принадлежит точка
    private int countOverlappingSegments(Segment[] segments, int point) {
        int count = 0;

        // Бинарный поиск первого подходящего отрезка
        int left = 0, right = segments.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // Линейный подсчёт оставшихся подходящих отрезков
        for (int i = Math.max(0, right); i >= 0 && segments[i].start <= point; i--) {
            if (point <= segments[i].stop) {
                count++;
            }
        }

        return count;
    }

    // Класс отрезка
    private class Segment implements Comparable<Segment> {
        int start, stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment other) {
            return Integer.compare(this.start, other.start);
        }
    }

    // Обмен элементов массива
    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }
}