package by.it.group451004.baranovskiy.lesson04;

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
Для каждого i от 1 до kk необходимо вывести индекс 1<=j<=n,
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
        // Чтение входного файла
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt");
        A_BinaryFind instance = new A_BinaryFind();

        // Вызов метода поиска и получение результатов
        int[] result = instance.findIndex(stream);

        // Вывод результатов
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    // Метод для поиска индексов элементов в отсортированном массиве
    int[] findIndex(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Чтение размера отсортированного массива
        int n = scanner.nextInt();
        int[] a = new int[n];

        // Заполнение отсортированного массива
        for (int i = 1; i <= n; i++) {
            a[i - 1] = scanner.nextInt();
        }

        // Чтение количества элементов для поиска
        int k = scanner.nextInt();
        int[] result = new int[k]; // Массив для хранения результатов

        // Поиск каждого элемента
        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt(); // Элемент, который нужно найти
            result[i] = -1; // Значение по умолчанию (если элемент не найден)

            // Границы поиска в массиве
            int right = n - 1;
            int left = 0;

            // Бинарный поиск
            while (left <= right) {
                // Вычисление середины текущего диапазона
                int mid = left + (right - left) / 2;

                if (value > a[mid]) {
                    // Искомый элемент в правой половине
                    left = mid + 1;
                } else if (value < a[mid]) {
                    // Искомый элемент в левой половине
                    right = mid - 1;
                } else {
                    // Элемент найден
                    result[i] = mid + 1; // +1 для 1-индексации (похоже на нумерацию с 1)

                    // Выход из цикла (можно использовать break)
                    left = right;
                    left++;
                }
            }
        }
        return result;
    }
}