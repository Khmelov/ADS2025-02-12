package by.it.group451002.vishnevskiy.lesson04;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер отсортированного массива
        int n = scanner.nextInt();
        //сам отсортированный массив
        int[] a = new int[n];
        for (int i = 1; i <= n; i++) {
            a[i - 1] = scanner.nextInt();
        }

        //размер массива индексов
        int k = scanner.nextInt(); // Считываем количество элементов, которые нужно найти
        int[] result = new int[k]; // Массив для хранения результатов (индексов или -1)
        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt(); // Считываем очередное значение, которое нужно найти
            int left = 0; // Левая граница поиска
            int right = n - 1; // Правая граница поиска
            int index = -1; // Индекс результата, по умолчанию -1 (если не найден)

            while (left <= right) { // Пока границы не сомкнулись
                int mid = left + (right - left) / 2; // Находим середину массива
                if (a[mid] == value) { // Если элемент найден
                    index = mid + 1; // Преобразуем индекс из 0-based в 1-based
                    break; // Выходим из цикла поиска
                } else if (a[mid] < value) {
                    left = mid + 1; // Ищем в правой половине массива
                } else {
                    right = mid - 1; // Ищем в левой половине массива
                }
            }
            result[i] = index; // Сохраняем найденный индекс (или -1)
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}
