package by.it.group451002.yeshmanski.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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

        // Поиск индексов
        int[] result = instance.findIndex(stream);

        // Выводим результат
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] findIndex(InputStream stream) throws FileNotFoundException {

        Scanner scanner = new Scanner(stream);

        // Читаем размер отсортированного массива
        int n = scanner.nextInt();

        // Создаем массив и заполняем его элементами
        int[] arr = new int[n];
        for (int i = 1; i <= n; i++) {
            arr[i - 1] = scanner.nextInt();
        }

        // Читаем количество искомых элементов
        int k = scanner.nextInt();

        // Создаем массив для хранения результатов
        int[] result = new int[k];

        // Проходим по каждому искомому элементу
        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt(); // Текущий элемент для поиска
            int left = 0, right = n - 1, mid = 0;

            // Бинарный поиск
            while (left < right) {
                mid = left + (right - left) / 2; // Вычисляем средний индекс

                // Сравниваем средний элемент с искомым значением
                if (arr[mid] > value)

                    // Границу поиска влево
                    right = mid - 1;
                else if (arr[mid] < value)

                    // Границу поиска вправо
                    left = mid + 1;
                else
                    break;
            }

            // Проверяем, совпадает ли найденный элемент с искомым
            result[i] = arr[mid] == value ? mid + 1 : -1;
        }

        return result; // Возвращаем массив с результатами
    }
}
