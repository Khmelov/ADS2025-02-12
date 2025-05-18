package by.it.group410902.saliev.lesson04;

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

        // сортировка слиянием прямо здесь
        // без дополнительных методов вне блока

        // рекурсивная функция
        class MergeSort {
            void sort(int[] array, int left, int right) {
                if (left < right) {
                    int mid = (left + right) / 2;
                    sort(array, left, mid);
                    sort(array, mid + 1, right);
                    merge(array, left, mid, right);
                }
            }

            void merge(int[] array, int left, int mid, int right) {
                int[] temp = new int[right - left + 1];
                int i = left, j = mid + 1, k = 0;

                while (i <= mid && j <= right) {
                    if (array[i] <= array[j]) {
                        temp[k++] = array[i++];
                    } else {
                        temp[k++] = array[j++];
                    }
                }

                while (i <= mid) {
                    temp[k++] = array[i++];
                }

                while (j <= right) {
                    temp[k++] = array[j++];
                }

                for (int t = 0; t < temp.length; t++) {
                    array[left + t] = temp[t];
                }
            }
        }

        // вызываем сортировку
        MergeSort ms = new MergeSort();
        ms.sort(a, 0, a.length - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

}
