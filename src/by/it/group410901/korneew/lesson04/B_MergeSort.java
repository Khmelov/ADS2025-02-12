package by.it.group410901.korneew.lesson04;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Чтение массива
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызов сортировки слиянием
        mergeSort(a, 0, a.length - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    // Основная функция сортировки слиянием
    private void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            // Находим среднюю точку
            int mid = left + (right - left) / 2;

            // Сортируем первую и вторую половины
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            // Сливаем отсортированные половины
            merge(arr, left, mid, right);
        }
    }

    // Функция слияния двух подмассивов
    private void merge(int[] arr, int left, int mid, int right) {
        // Размеры временных подмассивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создаем временные массивы
        int[] L = new int[n1];
        int[] R = new int[n2];

        // Копируем данные во временные массивы
        System.arraycopy(arr, left, L, 0, n1);
        System.arraycopy(arr, mid + 1, R, 0, n2);

        // Индексы для слияния
        int i = 0, j = 0;
        int k = left;

        // Слияние временных массивов обратно в основной
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        // Копируем оставшиеся элементы L[], если они есть
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        // Копируем оставшиеся элементы R[], если они есть
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }
}