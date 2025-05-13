package by.it.group451001.tsurko.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        // Считываем размер массива
        int n = scanner.nextInt();
        // Создаем массив и заполняем его значениями
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызываем сортировку слиянием
        mergeSort(a, 0, n - 1);

        return a;
    }

    // Рекурсивный метод сортировки слиянием
    private void mergeSort(int[] a, int left, int right) {
        if (left < right) {
            // Определяем середину
            int mid = left + (right - left) / 2;

            // Рекурсивно сортируем левую и правую части
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);

            // Объединяем отсортированные части
            merge(a, left, mid, right);
        }
    }

    // Метод слияния двух отсортированных подмассивов
    private void merge(int[] a, int left, int mid, int right) {
        // Определяем размеры двух подмассивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создаем временные массивы
        int[] L = new int[n1];
        int[] R = new int[n2];

        // Копируем данные в временные массивы
        for (int i = 0; i < n1; i++) {
            L[i] = a[left + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = a[mid + 1 + j];
        }

        // Слияние временных массивов в основной массив
        int i = 0, j = 0, k = left;

        // Покомпонентное сравнение элементов и слияние
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                a[k++] = L[i++];
            } else {
                a[k++] = R[j++];
            }
        }

        // Копируем оставшиеся элементы из L, если такие имеются
        while (i < n1) {
            a[k++] = L[i++];
        }
        // Копируем оставшиеся элементы из R, если такие имеются
        while (j < n2) {
            a[k++] = R[j++];
        }
    }
}
