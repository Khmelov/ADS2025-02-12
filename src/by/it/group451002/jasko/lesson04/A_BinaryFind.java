package by.it.group451002.jasko.lesson04;

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
        // Получаем входной поток данных из файла
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt");
        // Создаем экземпляр класса для поиска
        A_BinaryFind instance = new A_BinaryFind();
        // Вызываем метод поиска и получаем результат
        int[] result = instance.findIndex(stream);
        // Выводим результат
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] findIndex(InputStream stream) {
        // Инициализация сканера для чтения данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Чтение размера отсортированного массива
        int n = scanner.nextInt();
        // Чтение самого массива
        int[] a = new int[n];
        // Заполнение массива (обратите внимание на i-1, так как в Java индексация с 0)
        for (int i = 1; i <= n; i++) {
            a[i - 1] = scanner.nextInt();
        }

        // Чтение количества чисел, которые нужно найти
        int k = scanner.nextInt();
        // Массив для хранения результатов поиска
        int[] result = new int[k];
        // Поиск каждого числа из запроса
        for (int i = 0; i < k; i++) {
            // Чтение числа, которое нужно найти
            int value = scanner.nextInt();

            // Инициализация переменных для бинарного поиска
            int left = 0;          // Левая граница поиска
            int right = n - 1;     // Правая граница поиска
            int mid = 0;           // Середина текущего диапазона
            boolean found = false; // Флаг, указывающий, найдено ли число

            // Бинарный поиск
            while (left <= right) {
                // Вычисление середины диапазона (безопасное от переполнения)
                mid = left + (right - left) / 2;

                if (a[mid] == value) {
                    // Число найдено
                    found = true;
                    break;
                } else if (a[mid] < value) {
                    // Искомое число в правой половине
                    left = mid + 1;
                } else {
                    // Искомое число в левой половине
                    right = mid - 1;
                }
            }

            // Запись результата
            if (found) {
                // +1 из-за требования задачи (индексация с 1, а не с 0)
                result[i] = mid + 1;
            } else {
                // Число не найдено
                result[i] = -1;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}