package by.it.group451001.kurstak.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    /**
     * Читает массив из потока и возвращает число инверсий,
     * то есть количество пар индексов i<j таких, что A[i] > A[j].
     */
    int calc(InputStream stream) throws FileNotFoundException {
        // Подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        // Подсчет инверсий с помощью модифицированной сортировки слиянием
        long inversions = mergeSortAndCount(a, 0, n - 1);
        return (int) inversions;
    }

    /**
     * Рекурсивная функция сортировки слиянием, которая возвращает число инверсий в массиве a[left...right].
     */
    private long mergeSortAndCount(int[] a, int left, int right) {
        long count = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;
            count += mergeSortAndCount(a, left, mid);
            count += mergeSortAndCount(a, mid + 1, right);
            count += mergeAndCount(a, left, mid, right);
        }
        return count;
    }

    /**
     * Функция слияния двух отсортированных частей массива с подсчетом инверсий.
     * При слиянии если элемент из правой части меньше чем элемент из левой,
     * то все оставшиеся элементы левой части образуют инверсии с этим элементом.
     */
    private long mergeAndCount(int[] a, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left;       // индекс для левой части
        int j = mid + 1;    // индекс для правой части
        int k = 0;
        long count = 0;

        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
                // Все оставшиеся элементы в left (от i до mid) больше, чем a[j-1]
                count += (mid - i + 1);
            }
        }

        while (i <= mid) {
            temp[k++] = a[i++];
        }
        while (j <= right) {
            temp[k++] = a[j++];
        }

        // Копируем отсортированные элементы обратно в исходный массив
        System.arraycopy(temp, 0, a, left, temp.length);
        return count;
    }
}
