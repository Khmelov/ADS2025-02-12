package by.it.group410901.korneew.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        long finishTime = System.currentTimeMillis();
        System.out.println("Количество инверсий: " + result);
        System.out.println("Время выполнения: " + (finishTime - startTime) + " мс");
    }

    int calc(InputStream stream) throws FileNotFoundException {
        // Подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        // Размер массива
        int n = scanner.nextInt();
        // Сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        // Массив для временного хранения
        int[] temp = new int[n];
        return mergeSortAndCount(a, temp, 0, n - 1);
    }

    // Метод сортировки слиянием с подсчетом инверсий
    private int mergeSortAndCount(int[] arr, int[] temp, int left, int right) {
        int mid, invCount = 0;
        if (left < right) {
            mid = (left + right) / 2;

            invCount += mergeSortAndCount(arr, temp, left, mid);
            invCount += mergeSortAndCount(arr, temp, mid + 1, right);
            invCount += mergeAndCount(arr, temp, left, mid, right);
        }
        return invCount;
    }

    // Метод слияния двух подмассивов и подсчета инверсий
    private int mergeAndCount(int[] arr, int[] temp, int left, int mid, int right) {
        int i = left;    // Начало левой подчасти
        int j = mid + 1; // Начало правой подчасти
        int k = left;    // Индекс для временного массива
        int invCount = 0;

        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
                invCount += (mid - i + 1); // Все оставшиеся элементы в левой подчасти больше arr[j]
            }
        }

        // Копируем оставшиеся элементы левой подчасти
        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        // Копируем оставшиеся элементы правой подчасти
        while (j <= right) {
            temp[k++] = arr[j++];
        }

        // Копируем отсортированный временный массив обратно в оригинальный массив
        for (i = left; i <= right; i++) {
            arr[i] = temp[i];
        }

        return invCount;
    }
}