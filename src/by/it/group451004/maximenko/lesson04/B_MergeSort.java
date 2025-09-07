package by.it.group451004.maximenko.lesson04;

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
        long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    public static void mergeSort(int[] arr){
        int[] tempArray = new int[arr.length];
        int rbegin, rend;
        int i, j, m;
        for (int k = 1; k < arr.length; k *= 2) {
            for (int q = 0; q < arr.length; ) {
                System.out.print(arr[q] + " ");
                q++;
                if (q % k == 0)
                    System.out.print("    ");
            }
            System.out.println();
            for (int left = 0; left + k < arr.length; left += k * 2) {
                rbegin = left + k;
                rend = rbegin + k;
                if (rend > arr.length)
                    rend = arr.length;
                m = left; i = left; j = rbegin;
                while (i < rbegin && j < rend) {
                    if (arr[i] <= arr[j]) {
                        tempArray[m] = arr[i];
                        i++;
                    }
                    else {
                        tempArray[m] = arr[j];
                        j++;
                    }
                    m++;
                }
                while (i < rbegin) {
                    tempArray[m] = arr[i];
                    i++;
                    m++;
                }
                while (j < rend) {
                    tempArray[m] = arr[j];
                    j++;
                    m++;
                }
                for (m = left; m < rend; m++) {
                    arr[m] = tempArray[m];
                }
            }
        }
        for (int p = 0; p < arr.length; p++ )
            System.out.print(arr[p] + " ");
        System.out.println();
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

        mergeSort(a);


        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
