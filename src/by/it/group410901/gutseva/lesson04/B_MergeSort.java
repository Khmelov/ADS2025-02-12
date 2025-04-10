package by.it.group410901.gutseva.lesson04;

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
        a = sort(a);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
    //Метод mergeSort рекурсивно делит массив на две половины,
    // сортирует каждую и затем объединяет их.
    public int[] sort(int[] arr) {
        if (arr == null)
            return null;
        if (arr.length < 2)
            return arr;

        int[] arrA = new int[arr.length / 2];
        System.arraycopy(arr, 0, arrA, 0, arrA.length);

        int[] arrB = new int[arr.length - arrA.length];
        System.arraycopy(arr, arrA.length, arrB, 0, arr.length - arrA.length);

        arrA = sort(arrA);
        arrB = sort(arrB);

        return merge(arrA, arrB);
    }
    //В методе getMergeSort происходит чтение данных из файла и вызов метода mergeSort.
    public int[] merge(int[] arrA, int[] arrB) {

        int positionA = 0, positionB = 0;

        int[] arr = new int[arrA.length + arrB.length];

        for (int i = 0; i < arr.length; i++) {
            if (positionA == arrA.length) {
                arr[i] = arrB[positionB];
                positionB++;

            } else if (positionB == arrB.length) {
                arr[i] = arrA[positionA];
                positionA++;

            } else if (arrA[positionA] < arrB[positionB]) {
                arr[i] = arrA[positionA];
                positionA++;

            } else {
                arr[i] = arrB[positionB];
                positionB++;
            }
        }


        return arr;
    }

}
