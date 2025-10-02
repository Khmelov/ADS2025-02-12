package by.it.group451002.yeshmanski.lesson04;

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

        //long startTime = System.currentTimeMillis();

        int[] result = instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {

        Scanner scanner = new Scanner(stream);

        // Считываем размер массива
        int n = scanner.nextInt();
        int[] a = new int[n];

        // Заполняем массив элементами
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызываем рекурсивную сортировку слиянием
        mergeSort(a, 0, n - 1);
        return a; // Возвращаем отсортированный массив
    }

    // Рекурсивная сортировка массива слиянием
    private void mergeSort(int[] a, int left, int right) {
        // Если один элемент
        // Для рекурсии
        if (left >= right) {
            return;
        }

        // Разбиваем диапазон на две части
        int mid = left + (right - left) / 2;

        // Сортируем левую половину
        mergeSort(a, left, mid);

        // Сортируем правую половину
        mergeSort(a, mid + 1, right);

        // Сливаем две отсортированные половины
        merge(a, left, mid, right);
    }

    // Функция для слияния двух отсортированных частей массива
    private void merge(int[] a, int left, int mid, int right) {
        // Определяем размеры двух подмассивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создаем временные массивы для хранения значений
        int[] L = new int[n1];
        int[] R = new int[n2];

        // Копируем значения из исходного массива в временные массивы
        for (int i = 0; i < n1; i++) {
            L[i] = a[left + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = a[mid + 1 + j];
        }

        // Инициализируем индексы для слияния
        int i = 0, j = 0, k = left;

        // Сравниваем элементы из двух временных массивов
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                a[k++] = L[i++]; // Берем элемент из левого массива
            } else {
                a[k++] = R[j++]; // Берем элемент из правого массива
            }
        }

        // Добавляем оставшиеся элементы из левого массива, если они остались
        while (i < n1) {
            a[k++] = L[i++];
        }

        // Добавляем оставшиеся элементы из правого массива, если они остались
        while (j < n2) {
            a[k++] = R[j++];
        }
    }
}
