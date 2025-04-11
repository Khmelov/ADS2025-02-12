package by.it.group451001.klevko.lesson04;

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

        int size = 1;
        int start1, start2, end1, end2, i;
        //строго до end и начиная включая с start
        /*while (size < n) {
            start1 = 0;
            end2 = 0;
            int[] a2 = new int[n];
            while (start1 < n) {
                start1 = end2;
                end1 = start1 + size;
                start2 = end1;
                end2 = start2 + size;

                //sort 1 part
                int tempStart1 = start1;
                int tempStart2 = start2;
                for (int i = start1; ((i < end2) && (i < n)); i++) {
                    if ((tempStart2 >= end2) || (tempStart2 >= n)) {
                        tempStart2 = tempStart1;
                    }
                    if ((tempStart1 >= end1) || (tempStart1 >= n)) {
                        tempStart1 = tempStart2;
                    }
                    if (a[tempStart1] > a[tempStart2]) {
                        a2[i] = a[tempStart2];
                        ++tempStart2;
                    } else {
                        a2[i] = a[tempStart1];
                        ++tempStart1;
                    }
                }
            }
            a = a2;
            size*=2;
        }*/
        while (size < n) {
            start1 = 0;
            end2 = 0;
            int[] a2 = new int[n];
            while (start1 < n) {
                start1 = end2;
                end1 = Math.min(start1 + size, n);
                start2 = end1;
                end2 = Math.min(start2 + size, n);

                //sort 1 part
                i = 0;
                while ((start1 < end1) && (start2 < end2)) {
                    if (a[start1] > a[start2]) {
                        a2[i] = a[start2];
                        ++start2;
                    } else {
                        a2[i] = a[start1];
                        ++start1;
                    }
                    ++i;
                }
                if (start1 == end1) {
                    System.arraycopy(a, start2, a2, i, end2-start2);
                } else {
                    System.arraycopy(a, start1, a2, i, end1-start1);
                }
            }
            a = a2;
            size*=2;
        }
        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
