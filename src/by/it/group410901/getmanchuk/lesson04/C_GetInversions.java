package by.it.group410901.getmanchuk.lesson04;

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
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        return countInversions(a);
    }

    private int countInversions(int[] a) {
        if (a == null || a.length <= 1) {
            return 0;
        }
        int[] temp = new int[a.length];
        return mergeSortAndCount(a, temp, 0, a.length - 1);
    }

    private int mergeSortAndCount(int[] a, int[] temp, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;
            count += mergeSortAndCount(a, temp, left, mid);
            count += mergeSortAndCount(a, temp, mid + 1, right);
            count += mergeAndCount(a, temp, left, mid, right);
        }
        return count;
    }

    private int mergeAndCount(int[] a, int[] temp, int left, int mid, int right) {
        int i = left;       // Индекс для левого подмассива
        int j = mid + 1;    // Индекс для правого подмассива
        int k = left;       // Индекс для временного массива
        int count = 0;      // Счётчик инверсий

        // Слияние с подсчётом инверсий
        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
                count += (mid - i + 1); // Все элементы от i до mid образуют инверсии с a[j]
            }
        }

        // Дописываем оставшиеся элементы левого подмассива
        while (i <= mid) {
            temp[k++] = a[i++];
        }

        // Дописываем оставшиеся элементы правого подмассива
        while (j <= right) {
            temp[k++] = a[j++];
        }

        // Копируем отсортированный подмассив обратно в исходный массив
        for (i = left; i <= right; i++) {
            a[i] = temp[i];
        }

        return count;
    }
}