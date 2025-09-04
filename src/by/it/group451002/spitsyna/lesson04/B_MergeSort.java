package by.it.group451002.spitsyna.lesson04;

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
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием

        mergeSort(a,0,a.length-1);


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    private void mergeSort(int[] a, int l, int r) {
        if (l<r) {
            int m = (l + r) / 2;

            mergeSort(a, l, m);
            mergeSort(a, m + 1, r);

            merge(a, l, m, r);
        }
    }

    private void merge(int[] a, int l, int m,int r) {
        //размеры подмассивов
        int n1 = m-l+1;
        int n2 = r-m;

        int[] arrLeft = new int[n1];
        int[] arrRight = new int[n2];

        //заполнение первого подмассива
        for (int i = 0; i < n1; i++) {
            arrLeft[i] = a[l+i];
        }

        //заполнение второго подмассива
        for (int j = 0; j < n2; j++) {
            arrRight[j] = a[m+1+j];
        }

        //заполняем исходный массив элементами подмассивов в правильном порядке
        int i=0,j=0,k=l;
        while(i < n1 && j < n2){
            if (arrLeft[i]<=arrRight[j]) {
              a[k] = arrLeft[i];
              i++;
            }
            else {
                a[k] = arrRight[j];
                j++;
            }
            k++;
        }

        //добавляем оставшиеся элементы левого подмассива
        while(i < n1){
            a[k] = arrLeft[i];
            i++;
            k++;
        }

        //добавляем оставшиеся элементы правого подмассива
        while (j < n2){
            a[k] = arrRight[j];
            j++;
            k++;
        }

    }
}

