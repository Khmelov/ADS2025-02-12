package by.it.group410901.bukshta.lesson05;

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
*/
public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        if (stream == null) {
            stream = System.in; // Fallback to standard input
        }
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        // Initialize scanner
        Scanner scanner = new Scanner(stream);

        // Read number of segments and points
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int[] points = new int[m];
        int[] result = new int[m];

        // Read segments
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //Считываем количество камер n, событий m,
        // интервалы камер (отрезки) и моменты событий (точки).
        // Read points
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
      //Сортируем массив отрезков по начальной координате с помощью
        // оптимизированной быстрой сортировки:
        //Трёхпутевое разбиение для обработки повторяющихся значений.
        //Сортировка вставками для малых подмассивов (≤10 элементов).
        //Элиминация хвостовой рекурсии: рекурсия только для меньшей части, итерация для большей.
        //Сортировка на месте, без дополнительной памяти.
        // Sort segments using in-place QuickSort with 3-way partitioning
        quickSort3Way(segments, 0, n - 1);

        // Process each point
        for (int i = 0; i < m; i++) {
            int point = points[i];
            // Binary search to find the first segment where segment.start <= point
            int firstIndex = binarySearchFirst(segments, point);
            int count = 0;
            // Count segments that cover the point
            for (int j = firstIndex; j < n && segments[j].start <= point; j++) {
                if (point <= segments[j].stop) {
                    count++;
                }
            }
            result[i] = count;
        }
        scanner.close();
        return result;
    }

    // 3-way partitioning QuickSort with tail recursion elimination
    private void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            // Use insertion sort for small arrays (e.g., size <= 10)
            if (high - low <= 10) {
                insertionSort(arr, low, high);
                break;
            }

            // 3-way partition
            int[] partition = partition3Way(arr, low, high);
            int lt = partition[0];
            int gt = partition[1];

            // Recurse on smaller partition, iterate on larger to eliminate tail recursion
            if (lt - low < high - gt) {
                quickSort3Way(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort3Way(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    // 3-way partitioning
    private int[] partition3Way(Segment[] arr, int low, int high) {
        int pivotIndex = low + (high - low) / 2; // Middle pivot
        Segment pivot = arr[pivotIndex];
        swap(arr, low, pivotIndex); // Move pivot to start

        int lt = low; // Less than pivot
        int gt = high; // Greater than pivot
        int i = low + 1; // Current element

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

        return new int[]{lt, gt};
    }

    // Insertion sort for small arrays
    private void insertionSort(Segment[] arr, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            Segment key = arr[i];
            int j = i - 1;
            while (j >= low && arr[j].compareTo(key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // Swap elements in array
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    //Для каждой точки:
    //Бинарным поиском находим индекс первого отрезка, где начальная
    // координата ≤ времени точки.
    //Линейно проверяем отрезки, начиная с найденного индекса, увеличивая
    // счётчик, если точка попадает в интервал отрезка (включая границы).
    // Binary search to find the first segment where segment.start <= point
    private int binarySearchFirst(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = segments.length; // Default: no segment found

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid; // Potential first segment
                right = mid - 1; // Look for earlier segments
            } else {
                left = mid + 1; // Skip segments starting after point
            }
        }

        return result;
    }
    //Сохраняем количество подходящих отрезков для каждой точки в результирующий массив.
    //Возвращаем массив с количеством активных камер для каждой точки.
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
            // Compare by start; if equal, by stop
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}