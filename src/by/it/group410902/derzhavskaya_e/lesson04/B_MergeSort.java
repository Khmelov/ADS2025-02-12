package by.it.group410902.derzhavskaya_e.lesson04;

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

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием

        int[] tmp = new int[n];
        for (int width = 1; width < n; width *= 2) {
            for (int leftStart = 0; leftStart < n; leftStart += 2 * width) {
                int mid = Math.min(leftStart + width, n);
                int rightEnd = Math.min(leftStart + 2 * width, n);
                int i = leftStart;
                int j = mid;
                int k = leftStart;
                while (i < mid && j < rightEnd) {
                    if (a[i] <= a[j]) {
                        tmp[k++] = a[i++];
                    } else {
                        tmp[k++] = a[j++];
                    }
                }
                while (i < mid) {
                    tmp[k++] = a[i++];
                }
                while (j < rightEnd) {
                    tmp[k++] = a[j++];
                }
                for (int idx = leftStart; idx < rightEnd; idx++) {
                    a[idx] = tmp[idx];
                }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
