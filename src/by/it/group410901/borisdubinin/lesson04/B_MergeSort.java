package by.it.group410901.borisdubinin.lesson04;

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
        a = mergeSort(a);
        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
    public static int[] mergeSort(int[] arr){
        if(arr.length == 1) return arr;
        if(arr.length == 2) return merge(new int[]{arr[0]}, new int[]{arr[1]});

        int[] left = new int[arr.length/2];
        int[] right = new int[arr.length - arr.length/2];

        System.arraycopy(arr, 0, left, 0, left.length);
        System.arraycopy(arr, left.length, right, 0, right.length);

        return merge(mergeSort(left), mergeSort(right));
    }
    public static int[] merge(int[] arr1, int[] arr2) {
        int i = 0, i1 = 0, i2 = 0;
        int[] result = new int[arr1.length + arr2.length];
        while (i1 < arr1.length && i2 < arr2.length) {
            if (arr1[i1] < arr2[i2])
                result[i++] = arr1[i1++];
            else
                result[i++] = arr2[i2++];
        }
        while (i1 < arr1.length)
            result[i++] = arr1[i1++];
        while (i2 < arr2.length)
            result[i++] = arr2[i2++];
        return result;
    }

}
