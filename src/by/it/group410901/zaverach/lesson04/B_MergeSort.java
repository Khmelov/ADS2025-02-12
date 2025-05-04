package by.it.group410901.zaverach.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
        mergeSort(0,n-1,a);


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    void mergeSort(int l, int r, int[] a) {
        int s = (r - l) / 2+l;
        if (r > l + 1) {
            mergeSort(l, s,a);
            mergeSort(s + 1, r,a);
            merge(l,r,a,s);
        } else {
            if (a[l] > a[r]) {
                int temp = a[l];
                a[l] = a[r];
                a[r] = temp;
            }

        }
    }
    void merge(int l, int r, int [] a,int s){
        ArrayList<Integer> b=new ArrayList<>();
        int i = l;
        int j = s+1;

        while (i <= s && j <= r) {
            if (a[i] < a[j]) {
                b.add(a[i]);
                i += 1;
            } else {
                b.add(a[j]);
                j += 1;
            }
        }

        while (i <= s) {
            b.add(a[i]);
            i += 1;
        }


        while (j <= r) {
            b.add(a[j]);
            j += 1;
        }

        for (int k = l; k <= r; k++) {
            a[k] = b.getFirst();
            b.removeFirst();
        }
    }




}
