package by.it.group451003.burshtyn.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
В первой строке источника данных даны:
    - целое число 1<=n<=100000 (размер массива)
    - сам массив A[1...n] из n различных натуральных чисел,
      не превышающих 10E9, в порядке возрастания,
Во второй строке
    - целое число 1<=k<=10000 (сколько чисел нужно найти)
    - k натуральных чисел b1,...,bk не превышающих 10E9 (сами числа)
Для каждого i от 1 до k необходимо вывести индекс 1<=j<=n,
для которого A[j]=bi, или -1, если такого j нет.

    Sample Input:
    5 1 5 8 12 13
    5 8 1 23 1 11

    Sample Output:
    3 1 -1 1 -1

(!) Обратите внимание на смещение начала индекса массивов JAVA относительно условий задачи
*/

public class A_BinaryFind {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_BinaryFind.class
                .getResourceAsStream("dataA.txt");
        A_BinaryFind instance = new A_BinaryFind();
        int[] result = instance.findIndex(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] findIndex(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        // размер отсортированного массива
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // сколько чисел нужно найти
        int k = scanner.nextInt();
        int[] result = new int[k];

        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();
            result[i] = binarySearch(a, value);
        }

        return result;
    }

    /**
     * Выполняет классический итеративный бинарный поиск.
     * @param a отсортированный по возрастанию массив
     * @param key искомое значение
     * @return 1‑based индекс в массиве a, либо -1, если не найдено
     */
    private int binarySearch(int[] a, int key) {
        int left = 0;
        int right = a.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (a[mid] == key) {
                // возвращаем 1‑based позицию
                return mid + 1;
            } else if (a[mid] < key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
}
