package by.it.group410901.zaythev.lesson04;

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

    public void merge(int[] arr, int left, int mid, int right){
        int arr1 = mid - left + 1;
        int arr2 = right - mid;

        int[] leftArr = new int[arr1];
        int[] rightArr = new int[arr2];

        for(int i = 0; i < arr1; ++i){
            leftArr[i] = arr[left+i];
        }

        for(int j = 0; j < arr2; ++j){
            rightArr[j] = arr[mid+1+j];
        }

        int a = 0, b = 0, k = left;
        //a и b как индексы временных подмассивов
        while(a < arr1 && b < arr2){
            if(leftArr[a] <= rightArr[b]){
                arr[k] = leftArr[a];
                a++;
            }
            else{
                arr[k] = rightArr[b];
                b++;
            }
            k++;
        }

        //копируем оставшиеся элементы leftArr
        while(a < arr1){
            arr[k] = leftArr[a];
            a++;
            k++;
        }

        while(b < arr2){
            arr[k] = rightArr[b];
            b++;
            k++;
        }
    }

    public void mergeSort(int[] arr, int left, int right){
        if(left < right){
            int mid = left + (right-left) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid+1, right);
            merge(arr, left, mid, right);
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

        mergeSort(a, 0, n-1);
        // тут ваше решение (реализуйте сортировку слиянием)
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
