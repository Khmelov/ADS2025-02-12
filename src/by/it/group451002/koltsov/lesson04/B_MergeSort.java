package by.it.group451002.koltsov.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;
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
        int[] tempArray = new int[n];

        int num = 1;    // количество элементов в подмассиве
        int i = 0, j = 0;
        int x = 0, y = 0;
        while (num < n)
        {
            for (int index = 0; index < n; index += num * 2) {
                for (int k = 0; k < num * 2 && k < n; k++) {
                    if (i < num && index + i < n || j < num && index + num + j < n) {
                        if (i < num)
                            x = a[index + i];
                        else
                            x = Integer.MAX_VALUE;

                        if (j < num && index + num + j < n)
                            y = a[index + num + j];
                        else
                            y = Integer.MAX_VALUE;

                        if (x < y) {
                            tempArray[index + k] = x;
                            i++;
                        } else {
                            tempArray[index + k] = y;
                            j++;
                        }
                    }
                }
                i = 0;
                j = 0;
            }
            // копируем отсортированный на данной стадии массив в исходный
            System.arraycopy(tempArray, 0, a, 0, n);
            num *= 2;   // увеличиваем в два раза размер подмассивов
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return tempArray;
    }
}
