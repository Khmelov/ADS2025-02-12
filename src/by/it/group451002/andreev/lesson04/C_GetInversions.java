package by.it.group451002.andreev.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Класс C_GetInversions реализует подсчет инверсий в массиве с использованием сортировки слиянием.
 * Инверсии представляют собой пары элементов (i, j), для которых i < j и array[i] > array[j].
 */
public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataC.txt"
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();

        // Вычисляем количество инверсий и выводим результат
        int result = instance.calc(stream);
        System.out.print(result);
    }

    /**
     * Метод calc читает массив из входного потока и вызывает метод подсчета инверсий.
     * @param stream входной поток данных
     * @return количество инверсий в массиве
     * @throws FileNotFoundException если файл не найден
     */
    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем размер массива
        int n = scanner.nextInt();
        int[] a = new int[n];

        // Заполняем массив данными
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызываем метод подсчета инверсий
        return countInversions(a);
    }

    /**
     * Метод countInversions запускает рекурсивную сортировку слиянием, совмещенную с подсчетом инверсий.
     * @param array исходный массив
     * @return количество инверсий в массиве
     */
    private int countInversions(int[] array) {
        if (array == null || array.length <= 1) {
            return 0; // Если массив пустой или имеет один элемент, инверсий нет
        }

        int[] temp = new int[array.length]; // Временный массив для сортировки
        return mergeSortAndCount(array, temp, 0, array.length - 1);
    }

    /**
     * Метод mergeSortAndCount выполняет рекурсивную сортировку слиянием и подсчитывает инверсии.
     * @param array сортируемый массив
     * @param temp временный массив для хранения промежуточных значений
     * @param left индекс начала массива
     * @param right индекс конца массива
     * @return количество инверсий в массиве
     */
    private int mergeSortAndCount(int[] array, int[] temp, int left, int right) {
        int inversionCount = 0;

        if (left < right) {
            int mid = left + (right - left) / 2;

            // Подсчитываем инверсии в левой части
            inversionCount += mergeSortAndCount(array, temp, left, mid);

            // Подсчитываем инверсии в правой части
            inversionCount += mergeSortAndCount(array, temp, mid + 1, right);

            // Подсчитываем инверсии при слиянии двух отсортированных частей
            inversionCount += mergeAndCount(array, temp, left, mid, right);
        }

        return inversionCount;
    }

    /**
     * Метод mergeAndCount объединяет две отсортированные части массива и подсчитывает инверсии.
     * @param array исходный массив
     * @param temp временный массив
     * @param left индекс начала первой части массива
     * @param mid индекс середины массива
     * @param right индекс конца массива
     * @return количество инверсий
     */
    private int mergeAndCount(int[] array, int[] temp, int left, int mid, int right) {
        int i = left; // Индекс для левой половины
        int j = mid + 1; // Индекс для правой половины
        int k = left; // Индекс для временного массива
        int inversionCount = 0;

        // Объединяем два подмассива
        while (i <= mid && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
                inversionCount += (mid - i + 1); // Все оставшиеся элементы в левой половине создают инверсии
            }
        }

        // Копируем оставшиеся элементы из левой половины
        while (i <= mid) {
            temp[k++] = array[i++];
        }

        // Копируем оставшиеся элементы из правой половины
        while (j <= right) {
            temp[k++] = array[j++];
        }

        // Копируем отсортированные элементы обратно в исходный массив
        System.arraycopy(temp, left, array, left, right - left + 1);

        return inversionCount;
    }
}
