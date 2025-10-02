package by.it.group410902.grigorev.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_BinaryFind {
    public static void main(String[] args) throws FileNotFoundException {
        // Получаем поток ввода из файла "dataA.txt"
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt");

        // Создаем экземпляр класса для вызова метода поиска
        A_BinaryFind instance = new A_BinaryFind();

        // Выполняем поиск индексов нужных элементов
        int[] result = instance.findIndex(stream);

        // Выводим результат поиска
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] findIndex(InputStream stream) throws FileNotFoundException {
        // Считываем данные из потока
        Scanner scanner = new Scanner(stream);

        // Читаем количество элементов в массиве
        int n = scanner.nextInt();
        int[] a = new int[n];

        // Заполняем массив входными значениями
        for (int i = 1; i <= n; i++) {
            a[i - 1] = scanner.nextInt();
        }

        // Читаем количество элементов, которые нужно найти
        int k = scanner.nextInt();
        int[] result = new int[k];

        // Поиск каждого элемента с помощью бинарного поиска
        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();
            int left = 0;
            int right = n - 1;
            int index = -1;

            // Выполняем бинарный поиск
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (a[mid] == value) {
                    index = mid + 1; // +1 для 1-based индексации
                    break;
                } else if (a[mid] < value) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            result[i] = index;
        }
        return result;
    }
}

