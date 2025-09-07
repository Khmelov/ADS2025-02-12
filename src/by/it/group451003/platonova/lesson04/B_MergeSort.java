package by.it.group451003.platonova.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        int[] result = instance.getMergeSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Чтение размера массива
        int n = scanner.nextInt();
        int[] a = new int[n];

        // Чтение самого массива
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызов сортировки слиянием
        mergeSort(a, 0, a.length - 1);

        return a;
    }

    // Рекурсивная функция сортировки слиянием
    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Сортировка левой и правой половин
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            // Слияние отсортированных половин
            merge(array, left, mid, right);
        }
    }

    // Функция слияния двух отсортированных подмассивов
    private void merge(int[] array, int left, int mid, int right) {
        // Размеры временных подмассивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создание временных подмассивов
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Копирование данных во временные подмассивы
        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, mid + 1, rightArray, 0, n2);

        // Индексы для слияния
        int i = 0, j = 0, k = left;

        // Слияние leftArray и rightArray обратно в array
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // Копирование оставшихся элементов leftArray (если они есть)
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Копирование оставшихся элементов rightArray (если они есть)
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}
