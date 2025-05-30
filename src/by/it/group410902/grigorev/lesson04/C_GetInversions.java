package by.it.group410902.grigorev.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_GetInversions {
    public static void main(String[] args) throws FileNotFoundException {
        // Получаем поток ввода из файла "dataC.txt"
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");

        // Создаем экземпляр класса
        C_GetInversions instance = new C_GetInversions();

        // Подсчитываем инверсии в массиве
        int result = instance.calc(stream);

        // Выводим количество инверсий
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем количество элементов
        int n = scanner.nextInt();
        int[] a = new int[n];

        // Заполняем массив значениями
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызываем метод для подсчета инверсий
        return countInversions(a, 0, a.length - 1);
    }

    int countInversions(int[] a, int left, int right) {
        // Базовый случай
        if (left >= right) return 0;

        int mid = left + (right - left) / 2;

        // Рекурсивный подсчет инверсий
        int inv = countInversions(a, left, mid);
        inv += countInversions(a, mid + 1, right);

        // Подсчет инверсий во время слияния
        inv += mergeAndCount(a, left, mid, right);
        return inv;
    }

    int mergeAndCount(int[] a, int left, int mid, int right) {
        // Создаем временный массив
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        int inv = 0;

        // Подсчет инверсий во время слияния
        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
                inv += mid - i + 1;
            }
        }

        // Копируем оставшиеся элементы
        while (i <= mid) temp[k++] = a[i++];
        while (j <= right) temp[k++] = a[j++];

        System.arraycopy(temp, 0, a, left, temp.length);
        return inv;
    }
}

