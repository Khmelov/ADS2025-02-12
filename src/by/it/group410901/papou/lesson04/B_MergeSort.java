package by.it.group410901.papou.lesson04;

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
        // Чтение массива
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызов сортировки слиянием
        mergeSort(a, 0, n - 1);

        return a;
    }

    // Основной метод сортировки слиянием
    private void mergeSort(int[] a, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2; // Находим середину
            // Рекурсивно сортируем левую и правую половины
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);
            // Объединяем отсортированные половины
            merge(a, left, mid, right);
        }
    }

    // Метод для объединения двух отсортированных подмассивов
    private void merge(int[] a, int left, int mid, int right) {
        // Размеры подмассивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создаем временные массивы
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Копируем данные во временные массивы
        for (int i = 0; i < n1; i++) {
            leftArray[i] = a[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = a[mid + 1 + j];
        }

        // Объединяем временные массивы обратно в a[left..right]
        int i = 0; // Индекс для левого подмассива
        int j = 0; // Индекс для правого подмассива
        int k = left; // Индекс для объединенного массива

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                a[k] = leftArray[i];
                i++;
            } else {
                a[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // Копируем оставшиеся элементы leftArray, если есть
        while (i < n1) {
            a[k] = leftArray[i];
            i++;
            k++;
        }

        // Копируем оставшиеся элементы rightArray, если есть
        while (j < n2) {
            a[k] = rightArray[j];
            j++;
            k++;
        }
    }
}