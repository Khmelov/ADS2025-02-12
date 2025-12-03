package by.it.group451003.sorokin.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_GetInversions {
    private static long inversionCount = 0;

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        int result = instance.calc(stream);
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Чтение размера массива
        int n = scanner.nextInt();
        int[] a = new int[n];

        // Чтение элементов массива
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Сброс счетчика инверсий
        inversionCount = 0;

        // Вызов сортировки с подсчетом инверсий
        mergeSortWithInversions(a, 0, a.length - 1);

        return (int) inversionCount;
    }

    private void mergeSortWithInversions(int[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Рекурсивная сортировка двух половин
            mergeSortWithInversions(array, left, mid);
            mergeSortWithInversions(array, mid + 1, right);

            // Слияние с подсчетом инверсий
            mergeWithInversions(array, left, mid, right);
        }
    }

    private void mergeWithInversions(int[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Копирование данных во временные массивы
        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, mid + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
                // Все оставшиеся элементы в левом подмассиве будут больше rightArray[j]
                inversionCount += n1 - i;
            }
            k++;
        }

        // Копирование оставшихся элементов
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}