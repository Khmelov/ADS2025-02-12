package by.it.group451003.burshtyn.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.

При сортировке реализуйте метод со сложностью O(n)
*/

public class B_CountSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        // Считываем числа (в диапазоне 1..10)
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // 1) Считаем количество вхождений каждого значения
        // Диапазон значений: 1..10, но мы создаём размер 11, чтобы индекс совпадал со значением
        int[] count = new int[11];
        for (int value : points) {
            count[value]++;
        }

        // 2) Накопительный (необязательно, но можно — тогда получится стабильная сортировка):
        //for (int v = 1; v <= 10; v++) {
        //    count[v] += count[v - 1];
        //}

        // 3) Заполняем исходный массив в порядке неубывания
        int idx = 0;
        for (int v = 1; v <= 10; v++) {
            for (int c = 0; c < count[v]; c++) {
                points[idx++] = v;
            }
        }

        return points;
    }
}
