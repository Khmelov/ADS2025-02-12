package by.it.group410902.podryabinkin.lesson04;

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
        System.out.println("\n\nB:");
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }
        a = MySort(a);
        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.println(a[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    int[] MySort(int[] inp){
        if(inp.length <= 1) return inp;
        int arr1[] = new int[inp.length / 2];
        int arr2[] = new int[inp.length - inp.length / 2];

        for(int i = 0; i < inp.length / 2; i++){
            arr1[i] = inp[i];
        }
        for(int i = inp.length / 2, j = 0; i < inp.length; i++, j++){
            arr2[j] = inp[i];
        }

        arr1 = MySort(arr1);
        arr2 = MySort(arr2);
        for(int i = 0, j = 0; i < arr1.length || j < arr2.length; ){
            if(arr1.length > i && arr2.length > j){
                if(arr1[i] < arr2[j]){
                    inp[i + j] = arr1[i];
                    i++;
                }
                else if(arr1[i] > arr2[j]){
                    inp[i + j] = arr2[j];
                    j++;
                }
                else {
                    inp[i+j] = arr1[i];
                    i++;
                    inp[i+j] = arr2[j];
                    j++;
                }
            }
            else if (arr1.length <= i) {
                inp[i + j] = arr2[j];
                j++;
            }
            else {
                inp[i + j] = arr1[i];
                i++;
            }
        }
        return inp;
    }


}
