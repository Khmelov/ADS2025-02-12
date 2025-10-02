package by.it.group410902.yarmashuk.lesson04;

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
           // System.out.println(a[i]);
        }

        int[] buf = new int[n];
         for(int size = 1; size < n; size = 2*size){
             for(int left = 0; left < n-1; left +=2*size ){
                 int mid = Math.min(left + size-1 , n-1);
                 int right = Math.min(left +2*size-1, n-1);
                 for(int i = left ; i <= right ; i++){
                     buf[i]= a[i];
                 }
                 int i = left;
                 int j = mid +1;
                 int k = left;
                 while(i <= mid && j <= right){
                     if (buf[i]<= buf[j]){
                         a[k]= buf[i];
                         i++;
                     }else{
                         a[k]= buf[j];
                         j++;
                     }
                     k++;
                 }
                 while(i <= mid){
                     a[k]=buf[i];
                     i++;
                     k++;
                 }
             }
         }



        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
