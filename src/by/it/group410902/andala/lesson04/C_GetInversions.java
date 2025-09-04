package by.it.group410902.andala.lesson04;

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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
        result = mergeSortAndCount(a, 0, a.length - 1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    // Метод реализует модифицированную сортировку слиянием,
    // одновременно подсчитывая количество инверсий
    private int mergeSortAndCount(int[] arr, int left, int right) {
        int count = 0;
        if (left < right) {
            // Делим массив пополам
            int mid = (left + right) / 2;
            // Рекурсивно считаем инверсии в левой части
            count += mergeSortAndCount(arr, left, mid);
            // Рекурсивно считаем инверсии в правой части
            count += mergeSortAndCount(arr, mid + 1, right);
            // Считаем инверсии между двумя частями при их слиянии
            count += mergeAndCount(arr, left, mid, right);
        }
        return count;
    }
    // Метод объединяет две отсортированные части массива и считает количество инверсий
    private int mergeAndCount(int[] arr, int left, int mid, int right) {
        // Создаем временные массивы для левой и правой части
        int[] leftArr = new int[mid - left + 1];
        int[] rightArr = new int[right - mid];

        // Копируем данные в временные массивы
        for (int i = 0; i < leftArr.length; i++) {
            leftArr[i] = arr[left + i];
        }
        for (int j = 0; j < rightArr.length; j++) {
            rightArr[j] = arr[mid + 1 + j];
        }

        // Индексы для прохода по временным массивам и основному массиву
        int i = 0, j = 0, k = left;
        // Переменная для подсчета инверсий
        int swaps = 0;

        // Сливаем массивы обратно в основной массив
        while (i < leftArr.length && j < rightArr.length) {
            // Если текущий элемент из левой части меньше или равен правому — копируем его
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                // Иначе — копируем элемент из правой части и считаем инверсии
                arr[k++] = rightArr[j++];
                // Все оставшиеся элементы в левой части образуют инверсии с текущим элементом из правой
                swaps += (mid + 1) - (left + i);
            }
        }

        // Копируем оставшиеся элементы из левой части (если есть)
        while (i < leftArr.length) {
            arr[k++] = leftArr[i++];
        }

        // Копируем оставшиеся элементы из правой части (если есть)
        while (j < rightArr.length) {
            arr[k++] = rightArr[j++];
        }

        // Возвращаем количество найденных инверсий
        return swaps;
    }
}