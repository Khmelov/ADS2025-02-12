package by.it.group410901.evtuhovskaya.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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

        int n = scanner.nextInt(); //читаем размер массива
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt(); //читаем элементы массива
        }

        //рекурсивная сортировка
        mergeSort(a, 0, a.length - 1);

        return a;
    }

    //рекурсивная функция сортировки слиянием
    void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            //сортируем левую и правую части массива
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            //соединяем отсортированные части
            merge(arr, left, mid, right);
        }
    }

    //функция соединения двух отсортированных подмассивов
    void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left;     // начало левой части
        int j = mid + 1;  // начало правой части
        int k = 0;

        //пока не дошли до конца одной из частей
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }

        //если остались элементы в левой части
        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        //если остались элементы в правой части
        while (j <= right) {
            temp[k++] = arr[j++];
        }

        //копируем отсортированные элементы в исходный массив
        for (int t = 0; t < temp.length; t++) {
            arr[left + t] = temp[t];
        }
    }
}
