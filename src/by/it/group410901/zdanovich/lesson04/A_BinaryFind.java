package by.it.group410901.zdanovich.lesson04;

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
        //сам отсортированный массива
        int[] a = new int[n];
        for (int i = 1; i <= n; i++) {
            a[i - 1] = scanner.nextInt();
        }

        //размер массива индексов
        int k = scanner.nextInt();
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();
            //тут реализуйте бинарный поиск индекса
            result[i] = -1;
            // Инициализируем начальные значения для бинарного поиска
            int lowIndex = 0;
            int highIndex = a.length - 1;
            boolean found = false;
            int middleIndex = 0;
            // Выполняем бинарный поиск
            while (lowIndex <= highIndex && !found) {
                middleIndex = lowIndex + (highIndex - lowIndex) / 2;
                if (value < a[middleIndex]) {
                    highIndex = middleIndex - 1;
                }
                else if (value > a[middleIndex]) {
                    lowIndex = middleIndex + 1;
                }
                else {
                    result[i] = middleIndex;
                    found = true;
                }
            }
            // Если число найдено, сохраняем его индекс (с учетом смещения на 1)
            // Если число не найдено, сохраняем -1
            if (found)
                result[i] = middleIndex + 1;
            else
                result[i] = -1;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}
