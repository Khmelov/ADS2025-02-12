package by.it.group410902.andala.lesson04;

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
        // Чтение самого массива
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызов сортировки слиянием
        mergeSort(a, 0, a.length - 1);

        return a;
    }

    // Основная функция сортировки слиянием
    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            // Находим середину массива
            int mid = left + (right - left) / 2;

            // Рекурсивно сортируем левую и правую части
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            // Сливаем отсортированные части
            merge(array, left, mid, right);
        }
    }

    // Функция слияния двух отсортированных подмассивов
    private void merge(int[] array, int left, int mid, int right) {
        // Размеры временных подмассивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создаем временные массивы
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Копируем данные во временные массивы
        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = array[mid + 1 + j];
        }

        // Индексы для слияния
        int i = 0, j = 0;
        int k = left;

        // Сливаем временные массивы обратно в основной
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

        // Копируем оставшиеся элементы leftArray (если они есть)
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Копируем оставшиеся элементы rightArray (если они есть)
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}