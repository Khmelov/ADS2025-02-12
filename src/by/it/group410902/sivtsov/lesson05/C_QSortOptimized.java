package by.it.group410902.sivtsov.lesson05;

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
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        quickSort(segments,0, segments.length-1);
        for (int i = 0; i < m; i++) {
            result[i] = countSegments(segments,points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    static int countSegments(Segment[] segments, int point) {
        int index = binarySearch(segments, point);
        if (index == -1) return 0;

        int count = 0;
        // Ищем все подходящие отрезки
        for (int i = index; i < segments.length && segments[i].start <= point; i++) {
            if (segments[i].stop >= point) count++;
        }
        return count;
    }

    // Бинарный поиск первого подходящего отрезка
    static int binarySearch(Segment[] arr, int point) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid].start <= point && arr[mid].stop >= point)
                return mid;
            if (arr[mid].start > point)
                right = mid - 1;
            else
                left = mid + 1;
        }
        return -1;
    }

    static void quickSort(Segment[] arr, int low, int high) {
        while (low < high) {
            if (high - low < 10) {
                insertionSort(arr, low, high);
                break;
            }

            int pivot1 = arr[low].start;
            int pivot2 = arr[high].start;

            if (pivot1 > pivot2) {
                swap(arr, low, high);
                pivot1 = arr[low].start;
                pivot2 = arr[high].start;
            }

            int lt = low + 1, gt = high - 1, i = low + 1;

            while (i <= gt) {
                if (arr[i].start < pivot1) {
                    swap(arr, i, lt);
                    lt++;
                } else if (arr[i].start > pivot2) {
                    swap(arr, i, gt);
                    gt--;
                    i--;
                }
                i++;
            }

            swap(arr, low, lt - 1);
            swap(arr, high, gt + 1);

            quickSort(arr, low, lt - 2);
            quickSort(arr, gt + 2, high);

            low = lt;
        }
    }

    // Сортировка вставками для небольших массивов
    static void insertionSort(Segment[] arr, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            Segment key = arr[i];
            int j = i - 1;
            while (j >= low && arr[j].start > key.start) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // Метод обмена элементов
    static void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    //отрезок
    private class Segment implements Comparable <Segment>{
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            return Integer.compare(this.start,o.start);
        }
    }

}
