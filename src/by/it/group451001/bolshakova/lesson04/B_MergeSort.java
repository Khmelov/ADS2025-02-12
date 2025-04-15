package by.it.group451001.bolshakova.lesson04;

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

    private void mergeSort(int[] a, int[] temp, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(a, temp, left, mid);
            mergeSort(a, temp, mid + 1, right);
            merge(a, temp, left, mid, right);
        }
    }

    private void merge(int[] a, int[] temp, int left, int mid, int right) {
        // Копируем элементы во временный массив temp
        for (int i = left; i <= right; i++) {
            temp[i] = a[i];
        }

        int i = left; // Указатель на начало левой части
        int j = mid + 1; // Указатель на начало правой части
        int k = left;

        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                a[k] = temp[i];
                i++;
            } else {
                a[k] = temp[j];
                j++;
            }
            k++;
        }
        while (i <= mid) {
            a[k] = temp[i];
            i++;
            k++;
        }

        while (j <= right) {
            a[k] = temp[j];
            j++;
            k++;
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием
        int[] temp = new int[n];
        mergeSort(a, temp, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
