package by.it.group451002.kravtsov.lesson04;

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
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt");
        A_BinaryFind instance = new A_BinaryFind();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.findIndex(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] findIndex(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

// Читаем размер отсортированного массива из входных данных
        int n = scanner.nextInt();
// Инициализируем массив размером n и заполняем его числами
        int[] a = new int[n];
        for (int i = 1; i <= n; i++) {
            a[i - 1] = scanner.nextInt(); // Заполняем массив числами
        }

// Читаем количество искомых элементов
        int k = scanner.nextInt();
        int[] result = new int[k];

        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt(); // Считываем число, которое нужно найти

            // Реализация бинарного поиска индекса
            int left = 0, right = n - 1, index = -1; // Границы поиска
            while (left <= right) {
                int mid = left + (right - left) / 2; // Находим середину
                if (a[mid] == value) {
                    index = mid + 1; // Смещение индекса (начало с 1, а не с 0)
                    break;
                } else if (a[mid] < value) {
                    left = mid + 1; // Искомое число в правой части
                } else {
                    right = mid - 1; // Искомое число в левой части
                }
            }

            result[i] = index; // Записываем результат (-1, если число не найдено)
        }

//!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!



        return result;
    }

}
