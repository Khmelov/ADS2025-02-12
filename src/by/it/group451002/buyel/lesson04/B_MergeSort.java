package by.it.group451002.buyel.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
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

    private int[] MergeSort(int[] array) {
        if (array.length == 1 || array.length == 0) {
            return array;
        }
        int mid = (array.length-1) / 2;
        // копия array от 0 до [mid+1] элемента
        int[] left = Arrays.copyOfRange(array, 0, mid+1);
        // копия array от mid+1 до конца массива array
        int[] right = Arrays.copyOfRange(array, mid+1, array.length);

        int n, m, k;
        n = m = k = 0;
        int[] tempArray = new int[array.length];    // куда будет записываться сортированный массив

        // пока индексы будут < длин массивов
        while (n < left.length && m < right.length) {
            // если элемент левого массива < элемента правого
            if (left[n] <= right[m]) {
                tempArray[k] = left[n];
                n++;
            }
            else {
                tempArray[k] = right[m];
                m++;
            }
            k++;
        }

        // добавляем оставшиеся
        while (n < left.length) {
            tempArray[k] = left[n];
            n++;
            k++;
        }
        while (m < right.length) {
            tempArray[k] = right[m];
            m++;
            k++;
        }

        // присваимаем к "исходному"
        for (int i = 0; i < array.length; i++) {
            array[i] = tempArray[i];
        }
        return array;
    }


}
