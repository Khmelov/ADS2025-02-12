package by.it.group451001.romeyko.lesson04;

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
        mergeSort(a, 0, n - 1);


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            // сортировка левой половины
            mergeSort(array, left, mid);
            // сортировка правой половины
            mergeSort(array, mid + 1, right);
            // слияние двух отсортированных половин
            merge(array, left, mid, right);
        }
    }

    // Метод слияния двух отсортированных подмассивов array[left...mid] и array[mid+1...right]
    private void merge(int[] array, int left, int mid, int right) {
        // Размеры двух подмассивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Временные массивы для левой и правой половин
        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        // Копирование данных во временные массивы
        for (int i = 0; i < n1; i++) {
            leftArr[i] = array[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArr[j] = array[mid + 1 + j];
        }

        // Слияние временных массивов обратно в array[left...right]
        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                array[k] = leftArr[i];
                i++;
            } else {
                array[k] = rightArr[j];
                j++;
            }
            k++;
        }

        // Копирование оставшихся элементов из leftArr, если они есть
        while (i < n1) {
            array[k] = leftArr[i];
            i++;
            k++;
        }
        // Копирование оставшихся элементов из rightArr, если они есть
        while (j < n2) {
            array[k] = rightArr[j];
            j++;
            k++;
        }
    }
}
