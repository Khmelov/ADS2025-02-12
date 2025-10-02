package by.it.group451001.kazakov.lesson04;

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
        mergeSort(a);


        // https://ru.wikipedia.org/wiki/Сортировка_слиянием


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    void mergeSort(int[] A) {
        if (A.length <= 1) {
            return;
        }

        // Разделяем массив на две половины
        int[] L = new int[A.length / 2];
        int[] R = new int[A.length - A.length / 2];

        System.arraycopy(A, 0, L, 0, L.length);
        System.arraycopy(A, L.length, R, 0, R.length);

        // Рекурсивно сортируем каждую половину
        mergeSort(L);
        mergeSort(R);

        // Слияние отсортированных половин
        int n = 0, m = 0, k = 0;
        int[] C = new int[A.length];

        while (n < L.length && m < R.length) {
            if (L[n] <= R[m]) {
                C[k] = L[n];
                n++;
            } else {
                C[k] = R[m];
                m++;
            }
            k++;
        }

        while (n < L.length) {
            C[k] = L[n];
            n++;
            k++;
        }

        while (m < R.length) {
            C[k] = R[m];
            m++;
            k++;
        }

        // Копируем результат обратно в исходный массив
        System.arraycopy(C, 0, A, 0, A.length);
    }

}