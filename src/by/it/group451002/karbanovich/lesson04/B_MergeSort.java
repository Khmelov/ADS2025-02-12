package by.it.group451002.karbanovich.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
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

        // Читаем размер массива из файла
        int n = scanner.nextInt();

        // Читаем сам массив из файла
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Если длина массива нулевая, то возвращаем нулевой результат
        if (n == 0) { return a; }

        // Сортируем массив слиянием
        a = mergeSort(a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    // Функция для сортировки слиянием
    int[] mergeSort(int[] a, int leftBorder, int rightBorder) {
        // Если длина массива = 1, то возвращаем массив
        if (rightBorder == leftBorder) {
            return Arrays.copyOfRange(a, leftBorder, rightBorder + 1);
        }
        // Иначе делим массив на 2 части и снова вызываем эту функцию
        else {
            // Находим центральный элемент (чтобы поделить массив пополам)
            int midInd = (rightBorder + leftBorder) / 2;

            // Делим массив на 2 части и вызываем функцию сортировки для каждой половины
            int[] first = mergeSort(a, leftBorder, midInd);
            int[] second = mergeSort(a, midInd + 1, rightBorder);

            // Создаем массив для сложения двух отсортированных массивов (половин)
            int[] b = new int[rightBorder - leftBorder + 1];

            // Индексы в отсортированных массивах (левой и правой половине)
            int l_ind = 0, r_ind = 0;

            // Записываем в массив b по 1 наименьшему элементу из 2 массивов (сортируем)
            for (int i = 0; i < b.length; i++) {
                // Если в первом массиве закончились элементы, то записываем оставшиеся элементы со второго массива
                if (l_ind == first.length) {
                    System.arraycopy(second, r_ind, b, i, second.length-r_ind);
                    break;
                }
                // Если во втором массиве закончились элементы, то записываем оставшиеся элементы с первого массива
                else if (r_ind == second.length) {
                    System.arraycopy(first, l_ind, b, i, first.length-l_ind);
                    break;
                }
                // Выбираем, какой элемент меньше, и записываем его в конечный отсортированный массив b
                else {
                    if (first[l_ind] <= second[r_ind]) {
                        b[i] = first[l_ind];
                        l_ind++;
                    } else {
                        b[i] = second[r_ind];
                        r_ind++;
                    }
                }
            }
            return b;
        }
    }

}
