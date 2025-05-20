package by.it.group451002.mitskevich.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
ЗАДАЧА:
Рассчитать количество инверсий в массиве (пар i < j, где A[i] > A[j]).
Сложность — не хуже, чем O(n log n), поэтому используем модифицированную сортировку слиянием.
Пример:
Вход:
5
2 3 9 2 9
Выход:
2 (инверсии: (3,4) и (3,5))
*/

public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        int result = instance.calc(stream);
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Считываем размер массива
        int n = scanner.nextInt();

        // Считываем сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Запускаем сортировку слиянием и одновременно считаем инверсии
        return mergeSortAndCount(a, 0, n - 1);
    }

    // Метод сортировки слиянием с подсчётом инверсий
    private int mergeSortAndCount(int[] arr, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = (left + right) / 2;

            // Считаем инверсии в левой и правой половинах рекурсивно
            count += mergeSortAndCount(arr, left, mid);
            count += mergeSortAndCount(arr, mid + 1, right);

            // Считаем инверсии между левой и правой половинами
            count += mergeAndCount(arr, left, mid, right);
        }
        return count;
    }

    // Слияние двух отсортированных половин с подсчётом инверсий между ними
    private int mergeAndCount(int[] arr, int left, int mid, int right) {
        // Временные массивы для левой и правой частей
        int[] leftArr = new int[mid - left + 1];
        int[] rightArr = new int[right - mid];

        for (int i = 0; i < leftArr.length; i++)
            leftArr[i] = arr[left + i];
        for (int i = 0; i < rightArr.length; i++)
            rightArr[i] = arr[mid + 1 + i];

        int i = 0, j = 0, k = left, swaps = 0;

        // Сливаем массивы, считая инверсии
        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
                swaps += (leftArr.length - i); // всё, что осталось в leftArr — это инверсии
            }
        }

        // Добавляем оставшиеся элементы
        while (i < leftArr.length)
            arr[k++] = leftArr[i++];
        while (j < rightArr.length)
            arr[k++] = rightArr[j++];

        return swaps;
    }
}
