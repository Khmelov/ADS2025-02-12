package by.it.group410902.linnik.lesson04;

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
//ntetete222
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
        MergeSort(a, 0, n-1);
        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
    private void MergeSort(int[] a, int start, int end){
        if (start != end) {
            int mid = (end + start) / 2;

            MergeSort(a, start, mid);
            MergeSort(a, mid + 1, end);

            Merge(a, start, mid, end);
        }
    }
    private void Merge(int[] a, int start, int mid, int end) {//по сути передаем два массива
        int[] leftA = new int[mid - start + 1];
        int[] rightA = new int[end - mid];

        for (int i = 0; i < leftA.length; i++) {
            leftA[i] = a[start+i];
        }

        for (int i = 0; i < rightA.length; i++) {
            rightA[i] = a[mid + 1 + i];
        }
        int i = 0, j = 0, k = start;
        while (i < leftA.length && j < rightA.length) {
            if (leftA[i] < rightA[j]) {
                a[k] = leftA[i];
                i++;
            } else {
                a[k] = rightA[j];
                j++;
            }
            k++;
        }
        while (i < leftA.length) {
            a[k] = leftA[i];
            k++;
            i++;
        }
        while (j < rightA.length) {
            a[k] = rightA[j];
            j++;
            k++;
        }
    }
}
