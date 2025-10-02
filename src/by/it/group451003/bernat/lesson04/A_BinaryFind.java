package by.it.group451003.bernat.lesson04;

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
        // Загружаем входные данные из файла "dataA.txt"
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt");
        A_BinaryFind instance = new A_BinaryFind();
        // Вызываем метод для поиска индексов
        int[] result = instance.findIndex(stream);
        // Выводим результат: индексы элементов или -1, если элемент не найден
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] findIndex(InputStream stream) throws FileNotFoundException {
        // Создаем объект Scanner для чтения данных из входного потока
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Считываем размер отсортированного массива (n)
        int n = scanner.nextInt();
        // Создаем массив a размером n для хранения отсортированных чисел
        int[] a = new int[n];
        // Заполняем массив a числами из входных данных
        // Обратите внимание: в задаче индексация начинается с 1, а в Java с 0
        for (int i = 1; i <= n; i++) {
            a[i - 1] = scanner.nextInt();
        }

        // Считываем количество чисел (k), которые нужно найти
        int k = scanner.nextInt();
        // Создаем массив result для хранения индексов найденных чисел
        int[] result = new int[k];
        // Для каждого из k чисел выполняем поиск
        for (int i = 0; i < k; i++) {
            // Считываем очередное число для поиска
            int value = scanner.nextInt();
            // Реализация бинарного поиска для нахождения индекса числа value в массиве a
            int left = 0; // Левая граница поиска
            int right = n - 1; // Правая граница поиска
            int index = -1; // Инициализируем индекс как -1 (если элемент не найден)

            // Пока левая граница не превышает правую
            while (left <= right) {
                // Находим средний элемент, чтобы избежать переполнения
                int mid = left + (right - left) / 2;
                // Если средний элемент равен искомому числу
                if (a[mid] == value) {
                    index = mid; // Сохраняем индекс (0-based)
                    break; // Прерываем поиск
                } else if (a[mid] < value) {
                    // Если средний элемент меньше искомого, ищем в правой половине
                    left = mid + 1;
                } else {
                    // Если средний элемент больше искомого, ищем в левой половине
                    right = mid - 1;
                }
            }

            // Преобразуем 0-based индекс в 1-based (по условию задачи) или оставляем -1
            result[i] = (index == -1) ? -1 : index + 1;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // Возвращаем массив с индексами
        return result;
    }
}