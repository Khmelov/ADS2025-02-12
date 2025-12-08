package by.it.group410902.latipov.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_GetInversions {

    private long inversionCount = 0;

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

        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
        inversionCount = 0;
        mergeSortWithInversionCount(a, 0, a.length - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return (int) inversionCount;
    }

    // Модифицированная сортировка слиянием для подсчета инверсий
    private void mergeSortWithInversionCount(int[] array, int left, int right) {
        if (left < right) {
            int middle = left + (right - left) / 2;

            // Рекурсивно сортируем и считаем инверсии в левой и правой частях
            mergeSortWithInversionCount(array, left, middle);
            mergeSortWithInversionCount(array, middle + 1, right);

            // Сливаем и считаем инверсии между частями
            mergeWithInversionCount(array, left, middle, right);
        }
    }

    // Метод для слияния с подсчетом инверсий
    private void mergeWithInversionCount(int[] array, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Копируем данные во временные массивы
        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = array[middle + 1 + j];
        }

        // Сливаем временные массивы обратно в основной массив
        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
                // Все оставшиеся элементы в левом массиве образуют инверсии с текущим элементом правого массива
                inversionCount += (n1 - i);
            }
            k++;
        }

        // Копируем оставшиеся элементы
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}