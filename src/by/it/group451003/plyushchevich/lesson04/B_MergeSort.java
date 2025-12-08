package by.it.group451003.plyushchevich.lesson04;

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

    static void mergeHalves(int[] arr, final int FROM, final int MID, final int TO){
        int countLeft = MID - FROM + 1;
        int countRight = TO - MID;
        int[] leftTempArr = new int[countLeft];
        int[] rightTempArr = new int[countRight];
        for (int i = 0; i < countLeft; i++)
            leftTempArr[i] = arr[FROM + i];
        for (int j = 0; j < countRight; j++)
            rightTempArr[j] = arr[MID + 1 + j];
        int i = 0, j = 0, k = FROM;
        while(i < countLeft && j < countRight){
            if (leftTempArr[i] <= rightTempArr[j]){
                arr[k++] = leftTempArr[i++];
            }else
                arr[k++] = rightTempArr[j++];
        }

        while(i < countLeft){
            arr[k++] = leftTempArr[i++];
        }

        while(j < countRight){
            arr[k++] = rightTempArr[j++];
        }

    }

    static void mergeSort(int[] arr, final int FROM, final int TO){
            if (FROM >= TO) return;
            int mid = FROM + (TO - FROM)/2;
            mergeSort(arr, FROM, mid);
            mergeSort(arr, mid + 1, TO);
            mergeHalves(arr, FROM, mid, TO);
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
        mergeSort(a, 0, n-1 );

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
