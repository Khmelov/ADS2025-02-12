package by.it.group410901.evtuhovskaya.lesson04;

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

        int n = scanner.nextInt();     // читаем размер массива
        int[] a = new int[n];          // создаём массив
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();  // заполняем массив
        }

        // Вспомогательный массив для слияния
        int[] temp = new int[n];

        // Запускаем модифицированную сортировку с подсчётом инверсий
        return mergeSortAndCount(a, temp, 0, n - 1);
    }

    // Основная функция, выполняющая сортировку и подсчёт
    int mergeSortAndCount(int[] arr, int[] temp, int left, int right) {
        int mid, invCount = 0;
        if (right > left) {
            mid = (right + left) / 2;

            // Считаем инверсии слева, справа и в процессе слияния
            invCount += mergeSortAndCount(arr, temp, left, mid);
            invCount += mergeSortAndCount(arr, temp, mid + 1, right);
            invCount += mergeAndCount(arr, temp, left, mid + 1, right);
        }
        return invCount;
    }

    // Функция слияния с подсчётом инверсий
    int mergeAndCount(int[] arr, int[] temp, int left, int mid, int right) {
        int i = left;     // левая часть
        int j = mid;      // правая часть
        int k = left;     // индекс для temp
        int invCount = 0;

        while (i <= mid - 1 && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
                invCount += (mid - i); // Все оставшиеся элементы слева > arr[j]
            }
        }

        // Копируем оставшиеся элементы
        while (i <= mid - 1)
            temp[k++] = arr[i++];
        while (j <= right)
            temp[k++] = arr[j++];

        // Копируем обратно в оригинальный массив
        for (i = left; i <= right; i++)
            arr[i] = temp[i];

        return invCount;
    }
}
