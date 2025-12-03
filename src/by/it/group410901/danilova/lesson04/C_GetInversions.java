package by.it.group410901.danilova.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        // Размер массива
        int n = scanner.nextInt();
        // Сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        // Вызов модифицированной сортировки слиянием для подсчета инверсий
        return mergeSortAndCount(a, 0, a.length - 1);
    }

    // Основная функция для сортировки слиянием и подсчета инверсий
    private int mergeSortAndCount(int[] array, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;
            // Рекурсивный вызов для левой половины
            count += mergeSortAndCount(array, left, mid);
            // Рекурсивный вызов для правой половины
            count += mergeSortAndCount(array, mid + 1, right);
            // Слияние и подсчет инверсий между двумя половинами
            count += mergeAndCount(array, left, mid, right);
        }
        return count;
    }

    // Функция для слияния двух половин и подсчета инверсий
    private int mergeAndCount(int[] array, int left, int mid, int right) {
        // Размеры временных подмассивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создаем временные массивы
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Копируем данные во временные массивы
        for (int i = 0; i < n1; i++)
            leftArray[i] = array[left + i];
        for (int j = 0; j < n2; j++)
            rightArray[j] = array[mid + 1 + j];

        int i = 0, j = 0, k = left;
        int swaps = 0;

        // Слияние с подсчетом инверсий
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
                // Все оставшиеся элементы в leftArray будут больше rightArray[j]
                swaps += (n1 - i);
            }
        }

        // Копируем оставшиеся элементы
        while (i < n1)
            array[k++] = leftArray[i++];
        while (j < n2)
            array[k++] = rightArray[j++];

        return swaps;
    }
}