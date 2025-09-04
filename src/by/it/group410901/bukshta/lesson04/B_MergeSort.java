package by.it.group410901.bukshta.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
/*
Реализуйте сортировку слиянием для одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо отсортировать полученный массив.

Sample Input:
5
2 3 9 2 9
Sample Output:
2 2 3 9 9
*/
public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        int[] result = instance.getMergeSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        // Подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        // !!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Размер массива
        int n = scanner.nextInt();
        // Сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Реализация сортировки слиянием
        mergeSort(a, 0, n - 1);

        // !!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        scanner.close();
        return a;
    }

    // Рекурсивная функция сортировки слиянием
    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            // Сортируем левую половину
            mergeSort(array, left, mid);
            // Сортируем правую половину
            mergeSort(array, mid + 1, right);
            // Сливаем отсортированные половины
            merge(array, left, mid, right);
        }
    }

    // Функция слияния двух отсортированных подмассивов
    private void merge(int[] array, int left, int mid, int right) {
        // Размеры подмассивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создаём временные массивы
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Копируем данные во временные массивы
        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = array[mid + 1 + j];
        }

        // Слияние временных массивов обратно в основной
        int i = 0; // Индекс для левого подмассива
        int j = 0; // Индекс для правого подмассива
        int k = left; // Индекс для основного массива

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // Копируем оставшиеся элементы левого подмассива, если есть
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Копируем оставшиеся элементы правого подмассива, если есть
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}