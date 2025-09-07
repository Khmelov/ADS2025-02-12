package by.it.group451002.andreev.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Класс B_MergeSort реализует сортировку слиянием (Merge Sort).
 * Сортировка слиянием является алгоритмом "разделяй и властвуй":
 * 1. Разделяет массив на две половины
 * 2. Рекурсивно сортирует каждую половину
 * 3. Объединяет (сливает) две отсортированные части в один отсортированный массив.
 */
public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        // Чтение входных данных из файла "dataB.txt"
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();

        // Вызов сортировки и вывод результата
        int[] result = instance.getMergeSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    /**
     * Метод getMergeSort читает массив из входного потока и сортирует его.
     * @param stream входной поток данных
     * @return отсортированный массив
     * @throws FileNotFoundException если файл не найден
     */
    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем размер массива
        int n = scanner.nextInt();
        int[] a = new int[n];

        // Заполняем массив данными
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызываем сортировку слиянием
        mergeSort(a, 0, n - 1);
        return a;
    }

    /**
     * Метод mergeSort рекурсивно сортирует массив, разделяя его на две части.
     * @param arr массив, который нужно отсортировать
     * @param left индекс начала массива
     * @param right индекс конца массива
     */
    private void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            // Находим середину массива
            int mid = left + (right - left) / 2;

            // Рекурсивно сортируем левую и правую части
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            // Объединяем отсортированные части
            merge(arr, left, mid, right);
        }
    }

    /**
     * Метод merge объединяет две отсортированные части массива в один.
     * @param arr массив, который нужно объединить
     * @param left индекс начала первой половины
     * @param mid индекс середины массива
     * @param right индекс конца массива
     */
    private void merge(int[] arr, int left, int mid, int right) {
        // Размеры двух половин массива
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создаем временные массивы
        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        // Копируем данные во временные массивы
        System.arraycopy(arr, left, leftArr, 0, n1);
        System.arraycopy(arr, mid + 1, rightArr, 0, n2);

        // Индексы для временных массивов
        int i = 0, j = 0, k = left;

        // Объединяем временные массивы обратно в основной массив
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
            }
        }

        // Копируем оставшиеся элементы из левого временного массива (если они есть)
        while (i < n1) {
            arr[k++] = leftArr[i++];
        }

        // Копируем оставшиеся элементы из правого временного массива (если они есть)
        while (j < n2) {
            arr[k++] = rightArr[j++];
        }
    }
}
