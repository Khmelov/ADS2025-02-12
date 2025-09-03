package by.it.group410901.tomashevich.lesson04;

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
        Sort(a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    void Sort(int[] a, int low, int high) {
        int n = high - low + 1;
        if (n <= 1) {
            return;
        }
        int mid = (low + high)/ 2;
        Sort(a, low, mid);
        Sort(a, mid + 1, high);
        int[] temp = new int[n];
        int l1 = low, l2 = mid + 1;
        for(int i = 0; i < n; i++) {
            if(l1 > mid) {
                temp[i] = a[l2];
                l2++;
            }
            else if(l2 > high) {
                temp[i] = a[l1];
                l1++;
            }
            else {
                if(a[l1] < a[l2]) {
                    temp[i] = a[l1];
                    l1++;
                }
                else {
                    temp[i] = a[l2];
                    l2++;
                }
            }
        }
        for(int i = 0; i < n; i++) {
            a[low + i] = temp[i];
        }
        return;
    }
}

