package by.it.group410902.skobyalko.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_BinaryFind {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt");
        A_BinaryFind instance = new A_BinaryFind();
        int[] result = instance.findIndex(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] findIndex(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
/// ///////////////////////////
        int n = scanner.nextInt(); // длина массива A
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        int k = scanner.nextInt(); // количество запросов
        int[] result = new int[k];

        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();
            result[i] = binarySearch(a, value);
        }

        return result;
    }

    int binarySearch(int[] a, int key) {
        int left = 0, right = a.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (a[mid] == key) {
                return mid + 1; // сдвигаем индекс на 1, чтобы соответствовать условию задачи
            } else if (a[mid] < key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // если элемент не найден
    }
}
/// //////////////
