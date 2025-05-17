package by.it.group410902.harkavy.lesson04;

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

    // Метод для нахождения индексов
    int[] findIndex(InputStream stream) throws FileNotFoundException {
        // Создаем сканер для чтения данных из потока
        Scanner scanner = new Scanner(stream);

        // Чтение первой строки, которая содержит размер массива и сам массив
        String firstLine = scanner.nextLine();
        Scanner firstLineScanner = new Scanner(firstLine);

        // Считываем размер массива n
        int n = firstLineScanner.nextInt();
        int[] a = new int[n];
        // Считываем элементы массива
        for (int i = 0; i < n; i++) {
            a[i] = firstLineScanner.nextInt();
        }
        firstLineScanner.close(); // Закрываем сканер для первой строки

        // Чтение второй строки, которая содержит количество чисел для поиска и сами числа
        String secondLine = scanner.nextLine();
        Scanner secondLineScanner = new Scanner(secondLine);

        // Считываем количество чисел для поиска k
        int k = secondLineScanner.nextInt();
        int[] result = new int[k];
        // Для каждого числа из второй строки выполняем бинарный поиск
        for (int i = 0; i < k; i++) {
            int value = secondLineScanner.nextInt();
            // Результат бинарного поиска с добавлением 1 для корректного индекса (с 1)
            result[i] = binarySearch(a, value) + 1; // +1 для 1-базированного индекса
        }
        secondLineScanner.close(); // Закрываем сканер для второй строки

        scanner.close(); // Закрываем основной сканер
        return result; // Возвращаем массив с результатами поиска
    }

    // Метод для бинарного поиска в отсортированном массиве
    private int binarySearch(int[] array, int target) {
        int left = 0;
        int right = array.length - 1;

        // Процесс бинарного поиска
        while (left <= right) {
            int mid = left + (right - left) / 2; // Находим середину

            if (array[mid] == target) {
                return mid; // Если нашли элемент, возвращаем индекс
            } else if (array[mid] < target) {
                left = mid + 1; // Если элемент больше, ищем справа
            } else {
                right = mid - 1; // Если элемент меньше, ищем слева
            }
        }

        return -2; // Если не нашли элемент, возвращаем -2 (что после прибавления 1 даёт -1)
    }
}
