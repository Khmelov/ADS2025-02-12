package by.it.group451003.galuzo.lesson04;

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

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        // Чтение размера массива и его элементов
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        // Вызов модифицированной сортировки слиянием для подсчета инверсий
        return mergeSortAndCount(a, 0, a.length - 1);
    }

    // Рекурсивный метод для сортировки слиянием и подсчета инверсий
    private int mergeSortAndCount(int[] array, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;
            // Рекурсивный подсчет инверсий в левой и правой частях
            count += mergeSortAndCount(array, left, mid);
            count += mergeSortAndCount(array, mid + 1, right);
            // Подсчет инверсий при слиянии
            count += mergeAndCount(array, left, mid, right);
        }
        return count;
    }

    // Слияние двух подмассивов и подсчет инверсий
    private int mergeAndCount(int[] array, int left, int mid, int right) {
        // Временные массивы для левой и правой частей
        int[] leftArray = new int[mid - left + 1];
        int[] rightArray = new int[right - mid];

        // Копирование данных во временные массивы
        System.arraycopy(array, left, leftArray, 0, leftArray.length);
        System.arraycopy(array, mid + 1, rightArray, 0, rightArray.length);

        int i = 0, j = 0, k = left;
        int swaps = 0; // Счетчик инверсий

        // Слияние с подсчетом инверсий
        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] <= rightArray[j]) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
                // Все оставшиеся элементы в leftArray образуют инверсии с текущим элементом rightArray
                swaps += (mid + 1) - (left + i);
            }
        }

        // Копирование оставшихся элементов
        while (i < leftArray.length) {
            array[k++] = leftArray[i++];
        }
        while (j < rightArray.length) {
            array[k++] = rightArray[j++];
        }

        return swaps;
    }
}