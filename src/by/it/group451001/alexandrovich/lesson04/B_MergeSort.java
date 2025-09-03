package by.it.group451001.alexandrovich.lesson04;

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
            System.out.print(a[i] + " ");
        }
        System.out.println();
        mergeSort(a,0,n-1);


        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
    int[] mergeSort(int[] a, int l, int r){
        if (l < r){
            int m = (l+r)/2;
            int[] L = mergeSort(a,l,m).clone();
            int[] R = mergeSort(a,m+1,r).clone();
            int i = l;
            int j = m+1;
            int k = l;
            while ((i<=m) && (j<=r)){
                if (L[i]<R[j]){
                    a[k]=L[i];
                    i++;
                    k++;
                } else {
                    a[k]=R[j];
                    j++;
                    k++;
                }
            }
            while (i<=m){
                a[k]=L[i];
                i++;
                k++;
            }
            while (j<=r){
                a[k]=R[j];
                j++;
                k++;
            }
            return a;
        }
        else {
            return a;
        }
    }


}
