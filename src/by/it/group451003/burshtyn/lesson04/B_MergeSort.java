package by.it.group451003.burshtyn.lesson04;

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
        Scanner scanner = new Scanner(stream);

        // читаем вход
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // собственно сортировка слиянием
        int[] tmp = new int[n];
        mergeSort(a, tmp, 0, n - 1);

        return a;
    }

    /**
     * Рекурсивная сортировка слиянием фрагмента a[left..right]
     */
    private void mergeSort(int[] a, int[] tmp, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = left + (right - left) / 2;
        // сортируем левую половину
        mergeSort(a, tmp, left, mid);
        // сортируем правую половину
        mergeSort(a, tmp, mid + 1, right);
        // сливаем две отсортированные половины
        merge(a, tmp, left, mid, right);
    }

    /**
     * Сливает два соседних отсортированных массива:
     * a[left..mid] и a[mid+1..right] в один в tmp, затем копирует обратно в a.
     */
    private void merge(int[] a, int[] tmp, int left, int mid, int right) {
        int i = left;      // указатель для левой половины
        int j = mid + 1;   // указатель для правой половины
        int k = left;      // указатель для tmp

        // пока в обеих половинах есть элементы
        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                tmp[k++] = a[i++];
            } else {
                tmp[k++] = a[j++];
            }
        }
        // остатки левой
        while (i <= mid) {
            tmp[k++] = a[i++];
        }
        // остатки правой
        while (j <= right) {
            tmp[k++] = a[j++];
        }
        // копируем обратно в a
        for (int p = left; p <= right; p++) {
            a[p] = tmp[p];
        }
    }
}
