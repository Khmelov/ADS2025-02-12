package by.it.group451002.vishnevskiy.lesson04;

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

        mergeSort(a, 0, n - 1); // запускаем сортировку слиянием для всего массива от 0 до n-1

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    // Метод сортировки слиянием
    void mergeSort(int[] a, int left, int right) {
        if (left < right) { // если в подмассиве более одного элемента
            int mid = (left + right) / 2; // находим середину подмассива
            mergeSort(a, left, mid); // рекурсивно сортируем левую половину
            mergeSort(a, mid + 1, right); // рекурсивно сортируем правую половину
            merge(a, left, mid, right); // сливаем отсортированные половины
        }
    }

    // Метод слияния двух отсортированных подмассивов
    void merge(int[] a, int left, int mid, int right) {
        int n1 = mid - left + 1; // размер левого подмассива
        int n2 = right - mid; // размер правого подмассива

        int[] L = new int[n1]; // создаем временный массив для левой части
        int[] R = new int[n2]; // создаем временный массив для правой части

        for (int i = 0; i < n1; ++i) // копируем данные в левый временный массив
            L[i] = a[left + i];
        for (int j = 0; j < n2; ++j) // копируем данные в правый временный массив
            R[j] = a[mid + 1 + j];

        int i = 0, j = 0; // индексы для временных массивов
        int k = left; // индекс для слияния обратно в массив a

        while (i < n1 && j < n2) { // пока есть элементы в обоих массивах
            if (L[i] <= R[j]) { // если элемент из левого массива меньше или равен элементу из правого
                a[k] = L[i]; // записываем его в основной массив
                i++; // переходим к следующему элементу в левом массиве
            } else {
                a[k] = R[j]; // иначе записываем элемент из правого массива
                j++; // переходим к следующему элементу в правом массиве
            }
            k++; // переходим к следующей позиции в основном массиве
        }

        while (i < n1) { // копируем оставшиеся элементы из левого массива
            a[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) { // копируем оставшиеся элементы из правого массива
            a[k] = R[j];
            j++;
            k++;
        }
    }
}
