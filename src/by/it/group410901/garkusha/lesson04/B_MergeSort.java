package by.it.group410901.garkusha.lesson04;

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

        // Реализация сортировки слиянием
        int[] temp = new int[n];
        for (int size = 1; size < n; size *= 2) {
            for (int leftStart = 0; leftStart < n; leftStart += 2 * size) {
                int mid = Math.min(leftStart + size - 1, n - 1);//индекс конца левого подмассива
                int rightEnd = Math.min(leftStart + 2 * size - 1, n - 1);

                // Слияние двух подмассивов
                int i = leftStart;//текущий элемент левого подмассива
                int j = mid + 1;// текущий элемент правого подмассива
                int k = leftStart;//индекс в массиве temp
                while (i <= mid && j <= rightEnd) {
                    if (a[i] <= a[j]) {
                        temp[k++] = a[i++];
                    } else {
                        temp[k++] = a[j++];
                    }
                }

                // Добираем оставшиеся элементы
                while (i <= mid) temp[k++] = a[i++];
                while (j <= rightEnd) temp[k++] = a[j++];

                // Копирование обратно в исходный массив
                for (k = leftStart; k <= rightEnd; k++) {
                    a[k] = temp[k];
                }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
