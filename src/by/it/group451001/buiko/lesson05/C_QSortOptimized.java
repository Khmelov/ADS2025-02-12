package by.it.group451001.buiko.lesson05;

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
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение отрезков
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            segments[i] = new Segment(start, end);
        }

        // Чтение точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортировка отрезков с использованием 3-way QuickSort
        quickSort(segments, 0, segments.length - 1);

        // Создание массивов начал и концов отрезков
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        // Сортировка массивов starts и ends
        quickSortPrimitive(starts, 0, starts.length - 1);
        quickSortPrimitive(ends, 0, ends.length - 1);

        // Обработка каждой точки
        for (int i = 0; i < m; i++) {
            int p = points[i];
            // Находим количество отрезков, начавшихся до или в момент p
            int cntStart = upperBound(starts, p);
            // Находим количество отрезков, закончившихся до p
            int cntEnd = lowerBound(ends, p);
            // Разница дает количество активных отрезков в точке p
            result[i] = cntStart - cntEnd;
        }

        return result;
    }

    // Реализация 3-way QuickSort для массива отрезков
    private void quickSort(Segment[] arr, int low, int high) {
        while (low < high) {
            int[] pivots = partition(arr, low, high);
            quickSort(arr, low, pivots[0] - 1);
            low = pivots[1] + 1; // Элиминация хвостовой рекурсии
        }
    }

    // Разделение массива отрезков на три части
    private int[] partition(Segment[] arr, int low, int high) {
        Segment pivot = arr[low]; // Опорный элемент
        int lt = low; // Граница элементов меньше опорного
        int gt = high; // Граница элементов больше опорного
        int i = low + 1;

        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
            if (cmp < 0) {
                swap(arr, lt++, i++);
            } else if (cmp > 0) {
                swap(arr, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt, gt}; // Возвращаем границы равных элементов
    }

    // Обмен элементов массива отрезков
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // 3-way QuickSort для примитивных массивов (starts и ends)
    private void quickSortPrimitive(int[] arr, int low, int high) {
        while (low < high) {
            int[] pivots = partitionPrimitive(arr, low, high);
            quickSortPrimitive(arr, low, pivots[0] - 1);
            low = pivots[1] + 1; // Элиминация хвостовой рекурсии
        }
    }

    // Разделение примитивного массива
    private int[] partitionPrimitive(int[] arr, int low, int high) {
        int pivot = arr[low];
        int lt = low;
        int gt = high;
        int i = low + 1;

        while (i <= gt) {
            if (arr[i] < pivot) {
                swapPrimitive(arr, lt++, i++);
            } else if (arr[i] > pivot) {
                swapPrimitive(arr, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    // Обмен элементов примитивного массива
    private void swapPrimitive(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Поиск верхней границы для бинарного поиска
    private int upperBound(int[] array, int key) {
        int low = 0, high = array.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (array[mid] <= key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low; // Индекс первого элемента больше key
    }

    // Поиск нижней границы для бинарного поиска
    private int lowerBound(int[] array, int key) {
        int low = 0, high = array.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (array[mid] < key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low; // Индекс первого элемента не меньше key
    }

    // Класс отрезка с реализацией сравнения
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop); // Упорядочиваем начало и конец
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            // Сравниваем по началу, затем по концу
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            } else {
                return Integer.compare(this.stop, o.stop);
            }
        }
    }
}

//Трехраздельная быстрая сортировка (3-way QuickSort):
//Метод quickSort: Рекурсивно сортирует массив отрезков,
// используя разделение на три части. Элиминация хвостовой рекурсии достигается заменой
// второго рекурсивного вызова на цикл.
//Метод partition: Разделяет массив на элементы меньше, равные и больше опорного.
// Возвращает границы равных элементов для оптимизации рекурсии.