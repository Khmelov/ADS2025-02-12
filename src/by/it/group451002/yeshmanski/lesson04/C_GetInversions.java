package by.it.group451002.yeshmanski.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)
Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].
    (Такая пара элементов называется инверсией массива.
    Количество инверсий в массиве является в некотором смысле
    его мерой неупорядоченности: например, в упорядоченном по неубыванию
    массиве инверсий нет вообще, а в массиве, упорядоченном по убыванию,
    инверсию образуют каждые (т.е. любые) два элемента.
    )
Sample Input:
5
2 3 9 2 9
Sample Output:
2
*/


public class C_GetInversions {
    public static void main(String[] args) throws FileNotFoundException {

        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();

        // Вызов подпрограммы
        int result = instance.calc(stream);

        // Вывод результата
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {

        Scanner scanner = new Scanner(stream);

        // Чтение размера массива
        int n = scanner.nextInt();

        // Заполнение массива элементами
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        // Запуск сортировки с подсчетом инверсий
        return mergeSort(arr, 0, arr.length - 1);
    }


    //Функция для слияния двух отсортированных частей массива и подсчета инверсий.

    // Если элемент из правой части меньше, это означает,
    // что все оставшиеся элементы в левой части
    // образуют инверсии с текущим элементом правой части.
    int merge(int[] arr, int left, int mid, int right) {

        // Создаем временные массивы для хранения частей
        int[] arrFirst = Arrays.copyOfRange(arr, left, mid + 1);
        int[] arrSecond = Arrays.copyOfRange(arr, mid + 1, right + 1);

        // Инициализация указателей для массивов
        int i = 0, j = 0, k = left, inversions = 0;

        // Сравниваем элементы из двух массивов
        while (i < arrFirst.length && j < arrSecond.length) {
            if (arrFirst[i] <= arrSecond[j]) {
                // Элемент из левого массива меньше или равен элементу из правого
                arr[k] = arrFirst[i];
                i++;
            } else {
                // Элемент из правого массива меньше — это инверсия
                inversions += arrFirst.length - i;
                arr[k] = arrSecond[j];
                j++;
            }
            k++;
        }

        // Копируем оставшиеся элементы из левого массива
        while (i < arrFirst.length) {
            arr[k] = arrFirst[i];
            i++;
            k++;
        }

        // Копируем оставшиеся элементы из правого массива
        while (j < arrSecond.length) {
            arr[k] = arrSecond[j];
            j++;
            k++;
        }

        // Возвращаем количество найденных инверсий
        return inversions;
    }


    //Рекурсивная сортировка слиянием с подсчетом инверсий.

    // Если элемент из правой части меньше, это означает,
    // что все оставшиеся элементы в левой части
    // образуют инверсии с текущим элементом правой части.

    int mergeSort(int[] arr, int left, int right) {

        if (left >= right) {
            return 0;
        }

        // Разбиваем массив на две части
        int mid = left + (right - left) / 2;

        // Считаем инверсии в левой части
        int leftInv = mergeSort(arr, left, mid);

        // Считаем инверсии в правой части
        int rightInv = mergeSort(arr, mid + 1, right);

        // Считаем инверсии при слиянии двух частей
        int mergeInv = merge(arr, left, mid, right);

        // Возвращаем суммарное количество инверсий
        return leftInv + rightInv + mergeInv;
    }
}