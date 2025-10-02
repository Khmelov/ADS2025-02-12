package by.it.group410901.papou.lesson04;

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

        // Чтение размера массива
        int n = scanner.nextInt();
        // Чтение массива
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызов модифицированной сортировки слиянием для подсчета инверсий
        return mergeSortAndCount(a, 0, n - 1);
    }

    // Основной метод сортировки слиянием с подсчетом инверсий
    private int mergeSortAndCount(int[] a, int left, int right) {
        int invCount = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;
            // Рекурсивно подсчитываем инверсии в левой и правой половинах
            invCount += mergeSortAndCount(a, left, mid);
            invCount += mergeSortAndCount(a, mid + 1, right);
            // Подсчитываем инверсии при объединении
            invCount += mergeAndCount(a, left, mid, right);
        }
        return invCount;
    }

    // Метод для объединения двух подмассивов с подсчетом инверсий
    private int mergeAndCount(int[] a, int left, int mid, int right) {
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
        int invCount = 0;

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                a[k] = leftArray[i];
                i++;
            } else {
                // Если leftArray[i] > rightArray[j], то это инверсия
                // Все оставшиеся элементы leftArray[i..n1-1] тоже образуют инверсии
                a[k] = rightArray[j];
                invCount += n1 - i;
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

        return invCount;
    }
}