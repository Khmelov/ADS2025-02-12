package by.it.group410902.latipov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            //обеспечиваем правильный порядок концов отрезка
            segments[i] = new Segment(Math.min(start, stop), Math.max(start, stop));
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!

        // Создаем отдельные массивы для начал и концов отрезков
        int[] starts = new int[n];
        int[] ends = new int[n];

        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        // Сортируем начала и концы отрезков
        quickSort(starts, 0, n - 1);
        quickSort(ends, 0, n - 1);

        // Для каждой точки считаем количество отрезков, которые ее покрывают
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Количество отрезков, которые начались до или в точке
            int startedBefore = countLessOrEqual(starts, point);

            // Количество отрезков, которые закончились до точки (не включая точку)
            int endedBefore = countLess(ends, point);

            // Разница дает количество отрезков, которые покрывают точку
            result[i] = startedBefore - endedBefore;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Быстрая сортировка
    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Подсчет элементов <= target
    private int countLessOrEqual(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        int count = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= target) {
                count = mid + 1; // все элементы до mid включительно <= target
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return count;
    }

    // Подсчет элементов < target
    private int countLess(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        int count = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < target) {
                count = mid + 1; // все элементы до mid включительно < target
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return count;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            // Сравниваем по началу отрезка
            return Integer.compare(this.start, o.start);
        }

        @Override
        public String toString() {
            return "[" + start + ", " + stop + "]";
        }
    }
}