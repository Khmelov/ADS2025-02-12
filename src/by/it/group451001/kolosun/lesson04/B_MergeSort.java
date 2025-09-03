package by.it.group451001.kolosun.lesson04;

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

    int[] Merge(int[] arr1, int[] arr2){
        int f1 = 0;
        int f2 = 0;
        int r = 0;
        int[] res = new int[arr1.length + arr2.length];
        while (f1 < arr1.length && f2 < arr2.length){
            if(arr1[f1] <= arr2[f2]) {
                res[r] = arr1[f1];
                r++;
                f1++;
            }
            else{
                res[r] = arr2[f2];
                r++;
                f2++;
            }
        }

        while (f1 < arr1.length){
            res[r] = arr1[f1];
            r++;
            f1++;
        }

        while (f2 < arr2.length){
            res[r] = arr2[f2];
            r++;
            f2++;
        }
        return  res;
    }

    int[] MergeSort(int[] array){
        if(array.length == 1){
            return array;
        }else{
        int[] l = MergeSort( Arrays.copyOfRange(array, 0, array.length/ 2));
        int[] r = MergeSort(Arrays.copyOfRange(array, array.length / 2, array.length));
        return Merge(l,r);
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


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return MergeSort(a);
    }




}
