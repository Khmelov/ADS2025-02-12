package by.it.group451002.kureichuk.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
            System.out.println(arr[i]);
        }

        mergeSort(arr, 0, arr.length - 1);
        int i = 0;
        return arr;
    }

    void merge(int[] arr, int left, int mid, int right){
        int[] arrFirst = Arrays.copyOfRange(arr, left, mid + 1);
        int[] arrSecond = Arrays.copyOfRange(arr, mid + 1, right + 1);

        int i = 0, j = 0;
        int k = left;

        while (i < arrFirst.length && j < arrSecond.length){
            if (arrFirst[i] <= arrSecond[j]) {
                arr[k] = arrFirst[i];
                i++;
            }else{
                arr[k] = arrSecond[j];
                j++;
            }
            k++;
        }

        while (i < arrFirst.length){
            arr[k] = arrFirst[i];
            i++;
            k++;
        }

        while (j < arrSecond.length){
            arr[k] = arrSecond[j];
            j++;
            k++;
        }
    }
    void mergeSort(int[] arr, int left, int right){
        if ( left >= right )
            return;

        int mid = left + (right - left) / 2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

}
