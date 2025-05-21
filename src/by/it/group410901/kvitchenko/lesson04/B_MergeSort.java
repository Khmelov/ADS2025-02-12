package by.it.group410901.kvitchenko.lesson04;

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

        int[] temp = new int[n]; // временный массив для слияния
        for (int size = 1; size < n; size *= 2) { // размер подмассивов увеличиваем каждый раз
            for (int leftStart = 0; leftStart < n - size; leftStart += 2 * size) {
                int mid = leftStart + size - 1;
                int rightEnd = Math.min(leftStart + 2 * size - 1, n - 1);

                // Слияние двух подмассивов
                int left = leftStart;
                int right = mid + 1;
                int index = leftStart;

                while (left <= mid && right <= rightEnd) {
                    if (a[left] <= a[right]) {
                        temp[index++] = a[left++];
                    } else {
                        temp[index++] = a[right++];
                    }
                }

                // Копирование оставшихся элементов левого подмассива
                while (left <= mid) {
                    temp[index++] = a[left++];
                }

                // Копирование оставшихся элементов правого подмассива
                while (right <= rightEnd) {
                    temp[index++] = a[right++];
                }

                // Копируем отсортированную часть обратно в основной массив
                for (int i = leftStart; i <= rightEnd; i++) {
                    a[i] = temp[i];
                }
            }
        }
        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
}
