package by.it.group451004.rublevskaya.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        int result = instance.calc(stream);
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        // Размер массива
        int n = scanner.nextInt();
        // Сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        // Подсчёт инверсий через модифицированную сортировку слиянием
        return mergeSortAndCount(a, 0, n - 1);
    }

    private int mergeSortAndCount(int[] arr, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;
            // Рекурсивно считаем инверсии в левой и правой частях
            count += mergeSortAndCount(arr, left, mid);
            count += mergeSortAndCount(arr, mid + 1, right);
            // Считаем инверсии при слиянии
            count += mergeAndCount(arr, left, mid, right);
        }
        return count;
    }

    private int mergeAndCount(int[] arr, int left, int mid, int right) {
        // Создаём временные массивы для левой и правой частей
        int[] leftArr = new int[mid - left + 1];
        int[] rightArr = new int[right - mid];

        System.arraycopy(arr, left, leftArr, 0, mid - left + 1);
        System.arraycopy(arr, mid + 1, rightArr, 0, right - mid);

        int i = 0, j = 0, k = left, swaps = 0;

        // Слияние двух отсортированных массивов
        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
                // Все оставшиеся элементы в левой части образуют инверсии
                swaps += (mid + 1) - (left + i);
            }
        }

        // Добавляем оставшиеся элементы из левой части
        while (i < leftArr.length) {
            arr[k++] = leftArr[i++];
        }

        // Добавляем оставшиеся элементы из правой части
        while (j < rightArr.length) {
            arr[k++] = rightArr[j++];
        }

        return swaps;
    }
}