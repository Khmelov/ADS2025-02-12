package by.it.group410902.grigorev.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_MergeSort {
    public static void main(String[] args) throws FileNotFoundException {
        // Получаем поток ввода из файла "dataB.txt"
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");

        // Создаем экземпляр класса
        B_MergeSort instance = new B_MergeSort();

        // Выполняем сортировку
        int[] result = instance.getMergeSort(stream);

        // Выводим отсортированный массив
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем количество элементов
        int n = scanner.nextInt();
        int[] a = new int[n];

        // Заполняем массив значениями
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Запускаем сортировку слиянием
        mergeSort(a, 0, a.length - 1);
        return a;
    }

    void mergeSort(int[] a, int left, int right) {
        // Проверяем базовый случай
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Рекурсивно сортируем левую и правую половины
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);

            // Сливаем отсортированные части
            merge(a, left, mid, right);
        }
    }

    void merge(int[] a, int left, int mid, int right) {
        // Определяем размеры временных массивов
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] L = new int[n1];
        int[] R = new int[n2];

        // Заполняем временные массивы
        System.arraycopy(a, left, L, 0, n1);
        System.arraycopy(a, mid + 1, R, 0, n2);

        // Объединяем два отсортированных массива обратно в a[]
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                a[k] = L[i++];
            } else {
                a[k] = R[j++];
            }
            k++;
        }

        // Добавляем оставшиеся элементы
        while (i < n1) a[k++] = L[i++];
        while (j < n2) a[k++] = R[j++];
    }
}

