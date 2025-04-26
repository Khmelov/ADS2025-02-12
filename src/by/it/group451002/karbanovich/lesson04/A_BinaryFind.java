package by.it.group451002.karbanovich.lesson04;

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

        // Читаем из файла размер отсортированного массива
        int n = scanner.nextInt();

        // Читаем из файла сам отсортированный массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Читаем из файла размер второго массива
        int k = scanner.nextInt();

        // Читаем из файла по одному элементу массива и ищем бинарным поиском индекс
        // такого же элемента в первом массиве
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            // Читаем элемент
            int value = scanner.nextInt();

            // Определяем границы
            int leftBorder = 1;
            int rightBorder = n;

            // Запишем в result -1, на случай, если индекс не будет найден
            int midInd = -1;
            result[i] = midInd;

            // Ищем индекс такого же элемента из первого массива бинарным поиском
            while (leftBorder <= rightBorder) {
                midInd = (leftBorder + rightBorder) / 2;
                if (a[midInd-1] < value){
                    leftBorder = midInd + 1;
                }
                else if (a[midInd-1] > value){
                    rightBorder = midInd - 1;
                }
                else{
                    // Если индекс был найден, то записываем его
                    result[i] = midInd;
                    break;
                }
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}